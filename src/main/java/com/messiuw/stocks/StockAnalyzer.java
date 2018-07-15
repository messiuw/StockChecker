package com.messiuw.stocks;

import com.messiuw.graphics.DataToPlot;
import com.messiuw.system.SystemAPI;
import com.messiuw.utilities.AbstractAnalyzer;
import com.messiuw.utilities.FormatUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

class StockAnalyzer extends AbstractAnalyzer {

    StockAnalyzer(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    StockAnalyzer(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    void analyzeOhlc() {
        // maybe we need a special implementation for ohlc requests...
        SystemAPI.showInfoDialog("Not yet implemented",this.getClass());
    }

    public Map<String,String> extractDataToPlot(DataToPlot dataToPlot) {
        Map<String,String> returnMap = new LinkedHashMap<>(this.dataSortedBySuperKey.size());
        String key;
        String value;
        Map<String, String> innerMap;

        for (Map.Entry<String,Map<String,String>> outerMap : this.dataSortedBySuperKey.entrySet()) {
            innerMap = outerMap.getValue();
            key = outerMap.getKey();
            value = innerMap.get(dataToPlot.getValue());
            value = FormatUtil.formatNumberToUseTwoDecimals(value);
            returnMap.put(key,value);
        }

        return Collections.unmodifiableMap(returnMap);
    }

    @Override
    protected void putJsonObjectInContainer(JSONObject jsonObject, Map<String,String> inputOutputMap) {
        for (String key : jsonObject.keySet()) {
            Object object = jsonObject.get(key);
            putObjectInMap(object,key,inputOutputMap);
        }
    }

    @Override
    protected void putJsonArrayInContainer() {
        Object object;
        JSONObject jsonObject;
        Map<String,String> data;
        String superKey;

        for (int entry=0; entry<this.jsonArray.length(); entry++) {
            object = this.jsonArray.get(entry);
            data = new HashMap<>();
            if (object instanceof JSONObject) {
                jsonObject = (JSONObject) object;
                superKey = (String) jsonObject.get(this.keyName);
                putJsonObjectInContainer(jsonObject,data);
                putMapInContainer(superKey,data);
            }
        }
    }

    @Override
    protected void putJsonObjectInContainer() {
        putJsonObjectInContainer(this.jsonObject,this.dataMap);
    }
}
