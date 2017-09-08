package com.app.baccoon.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by admin1 on 3/5/16.
 */
public class ChatBean implements Serializable {

    String productId="";
    String sellerId="";
    String buyerId="";
    String offer="";
    String chat_Id ="";
    String chat_date="";
    String status="";
    String buyerName="";
    String profileImage="";
    String unread_msg="";
    String last_unread_msg="";
    String msg_data="";
    ArrayList<MessgeBean> msgList;
    String user_type="";
    String is_read="";

    public String getIs_read() {
        return is_read;
    }

    public void setIs_read(String is_read) {
        this.is_read = is_read;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public ArrayList<MessgeBean> getMsgList() {
        return msgList;
    }

    public void setMsgList(ArrayList<MessgeBean> msgList) {
        this.msgList = msgList;
    }

    public String getChat_Id() {
        return chat_Id;
    }

    public void setChat_Id(String chat_Id) {
        this.chat_Id = chat_Id;
    }

    public String getMsg_data() {
        return msg_data;
    }

    public void setMsg_data(String msg_data) {
        this.msg_data = msg_data;
    }

    public String getLast_unread_msg() {
        return last_unread_msg;
    }

    public void setLast_unread_msg(String last_unread_msg) {
        this.last_unread_msg = last_unread_msg;
    }

    public String getUnread_msg() {
        return unread_msg;
    }

    public void setUnread_msg(String unread_msg) {
        this.unread_msg = unread_msg;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getChat_date() {
        return chat_date;
    }

    public void setChat_date(String chat_date) {
        this.chat_date = chat_date;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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


    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }


}
