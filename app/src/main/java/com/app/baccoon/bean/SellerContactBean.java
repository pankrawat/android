package com.app.baccoon.bean;

public class SellerContactBean {
    private String pid;
    private String image;
    private String mobile;
    private String name;


    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SellerContactBean(String pid, String image, String mobile, String name) {
        this.pid = pid;
        this.image = image;
        this.mobile = mobile;
        this.name = name;
    }



}
