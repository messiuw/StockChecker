package com.messiuw.graphics;

import com.messiuw.utilities.GraphicsDataIF;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * Created by matthias.polkehn on 15.03.2018.
 */
public class GraphicPanel extends JPanel implements GraphicsDataIF {
    private Map<String,String> data;
    private Map<String,String> axisData;
    private static final Stroke GRAPH_STROKE = new BasicStroke(2f);
    private static final int TICS_LENGTH = 10;
    private static final int AXIS_PADDING = 50;
    private static final int LABEL_PADDING = 10;
    private static final int Y_TICS_NUM = 20;
    private static final int Y_TICS_THRESHOLD = 12;
    private static final int X_TICS_NUM = 5;
    private static final int X_TICS_THRESHOLD = 8;
    private static final int POINT_WIDTH = 10;

    private List<Integer> xTicsList = new ArrayList<>();
    private List<Integer> yTicsList = new ArrayList<>();

    private Font labelFont = new Font("Arial",Font.ITALIC,10);
    private Font axisFont = new Font("Arial",Font.BOLD,16);

    private boolean isDate = true;

    public GraphicPanel(Map<String,String> p_inputMap, Map<String,String> p_axisData) {
        this.data = p_inputMap;
        this.axisData = p_axisData;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // since there are container listeners added to the calling code, we have to clear the caches for the positions
        // otherwise we will get additional labels after the listeners fire an event
        initializeArrays();

        // draw border
        drawBorder(graphics2D);

        // create tics along X & Y
        createTicsAlongAxis(graphics2D);

        // write labels to tics on axes
        writeTicsLabels(graphics2D);

        // write label legend
        writeAxisLabels(graphics2D);

        // put data in graph
        plotData(graphics2D);
    }

    private void drawBorder(Graphics2D p_graphics2D) {
        p_graphics2D.setColor(Color.BLACK);
        Stroke oldStroke = p_graphics2D.getStroke();
        p_graphics2D.setStroke(GRAPH_STROKE);
        p_graphics2D.drawRect(AXIS_PADDING,AXIS_PADDING,getWidth()-2*AXIS_PADDING,getHeight()-2*AXIS_PADDING);
        p_graphics2D.setStroke(oldStroke);
    }

    private void createTicsAlongAxis(Graphics2D p_graphics2D) {
        int xTicsNum = this.data.size() < X_TICS_THRESHOLD ? this.data.size() : X_TICS_NUM;
        int distanceBetweenXTics = (getWidth() - 2*AXIS_PADDING)/(xTicsNum-1);
        xTicsList.add(AXIS_PADDING);
        for (int tic=1; tic<xTicsNum; tic++) {
            int xPosition = tic*distanceBetweenXTics+AXIS_PADDING;
            xTicsList.add(xPosition);
            p_graphics2D.drawLine(xPosition,getHeight()-AXIS_PADDING,xPosition,getHeight()-AXIS_PADDING-TICS_LENGTH);
            p_graphics2D.drawLine(xPosition,AXIS_PADDING,xPosition,AXIS_PADDING+TICS_LENGTH);
        }

        int yTicsNum = this.data.size() < Y_TICS_THRESHOLD ? this.data.size() : Y_TICS_NUM;
        int distanceBetweenYTics = (getHeight() - 2*AXIS_PADDING)/(yTicsNum-1);
        yTicsList.add(AXIS_PADDING);
        for (int tic=1; tic<yTicsNum; tic++) {
            int yPosition = tic*distanceBetweenYTics+AXIS_PADDING;
            yTicsList.add(yPosition);
            p_graphics2D.drawLine(AXIS_PADDING,yPosition,AXIS_PADDING+TICS_LENGTH,yPosition);
            p_graphics2D.drawLine(getWidth()-AXIS_PADDING,yPosition,getWidth()-(AXIS_PADDING+TICS_LENGTH),yPosition);
        }
        // we have to reverse the list because the jpanel is designed from top to bottom and not bottom to top
        Collections.reverse(yTicsList);
    }

    private void writeTicsLabels(Graphics2D p_graphics2d) {
        Point position;

        int counter = 0;
        String xLabel;
        for (Integer xPosition : xTicsList) {
            try {
                xLabel = getNthEntryFromMap(true,counter);
                Date labelAsDate = new SimpleDateFormat("yyyy-MM-dd").parse(xLabel);
                xLabel = new SimpleDateFormat("dd.MM.").format(labelAsDate);
            } catch (ParseException pse) {
                this.isDate = false;
                xLabel = calculateLabel(Axis.X,counter);
            }
            position = new Point(xPosition,getHeight()-30);
            drawStringCenteredAt(p_graphics2d,xLabel,position);
            counter++;
        }

        counter = 0;
        String yLabel;
        for (Integer yPosition : yTicsList) {
            yLabel = calculateLabel(Axis.Y,counter);
            position = new Point(AXIS_PADDING / 2, yPosition);
            drawStringCenteredAt(p_graphics2d, yLabel, position);
            counter++;
        }
    }

    private String calculateLabel(Axis axis, int counter) {
        double minValue = 0;
        double maxValue;
        double increment = 0;

        if (Axis.X.equals(axis)) {
            if ("date".equalsIgnoreCase(this.axisData.get(GraphicsDataIF.X_AXIS_LABEL))) {
                System.out.println();
            } else {
                minValue = Double.parseDouble(this.axisData.get(GraphicsDataIF.MIN_X));
                maxValue = Double.parseDouble(this.axisData.get(GraphicsDataIF.MAX_X));
                increment = (maxValue-minValue)/((double)this.xTicsList.size()-1);
            }
        }
        if (Axis.Y.equals(axis)) {
            minValue = Double.parseDouble(this.axisData.get(GraphicsDataIF.MIN_Y));
            maxValue = Double.parseDouble(this.axisData.get(GraphicsDataIF.MAX_Y));
            increment = (maxValue-minValue)/((double)this.yTicsList.size()-1);
        }

        DecimalFormat decimalFormat = new DecimalFormat(".##");
        return decimalFormat.format(minValue+counter*increment);
    }

    private void writeAxisLabels(Graphics2D p_graphics2D) {
        // x label
        String xLabel = getDefaultValueIfNull();
        xLabel = Character.toUpperCase(xLabel.charAt(0)) + xLabel.substring(1);
        drawCenteredString(p_graphics2D,xLabel);
        // y label
        String yLabel = "US $";
        drawRotate(p_graphics2D,270,yLabel);
    }

    private void plotData(Graphics2D p_graphics2D) {
        List<Point2D> dataPoints = calculateDataPoints();
        drawPoints(dataPoints,p_graphics2D);
        drawLine(dataPoints,p_graphics2D);
    }

    private void drawRotate(Graphics2D p_graphics2D, int angle, String text) {
        // we use a factor of 2 such that it looks a little bit nicer
        int x = 2*LABEL_PADDING;
        int y = getCenter(p_graphics2D,Axis.Y,text);
        p_graphics2D.translate((float)x,(float)y);
        p_graphics2D.rotate(Math.toRadians(angle));
        p_graphics2D.drawString(text,0,0);
        p_graphics2D.rotate(-Math.toRadians(angle));
        p_graphics2D.translate(-(float)x,-(float)y);
    }

    private void drawCenteredString(Graphics2D p_graphics2D, String text) {
        int x = getCenter(p_graphics2D,Axis.X,text);
        int y = getHeight()-LABEL_PADDING;
        p_graphics2D.setFont(axisFont);
        p_graphics2D.drawString(text, x, y);
    }

    private void drawStringCenteredAt(Graphics2D p_graphics2d, String text, Point point) {
        int x = point.x;
        int y = point.y;

        p_graphics2d.setFont(labelFont);
        FontMetrics metrics = p_graphics2d.getFontMetrics();
        int stringCenter = metrics.stringWidth(text)/2;
        p_graphics2d.drawString(text,x-stringCenter,y);
    }

    private int getCenter(Graphics2D p_graphics2D, Axis axis, String p_text) {
        FontMetrics metrics = p_graphics2D.getFontMetrics();
        Rectangle rectangle = p_graphics2D.getClipBounds();

        if (Axis.X.equals(axis)) {
            return rectangle.x + (rectangle.width - metrics.stringWidth(p_text)) / 2;
        } else {
            return rectangle.y + (rectangle.height + metrics.stringWidth(p_text)) / 2;
        }
    }

    private String getDefaultValueIfNull() {
        String xLabel = this.axisData.get(X_AXIS_LABEL);
        if (xLabel == null) {
            return "";
        }
        return xLabel;
    }

    private String getNthEntryFromMap(boolean isKey, int nthEntry) {
        int counter = 0;

       for (Map.Entry<String,String> mapEntry : this.data.entrySet()) {
           if (counter == nthEntry) {
               if (isKey) {
                   return mapEntry.getKey();
               } else {
                   return mapEntry.getValue();
               }
           }
           counter++;
       }
       return null;
    }

    private void initializeArrays() {
        xTicsList.clear();
        yTicsList.clear();
    }

    private List<Point2D> calculateDataPoints() {
        double minY = Double.parseDouble(this.axisData.get(GraphicsDataIF.MIN_Y));
        double maxY = Double.parseDouble(this.axisData.get(GraphicsDataIF.MAX_Y));
        double yOffset = getHeight()-AXIS_PADDING;
        double yScaling = (double)(getHeight()-2*AXIS_PADDING)/(maxY-minY);
        yScaling *= (-1);

        double minX=0;
        double maxX;
        double xOffset=0;
        double xScaling=0;
        if (!isDate) {
            minX = Double.parseDouble(this.axisData.get(GraphicsDataIF.MIN_X));
            maxX = Double.parseDouble(this.axisData.get(GraphicsDataIF.MAX_X));
            xOffset = AXIS_PADDING;
            xScaling = (double)(getWidth()-2*AXIS_PADDING)/(maxX-minX);
        }
        List<Point2D> dataPoints = new ArrayList<>();
        int counter = 0;
        for (Map.Entry<String,String> dataEntry : this.data.entrySet()) {
            double xValueScaled;
            if (!isDate) {
                double xValueDouble = Double.parseDouble(dataEntry.getKey());
                xValueDouble -= minX;
                xValueScaled = (xValueDouble*xScaling)+xOffset;
            } else {
                xValueScaled = (double)this.xTicsList.get(counter);
            }

            double yValueDouble = Double.parseDouble(dataEntry.getValue());
            yValueDouble -= minY;
            double yValueScaled = (yValueDouble*yScaling)+yOffset;

            dataPoints.add(new Point2D.Double(xValueScaled,yValueScaled));
            counter++;
        }
        return dataPoints;
    }

    private void drawPoints(List<Point2D> dataPointList, Graphics2D p_graphics2D) {
        int internalOffset = POINT_WIDTH /2;
        for (Point2D point : dataPointList) {
            Shape ellipse = new Ellipse2D.Double(point.getX()-internalOffset,point.getY()-internalOffset, POINT_WIDTH, POINT_WIDTH);
            p_graphics2D.draw(ellipse);
        }
    }

    private void drawLine(List<Point2D> dataPointList, Graphics2D p_graphics2D) {
        for (int data=0; data<dataPointList.size()-1; data++) {
            Point2D dataPoint = dataPointList.get(data);
            Point2D nextDataPoint = dataPointList.get(data+1);

            Shape line = new Line2D.Double(dataPoint.getX(),dataPoint.getY(),nextDataPoint.getX(),nextDataPoint.getY());
            p_graphics2D.draw(line);
        }
    }

}

enum Axis {

    X("X"),
    Y("Y");

    private String value;

    Axis(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
