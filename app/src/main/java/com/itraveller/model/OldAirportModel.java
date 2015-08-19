package com.itraveller.model;

/**
 * Created by VNK on 6/17/2015.
 */
public class OldAirportModel {
    int Id,Status;
    String Code,Name,Lat,Long,Timezone,City,Country,Country_Code;

    public OldAirportModel() {
    }

    public OldAirportModel(int id, int status, String code, String name, String lat, String aLong, String timezone, String city, String country, String country_Code) {
        Id = id;
        Status = status;
        Code = code;
        Name = name;
        Lat = lat;
        Long = aLong;
        Timezone = timezone;
        City = city;
        Country = country;
        Country_Code = country_Code;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLat() {
        return Lat;
    }

    public void setLat(String lat) {
        Lat = lat;
    }

    public String getLong() {
        return Long;
    }

    public void setLong(String aLong) {
        Long = aLong;
    }

    public String getTimezone() {
        return Timezone;
    }

    public void setTimezone(String timezone) {
        Timezone = timezone;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getCountry_Code() {
        return Country_Code;
    }

    public void setCountry_Code(String country_Code) {
        Country_Code = country_Code;
    }
}
