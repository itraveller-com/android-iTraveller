package com.itraveller.model;

/**
 * Created by VNK on 6/29/2015.
 */
public class SearchBarModel {

    String value,key;

    public SearchBarModel() {
    }

    public SearchBarModel(String value, String key) {
        this.value = value;
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
