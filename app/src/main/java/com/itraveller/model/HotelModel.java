package com.itraveller.model;

/**
 * Created by VNK on 6/26/2015.
 */
public class HotelModel {

    int Hotel_Id, Hotel_Status, Dinner, Lunch, B2C_Flag, Extra_Adult, Visibility;


    String Region_Id,Destination_Id, Hotel_Name, Hotel_Email, Hotel_Description, Hotel_Tripadvisor, Hotel_Meal_Plan, Hotel_Image,
            Hotel_Star_Rating, Hotel_Address, Hotel_Latitude, Hotel_Longitude, Hotel_URL, Hotel_Number, District, State, Country,
            Pincode, Website, Trip_Image, Trip_Script, Account_Holder, Account_Number, Bank, Branch, IFSC_Code, Date, admin_Id;
    boolean checked=false;

   public HotelModel() {
   }

   public HotelModel(int hotel_Id, int hotel_Status, int dinner, int lunch, int b2C_Flag, int extra_Adult, int visibility, String region_Id, String destination_Id, String hotel_Name, String hotel_Email, String hotel_Description, String hotel_Tripadvisor, String hotel_Meal_Plan, String hotel_Image, String hotel_Star_Rating, String hotel_Address, String hotel_Latitude, String hotel_Longitude, String hotel_URL, String hotel_Number, String district, String state, String country, String pincode, String website, String trip_Image, String trip_Script, String account_Holder, String account_Number, String bank, String branch, String IFSC_Code, String date, String admin_Id,boolean checked) {
      Hotel_Id = hotel_Id;
      Hotel_Status = hotel_Status;
      Dinner = dinner;
      Lunch = lunch;
      B2C_Flag = b2C_Flag;
      Extra_Adult = extra_Adult;
      Visibility = visibility;
      Region_Id = region_Id;
      Destination_Id = destination_Id;
      Hotel_Name = hotel_Name;
      Hotel_Email = hotel_Email;
      Hotel_Description = hotel_Description;
      Hotel_Tripadvisor = hotel_Tripadvisor;
      Hotel_Meal_Plan = hotel_Meal_Plan;
      Hotel_Image = hotel_Image;
      Hotel_Star_Rating = hotel_Star_Rating;
      Hotel_Address = hotel_Address;
      Hotel_Latitude = hotel_Latitude;
      Hotel_Longitude = hotel_Longitude;
      Hotel_URL = hotel_URL;
      Hotel_Number = hotel_Number;
      District = district;
      State = state;
      Country = country;
      Pincode = pincode;
      Website = website;
      Trip_Image = trip_Image;
      Trip_Script = trip_Script;
      Account_Holder = account_Holder;
      Account_Number = account_Number;
      Bank = bank;
      Branch = branch;
      this.IFSC_Code = IFSC_Code;
      Date = date;
      this.admin_Id = admin_Id;
      this.checked=checked;
   }

   public int getHotel_Id() {
      return Hotel_Id;
   }

   public void setHotel_Id(int hotel_Id) {
      Hotel_Id = hotel_Id;
   }

   public int getHotel_Status() {
      return Hotel_Status;
   }

   public void setHotel_Status(int hotel_Status) {
      Hotel_Status = hotel_Status;
   }

   public int getDinner() {
      return Dinner;
   }

   public void setDinner(int dinner) {
      Dinner = dinner;
   }

   public int getLunch() {
      return Lunch;
   }

   public void setLunch(int lunch) {
      Lunch = lunch;
   }

   public int getB2C_Flag() {
      return B2C_Flag;
   }

   public void setB2C_Flag(int b2C_Flag) {
      B2C_Flag = b2C_Flag;
   }

   public int getExtra_Adult() {
      return Extra_Adult;
   }

   public void setExtra_Adult(int extra_Adult) {
      Extra_Adult = extra_Adult;
   }

   public int getVisibility() {
      return Visibility;
   }

   public void setVisibility(int visibility) {
      Visibility = visibility;
   }

   public String getRegion_Id() {
      return Region_Id;
   }

   public void setRegion_Id(String region_Id) {
      Region_Id = region_Id;
   }

   public String getDestination_Id() {
      return Destination_Id;
   }

   public void setDestination_Id(String destination_Id) {
      Destination_Id = destination_Id;
   }

   public String getHotel_Name() {
      return Hotel_Name;
   }

   public void setHotel_Name(String hotel_Name) {
      Hotel_Name = hotel_Name;
   }

   public String getHotel_Email() {
      return Hotel_Email;
   }

   public void setHotel_Email(String hotel_Email) {
      Hotel_Email = hotel_Email;
   }

   public String getHotel_Description() {
      return Hotel_Description;
   }

   public void setHotel_Description(String hotel_Description) {
      Hotel_Description = hotel_Description;
   }

   public String getHotel_Tripadvisor() {
      return Hotel_Tripadvisor;
   }

   public void setHotel_Tripadvisor(String hotel_Tripadvisor) {
      Hotel_Tripadvisor = hotel_Tripadvisor;
   }

   public String getHotel_Meal_Plan() {
      return Hotel_Meal_Plan;
   }

   public void setHotel_Meal_Plan(String hotel_Meal_Plan) {
      Hotel_Meal_Plan = hotel_Meal_Plan;
   }

   public String getHotel_Image() {
      return Hotel_Image;
   }

   public void setHotel_Image(String hotel_Image) {
      Hotel_Image = hotel_Image;
   }

   public String getHotel_Star_Rating() {
      return Hotel_Star_Rating;
   }

   public void setHotel_Star_Rating(String hotel_Star_Rating) {
      Hotel_Star_Rating = hotel_Star_Rating;
   }

   public String getHotel_Address() {
      return Hotel_Address;
   }

   public void setHotel_Address(String hotel_Address) {
      Hotel_Address = hotel_Address;
   }

   public String getHotel_Latitude() {
      return Hotel_Latitude;
   }

   public void setHotel_Latitude(String hotel_Latitude) {
      Hotel_Latitude = hotel_Latitude;
   }

   public String getHotel_Longitude() {
      return Hotel_Longitude;
   }

   public void setHotel_Longitude(String hotel_Longitude) {
      Hotel_Longitude = hotel_Longitude;
   }

   public String getHotel_URL() {
      return Hotel_URL;
   }

   public void setHotel_URL(String hotel_URL) {
      Hotel_URL = hotel_URL;
   }

   public String getHotel_Number() {
      return Hotel_Number;
   }

   public void setHotel_Number(String hotel_Number) {
      Hotel_Number = hotel_Number;
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

   public String getCountry() {
      return Country;
   }

   public void setCountry(String country) {
      Country = country;
   }

   public String getPincode() {
      return Pincode;
   }

   public void setPincode(String pincode) {
      Pincode = pincode;
   }

   public String getWebsite() {
      return Website;
   }

   public void setWebsite(String website) {
      Website = website;
   }

   public String getTrip_Image() {
      return Trip_Image;
   }

   public void setTrip_Image(String trip_Image) {
      Trip_Image = trip_Image;
   }

   public String getTrip_Script() {
      return Trip_Script;
   }

   public void setTrip_Script(String trip_Script) {
      Trip_Script = trip_Script;
   }

   public String getAccount_Holder() {
      return Account_Holder;
   }

   public void setAccount_Holder(String account_Holder) {
      Account_Holder = account_Holder;
   }

   public String getAccount_Number() {
      return Account_Number;
   }

   public void setAccount_Number(String account_Number) {
      Account_Number = account_Number;
   }

   public String getBank() {
      return Bank;
   }

   public void setBank(String bank) {
      Bank = bank;
   }

   public String getBranch() {
      return Branch;
   }

   public void setBranch(String branch) {
      Branch = branch;
   }

   public String getIFSC_Code() {
      return IFSC_Code;
   }

   public void setIFSC_Code(String IFSC_Code) {
      this.IFSC_Code = IFSC_Code;
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

   public boolean isChecked() {
      return checked;
   }

   public void setChecked(boolean checked) {
      this.checked = checked;
   }
}
