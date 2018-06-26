package com.messiuw.query;

/**
 * Created by matthias.polkehn on 14.03.2018.
 */
public enum ListSpecifier implements SpecifierIF {

    MOSTACTIVE("mostactive"),
    GAINERS("gainers"),
    LOSERS("losers"),
    IEXVOLUME("iexvolume"),
    IEXPERCENT("iexpercent");

    private String value;

    ListSpecifier(String value) {
        this.value = value;
    }


    @Override
    public String getValue() {
        return this.value;
    }
}
