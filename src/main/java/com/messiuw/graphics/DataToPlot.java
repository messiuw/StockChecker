package com.messiuw.graphics;

public enum DataToPlot {

    AVERAGE("average"),
    MARKETAVERAGE("marketAverage"),
    HIGH("high");

    private String value;

    DataToPlot(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }


}
