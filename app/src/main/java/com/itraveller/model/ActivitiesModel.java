package com.itraveller.model;

/**
 * Created by VNK on 6/26/2015.
 */
public class ActivitiesModel {

    int Id, Cost, Markup, Display, Status, Destination_Id, Flag;

    String  Hotel_Id, Region_Id, Company_Id, Title, Day, Duration, Image,Description, Not_Available_Month, Not_Available_Days, Destination_Id_From, Bookable;
    boolean checked=false;

   public ActivitiesModel() {
   }

   public ActivitiesModel(int id, int cost, int markup, int display, int status, int destination_Id, int flag, String hotel_Id, String region_Id, String company_Id, String title, String day, String duration, String image, String description, String not_Available_Month, String not_Available_Days, String destination_Id_From, String bookable, boolean checked) {
      Id = id;
      Cost = cost;
      Markup = markup;
      Display = display;
      Status = status;
      Destination_Id = destination_Id;
      Flag = flag;
      Hotel_Id = hotel_Id;
      Region_Id = region_Id;
      Company_Id = company_Id;
      Title = title;
      Day = day;
      Duration = duration;
      Image = image;
      Description = description;
      Not_Available_Month = not_Available_Month;
      Not_Available_Days = not_Available_Days;
      Destination_Id_From = destination_Id_From;
      Bookable = bookable;
      this.checked = checked;
   }

   public int getId() {
      return Id;
   }

   public void setId(int id) {
      Id = id;
   }

   public int getCost() {
      return Cost;
   }

   public void setCost(int cost) {
      Cost = cost;
   }

   public int getMarkup() {
      return Markup;
   }

   public void setMarkup(int markup) {
      Markup = markup;
   }

   public int getDisplay() {
      return Display;
   }

   public void setDisplay(int display) {
      Display = display;
   }

   public int getStatus() {
      return Status;
   }

   public void setStatus(int status) {
      Status = status;
   }

   public int getDestination_Id() {
      return Destination_Id;
   }

   public void setDestination_Id(int destination_Id) {
      Destination_Id = destination_Id;
   }

   public int getFlag() {
      return Flag;
   }

   public void setFlag(int flag) {
      Flag = flag;
   }

   public String getHotel_Id() {
      return Hotel_Id;
   }

   public void setHotel_Id(String hotel_Id) {
      Hotel_Id = hotel_Id;
   }

   public String getRegion_Id() {
      return Region_Id;
   }

   public void setRegion_Id(String region_Id) {
      Region_Id = region_Id;
   }

   public String getCompany_Id() {
      return Company_Id;
   }

   public void setCompany_Id(String company_Id) {
      Company_Id = company_Id;
   }

   public String getTitle() {
      return Title;
   }

   public void setTitle(String title) {
      Title = title;
   }

   public String getDay() {
      return Day;
   }

   public void setDay(String day) {
      Day = day;
   }

   public String getDuration() {
      return Duration;
   }

   public void setDuration(String duration) {
      Duration = duration;
   }

   public String getImage() {
      return Image;
   }

   public void setImage(String image) {
      Image = image;
   }

   public String getDescription() {
      return Description;
   }

   public void setDescription(String description) {
      Description = description;
   }

   public String getNot_Available_Month() {
      return Not_Available_Month;
   }

   public void setNot_Available_Month(String not_Available_Month) {
      Not_Available_Month = not_Available_Month;
   }

   public String getNot_Available_Days() {
      return Not_Available_Days;
   }

   public void setNot_Available_Days(String not_Available_Days) {
      Not_Available_Days = not_Available_Days;
   }

   public String getDestination_Id_From() {
      return Destination_Id_From;
   }

   public void setDestination_Id_From(String destination_Id_From) {
      Destination_Id_From = destination_Id_From;
   }

   public String getBookable() {
      return Bookable;
   }

   public void setBookable(String bookable) {
      Bookable = bookable;
   }

   public boolean isChecked() {
      return checked;
   }

   public void setChecked(boolean checked) {
      this.checked = checked;
   }
}
