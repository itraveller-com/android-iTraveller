package com.itraveller.model;
/**
 * Created by VNK on 7/8/2015.
 */
public class HotelRoomModel {

    private int Hotel_Room_Id, Hotel_Id, Room_Status, Rack_Rate, Default_Number, Maximum_Number, Hotel_Room_Tariff_Id, TAC, Cost, Mark_Up, Display_Tariff, Company_Id;
    private String Room_Type, Room_Description, From, To;
    private Boolean Check = false;


    public HotelRoomModel() {
    }

    public HotelRoomModel(int hotel_Room_Id, int hotel_Id, int room_Status, int rack_Rate, int default_Number, int maximum_Number, int hotel_Room_Tariff_Id, int TAC, int cost, int mark_Up, int display_Tariff, int company_Id, String room_Type, String room_Description, String from, String to) {
        Hotel_Room_Id = hotel_Room_Id;
        Hotel_Id = hotel_Id;
        Room_Status = room_Status;
        Rack_Rate = rack_Rate;
        Default_Number = default_Number;
        Maximum_Number = maximum_Number;
        Hotel_Room_Tariff_Id = hotel_Room_Tariff_Id;
        this.TAC = TAC;
        Cost = cost;
        Mark_Up = mark_Up;
        Display_Tariff = display_Tariff;
        Company_Id = company_Id;
        Room_Type = room_Type;
        Room_Description = room_Description;
        From = from;
        To = to;
    }
    public Boolean getCheck() {
        return Check;
    }

    public void setCheck(Boolean check) {
        Check = check;
    }


    public int getHotel_Room_Id() {
        return Hotel_Room_Id;
    }

    public void setHotel_Room_Id(int hotel_Room_Id) {
        Hotel_Room_Id = hotel_Room_Id;
    }

    public int getHotel_Id() {
        return Hotel_Id;
    }

    public void setHotel_Id(int hotel_Id) {
        Hotel_Id = hotel_Id;
    }

    public int getRoom_Status() {
        return Room_Status;
    }

    public void setRoom_Status(int room_Status) {
        Room_Status = room_Status;
    }

    public int getRack_Rate() {
        return Rack_Rate;
    }

    public void setRack_Rate(int rack_Rate) {
        Rack_Rate = rack_Rate;
    }

    public int getDefault_Number() {
        return Default_Number;
    }

    public void setDefault_Number(int default_Number) {
        Default_Number = default_Number;
    }

    public int getMaximum_Number() {
        return Maximum_Number;
    }

    public void setMaximum_Number(int maximum_Number) {
        Maximum_Number = maximum_Number;
    }

    public int getHotel_Room_Tariff_Id() {
        return Hotel_Room_Tariff_Id;
    }

    public void setHotel_Room_Tariff_Id(int hotel_Room_Tariff_Id) {
        Hotel_Room_Tariff_Id = hotel_Room_Tariff_Id;
    }

    public int getTAC() {
        return TAC;
    }

    public void setTAC(int TAC) {
        this.TAC = TAC;
    }

    public int getCost() {
        return Cost;
    }

    public void setCost(int cost) {
        Cost = cost;
    }

    public int getMark_Up() {
        return Mark_Up;
    }

    public void setMark_Up(int mark_Up) {
        Mark_Up = mark_Up;
    }

    public int getDisplay_Tariff() {
        return Display_Tariff;
    }

    public void setDisplay_Tariff(int display_Tariff) {
        Display_Tariff = display_Tariff;
    }

    public int getCompany_Id() {
        return Company_Id;
    }

    public void setCompany_Id(int company_Id) {
        Company_Id = company_Id;
    }

    public String getRoom_Type() {
        return Room_Type;
    }

    public void setRoom_Type(String room_Type) {
        Room_Type = room_Type;
    }

    public String getRoom_Description() {
        return Room_Description;
    }

    public void setRoom_Description(String room_Description) {
        Room_Description = room_Description;
    }

    public String getFrom() {
        return From;
    }

    public void setFrom(String from) {
        From = from;
    }

    public String getTo() {
        return To;
    }

    public void setTo(String to) {
        To = to;
    }
}
