package com.itraveller.model;

/**
 * Created by VNK on 6/25/2015.
 */
public class ModelClass {


    private String titleToDisplay;



    public ModelClass(String question) {
        super();
        this.titleToDisplay = question;
    }

    public String getTitleToDisplay() {
        return titleToDisplay;
    }

    public void setTitleToDisplay(String titleToDisplay) {
        this.titleToDisplay = titleToDisplay;
    }
}
