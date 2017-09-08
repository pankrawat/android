package com.app.baccoon.bean;

import java.io.Serializable;

/**
 * Created by admin1 on 10/8/16.
 */
public class MyProductBean implements Serializable {


    public MyProductBean(String prod_id, String sellerId, String buyerId, String prod_name, String prod_desc, String prod_category, String prod_image, String prod_price, String prod_currency, String prod_fav, String prod_location, String prod_lat, String prod_long, String isSold, String put_by_buyer, String by_dhl, String by_dhl_price, String by_postoffice, String by_postoffice_price, String createdDate) {
        this.prod_id = prod_id;
        this.sellerId = sellerId;
        this.buyerId = buyerId;
        this.prod_name = prod_name;
        this.prod_desc = prod_desc;
        this.prod_category = prod_category;
        this.prod_image = prod_image;
        this.prod_price = prod_price;
        this.prod_currency = prod_currency;
        this.prod_fav = prod_fav;
        this.prod_location = prod_location;
        this.prod_lat = prod_lat;
        this.prod_long = prod_long;
        this.isSold = isSold;
        this.put_by_buyer = put_by_buyer;
        this.by_dhl = by_dhl;
        this.by_dhl_price = by_dhl_price;
        this.by_postoffice = by_postoffice;
        this.by_postoffice_price = by_postoffice_price;
        this.createdDate = createdDate;
    }

    /**
     * prod_id : 58
     * sellerId : 55
     * buyerId : 0
     * prod_name : Footwear product
     * prod_desc : this is gopal product
     * prod_category : Footwear
     * prod_image : http://ec2-52-205-20-98.compute-1.amazonaws.com/baccoon/images/productImages/121470405849.png
     * prod_price : 999
     * prod_currency : INR
     * prod_fav : 0
     * prod_location : Vaishali, Ghaziabad, Uttar Pradesh, India
     * prod_lat : 28.643317500000002
     * prod_long : 77.33818939999999
     * isSold : 1
     * put_by_buyer : 1
     * by_dhl : 1
     * by_dhl_price : 200
     * by_postoffice : 0
     * by_postoffice_price : 0
     * createdDate : 2016-06-02 13:21:45
     */

    private String prod_id;
    private String sellerId;
    private String buyerId;
    private String prod_name;
    private String prod_desc;
    private String prod_category;
    private String prod_image;
    private String prod_price;
    private String prod_currency;
    private String prod_fav;
    private String prod_location;
    private String prod_lat;
    private String prod_long;
    private String isSold;
    private String put_by_buyer;
    private String by_dhl;
    private String by_dhl_price;
    private String by_postoffice;
    private String by_postoffice_price;
    private String createdDate;

    public String getProd_id() {
        return prod_id;
    }

    public void setProd_id(String prod_id) {
        this.prod_id = prod_id;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getProd_name() {
        return prod_name;
    }

    public void setProd_name(String prod_name) {
        this.prod_name = prod_name;
    }

    public String getProd_desc() {
        return prod_desc;
    }

    public void setProd_desc(String prod_desc) {
        this.prod_desc = prod_desc;
    }

    public String getProd_category() {
        return prod_category;
    }

    public void setProd_category(String prod_category) {
        this.prod_category = prod_category;
    }

    public String getProd_image() {
        return prod_image;
    }

    public void setProd_image(String prod_image) {
        this.prod_image = prod_image;
    }

    public String getProd_price() {
        return prod_price;
    }

    public void setProd_price(String prod_price) {
        this.prod_price = prod_price;
    }

    public String getProd_currency() {
        return prod_currency;
    }

    public void setProd_currency(String prod_currency) {
        this.prod_currency = prod_currency;
    }

    public String getProd_fav() {
        return prod_fav;
    }

    public void setProd_fav(String prod_fav) {
        this.prod_fav = prod_fav;
    }

    public String getProd_location() {
        return prod_location;
    }

    public void setProd_location(String prod_location) {
        this.prod_location = prod_location;
    }

    public String getProd_lat() {
        return prod_lat;
    }

    public void setProd_lat(String prod_lat) {
        this.prod_lat = prod_lat;
    }

    public String getProd_long() {
        return prod_long;
    }

    public void setProd_long(String prod_long) {
        this.prod_long = prod_long;
    }

    public String getIsSold() {
        return isSold;
    }

    public void setIsSold(String isSold) {
        this.isSold = isSold;
    }

    public String getPut_by_buyer() {
        return put_by_buyer;
    }

    public void setPut_by_buyer(String put_by_buyer) {
        this.put_by_buyer = put_by_buyer;
    }

    public String getBy_dhl() {
        return by_dhl;
    }

    public void setBy_dhl(String by_dhl) {
        this.by_dhl = by_dhl;
    }

    public String getBy_dhl_price() {
        return by_dhl_price;
    }

    public void setBy_dhl_price(String by_dhl_price) {
        this.by_dhl_price = by_dhl_price;
    }

    public String getBy_postoffice() {
        return by_postoffice;
    }

    public void setBy_postoffice(String by_postoffice) {
        this.by_postoffice = by_postoffice;
    }

    public String getBy_postoffice_price() {
        return by_postoffice_price;
    }

    public void setBy_postoffice_price(String by_postoffice_price) {
        this.by_postoffice_price = by_postoffice_price;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
