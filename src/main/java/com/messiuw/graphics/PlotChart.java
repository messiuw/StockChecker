package com.messiuw.graphics;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.Map;

/**
 * Created by matthias.polkehn on 15.03.2018.
 */
public class PlotChart {

    private Map<String,String> data;
    private Map<String,String> axisData;

    public PlotChart(Map<String,String> p_inputData, Map<String,String> axisData) {
        this.data = p_inputData;
        this.axisData = axisData;
        /*for (String key : axisData.keySet()) {
            System.out.println("key: " + key);
            System.out.println("value: " + this.axisData.get(key));
        }*/
        SwingUtilities.invokeLater(this::plot);
    }

    private void plot() {
        GraphicPanel panel = new GraphicPanel(this.data,this.axisData);
        panel.setPreferredSize(new Dimension(800, 600));
        JFrame frame = new JFrame("Data");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
