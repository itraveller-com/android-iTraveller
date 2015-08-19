package com.itraveller.model;

/**
 * Created by VNK on 6/10/2015.
 */
public class RearrangePlaceModel {

    private String Place, Nights;

    private int PlaceID;

    public RearrangePlaceModel() {

    }

    public int getPlaceID() {
        return PlaceID;
    }

    public void setPlaceID(int placeID) {
        PlaceID = placeID;
    }

    public String getNights() {
        return Nights;
    }

    public void setNights(String nights) {
        Nights = nights;
    }

    public String getPlace() {
        return Place;
    }

    public void setPlace(String place) {
        Place = place;
    }
}