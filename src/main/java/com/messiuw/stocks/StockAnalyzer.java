package com.messiuw.stocks;

import com.messiuw.system.SystemAPI;
import com.messiuw.utilities.AbstractAnalyzer;
import org.json.JSONArray;
import org.json.JSONObject;

class StockAnalyzer extends AbstractAnalyzer {

    StockAnalyzer(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    StockAnalyzer(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    void analyzeOhlc() {
        // maybe we need a special implementation for ohlc requests...see also deprected class _StockResult
        SystemAPI.showInfoDialog("Not yet implemented",this.getClass());
    }

    public void gatherMetaDataForPlot() {

    }

}
