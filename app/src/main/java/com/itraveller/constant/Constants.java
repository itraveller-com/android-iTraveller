package com.itraveller.constant;


/**
 * Created by VNK on 6/22/2015.
 */
public class Constants {

/*
    private static String API_domain = "http://stage.itraveller.com/backend";
*/
    private static String API_domain = "http://itraveller.com";

    public static String API_ViewPagerAdapter_ImageURL = API_domain + "/images/hotels/";

    public static String API_TransportationActivity_Tra_URL = API_domain + "/api/v1/b2ctransportation?regionId=";

    public static String API_ItinerarySummaryActivity_ImageURL = API_domain + "/images/hotels/" ;

    public static String API_HotelRoomActivity_CheckRoom = API_domain + "/api/v1/roomtariff?region=7&room=";

    public static String API_HotelRoomActivity_URL = API_domain + "/api/v1/hotelRoom/hotelId/[19]";

    public static String API_HotelActivity_HOTEL_ROOMS = API_domain + "/api/v1/hotelRoom?regionId=";

    public static String API_HotelActivity_Checkroom= API_domain + "/api/v1/roomtariff?region=";

    public static String API_HotelActivity_HotelList= API_domain + "/api/v1/hotel/destintionId/" ;

    public static String API_HotelActivity_Lowest_Hotel= API_domain + "/api/v1/lowesthotel?destinationId=";

//    public static String API_HotelActivity_DefaultRoom=API_HotelActivity_Lowest_Hotel+ HotelActivity.temp_hotel_destination + "&checkInDate="+ HotelActivity.temp_destination_date +"&regionId=" + HotelActivity.temp_region_id;

    public static String API_ActivitiesActivity_URL= API_domain + "/api/v1/activities?fromDestination=" ;

    public static String API_LandingAdapter_ImageURL= API_domain + "/images/destinations/" ;

    public static String API_TransportationAdapter_ImageURL= API_domain + "/images/transfers/" ;

    public static String API_ViewPagerActivityAdapter_ImageURL= API_domain + "/images/activity/" ;

    public static String API_RegionPlace_ImageURL= API_domain + "/images/packages/" ;

    public static String API_RegionPlace_URL= API_domain + "/api/v1/itinerary/regionId/";

    public static String API_TransportationActivity_New_URL= API_domain + "/api/v1/destination/destinationId/";

    public static String API_TransportationActivity_URL= API_domain + "/api/v1/transportation?region=";

    public static String API_LandingActivity_Region= API_domain + "/api/v1/region";

    public static String API_LandingActivity_Search_Region= API_domain + "/api/v1/region/places";

    public static String API_Domestic_Flights= API_domain + "/api/v1/domesticflight?" ;

    public static String API_International_Flights= API_domain + "/api/v1/internationalflight?Trip=ROUND&" ;

    public static String API_login= API_domain + "/api/v1/users/auth"   ;

    public static String API_signup= API_domain + "/api/v1/users/" ;

    public static String API_logout= API_domain + "/api/v1/users/";

    public static String API_forgot= API_domain + "/api/v1/forgotpassword";

    public static String API_CustomerPackage = API_domain + "/api/v1/customerpackages?email=bsk0292@gmail.com&booked=1";

}
