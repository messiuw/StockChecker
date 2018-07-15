package com.messiuw.utilities;

import com.messiuw.system.SystemAPI;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class AbstractAnalyzer {

    protected String keyName;

    protected JSONObject jsonObject = null;
    protected JSONArray jsonArray = null;

    protected Map<String,String> dataMap = new LinkedHashMap<>();
    protected Map<String,Map<String,String>> dataSortedBySuperKey = new LinkedHashMap<>();

    private static final boolean DEBUG = false;

    public void analyze(String keyName) {
        if (this.jsonArray != null && keyName != null) {
            this.keyName = keyName;
            putJsonArrayInContainer();
        } else if (this.jsonObject != null) {
            putJsonObjectInContainer();
        } else {
            SystemAPI.showInfoDialog("No data available to be analyzed",this.getClass());
        }
    }

    public void analyze() {
        analyze(null);
    }

    protected abstract void putJsonArrayInContainer();

    protected abstract void putJsonObjectInContainer();

    protected abstract void putJsonObjectInContainer(JSONObject jsonObject, Map<String,String> inputOutputMap);

    protected void putObjectInMap(Object p_value, String p_key, Map<String,String> p_inOutMap) {
        if (p_value instanceof String) {
            p_inOutMap.put(p_key,(String) p_value);
        } else if (p_value instanceof Double) {
            Double doubleValue = (Double) p_value;
            p_inOutMap.put(p_key,doubleValue.toString());
        } else if (p_value instanceof Integer) {
            Integer integerValue = (Integer) p_value;
            p_inOutMap.put(p_key,integerValue.toString());
        } else if (p_value instanceof Long) {
            Long longValue = (Long) p_value;
            p_inOutMap.put(p_key,longValue.toString());
        }
    }

    protected void putMapInContainer(String superKey, Map<String,String> innerMap) {
        if (superKey != null) {
            this.dataSortedBySuperKey.put(superKey,innerMap);
        }
    }

    public Map<String,String> getSimpleDataMap() {
        return Collections.unmodifiableMap(this.dataMap);
    }

    public Map<String,Map<String,String>> getComplexDataMap() {
        return Collections.unmodifiableMap(this.dataSortedBySuperKey);
    }

    public String getKeyName() {
        return this.keyName;
    }

    protected void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    protected void setJsonArray(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }
}
