package com.itraveller.model;

public class ReturnDomesticFlightModel {

        String ActualBaseFare,Tax,STax,TCharge,SCharge,TDiscount,TMarkup,TPartnerCommission,TSdiscount,ocTax,id,key,AirEquipType,ArrivalAirportCode,ArrivalAirportName,ArrivalDateTime,DepartureAirportCode,DepartureAirportName,DepartureDateTime,FlightNumber,MarketingAirlineCode,OperatingAirlineCode,OperatingAirlineName,OperatingAirlineFlightNumber,NumStops,LinkSellAgrmnt,Conx,AirpChg,InsideAvailOption,GenTrafRestriction,DaysOperates,JrnyTm,EndDt,StartTerminal,EndTerminal;
        int Total_Price;

    public ReturnDomesticFlightModel() {
    }

    public ReturnDomesticFlightModel(String actualBaseFare, String tax, String STax, String TCharge, String SCharge, String TDiscount, String TMarkup, String TPartnerCommission, String TSdiscount, String ocTax, String id, String key, String airEquipType, String arrivalAirportCode, String arrivalAirportName, String arrivalDateTime, String departureAirportCode, String departureAirportName, String departureDateTime, String flightNumber, String marketingAirlineCode, String operatingAirlineCode, String operatingAirlineName, String operatingAirlineFlightNumber, String numStops, String linkSellAgrmnt, String conx, String airpChg, String insideAvailOption, String genTrafRestriction, String daysOperates, String jrnyTm, String endDt, String startTerminal, String endTerminal) {
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
        AirEquipType = airEquipType;
        ArrivalAirportCode = arrivalAirportCode;
        ArrivalAirportName = arrivalAirportName;
        ArrivalDateTime = arrivalDateTime;
        DepartureAirportCode = departureAirportCode;
        DepartureAirportName = departureAirportName;
        DepartureDateTime = departureDateTime;
        FlightNumber = flightNumber;
        MarketingAirlineCode = marketingAirlineCode;
        OperatingAirlineCode = operatingAirlineCode;
        OperatingAirlineName = operatingAirlineName;
        OperatingAirlineFlightNumber = operatingAirlineFlightNumber;
        NumStops = numStops;
        LinkSellAgrmnt = linkSellAgrmnt;
        Conx = conx;
        AirpChg = airpChg;
        InsideAvailOption = insideAvailOption;
        GenTrafRestriction = genTrafRestriction;
        DaysOperates = daysOperates;
        JrnyTm = jrnyTm;
        EndDt = endDt;
        StartTerminal = startTerminal;
        EndTerminal = endTerminal;
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

    public String getAirEquipType() {
        return AirEquipType;
    }

    public void setAirEquipType(String airEquipType) {
        AirEquipType = airEquipType;
    }

    public String getArrivalAirportCode() {
        return ArrivalAirportCode;
    }

    public void setArrivalAirportCode(String arrivalAirportCode) {
        ArrivalAirportCode = arrivalAirportCode;
    }


    public String getArrivalDateTime() {
        return ArrivalDateTime;
    }

    public void setArrivalDateTime(String arrivalDateTime) {
        ArrivalDateTime = arrivalDateTime;
    }

    public String getDepartureAirportCode() {
        return DepartureAirportCode;
    }

    public void setDepartureAirportCode(String departureAirportCode) {
        DepartureAirportCode = departureAirportCode;
    }


    public String getDepartureDateTime() {
        return DepartureDateTime;
    }

    public void setDepartureDateTime(String departureDateTime) {
        DepartureDateTime = departureDateTime;
    }

    public String getFlightNumber() {
        return FlightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        FlightNumber = flightNumber;
    }



    public String getOperatingAirlineName() {
        return OperatingAirlineName;
    }

    public void setOperatingAirlineName(String operatingAirlineName) {
        OperatingAirlineName = operatingAirlineName;
    }

    public void setTotal_Price(int Total_Price)
    {
        this.Total_Price=Total_Price;
    }
    public int getTotal_Price()
    {
        return Total_Price;
    }
}
