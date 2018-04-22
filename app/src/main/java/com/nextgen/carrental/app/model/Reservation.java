package com.nextgen.carrental.app.model;

/**
 * Reservation Model to display details
 * @author Prithwish
 */

public class Reservation {
    private String number;
    private String status;
    private String carType;
    private String pickUpPoint;
    private String dropOffPoint;
    private String pickUpTime;
    private String dropOffTime;

    public Reservation(String number) {
        this.number = number;
    }

    public Reservation(String number, String status, String carType, String pickUpPoint, String dropOffPoint,
                       String pickUpTime, String dropOffTime) {
        this.number = number;
        this.status = status;
        this.carType = carType;
        this.pickUpPoint = pickUpPoint;
        this.dropOffPoint = dropOffPoint;
        this.pickUpTime = pickUpTime;
        this.dropOffTime = dropOffTime;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCarType() {
        return carType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getPickUpPoint() {
        return pickUpPoint;
    }

    public void setPickUpPoint(String pickUpPoint) {
        this.pickUpPoint = pickUpPoint;
    }

    public String getDropOffPoint() {
        return dropOffPoint;
    }

    public void setDropOffPoint(String dropOffPoint) {
        this.dropOffPoint = dropOffPoint;
    }

    public String getPickUpTime() {
        return pickUpTime;
    }

    public void setPickUpTime(String pickUpTime) {
        this.pickUpTime = pickUpTime;
    }

    public String getDropOffTime() {
        return dropOffTime;
    }

    public void setDropOffTime(String dropOffTime) {
        this.dropOffTime = dropOffTime;
    }
}
