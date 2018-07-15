package com.messiuw.graphics;

import com.messiuw.utilities.GraphicsDataIF;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.Map;

/**
 * Created by matthias.polkehn on 15.03.2018.
 */
public class Plot {

    private Map<String,String> data;
    private Map<String,String> axisData;

    public Plot(Map<String,String> p_inputData, Map<String,String> axisData) {
        this.data = p_inputData;
        this.axisData = axisData;

        SwingUtilities.invokeLater(this::plot);
    }

    private void plot() {
        GraphicPanel panel = new GraphicPanel(this.data,this.axisData);
        panel.setPreferredSize(new Dimension(800, 600));
        JFrame frame = new JFrame(this.axisData.get(GraphicsDataIF.CHARTNAME));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
