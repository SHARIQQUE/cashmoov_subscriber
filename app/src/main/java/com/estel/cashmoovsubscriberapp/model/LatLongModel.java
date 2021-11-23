package com.estel.cashmoovsubscriberapp.model;

public class LatLongModel {

    public int id;
    public String name;
    public String address;
    public String outlateName;
    public String latitude;
    public String lognitude;
    public String info;

    public LatLongModel(int id, String name, String address, String outlateName, String latitude, String lognitude, String info) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.outlateName = outlateName;
        this.latitude = latitude;
        this.lognitude = lognitude;
        this.info = info;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOutlateName() {
        return outlateName;
    }

    public void setOutlateName(String outlateName) {
        this.outlateName = outlateName;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLognitude() {
        return lognitude;
    }

    public void setLognitude(String lognitude) {
        this.lognitude = lognitude;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
