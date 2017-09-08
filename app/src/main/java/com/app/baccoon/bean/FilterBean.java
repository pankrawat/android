package com.app.baccoon.bean;

/**
 * Created by gopalgupta on 11/03/16.
 */
public class FilterBean {
    private static FilterBean ourInstance = new FilterBean();




    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getSortby() {
        return sortby;
    }

    public void setSortby(int sortby) {
        this.sortby = sortby;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getPriceTo() {
        return priceTo;
    }

    public void setPriceTo(String priceTo) {
        this.priceTo = priceTo;
    }

    public String getPriceFrom() {
        return priceFrom;
    }

    public void setPriceFrom(String priceFrom) {
        this.priceFrom = priceFrom;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    private String lat="",lng="";

    public void setValues(int radius, int sortby, int category, String priceTo, String priceFrom, String location,String lat,String lng) {
        this.radius = radius;
        this.sortby = sortby;
        this.category = category;
        this.priceTo = priceTo;
        this.priceFrom = priceFrom;
        this.location = location;
        this.lat = lat;
        this.lng = lng;

    }



    public void clearValues(int radius, int sortby, int category, String priceTo, String priceFrom, String location) {
        this.radius = radius;
        this.sortby = sortby;
        this.category = category;
        this.priceTo = priceTo;
        this.priceFrom = priceFrom;
        this.location = location;
    }

    private int radius=0;
    private int sortby=0;
    private int category=0;
    private String priceTo="";
    private String priceFrom="";
    private String location="";








    public static FilterBean getInstance() {
        return ourInstance;
    }

    private FilterBean() {
    }





}
