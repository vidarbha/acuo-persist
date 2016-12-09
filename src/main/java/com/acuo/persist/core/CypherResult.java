package com.acuo.persist.core;

import java.util.Map;

public class CypherResult implements ICypherResult {

    private final Map<String, Object> results;

    public CypherResult(Map<String, Object> results) {
        this.results = results;
    }

    private Object getResult(String key) {
        if (!hasProperty(key)) {
            throw new PersistenceException("Key " + key + " not present in results. Keys " + results.keySet());
        }
        return results.get(key);
    }

    @Override
    public Integer getIntegerResult(String key) {
        return Integer.valueOf(getResult(key).toString());
    }

    @Override
    public String getStringResult(String key) {
        return getResult(key).toString();
    }

    @Override
    public Boolean hasProperty(String key) {
        return results.containsKey(key);
    }

}