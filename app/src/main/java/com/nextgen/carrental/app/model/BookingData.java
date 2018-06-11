package com.nextgen.carrental.app.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BookingData {
    public String step;
    public String status;
    public String confNum;
    public Date pickupDateTime;
    public Date returnDateTime;
    public String pickupLoc;
    public String returnLoc;
    public String carType;
    public String vehicleRentPrice;
    public String consessionFee;
    public String salesTax;
    public String estimatedTotal;
    public List<String> additionalEquip = new ArrayList<>();
}
