package com.app.baccoon.bean;

import java.io.Serializable;

/**
 * Created by gopalgupta on 07/03/16.
 */
public class DrawerItem implements Serializable{
    private String title;
   private boolean isSelected=false;

    public DrawerItem(String title, boolean isSelected) {
        this.title = title;
        this.isSelected = isSelected;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }







}
