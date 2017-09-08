package com.app.baccoon.utils;

/**
 * Created by admin1 on 6/10/16.
 */
import android.app.Application;

import com.app.baccoon.R;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.acra.sender.HttpSender;

/**
 * Created by nuuneoi on 2/19/2015.
 */

@ReportsCrashes(

        mailTo = "siddharth.sharma42@gmail.com", mode = ReportingInteractionMode.TOAST,
        resToastText = R.string.Acra_msg

       /* httpMethod = HttpSender.Method.PUT,
        reportType = HttpSender.Type.JSON,
        formUri = "http://YOUR_SERVER_IP:5984/acra-myapp/_design/acra-storage/_update/report",
        formUriBasicAuthLogin = "tester",
        formUriBasicAuthPassword = "12345"*/
)
public class Acra extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ACRA.init(this);
    }

}