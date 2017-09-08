package com.app.baccoon.activity;

/**
 * Created by gopalgupta on 23/02/16.
 */


public class User {
    private static User ourInstance = new User();

    public User() {

    }

    public static User getInstance() {
        return ourInstance;
    }

   String firstName;
    String lastName;
    String uid;
    String email;
    boolean isCompany;

    public boolean isCompany() {
        return isCompany;
    }

    public void setIsCompany(boolean isCompany) {
        this.isCompany = isCompany;
    }



    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public User(String firstName, String lastName, String uid, String email, String isCompany) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.uid = uid;
        this.email = email;
        this.isCompany=isCompany();

    }
}
