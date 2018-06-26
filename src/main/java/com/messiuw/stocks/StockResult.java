package com.messiuw.stocks;

import com.messiuw.response.AbstractDataResponse;
import com.messiuw.system.SystemAPI;
import com.messiuw.graphics.DataToPlot;
import com.messiuw.exceptions.IexTradingException;
import com.messiuw.graphics.PlotChart;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by matthias.polkehn on 09.03.2018.
 */
public class StockResult extends AbstractDataResponse {

    private StockAnalyzer analyzer;

    public StockResult(Object json, String finalQuery) throws IexTradingException {
        if (json == null) {
            throw new IexTradingException("Object returned from HTTP Request is null");
        }
        // this should not be possible because we would have gotten an exception earlier already!
        if (finalQuery == null) {
            throw new IexTradingException("Endpoint used to access IExTrading is null");
        }
        if (DEBUG) System.out.println("Request returned object of type " + json.getClass().getName());
        if (json instanceof JSONObject) {
            this.jsonObject = (JSONObject) json;
            analyzer = new StockAnalyzer(this.jsonObject);
        } else if (json instanceof JSONArray) {
            this.jsonArray = (JSONArray) json;
            analyzer = new StockAnalyzer(this.jsonArray);
        } else if (json instanceof Double) {
            this.currentPrice = (Double) json;
        }
        this.finalQuery = finalQuery;
    }

    public void analyze() {
        if (this.jsonObject != null) {
            if (this.finalQuery.contains("ohlc")) {
                analyzer.analyzeOhlc();
            } else {
                analyzer.analyze();
            }
            this.dataMap = analyzer.getSimpleDataMap();
        } else if (this.jsonArray != null) {
            String keyName = null;
            if (this.finalQuery.contains("chart")) {
                keyName = this.finalQuery.contains("1d") ? MINUTE_KEY_NAME : DATE_KEY_NAME;
            } else if (this.finalQuery.contains("earnings")) {
                keyName = FISCAL_END_DATE_KEY_NAME;
            } else if (this.finalQuery.contains("effective-spread") || this.finalQuery.contains("volume-by-venue")) {
                keyName = VENUE_NAME_KEY_NAME;
            } else if (this.finalQuery.contains("dividends")) {
                keyName = PAYMENT_DATE_KEY_NAME;
            } else if (this.finalQuery.contains("list")) {
                keyName = COMPANY_NAME_KEY_NAME;
            } else {
                SystemAPI.showInfoDialog("","");
            }
            analyzer.analyze(keyName);
            setSuperKeyName(analyzer.getKeyName());
            this.dataMapInContainer = analyzer.getComplexDataMap();
        } else {
            SystemAPI.showInfoDialog("The API endpoint '" + this.finalQuery + "' is currently not supported.",this.getClass());
        }
    }

    public void printData() {
        if (this.dataMap != null) {
            if (DEBUG) System.out.println("datamap");
            printDatamap();
        } else if (this.dataMapInContainer != null) {
            if (DEBUG) System.out.println("datamap in container");
            printDatamapInContainer();
        } else {
            SystemAPI.showInfoDialog("No data to print available. Run analyze() first",this.getClass());
        }
    }

    public void plotChart(DataToPlot dataToPlot) {
        if (!this.finalQuery.contains("chart")) {
            SystemAPI.showInfoDialog("This type of query is not yet available to plot.","Not available");
            return;
        }

        if (this.dataMap == null || this.dataMapInContainer == null) {
            analyze();
        }

        extractDataToPlotFromFullData(dataToPlot);
        // in some cases there are values of -1 in the data, so we want to remove them in order to get a clean plot
        if (this.dataMapForPlot.containsValue(OMITTED_VALUES)) {
            removeNegativeValues();
        }

        gatherMetaDataForPlot(dataToPlot);

        new PlotChart(this.dataMapForPlot,this.metaDataForPlot);
    }

    /**
     * Method used to extract data to plot from full data. Fills the {@link AbstractDataResponse#dataMapForPlot} with data
     * @param dataToPlot An Enum corresponding to the available plot types
     */
    private void extractDataToPlotFromFullData(DataToPlot dataToPlot) {
        String key;
        String value;
        Map<String, String> innerMap;
        if (this.dataMapForPlot == null) {
            this.dataMapForPlot = new LinkedHashMap<>(dataMapInContainer.size());
        }

        for (Map.Entry<String,Map<String,String>> outerMap : this.dataMapInContainer.entrySet()) {
            innerMap = outerMap.getValue();
            key = outerMap.getKey();
            value = innerMap.get(dataToPlot.getValue());
            this.dataMapForPlot.put(key,value);
        }
    }

    private void removeNegativeValues() {
        this.dataMapForPlot.entrySet().removeIf(entry -> entry.getValue().equalsIgnoreCase(OMITTED_VALUES));
    }

}

