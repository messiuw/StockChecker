package com.messiuw.dateUtil;

import com.messiuw.utilities.DateUtil;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

public class DateUtilTest {

    private DateUtil dateUtilWithArgs; //accepts only minute resolved data, i.e. firstTimeAsString and secondTimeAsString
    private DateUtil dateUtilWithoutArgs; //accepts only date resolved data, i.e. firstDateAsString and secondDateAsString
    private String firstTimeAsString = "9:30";
    private String secondTimeAsString = "10:42";
    private String firstDateAsString = "2018-05-25";
    private String secondDateAsString = "2018-06-31";

    @Before
    public void initialize() {
        dateUtilWithArgs = new DateUtil(firstTimeAsString,secondTimeAsString);
        dateUtilWithArgs.setIsMinuteResolvedData(true);

        dateUtilWithoutArgs = new DateUtil();
        dateUtilWithoutArgs.setIsMinuteResolvedData(false);
    }

    @Test
    public void testConstructorWithArgs() {
        DateUtil dateUtil = new DateUtil("9:00","10:00");
        dateUtil.setIsMinuteResolvedData(true);
    }

    @Test
    public void testContructorWithoutArgs() {
        DateUtil dateUtil = new DateUtil("2017-01-01","2017-01-02");
        dateUtil.setIsMinuteResolvedData(false);
    }

    @Test
    public void testTimeDifference() throws Exception {
        Object result = dateUtilWithArgs.getDifferenceOfTwoTimesOrDatesAsString();
        assertTrue(result instanceof String);
    }

    @Test
    public void testDateDifference() throws Exception {
        dateUtilWithoutArgs.setFirstDateOrTime(firstDateAsString);
        dateUtilWithoutArgs.setSecondDateOrTime(secondDateAsString);
        dateUtilWithoutArgs.setIsMinuteResolvedData(false);

        Object result = dateUtilWithoutArgs.getDifferenceOfTwoTimesOrDatesAsString();
        assertTrue(result instanceof String);
    }

    @Test(expected = Exception.class)
    public void testException() throws Exception {
        DateUtil dateUtil = new DateUtil();
        dateUtil.getDifferenceOfTwoTimesOrDatesAsString();
    }

}
