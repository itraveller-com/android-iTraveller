package com.itraveller.dashboard;

/**
 * Created by rohan bundelkhandi on 19/02/2016.
 */
public class MyTravelActivityModel {

    private String mActivityName;
    private String mActivityDate;

    public String getActivityName(){
        return this.mActivityName;
    }

    public void setActivityName(String mActivityName) {
        this.mActivityName=mActivityName;
    }
    public String getActivityDate(){
        return this.mActivityDate;
    }

    public void setActivityDate(String mActivityDate) {
        this.mActivityDate=mActivityDate;
    }

}
