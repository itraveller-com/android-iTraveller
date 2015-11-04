package com.itraveller.model;

/**
 * Created by iTraveller on 10/29/2015.
 */
public class TestModelData {
    private String mName;
    private String mDes;
    private int mThumbnail;
    private String mTitle;
    private String mNoOfNights;
    private String mDestination;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getDes() {
        return mDes;
    }

    public void setDes(String des) {
        this.mDes = des;
    }

    public int getThumbnail() {
        return mThumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.mThumbnail = thumbnail;
    }

    public String getTitle()
    {
        return mTitle;
    }
    public void setTitle(String title)
    {
        mTitle=title;
    }
    public String getNoOfNights()
    {
        return mNoOfNights;
    }
    public void setNoOfNights(String noOfNights)
    {
        mNoOfNights=noOfNights;
    }
    public String getDestination()
    {
        return mDestination;
    }
    public void setDestination(String dest)
    {
        mDestination=dest;
    }
}
