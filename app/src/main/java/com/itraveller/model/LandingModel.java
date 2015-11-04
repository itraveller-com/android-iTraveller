package com.itraveller.model;

/**
 * Created by VNK-LAP on 5/29/2015.
 */
public class LandingModel {

    private int Region_Id, Slider, Home_Page, Advance, Intermediate_Payment;

    private String Region_Name, Enable_Flag, Alias, Tourism_Story, Region_Story, Left_Alias, Places_To_Visit, Page_Title, Page_Description,
            Page_Heading, Date, admin_Id , Popular;

    public LandingModel(int region_Id, int slider, int home_Page, int advance, int intermediate_Payment, String popular, String region_Name, String enable_Flag, String alias, String tourism_Story, String region_Story, String left_Alias, String places_To_Visit, String page_Title, String page_Description, String page_Heading, String date, String admin_Id) {
        Region_Id = region_Id;
        Slider = slider;
        Home_Page = home_Page;
        Advance = advance;
        Intermediate_Payment = intermediate_Payment;
        Popular = popular;
        Region_Name = region_Name;
        Enable_Flag = enable_Flag;
        Alias = alias;
        Tourism_Story = tourism_Story;
        Region_Story = region_Story;
        Left_Alias = left_Alias;
        Places_To_Visit = places_To_Visit;
        Page_Title = page_Title;
        Page_Description = page_Description;
        Page_Heading = page_Heading;
        Date = date;
        this.admin_Id = admin_Id;
    }

    public LandingModel()
    {

    }

    public int getRegion_Id() {
        return Region_Id;
    }

    public void setRegion_Id(int region_Id) {
        Region_Id = region_Id;
    }

    public void setSlider(int slider) {
        Slider = slider;
    }

    public int getHome_Page() {
        return Home_Page;
    }

    public void setHome_Page(int home_Page) {
        Home_Page = home_Page;
    }

    public void setAdvance(int advance) {
        Advance = advance;
    }

    public void setIntermediate_Payment(int intermediate_Payment) {
        Intermediate_Payment = intermediate_Payment;
    }

    public void setPopular(String popular) {
        Popular = popular;
    }

    public String getRegion_Name() {
        return Region_Name;
    }

    public void setRegion_Name(String region_Name) {
        Region_Name = region_Name;
    }

    public void setEnable_Flag(String enable_Flag) {
        Enable_Flag = enable_Flag;
    }


    public void setAlias(String alias) {
        Alias = alias;
    }

    public void setTourism_Story(String tourism_Story) {
        Tourism_Story = tourism_Story;
    }

    public void setRegion_Story(String region_Story) {
        Region_Story = region_Story;
    }


    public void setLeft_Alias(String left_Alias) {
        Left_Alias = left_Alias;
    }


    public void setPlaces_To_Visit(String places_To_Visit) {
        Places_To_Visit = places_To_Visit;
    }


    public void setPage_Title(String page_Title) {
        Page_Title = page_Title;
    }

    public String getPage_Title() {
        return Page_Title;
    }
    public void setPage_Description(String page_Description) {
        Page_Description = page_Description;
    }

    public void setPage_Heading(String page_Heading) {
        Page_Heading = page_Heading;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }


    public void setAdmin_Id(String admin_Id) {
        this.admin_Id = admin_Id;
    }

}
