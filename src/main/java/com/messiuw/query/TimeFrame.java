package com.messiuw.query;

/**
 * Created by matthias.polkehn on 08.03.2018.
 */
public enum TimeFrame implements SpecifierIF {

    FIVE_YEAR("5y"),
    TWO_YEAR("2y"),
    ONE_YEAR("1y"),
    YEAR_TO_DATE("ytd"),
    SIX_MONTH("6m"),
    THREE_MONTH("3m"),
    ONE_MONTH("1m"),
    FIVE_DAY("5d"),
    ONE_DAY("1d");

    private String value;

    TimeFrame(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return this.value;
    }

}
