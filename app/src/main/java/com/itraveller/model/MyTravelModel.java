package com.itraveller.model;

/**
 * Created by VNK on 6/26/2015.
 */
public class MyTravelModel {


    String Travel_Date,Booking_Date,Region_Name,
    Hotel_Name,Destination_Id,Hotel_Star_Rating,Hotel_CheckIn_Date,Hotel_CheckOut_Date,
    Transportation_Start_Date,Transportation_End_Date;

    int Transportation_Travel_Id,No_of_Room_Days,Hotel_Id,No_of_Rooms,Itinerary_id,No_of_days,Package_value,Region_Id,Transportation_No_of_Days;

    public MyTravelModel() {
    }

    public MyTravelModel(int Itinerary_id,int Package_value,String Travel_Date,int No_of_Days,String Booking_Date,int Region_Id,String Region_Name,String Destination_Id,int Hotel_Id, String Hotel_Name,int No_of_Rooms , String Hotel_CheckIn_Date, String Hotel_CheckOut_Date,int No_of_Room_Days,  String Hotel_Star_Rating,int  Transportation_Travel_Id,String Transportation_Start_Date,String Transportation_End_Date,int Transportation_No_of_Days) {
        this.Itinerary_id=Itinerary_id;
        this.Package_value=Package_value;
        this.Travel_Date=Travel_Date;
        this.No_of_days=No_of_Days;
        this.No_of_Rooms=No_of_Rooms;
        this.Booking_Date=Booking_Date;
        this.Region_Id=Region_Id;
        this.Region_Name=Region_Name;
        this.Destination_Id=Destination_Id;
        this.Hotel_Id=Hotel_Id;
        this.Hotel_Name=Hotel_Name;
        this.Hotel_CheckIn_Date=Hotel_CheckIn_Date;
        this.Hotel_CheckOut_Date=Hotel_CheckOut_Date;
        this.No_of_Room_Days=No_of_Room_Days;
        this.Hotel_Star_Rating=Hotel_Star_Rating;
        this.Transportation_Travel_Id=Transportation_Travel_Id;
        this.Transportation_Start_Date=Transportation_Start_Date;
        this.Transportation_End_Date=Transportation_End_Date;
        this.Transportation_No_of_Days=Transportation_No_of_Days;
    }
    public int getItineraty_Id() {
        return this.Itinerary_id;
    }

    public void setItinerary_Id(int Itinerary_id) {
        this.Itinerary_id=Itinerary_id;
    }

    public int getPackageValue() {
        return this.Package_value;
    }

    public void setPackageValue(int Package_value) {
        this.Package_value=Package_value;
    }

    public String getTravelDate() {
        return Travel_Date;
    }

    public void setTravelDate(String Travel_Date) {
        this.Travel_Date=Travel_Date;
    }


    public int getNo_of_Days() {
        return this.No_of_days;
    }

    public void setNo_of_Days(int No_of_Days ){
        this.No_of_days=No_of_Days;
    }

    public String getBooking_Date() {
        return this.Booking_Date;
    }

    public void setBooking_Date(String Booking_Date) {
        this.Booking_Date=Booking_Date;
    }

    public int getRegion_Id() {
        return this.Region_Id;
    }


    public void setRegion_Id(int Region_Id) {
        this.Region_Id = Region_Id;
    }

    public String getRegion_Name() {
        return this.Region_Name;
    }

    public void setRegion_Name(String Region_Name) {
        this.Region_Name=Region_Name;
    }


    public String getDestination_Id() {
        return Destination_Id;
    }

    public void setDestination_Id(String destination_Id) {
        Destination_Id = destination_Id;
    }

    public int getHotel_Id() {
        return Hotel_Id;
    }

    public void setHotel_Id(int hotel_Id) {
        Hotel_Id = hotel_Id;
    }

    public String getHotel_Name() {
        return Hotel_Name;
    }

    public void setHotel_Name(String hotel_Name) {
        Hotel_Name = hotel_Name;
    }

    public int getNo_of_Room_Days(){
        return this.No_of_Room_Days;
    }

    public void setNo_of_Room_Days(int No_of_Room_Days) {
        this.No_of_Room_Days=No_of_Room_Days;
    }

    public String getHotel_Star_Rating() {
        return Hotel_Star_Rating;
    }

    public void setHotel_Star_Rating(String Hotel_Star_Rating) {
        this.Hotel_Star_Rating=Hotel_Star_Rating;
    }

    public int getTransportation_Travel_Id(){
        return this.Transportation_Travel_Id;
    }

    public void setTransportation_Travel_Id(int Transportation_Travel_Id) {
        this.Transportation_Travel_Id=Transportation_Travel_Id;
    }
    public String getTransportation_Start_Date() {
        return this.Transportation_Start_Date;
    }

    public void setTransportation_Start_Date(String Transportation_Start_Date) {
        this.Transportation_Start_Date=Transportation_Start_Date;
    }
    public String getTransportation_End_Date() {
        return this.Transportation_End_Date;
    }

    public void setTransportation_End_Date(String Transportation_End_Date) {
        this.Transportation_End_Date=Transportation_End_Date;
    }

    public int getTransportation_No_of_Days(){
        return this.Transportation_No_of_Days;
    }

    public void setTransportation_No_of_Days(int Transportation_No_of_Days) {
        this.Transportation_Travel_Id=Transportation_Travel_Id;
    }
}
