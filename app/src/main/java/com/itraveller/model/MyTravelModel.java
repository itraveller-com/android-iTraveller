package com.itraveller.model;

/**
 * Created by VNK on 6/26/2015.
 */
public class MyTravelModel {


    String Travel_Date,Booking_Date,Booking_Mode,Region_Name,Itinerary_Main_Id,
    Hotel_Name,Destination_Id,Hotel_Star_Rating,Hotel_CheckIn_Date,Hotel_CheckOut_Date,
    Transportation_Start_Date,Transportation_End_Date,Customer_Name,Customer_Email,Discount_value,
    Adv_amt,Inter_amt,Final_amt,Flight_price,Destination_name,Hotel_Room_Type,Transportation_Name,Activity_Name,Activity_Date, Customer_Phone_Number;

    int Transportation_Travel_Id,No_of_Room_Days,Hotel_Id,No_of_Rooms,Itinerary_id,No_of_days,Package_value,Region_Id,Customer_Id,
            No_of_Adults,No_of_Child,No_of_Infant;

    public MyTravelModel() {
    }

    public int getItineraty_Id() {
        return this.Itinerary_id;
    }

    public void setItinerary_Id(int Itinerary_id) {
        this.Itinerary_id=Itinerary_id;
    }

    public int getNo_of_Adults() {
        return this.No_of_Adults;
    }

    public void setNo_of_Adults(int No_of_Adults) {
        this.No_of_Adults=No_of_Adults;
    }

    public int getNo_of_Child() {
        return this.No_of_Child;
    }

    public void setNo_of_Child(int No_of_Child) {
        this.No_of_Child=No_of_Child;
    }

    public int getNo_of_Infant() {
        return this.No_of_Infant;
    }

    public void setNo_of_Infant(int No_of_Infant) {
        this.No_of_Infant=No_of_Infant;
    }

    public String getItinerary_Main_Id() {
        return Itinerary_Main_Id;
    }

    public void setItinerary_Main_Id(String Itinerary_Main_Id) {
        this.Itinerary_Main_Id=Itinerary_Main_Id;
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

    public String getBooking_Mode() {
        return this.Booking_Mode;
    }

    public void setBooking_Mode(String Booking_Mode) {
        this.Booking_Mode=Booking_Mode;
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


    public String getCheckInDate() {
        return Hotel_CheckIn_Date;
    }

    public void setCheckInDate(String Hotel_CheckIn_Date) {
        this.Hotel_CheckIn_Date=Hotel_CheckIn_Date;
    }

    public String getCheckOutDate() {
        return Hotel_CheckOut_Date;
    }

    public void setCheckOutDate(String Hotel_CheckOut_Date) {
        this.Hotel_CheckOut_Date=Hotel_CheckOut_Date;
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


    public int getCustomer_Id(){
        return this.Customer_Id;
    }

    public void setCustomer_Id(int Customer_Id) {
        this.Customer_Id=Customer_Id;
    }
    public String getCustomer_Name(){
        return this.Customer_Name;
    }

    public void setCustomer_Name(String Customer_Name) {
        this.Customer_Name=Customer_Name;
    }
    public String getCustomer_Email(){
        return this.Customer_Email;
    }

    public void setCustomer_Email(String Customer_Email) {
        this.Customer_Email=Customer_Email;
    }

    public String getAdv_amt(){
        return this.Adv_amt;
    }

    public void setAdv_amt(String Adv_amt) {
        this.Adv_amt=Adv_amt;
    }

    public String getInter_amt(){
        return this.Inter_amt;
    }

    public void setInter_amt(String Inter_amt) {
        this.Inter_amt=Inter_amt;
    }

    public String getFinal_amt(){
        return this.Final_amt;
    }

    public void setFinal_amt(String Final_amt) {
        this.Final_amt=Final_amt;
    }

    public String getFlight_price(){
        return this.Flight_price;
    }

    public void setFlight_price(String Flight_price) {
        this.Flight_price=Flight_price;
    }


    public String getDestination_name(){
        return this.Destination_name;
    }

    public void setDestination_name(String Destination_name) {
        this.Destination_name=Destination_name;
    }

    public String getTransportation_Name(){
        return this.Transportation_Name;
    }

    public void setTransportation_Name(String Transportation_Name) {
        this.Transportation_Name=Transportation_Name;
    }

    public String getActivity_Name(){
        return this.Activity_Name;
    }

    public void setActivity_Name(String Activity_Name) {
        this.Activity_Name=Activity_Name;
    }
    public String getActivity_Date(){
        return this.Activity_Date;
    }

    public void setActivity_Date(String Activity_Date) {
        this.Activity_Date=Activity_Date;
    }


    public String getHotel_Room_Type(){
        return this.Hotel_Room_Type;
    }

    public void setHotel_Room_Type(String Hotel_Room_Type) {
        this.Hotel_Room_Type=Hotel_Room_Type;
    }


    public String getCustomer_Phone_Number(){
        return this.Customer_Phone_Number;
    }

    public void setCustomer_Phone_Number(String Customer_Phone_Number) {
        this.Customer_Phone_Number=Customer_Phone_Number;
    }


    public int getNo_of_Rooms() {
        return No_of_Rooms;
    }

    public void setNo_of_Rooms(int No_of_Rooms) {
        this.No_of_Rooms = No_of_Rooms;
    }


    public String getDiscount_value(){
        return this.Discount_value;
    }

    public void setDiscount_value(String Discount_value) {
        this.Discount_value=Discount_value;
    }

}
