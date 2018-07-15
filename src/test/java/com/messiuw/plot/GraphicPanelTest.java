package com.messiuw.plot;

import com.messiuw.graphics.Plot;
import org.junit.Test;

import java.util.*;

import static com.messiuw.utilities.GraphicsDataIF.*;

public class GraphicPanelTest {

    private Map<String,String> dataWithDate = new LinkedHashMap<>();
    private Map<String,String> dataWithValue = new LinkedHashMap<>();
    private Map<String,String> metaData = new HashMap<>();

    private int smallestValue;
    private int highestValue;

    private final List<String> dateValues = Arrays.asList("2018-07-09","2018-07-10","2018-07-11","2018-07-12","2018-07-13");
    private final List<String> dataValues = Arrays.asList("10","20","30","40","50");

    private final int maxValue = 100;

    @Test
    public void plotDateValues() {
        setUp();
        assignMetadata("date");
        Plot plot = new Plot(dataWithDate,metaData);
    }

    @Test
    public void plotDataValues() {
        setUp();
        assignMetadata("data");
        Plot plot = new Plot(dataWithValue,metaData);
    }

    private void setUp() {
        Random random = new Random();
        String randomIntString;
        this.smallestValue = Integer.MAX_VALUE;
        this.highestValue = Integer.MIN_VALUE;
        for (String dateValue : dateValues) {
            int randomInt = random.nextInt(maxValue);
            if (randomInt < this.smallestValue) {
                this.smallestValue = randomInt;
            }
            if (randomInt > this.highestValue) {
                this.highestValue = randomInt;
            }
            randomIntString = Integer.toString(randomInt);
            dataWithDate.put(dateValue, randomIntString);
        }

        for (String dataValue : dataValues) {
            int randomInt = random.nextInt(maxValue);
            if (randomInt < this.smallestValue) {
                this.smallestValue = randomInt;
            }
            if (randomInt > this.highestValue) {
                this.highestValue = randomInt;
            }
            randomIntString = Integer.toString(randomInt);
            dataWithValue.put(dataValue,randomIntString);
        }
    }

    private void assignMetadata(String testCase) {
        String minX = testCase.equalsIgnoreCase("data") ? "10" : "2018-07-09";
        String maxX = testCase.equalsIgnoreCase("data") ? "50" : "2018-07-13";
        this.metaData.put(DATA_TO_PLOT,"Dummy data");
        this.metaData.put(MIN_X,minX);
        this.metaData.put(MAX_X,maxX);
        this.metaData.put(MIN_Y,Integer.toString(this.smallestValue));
        this.metaData.put(MAX_Y,Integer.toString(this.highestValue));
        this.metaData.put(DATA_POINTS_X,"5");
        this.metaData.put(DATA_POINTS_Y,"5");
        this.metaData.put(INCREMENT_X,"1");
        this.metaData.put(INCREMENT_Y,"1");
        this.metaData.put(X_AXIS_LABEL,"x");
        this.metaData.put(Y_AXIS_LABEL,"y");
        this.metaData.put(CHARTNAME,"Dummy data");
    }
}
