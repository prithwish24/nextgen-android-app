package com.nextgen.carrental.app.bo;


import com.fasterxml.jackson.annotation.JsonProperty;

public class TripsResponse {

    @JsonProperty("id")
    private String id;
    @JsonProperty("pickupPoint")
    private String pickupPoint;
    @JsonProperty("dropPoint")
    private String dropPoint;
    @JsonProperty("pickupDateTime")
    private String pickupDateTime;
    @JsonProperty("dropoffDateTime")
    private String dropoffDateTime;
    @JsonProperty("carType")
    private String carType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPickupPoint() {
        return pickupPoint;
    }

    public void setPickupPoint(String pickupPoint) {
        this.pickupPoint = pickupPoint;
    }

    public String getDropPoint() {
        return dropPoint;
    }

    public void setDropPoint(String dropPoint) {
        this.dropPoint = dropPoint;
    }

    public String getPickupDateTime() {
        return pickupDateTime;
    }

    public void setPickupDateTime(String pickupDateTime) {
        this.pickupDateTime = pickupDateTime;
    }

    public String getDropoffDateTime() {
        return dropoffDateTime;
    }

    public void setDropoffDateTime(String dropoffDateTime) {
        this.dropoffDateTime = dropoffDateTime;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }
}
