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

   public void setMarkup(int markup) {
      Markup = markup;
   }

   public void setDisplay(int display) {
      Display = display;
   }

   public void setStatus(int status) {
      Status = status;
   }

   public void setDestination_Id(int destination_Id) {
      Destination_Id = destination_Id;
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

   public void setRegion_Id(String region_Id) {
      Region_Id = region_Id;
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

   public void setDescription(String description) {
      Description = description;
   }

   public void setNot_Available_Month(String not_Available_Month) {
      Not_Available_Month = not_Available_Month;
   }

   public void setNot_Available_Days(String not_Available_Days) {
      Not_Available_Days = not_Available_Days;
   }

   public void setDestination_Id_From(String destination_Id_From) {
      Destination_Id_From = destination_Id_From;
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
