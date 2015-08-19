package com.itraveller.model;

/**
 * Created by VNK on 6/21/2015.
 */
public class OldPortAndLocModel {

    int Destination_Id, Pivot_Latitude, Pivot_Longitude, Enable_Flag, Min_Stay, Port, Sight_Seeing, Region;

    String Destination_Name, District, State, Alias, Story, Page_Title, Page_Description, Page_Heading, Code, Type, Sealevel, Summer_Temp, Winter_Temp, Date, admin_Id;

    public OldPortAndLocModel() {
    }

    public OldPortAndLocModel(int destination_Id, int pivot_Latitude, int pivot_Longitude, int enable_Flag, int min_Stay, int port, int sight_Seeing, int region, String destination_Name, String district, String state, String alias, String story, String page_Title, String page_Description, String page_Heading, String code, String type, String sealevel, String summer_Temp, String winter_Temp, String date, String admin_Id) {
        Destination_Id = destination_Id;
        Pivot_Latitude = pivot_Latitude;
        Pivot_Longitude = pivot_Longitude;
        Enable_Flag = enable_Flag;
        Min_Stay = min_Stay;
        Port = port;
        Sight_Seeing = sight_Seeing;
        Region = region;
        Destination_Name = destination_Name;
        District = district;
        State = state;
        Alias = alias;
        Story = story;
        Page_Title = page_Title;
        Page_Description = page_Description;
        Page_Heading = page_Heading;
        Code = code;
        Type = type;
        Sealevel = sealevel;
        Summer_Temp = summer_Temp;
        Winter_Temp = winter_Temp;
        Date = date;
        this.admin_Id = admin_Id;
    }

    public int getDestination_Id() {
        return Destination_Id;
    }

    public void setDestination_Id(int destination_Id) {
        Destination_Id = destination_Id;
    }

    public int getPivot_Latitude() {
        return Pivot_Latitude;
    }

    public void setPivot_Latitude(int pivot_Latitude) {
        Pivot_Latitude = pivot_Latitude;
    }

    public int getPivot_Longitude() {
        return Pivot_Longitude;
    }

    public void setPivot_Longitude(int pivot_Longitude) {
        Pivot_Longitude = pivot_Longitude;
    }

    public int getEnable_Flag() {
        return Enable_Flag;
    }

    public void setEnable_Flag(int enable_Flag) {
        Enable_Flag = enable_Flag;
    }

    public int getMin_Stay() {
        return Min_Stay;
    }

    public void setMin_Stay(int min_Stay) {
        Min_Stay = min_Stay;
    }

    public int getPort() {
        return Port;
    }

    public void setPort(int port) {
        Port = port;
    }

    public int getSight_Seeing() {
        return Sight_Seeing;
    }

    public void setSight_Seeing(int sight_Seeing) {
        Sight_Seeing = sight_Seeing;
    }

    public int getRegion() {
        return Region;
    }

    public void setRegion(int region) {
        Region = region;
    }

    public String getDestination_Name() {
        return Destination_Name;
    }

    public void setDestination_Name(String destination_Name) {
        Destination_Name = destination_Name;
    }

    public String getDistrict() {
        return District;
    }

    public void setDistrict(String district) {
        District = district;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getAlias() {
        return Alias;
    }

    public void setAlias(String alias) {
        Alias = alias;
    }

    public String getStory() {
        return Story;
    }

    public void setStory(String story) {
        Story = story;
    }

    public String getPage_Title() {
        return Page_Title;
    }

    public void setPage_Title(String page_Title) {
        Page_Title = page_Title;
    }

    public String getPage_Description() {
        return Page_Description;
    }

    public void setPage_Description(String page_Description) {
        Page_Description = page_Description;
    }

    public String getPage_Heading() {
        return Page_Heading;
    }

    public void setPage_Heading(String page_Heading) {
        Page_Heading = page_Heading;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getSealevel() {
        return Sealevel;
    }

    public void setSealevel(String sealevel) {
        Sealevel = sealevel;
    }

    public String getSummer_Temp() {
        return Summer_Temp;
    }

    public void setSummer_Temp(String summer_Temp) {
        Summer_Temp = summer_Temp;
    }

    public String getWinter_Temp() {
        return Winter_Temp;
    }

    public void setWinter_Temp(String winter_Temp) {
        Winter_Temp = winter_Temp;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getAdmin_Id() {
        return admin_Id;
    }

    public void setAdmin_Id(String admin_Id) {
        this.admin_Id = admin_Id;
    }
}
