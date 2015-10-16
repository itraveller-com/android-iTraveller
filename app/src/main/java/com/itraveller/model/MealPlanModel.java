package com.itraveller.model;

/**
 * Created by iTraveller on 10/7/2015.
 */
public class MealPlanModel {

    private String From_Date, To_Date;

    private int Id;
    private int Company_Id;
    private int Hotel_Id;
    private int Hotel_Room_Id;
    private int Breakfast;
    private int Lunch;
    private int Dinner;
    private int Adult_With_Bed;
    private int Adult_Without_Bed;
    private int Child_With_Bed;
    private int Child_Without_Bed;
    private int Child_Below_Five;
    private int Markup;

    public MealPlanModel() {
    }

    public MealPlanModel(String from_Date, String to_Date, int id, int company_Id, int hotel_Id, int hotel_Room_Id, int breakfast, int lunch, int dinner, int adult_With_Bed, int adult_Without_Bed, int child_With_Bed, int child_Without_Bed, int child_Below_Five, int markup) {
        From_Date = from_Date;
        To_Date = to_Date;
        Id = id;
        Company_Id = company_Id;
        Hotel_Id = hotel_Id;
        Hotel_Room_Id = hotel_Room_Id;
        Breakfast = breakfast;
        Lunch = lunch;
        Dinner = dinner;
        Adult_With_Bed = adult_With_Bed;
        Adult_Without_Bed = adult_Without_Bed;
        Child_With_Bed = child_With_Bed;
        Child_Without_Bed = child_Without_Bed;
        Child_Below_Five = child_Below_Five;
        Markup = markup;
    }

    public String getFrom_Date() {
        return From_Date;
    }

    public void setFrom_Date(String from_Date) {
        From_Date = from_Date;
    }

    public String getTo_Date() {
        return To_Date;
    }

    public void setTo_Date(String to_Date) {
        To_Date = to_Date;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getCompany_Id() {
        return Company_Id;
    }

    public void setCompany_Id(int company_Id) {
        Company_Id = company_Id;
    }

    public int getHotel_Id() {
        return Hotel_Id;
    }

    public void setHotel_Id(int hotel_Id) {
        Hotel_Id = hotel_Id;
    }

    public int getHotel_Room_Id() {
        return Hotel_Room_Id;
    }

    public void setHotel_Room_Id(int hotel_Room_Id) {
        Hotel_Room_Id = hotel_Room_Id;
    }

    public int getBreakfast() {
        return Breakfast;
    }

    public void setBreakfast(int breakfast) {
        Breakfast = breakfast;
    }

    public int getLunch() {
        return Lunch;
    }

    public void setLunch(int lunch) {
        Lunch = lunch;
    }

    public int getDinner() {
        return Dinner;
    }

    public void setDinner(int dinner) {
        Dinner = dinner;
    }

    public int getAdult_With_Bed() {
        return Adult_With_Bed;
    }

    public void setAdult_With_Bed(int adult_With_Bed) {
        Adult_With_Bed = adult_With_Bed;
    }

    public int getAdult_Without_Bed() {
        return Adult_Without_Bed;
    }

    public void setAdult_Without_Bed(int adult_Without_Bed) {
        Adult_Without_Bed = adult_Without_Bed;
    }

    public int getChild_With_Bed() {
        return Child_With_Bed;
    }

    public void setChild_With_Bed(int child_With_Bed) {
        Child_With_Bed = child_With_Bed;
    }

    public int getChild_Without_Bed() {
        return Child_Without_Bed;
    }

    public void setChild_Without_Bed(int child_Without_Bed) {
        Child_Without_Bed = child_Without_Bed;
    }

    public int getChild_Below_Five() {
        return Child_Below_Five;
    }

    public void setChild_Below_Five(int child_Below_Five) {
        Child_Below_Five = child_Below_Five;
    }

    public int getMarkup() {
        return Markup;
    }

    public void setMarkup(int markup) {
        Markup = markup;
    }
}
