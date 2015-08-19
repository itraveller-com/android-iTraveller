package com.itraveller.model;

/**
 * Created by VNK-LAP on 5/29/2015.
 */
public class PortAndLocModel {

    private int Key;

    private String Value;

    public PortAndLocModel() {
    }

    public PortAndLocModel(int key, String value) {
        Key = key;
        Value = value;
    }

    public int getKey() {
        return Key;
    }

    public void setKey(int key) {
        Key = key;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }
}
