package com.app.baccoon.utils;

/**
 * Created by gopalgupta on 23/05/16.
 */
public class Validation {


    public boolean isFloat(String val) {
        boolean result = true;
        try {
            float x = Float.parseFloat(val);
            result = true;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            result = false;
        } finally {
            return result;
        }

    }


}
