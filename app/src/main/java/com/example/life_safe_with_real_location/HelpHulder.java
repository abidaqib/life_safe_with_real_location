package com.example.life_safe_with_real_location;

public class HelpHulder {

    public HelpHulder() {
    }

    public HelpHulder(String h_id, String lat, String lng) {
        this.h_id = h_id;
        this.lat = lat;
        this.lng = lng;
    }

    public String getH_id() {
        return h_id;
    }

    public void setH_id(String h_id) {
        this.h_id = h_id;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    String h_id, lat, lng;
}
