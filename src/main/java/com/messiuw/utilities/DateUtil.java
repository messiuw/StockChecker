package com.messiuw.utilities;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    private String firstDateOrTime;
    private String secondDateOrTime;

    private SimpleDateFormat formatter;
    private Boolean isMinuteResolvedData = null;

    public DateUtil() {
        new DateUtil(null,null);
    }

    public DateUtil(String firstDateOrTime, String secondDateOrTime) {
        this.firstDateOrTime = firstDateOrTime;
        this.secondDateOrTime = secondDateOrTime;
    }

    public void setFirstDateOrTime(String firstDateOrTime) {
        this.firstDateOrTime = firstDateOrTime;
    }

    public void setSecondDateOrTime(String secondDateOrTime) {
        this.secondDateOrTime = secondDateOrTime;
    }

    public void setIsMinuteResolvedData(boolean isMinuteResolvedData) {
        this.isMinuteResolvedData = isMinuteResolvedData;
    }

    public String getDifferenceOfTwoTimesOrDatesAsString() throws Exception {
        return getDifferenceOfTwoTimesOrDatesAsString(this.firstDateOrTime,this.secondDateOrTime);
    }

    public String getDifferenceOfTwoTimesOrDatesAsString(String firstDateOrTime, String secondDateOrTime) throws Exception {
        initializeFormatter();

        Date firstDateOrTimeAsDate = formatter.parse(firstDateOrTime);
        Date secondDateOrTimeAsDate = formatter.parse(secondDateOrTime);

        long firstTime = firstDateOrTimeAsDate.getTime();
        long secondTime = secondDateOrTimeAsDate.getTime();
        Long differenceInMin;
        if (isMinuteResolvedData) {
            differenceInMin = Math.abs(secondTime - firstTime)/(1000*60);
        } else {
            differenceInMin = Math.abs(secondTime - firstTime)/(1000*60*60*24);
        }
        return differenceInMin.toString();
    }

    private void initializeFormatter() throws Exception {
        if (isMinuteResolvedData == null) {
            throw new Exception("Could not determine weather data is a time tor date...");
        }

        if (isMinuteResolvedData) {
            formatter = new SimpleDateFormat("HH:mm");
        } else {
            formatter = new SimpleDateFormat("yyyy-MM-dd");
        }
    }
}
