package com.messiuw.response;

import com.messiuw.graphics.DataToPlot;
import com.messiuw.utilities.GraphicsDataIF;
import com.messiuw.utilities.DateUtil;
import com.messiuw.utilities.ResponseKeyNameIF;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by matthias.polkehn on 09.03.2018.
 */
public abstract class AbstractDataResponse implements GraphicsDataIF, ResponseKeyNameIF {

    protected static final String OMITTED_VALUES = "-1";
    protected static final boolean DEBUG = false;

    protected JSONObject jsonObject = null;
    protected JSONArray jsonArray = null;
    protected Double currentPrice = null;
    protected String finalQuery = null;
    protected String superKeyName = null;

    protected Map<String,String> dataMap = null; // the data obtained from a jsonobject
    protected Map<String,Map<String,String>> dataMapInContainer = null; // the data obtained from a jsonarray
    protected Map<String,String> dataMapForPlot = null; // here we store the information for the plot
    protected Map<String,String> metaDataForPlot = null; // meta data for the plot, e.g. axis labels, min/max values etc (see also GraphicsDataIF)

    private DateUtil dateUtil;

    public String getFinalQuery() {
        return this.finalQuery;
    }

    public JSONObject getJsonObject() {
        return this.jsonObject;
    }

    public JSONArray getJsonArray() {
        return this.jsonArray;
    }

    protected void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    protected void setJsonArray(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    public String toString() {
        return this.getJsonArray().toString();
    }

    protected void gatherMetaDataForPlot(DataToPlot p_ordinateData) {
        this.metaDataForPlot = new HashMap<>();
        Integer dataPointsX = this.dataMapInContainer.size();
        Integer dataPointsY = this.dataMapInContainer.size();

        this.metaDataForPlot.put(DATA_TO_PLOT,p_ordinateData.getValue());
        this.metaDataForPlot.put(MIN_X,getMinOfAbscissa());
        this.metaDataForPlot.put(MAX_X,getMaxOfAbscissa());
        this.metaDataForPlot.put(MIN_Y,getMinOfOrdinate(p_ordinateData));
        this.metaDataForPlot.put(MAX_Y,getMaxOfOrdinate(p_ordinateData));
        this.metaDataForPlot.put(DATA_POINTS_X,dataPointsX.toString());
        this.metaDataForPlot.put(DATA_POINTS_Y,dataPointsY.toString());
        try {
            this.metaDataForPlot.put(INCREMENT_X,getIncrementOnAbscissa());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.metaDataForPlot.put(INCREMENT_Y,getIncrementOnOrdinate());
        this.metaDataForPlot.put(X_AXIS_LABEL,this.superKeyName);
        this.metaDataForPlot.put(Y_AXIS_LABEL,p_ordinateData.getValue());
        this.metaDataForPlot.put(CHARTNAME,getNameForChart(p_ordinateData));
    }

    protected void printDatamap() {
        this.dataMap.forEach((key,value) -> System.out.println(key + ":\t" + value));
    }

    protected void printDatamapInContainer() {
        for (Map.Entry<String,Map<String,String>> outerMap : this.dataMapInContainer.entrySet()) {
            System.out.println("=======================");
            System.out.println(outerMap.getKey());
            System.out.println("=======================");
            outerMap.getValue().forEach((key,value) -> System.out.println(key + ":\t" + value));
        }
    }

    protected void setSuperKeyName(String superKeyName) {
        this.superKeyName = superKeyName;
    }

    private String getMinOfOrdinate(DataToPlot p_dataToPlot) {
        Double minValue = Double.MAX_VALUE;
        Double doubleValue;

        for (int entry=0; entry < this.dataMapInContainer.size(); entry++) {
            String valueAsString = getNthEntryOfInnerMap(entry,p_dataToPlot.getValue());
            if (valueAsString != null) {
                doubleValue = Double.parseDouble(valueAsString);
                if (doubleValue < minValue) {
                    minValue = doubleValue;
                }
            }
        }
        return minValue.toString();
    }

    private String getMaxOfOrdinate(DataToPlot p_dataToPlot) {
        Double maxValue = Double.MIN_VALUE;
        Double doubleValue;

        for (int entry=0; entry < this.dataMapInContainer.size(); entry++) {
            String valueAsString = getNthEntryOfInnerMap(entry,p_dataToPlot.getValue());
            if (valueAsString != null) {
                doubleValue = Double.parseDouble(valueAsString);
                if (doubleValue > maxValue) {
                    maxValue = doubleValue;
                }
            }
        }
        return maxValue.toString();
    }

    private String getMinOfAbscissa() {
        return this.dataMapInContainer.keySet().iterator().next();
    }

    private String getMaxOfAbscissa() {
        Iterator<String> iterator = this.dataMapInContainer.keySet().iterator();
        String lastEntry = null;
        while (iterator.hasNext()) {
            lastEntry = iterator.next();
        }
        return lastEntry;
    }

    private String getIncrementOnAbscissa() throws Exception {
        Object[] arrayWithTimeOrDate = this.dataMapForPlot.keySet().toArray();
        String firstDateOrTime = (String) arrayWithTimeOrDate[0];
        String secondDateOrTime = (String) arrayWithTimeOrDate[1];

        dateUtil = new DateUtil(firstDateOrTime,secondDateOrTime);
        dateUtil.setIsMinuteResolvedData(superKeyName.equalsIgnoreCase(MINUTE_KEY_NAME));
        if (superKeyName.equalsIgnoreCase(MINUTE_KEY_NAME)) {
            return dateUtil.getDifferenceOfTwoTimesOrDatesAsString();
        } else if (superKeyName.equalsIgnoreCase(DATE_KEY_NAME)) {
            return dateUtil.getDifferenceOfTwoTimesOrDatesAsString();
        } else {
            // todo: this should come earlier than here
            throw new Exception("There is no suitable data to generate a plot!");
        }
    }

    private String getIncrementOnOrdinate() {
        Object[] arrayFromDataForPlot = this.dataMapForPlot.values().toArray();
        String firstValue = (String) arrayFromDataForPlot[0];
        String secondValue = (String) arrayFromDataForPlot[1];

        Double firstValueAsInt = Double.parseDouble(firstValue);
        Double secondValueAsInt = Double.parseDouble(secondValue);
        Double difference = Math.abs(secondValueAsInt-firstValueAsInt);

        return difference.toString();
    }

    private String getNthEntryOfInnerMap(int entry, String dataOfInterest) {
        Map<String,String> innerMap;
        int counter = 0;

        for (Map<String,String> value : this.dataMapInContainer.values()) {
            if (counter == entry) {
                innerMap = value;
                return innerMap.get(dataOfInterest);
            }
            counter++;
        }
        return null;
    }

    private String getNameForChart(DataToPlot p_ordinateData) {
        String[] splittedQuery = this.finalQuery.split("/");
        return splittedQuery[4].toUpperCase()+" "+p_ordinateData.getValue()+" for '"+splittedQuery[2]+"'";
    }
}
