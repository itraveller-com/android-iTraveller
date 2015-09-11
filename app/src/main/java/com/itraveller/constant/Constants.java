package com.itraveller.constant;

import android.content.SharedPreferences;

import com.itraveller.activity.ActivitiesActivity;
import com.itraveller.activity.FlightActivity;
import com.itraveller.activity.HotelActivity;
import com.itraveller.activity.HotelRoomActvity;
import com.itraveller.activity.ItinerarySummaryActivity;
import com.itraveller.activity.LoginFragment;
import com.itraveller.activity.MainActivity;
import com.itraveller.activity.RegionPlace;
import com.itraveller.activity.SignupFragment;
import com.itraveller.adapter.LandingAdapter;
import com.itraveller.adapter.TransportationAdapter;
import com.itraveller.adapter.ViewPagerActivitiesAdapter;
import com.itraveller.adapter.ViewPagerAdapter;

/**
 * Created by VNK on 6/22/2015.
 */
public class Constants {


    public static String API_ViewPagerAdapter_ImageURL = "http://stage.itraveller.com/backend/images/hotels/";


    public static String API_TransportationActivity_Tra_URL = "http://stage.itraveller.com/backend/api/v1/b2ctransportation?transportationId=";

    public static String API_ItinerarySummaryActivity_ImageURL = "http://stage.itraveller.com/backend/images/hotels/" ;

    public static String API_HotelRoomActivity_CheckRoom = "http://stage.itraveller.com/backend/api/v1/roomtariff?region=7&room=";

    public static String API_HotelRoomActivity_URL = "http://stage.itraveller.com/backend/api/v1/hotelRoom/hotelId/[19]";

    public static String API_HotelActivity_HOTEL_ROOMS = "http://stage.itraveller.com/backend/api/v1/hotelRoom?regionId=";

    public static String API_HotelActivity_Checkroom="http://stage.itraveller.com/backend/api/v1/roomtariff?region=";

    public static String API_HotelActivity_HotelList="http://stage.itraveller.com/backend/api/v1/hotel/destintionId/" ;

    public static String API_HotelActivity_Lowest_Hotel="http://stage.itraveller.com/backend/api/v1/lowesthotel?destinationId=";

//    public static String API_HotelActivity_DefaultRoom=API_HotelActivity_Lowest_Hotel+ HotelActivity.temp_hotel_destination + "&checkInDate="+ HotelActivity.temp_destination_date +"&regionId=" + HotelActivity.temp_region_id;

    public static String API_ActivitiesActivity_URL="http://stage.itraveller.com/backend/api/v1/activities?fromDestination=" ;

    public static String API_LandingAdapter_ImageURL="http://stage.itraveller.com/backend/images/destinations/" ;

    public static String API_TransportationAdapter_ImageURL="http://stage.itraveller.com/backend/images/transfers/" ;

    public static String API_ViewPagerActivityAdapter_ImageURL="http://stage.itraveller.com/backend/images/activity/" ;

    public static String API_RegionPlace_ImageURL="http://stage.itraveller.com/backend/images/packages/" ;

    public static String API_RegionPlace_URL="http://stage.itraveller.com/backend/api/v1/itinerary/regionId/";

    public static String API_TransportationActivity_New_URL="http://stage.itraveller.com/backend/api/v1/destination/destinationId/";

    public static String API_TransportationActivity_URL="http://stage.itraveller.com/backend/api/v1/transportation?region=";

    public static String API_LandingActivity_Region="http://stage.itraveller.com/backend/api/v1/region";

    public static String API_LandingActivity_Search_Region="http://stage.itraveller.com/backend/api/v1/region/places";

    public static String API_Domestic_Flights="http://stage.itraveller.com/backend/api/v1/domesticflight?" ;

    public static String API_International_Flights="http://stage.itraveller.com/backend/api/v1/internationalflight?" ;

    public static String API_login="http://stage.itraveller.com/backend/api/v1/users/auth?email="   ;

    public static String API_signup="http://stage.itraveller.com/backend/api/v1/users/ -d  email=" ;

    public static String API_logout="http://stage.itraveller.com/backend/api/v1/users/";
}
