package com.app.baccoon.bean;

/**
 * Created by admin1 on 10/8/16.
 */
public class NotificationBean {


    public NotificationBean(String buyerId, String product_id, String status, String message, String prod_name, String name, String profileImage) {
        this.buyerId = buyerId;
        this.product_id = product_id;
        this.status = status;
        this.message = message;
        this.prod_name = prod_name;
        this.name = name;
        this.profileImage = profileImage;
    }

    /**
     * buyerId : 57
     * product_id : 58
     * status : 1
     * message : Ali Sheikh put gopal product to favourite
     * prod_name : Footwear product
     * name : Ali Sheikh
     * profileImage :
     */

    private String buyerId;
    private String product_id;
    private String status;
    private String message;
    private String prod_name;
    private String name;
    private String profileImage;

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getProd_name() {
        return prod_name;
    }

    public void setProd_name(String prod_name) {
        this.prod_name = prod_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
