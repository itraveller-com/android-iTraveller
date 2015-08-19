package com.itraveller.model;

import java.util.ArrayList;

/**
 * Created by VNK on 7/14/2015.
 */
public class FlightModel {

    String ActualBaseFare,Tax,STax,TCharge,SCharge,TDiscount,TMarkup,TPartnerCommission,TSdiscount,ocTax,id,key;

    ArrayList<OnwardFlightModel> onward_model;
    ArrayList<ReturnFlightModel> return_model;

    public FlightModel() {
    }

    public FlightModel(String actualBaseFare, String tax, String STax, String TCharge, String SCharge, String TDiscount, String TMarkup, String TPartnerCommission, String TSdiscount, String ocTax, String id, String key, ArrayList<OnwardFlightModel> onward_model, ArrayList<ReturnFlightModel> return_model) {
        ActualBaseFare = actualBaseFare;
        Tax = tax;
        this.STax = STax;
        this.TCharge = TCharge;
        this.SCharge = SCharge;
        this.TDiscount = TDiscount;
        this.TMarkup = TMarkup;
        this.TPartnerCommission = TPartnerCommission;
        this.TSdiscount = TSdiscount;
        this.ocTax = ocTax;
        this.id = id;
        this.key = key;
        this.onward_model = onward_model;
        this.return_model = return_model;
    }

    public String getActualBaseFare() {
        return ActualBaseFare;
    }

    public void setActualBaseFare(String actualBaseFare) {
        ActualBaseFare = actualBaseFare;
    }

    public String getTax() {
        return Tax;
    }

    public void setTax(String tax) {
        Tax = tax;
    }

    public String getSTax() {
        return STax;
    }

    public void setSTax(String STax) {
        this.STax = STax;
    }

    public String getTCharge() {
        return TCharge;
    }

    public void setTCharge(String TCharge) {
        this.TCharge = TCharge;
    }

    public String getSCharge() {
        return SCharge;
    }

    public void setSCharge(String SCharge) {
        this.SCharge = SCharge;
    }

    public String getTDiscount() {
        return TDiscount;
    }

    public void setTDiscount(String TDiscount) {
        this.TDiscount = TDiscount;
    }

    public String getTMarkup() {
        return TMarkup;
    }

    public void setTMarkup(String TMarkup) {
        this.TMarkup = TMarkup;
    }

    public String getTPartnerCommission() {
        return TPartnerCommission;
    }

    public void setTPartnerCommission(String TPartnerCommission) {
        this.TPartnerCommission = TPartnerCommission;
    }

    public String getTSdiscount() {
        return TSdiscount;
    }

    public void setTSdiscount(String TSdiscount) {
        this.TSdiscount = TSdiscount;
    }

    public String getOcTax() {
        return ocTax;
    }

    public void setOcTax(String ocTax) {
        this.ocTax = ocTax;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ArrayList<OnwardFlightModel> getOnward_model() {
        return onward_model;
    }

    public void setOnward_model(ArrayList<OnwardFlightModel> onward_model) {
        this.onward_model = onward_model;
    }

    public ArrayList<ReturnFlightModel> getReturn_model() {
        return return_model;
    }

    public void setReturn_model(ArrayList<ReturnFlightModel> return_model) {
        this.return_model = return_model;
    }


}

