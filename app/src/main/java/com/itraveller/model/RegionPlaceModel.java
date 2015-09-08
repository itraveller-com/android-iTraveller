package com.itraveller.model;

/**
 * Created by VNK on 6/10/2015.
 */
public class RegionPlaceModel {

    private int Arrival_Port_Id, Departure_Port_Id, Itinerary_Id, Duration_Day, Price, Discount;

    private String Title, Link, Image, Destination, Destination_Key, Destination_Count;

    public RegionPlaceModel() {


    }

    public RegionPlaceModel(int arrival_Port_Id, String destination_Count, int departure_Port_Id, int itinerary_Id, int duration_Day, int price, int discount, String destination_Key, String title, String link, String image, String destination) {
        Arrival_Port_Id = arrival_Port_Id;
        Departure_Port_Id = departure_Port_Id;
        Itinerary_Id = itinerary_Id;
        Duration_Day = duration_Day;
        Price = price;
        Discount = discount;
        Destination_Key = destination_Key;
        Title = title;
        Link = link;
        Image = image;
        Destination = destination;
        Destination_Count = destination_Count;
    }
    public String getDestination_Count() {
        return Destination_Count;
    }

    public void setDestination_Count(String destination_Count) {
        Destination_Count = destination_Count;
    }
    public int getArrival_Port_Id() {
        return Arrival_Port_Id;
    }

    public void setArrival_Port_Id(int arrival_Port_Id) {
        Arrival_Port_Id = arrival_Port_Id;
    }

    public int getDeparture_Port_Id() {
        return Departure_Port_Id;
    }

    public void setDeparture_Port_Id(int departure_Port_Id) {
        Departure_Port_Id = departure_Port_Id;
    }

    public int getItinerary_Id() {
        return Itinerary_Id;
    }

    public void setItinerary_Id(int itinerary_Id) {
        Itinerary_Id = itinerary_Id;
    }

    public int getDuration_Day() {
        return Duration_Day;
    }

    public void setDuration_Day(int duration_Day) {
        Duration_Day = duration_Day;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public int getDiscount() {
        return Discount;
    }

    public void setDiscount(int discount) {
        Discount = discount;
    }

    public String getDestination_Key() {
        return Destination_Key;
    }

    public void setDestination_Key(String destination_Key) {
        Destination_Key = destination_Key;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }
    public void setLink(String link) {
        Link = link;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getDestination() {
        return Destination;
    }

    public void setDestination(String destination) {
        Destination = destination;
    }
}