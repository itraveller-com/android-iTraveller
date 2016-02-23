package com.itraveller.dashboard;

/**
 * Created by rohan bundelkhandi on 19/02/2016.
 */
public class MyTravelHotelModel {

    private String mHotelName;
    private String mHotelCheckInDate;
    private String mHotelCheckOutDate;
    private String mDestinationName;
    private String mHotelRoomType;
    private int mNoOfRoomDays;
    private int mNoOfRooms;

    public String getHotelName() {
        return mHotelName;
    }

    public void setHotelName(String mHotelName) {
        this.mHotelName = mHotelName;
    }

    public String getCheckInDate() {
        return mHotelCheckInDate;
    }

    public void setCheckInDate(String mHotelCheckInDate) {
        this.mHotelCheckInDate=mHotelCheckInDate;
    }

    public String getCheckOutDate() {
        return mHotelCheckOutDate;
    }

    public void setCheckOutDate(String mHotelCheckOutDate) {
        this.mHotelCheckOutDate=mHotelCheckOutDate;
    }


    public int getNoOfRoomDays(){
        return this.mNoOfRoomDays;
    }

    public void setNoOfRoomDays(int mNoOfRoomDays) {
        this.mNoOfRoomDays=mNoOfRoomDays;
    }

    public String getHotelRoomType(){
        return this.mHotelRoomType;
    }

    public void setHotelRoomType(String mHotelRoomType) {
        this.mHotelRoomType=mHotelRoomType;
    }

    public int getNoOfRooms() {
        return mNoOfRooms;
    }

    public void setNoOfRooms(int mNoOfRooms) {
        this.mNoOfRooms = mNoOfRooms;
    }

    public String getDestinationName(){
        return this.mDestinationName;
    }

    public void setDestinationName(String mDestinationName) {
        this.mDestinationName=mDestinationName;
    }
}
