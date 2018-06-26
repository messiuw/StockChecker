package com.messiuw.referenceData;

/**
 * Created by matthias.polkehn on 14.03.2018.
 */
public enum ReferenceData {

    SYMBOLS("/ref-data/symbols");

    private String value;

    ReferenceData(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
