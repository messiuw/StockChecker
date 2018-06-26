package com.messiuw.graphics;

import com.messiuw.query.StockData;
import com.messiuw.response.AbstractDataResponse;
import com.messiuw.utilities.GraphicsDataIF;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;

/**
 * Created by matthias.polkehn on 15.03.2018.
 */
public class GraphicPanel extends JPanel implements GraphicsDataIF {
    private Map<String,String> data;
    private Map<String,String> axisData;
    private static final int TICS_LENGTH = 10;
    private static final int AXIS_PADDING = 50;
    private static final int LABEL_PADDING = 10;
    private static final Color LINE_COLOR = new Color(44, 102, 230, 180);
    private static final  Color POINT_COLOR = new Color(100, 100, 100, 180);
    private static final Color GRID_COLOR = new Color(200, 200, 200, 200);
    private static final Stroke GRAPH_STROKE = new BasicStroke(2f);
    private static final int POINT_WIDTH = 4;
    private static final int Y_TICS_NUM = 20;
    private static final int X_TICS_NUM = 10;

    private List<Integer> xTicsList = new ArrayList<>();
    private List<Integer> yTicsList = new ArrayList<>();

    private Font labelFont = new Font("Arial",Font.ITALIC,10);
    private Font axisFont = new Font("Arial",Font.BOLD,16);

    private String dataToPlot;

    private static final boolean DEBUG = false;


    public GraphicPanel(Map<String,String> p_inputMap, Map<String,String> p_axisData) {
        this.data = p_inputMap;
        this.axisData = p_axisData;

        this.dataToPlot = this.axisData.get(DATA_TO_PLOT);
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
        createTicsAlongAxis(graphics2D,Axis.X);
        createTicsAlongAxis(graphics2D,Axis.Y);

        // write labels to tics on axes
        writeTicsLabels(graphics2D,Axis.X);
        writeTicsLabels(graphics2D,Axis.Y);

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

    private void createTicsAlongAxis(Graphics2D p_graphics2D, Axis axis) {
        int distanceBetweenTics;
        if (DEBUG) p_graphics2D.setColor(Color.RED);
        if (Axis.Y.equals(axis)) {
            distanceBetweenTics = getHeight()/(Y_TICS_NUM);
            if (DEBUG) System.out.println("A tic will be set every " + distanceBetweenTics + " pixels");
            for (int tic=1;tic<=Y_TICS_NUM;tic++) {
                if (tic*distanceBetweenTics <= AXIS_PADDING) {
                    continue;
                }
                if (tic*distanceBetweenTics >= getHeight()-AXIS_PADDING) {
                    break;
                }
                if (DEBUG) System.out.println("Tic no. " + tic + " will be set at " + (tic*distanceBetweenTics) + " pixels in y direction");
                yTicsList.add((tic*distanceBetweenTics));
                p_graphics2D.drawLine(AXIS_PADDING,(tic*distanceBetweenTics),AXIS_PADDING+TICS_LENGTH,(tic*distanceBetweenTics));
                p_graphics2D.drawLine(getWidth()-AXIS_PADDING,(tic*distanceBetweenTics),getWidth()-(AXIS_PADDING+TICS_LENGTH),tic*distanceBetweenTics);
            }
        } else if (Axis.X.equals(axis)) {
            distanceBetweenTics = getWidth()/(X_TICS_NUM);
            if (DEBUG) System.out.println("A tic will be set every " + distanceBetweenTics + " pixels.");
            for (int tic=1;tic<=X_TICS_NUM;tic++) {
                if (tic*distanceBetweenTics <= AXIS_PADDING) {
                    continue;
                }
                if (tic*distanceBetweenTics >= getWidth()-AXIS_PADDING) {
                    break;
                }
                if (DEBUG) System.out.println("Tic no. " + tic + " will be set at " + (tic*distanceBetweenTics) + " pixels in x direction");
                xTicsList.add((tic*distanceBetweenTics));
                p_graphics2D.drawLine((tic*distanceBetweenTics),AXIS_PADDING,(tic*distanceBetweenTics),AXIS_PADDING+TICS_LENGTH);
                p_graphics2D.drawLine((tic*distanceBetweenTics),getHeight()-AXIS_PADDING,(tic*distanceBetweenTics),getHeight()-(AXIS_PADDING+TICS_LENGTH));
            }
        }
    }

    private void writeAxisLabels(Graphics2D p_graphics2D) {
        if (DEBUG) p_graphics2D.setColor(Color.BLUE);
        // x label
        String xLabel = getDefaultValueIfNull();
        drawCenteredString(p_graphics2D,xLabel);
        // y label
        String yLabel = (String) this.axisData.get(AbstractDataResponse.Y_AXIS_LABEL);
        //drawRotate(p_graphics2D,270,yLabel);
    }

    private void plotData(Graphics2D p_graphics2D) {

        List<Point2D.Double> graphPoints = new ArrayList<>();
        Point2D.Double point;

        double maxY = Double.parseDouble(axisData.get(MAX_Y));
        double minY = Double.parseDouble(axisData.get(MIN_Y));

        double xScale = ((double) getWidth() - (2 * AXIS_PADDING)) / (data.size() - 1);
        double yScale = ((double) getHeight() - (2 * AXIS_PADDING)) /( maxY - minY);

        int counter = 0;
        for (String date : data.keySet()) {
            double x1 = (counter * xScale + AXIS_PADDING);
            double y1 = (maxY - Double.parseDouble(data.get(date))) * yScale + AXIS_PADDING;
            graphPoints.add(new Point2D.Double(x1,y1));
            counter++;
        }

        for (int entry=0; entry<graphPoints.size()-1; entry++) {
            Double x1 = graphPoints.get(entry).x;
            Double x2 = graphPoints.get(entry+1).x;
            Double y1 = graphPoints.get(entry).y;
            Double y2 = graphPoints.get(entry+1).y;

            p_graphics2D.fillOval(x1.intValue(),y1.intValue(),10,10);

            p_graphics2D.draw(new Line2D.Double(x1,y1,x2,y2));
        }
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

    private void drawStringAt(Graphics2D p_graphics2D, String text, Point point) {
        p_graphics2D.setFont(labelFont);
        p_graphics2D.drawString(text,point.x,point.y);

    }

    private void drawStringCenteredAt(Graphics2D p_graphics2d, String text, Point point) {
        int x = point.x;
        int y = point.y;

        p_graphics2d.setFont(labelFont);
        FontMetrics metrics = p_graphics2d.getFontMetrics();
        int stringCenter = metrics.stringWidth(text)/2;
        if(DEBUG) System.out.println(metrics.stringWidth(text));
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

    private void writeTicsLabels(Graphics2D p_graphics2d, Axis axis) {
        Point position;
        String xLabel;
        String yLabel;
        int counter = 0;

        if (Axis.X.equals(axis)) {
            for (Integer xPosition : xTicsList) {
                if (DEBUG) System.out.println(xPosition);
                xLabel = getNthEntryFromMap(true,counter*(this.data.size()/X_TICS_NUM));
                xLabel = removeTrailingZeros(xLabel);
                // todo: make the y position more variable in terms of padding...
                position = new Point(xPosition,getHeight()-10);
                drawStringCenteredAt(p_graphics2d,xLabel,position);
                counter++;
            }
        }

        if (Axis.Y.equals(axis)) {
            for (Integer yPosition : yTicsList) {
                if (DEBUG) System.out.println(yPosition);
                yLabel = getNthEntryFromMap(false,counter*(this.data.size()/Y_TICS_NUM));
                position = new Point(AXIS_PADDING/2,yPosition);
                drawStringCenteredAt(p_graphics2d,yLabel,position);
                counter++;
            }
        }
    }

    private void initializeArrays() {
        if (!xTicsList.isEmpty() && !yTicsList.isEmpty()) {
            xTicsList.clear();
            yTicsList.clear();
        }

        xTicsList.add(AXIS_PADDING);
        yTicsList.add(AXIS_PADDING);
    }

    private String removeTrailingZeros(String label) {
        String[] splittedLabel;
        if (label != null && label.length() == 8) {
            splittedLabel = label.split(":");
            return splittedLabel[0]+":"+splittedLabel[1];
        }
        return label;
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
