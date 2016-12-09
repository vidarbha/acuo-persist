package com.acuo.persist.core;

public interface ICypherResult {
    Boolean hasProperty(String key);

    String getStringResult(String key);

    Integer getIntegerResult(String key);
}