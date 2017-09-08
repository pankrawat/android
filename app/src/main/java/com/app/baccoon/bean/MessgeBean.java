package com.app.baccoon.bean;

import java.io.Serializable;

/**
 * Created by admin1 on 5/5/16.
 */
public class MessgeBean implements Serializable {

    public String getChat_msg() {
        return chat_msg;
    }

    public void setChat_msg(String chat_msg) {
        this.chat_msg = chat_msg;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getChat_date() {
        return chat_date;
    }

    public void setChat_date(String chat_date) {
        this.chat_date = chat_date;
    }

    String chat_msg="";
    String user_type="";
    String chat_date="";
}
