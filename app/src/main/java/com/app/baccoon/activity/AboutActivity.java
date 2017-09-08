package com.app.baccoon.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.baccoon.R;
import com.app.baccoon.utils.SharedPreference;
import com.app.baccoon.utils.SpKey;

import java.util.List;

/**
 * Created by admin1 on 3/5/16.
 */
public class AboutActivity extends Activity implements View.OnClickListener {

    SharedPreference sharedPreference;
    private Uri u;
    ImageView btnBack;
Button btnFullPayment, btnCustomPayment, btnPicWisePayment;
    private TextView txtHeader;
private  String urlToShare = "www.google.com";
    LinearLayout layoutAbout;
    TextView txtContactId, txtSupportId, txtPhone, txtFax,share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        sharedPreference = SharedPreference.getInstance(this);
        initView();
    }

    private void initView() {

        layoutAbout = (LinearLayout) findViewById(R.id.layoutAbout);
        btnFullPayment = (Button) findViewById(R.id.btnFullPayment);
        btnPicWisePayment = (Button) findViewById(R.id.btnPicWisePayment);
        btnCustomPayment = (Button) findViewById(R.id.btnCustomPayment);
        txtContactId = (TextView) findViewById(R.id.txtContactId);
        txtSupportId = (TextView) findViewById(R.id.txtSupportId);
        share=(TextView)findViewById(R.id.share_app);
        share.setOnClickListener(this);
        txtPhone = (TextView) findViewById(R.id.txtPhone);
        txtFax = (TextView) findViewById(R.id.txtFax);
        btnBack = (ImageView) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnCustomPayment.setOnClickListener(this);
        btnFullPayment.setOnClickListener(this);
        btnPicWisePayment.setOnClickListener(this);
        layoutAbout.setOnClickListener(this);


    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.btnFullPayment :
            case R.id.btnPicWisePayment :
            case R.id.btnCustomPayment:
              Intent  intent= new Intent(AboutActivity.this, PaymentActivity.class);
                startActivity(intent);
                break;

            case R.id.layoutAbout :
                 Intent  intent2= new Intent(AboutActivity.this, AboutDetailsActivity.class);
                startActivity(intent2);
                break;
            case R.id.share_app:
            {
                show_Dialog();
            }
        }
    }

    private void show_Dialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(AboutActivity.this);
        final AlertDialog dialog;
        ImageView fb,twitter,whatsapp,google_plus,gmail;
        final View vd = this.getLayoutInflater().inflate(R.layout.share_dialog, null, false);
        builder.setView(vd);
        dialog = builder.create();
        dialog.setCancelable(true);
        fb=(ImageView)vd.findViewById(R.id.fb_view);
        whatsapp=(ImageView)vd.findViewById(R.id.whatsapp_view);
        twitter=(ImageView)vd.findViewById(R.id.twiter_view);
        gmail=(ImageView)vd.findViewById(R.id.gmail_view);
        google_plus=(ImageView)vd.findViewById(R.id.google_plus);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            shareOnFb();
            }


        });
        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            shareOnChat();
            }
        });
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            shareOnTw();
            }
        });
        gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            shareOnMail();
            }
        });
        google_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                shareOnGp();
            }
        });
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;

        dialog.show();
    }

    private void shareOnFb() {


        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, urlToShare);

        boolean facebookAppFound = false;
        List<ResolveInfo> matches = this.getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo info : matches) {
            if (info.activityInfo.packageName.toLowerCase().startsWith("com.app.baccon")) {
                intent.setPackage(info.activityInfo.packageName);
                facebookAppFound = true;
                break;
            }
        }
        if (!facebookAppFound) {
            String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" + urlToShare;
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
        }
        startActivity(intent);

    }



    private void shareOnChat() {

        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Baccon App");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml(" http://www.google.com"));
        emailIntent.setType("text/plain");
        startActivity(Intent.createChooser(emailIntent, "Send to friend"));


      /*  try {
            Intent fbIntent = new Intent(Intent.ACTION_SEND);
            fbIntent.putExtra(Intent.EXTRA_TEXT, ""+urlToShare);
            fbIntent.putExtra(Intent.EXTRA_SUBJECT, "SUBJECT");
            fbIntent.putExtra(Intent.EXTRA_TITLE, "Title");

         *//*   if (u != null) {
                fbIntent.putExtra(Intent.EXTRA_STREAM, u);
            }*//*
            // fbIntent.setType("text/plain");
            fbIntent.setType("image*//*");

            PackageManager packManager = getPackageManager();
            List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(fbIntent, PackageManager.MATCH_DEFAULT_ONLY);

            boolean resolved = false;
            for (ResolveInfo resolveInfo : resolvedInfoList) {

                if (resolveInfo.activityInfo.packageName.startsWith("com.whatsapp")) {
                    fbIntent.setClassName(
                            resolveInfo.activityInfo.packageName,
                            resolveInfo.activityInfo.name);
                    resolved = true;
                    break;
                }
            }
            if (resolved) {
                try {
                    startActivity(fbIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Whatsapp not available. ", Toast.LENGTH_LONG).show();

                }
            }
        } catch (Exception e) {
            //Log.e("Exception posting on fb", "" + e);
            Toast.makeText(this, "Some problem occured", Toast.LENGTH_SHORT).show();
        } catch (Throwable th) {
            th.printStackTrace();
            Toast.makeText(this, "Some problem occured", Toast.LENGTH_SHORT).show();

        }*/
    }

    //share on mail
    private void shareOnMail() {
        try {


            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", "bacoon@gmail.com", null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Baccoon Product");
            emailIntent.putExtra(Intent.EXTRA_TEXT, ""+urlToShare);
            emailIntent.putExtra(Intent.EXTRA_STREAM, u);
            List<ResolveInfo> matches = getPackageManager().queryIntentActivities(emailIntent, 0);
            for (ResolveInfo info : matches) {
                if (info.activityInfo.packageName.toLowerCase().startsWith("com.google.android.gm")) {
                    emailIntent.setPackage(info.activityInfo.packageName);
                    break;
                }
            }
            startActivity(emailIntent);
            // emailIntent.setType("image/*");
          //  startActivity(Intent.createChooser(emailIntent, "Send via..."));
        } catch (Exception e) {
            //Log.e("Exception posting on fb", "" + e);
            Toast.makeText(this, "Some problem occured", Toast.LENGTH_SHORT).show();
        } catch (Throwable th) {
            th.printStackTrace();
            Toast.makeText(this, "Some problem occured", Toast.LENGTH_SHORT).show();

        }

    }


    //share on twitter
    private void shareOnTw() {
        try {
            Intent tweetIntent = new Intent(Intent.ACTION_SEND);
            tweetIntent.putExtra(Intent.EXTRA_TEXT, ""+urlToShare);

            if (u != null) {
                tweetIntent.putExtra(Intent.EXTRA_STREAM, u);
            }

            // tweetIntent.setType("image/*");

            PackageManager packManager = getPackageManager();
            List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(tweetIntent, PackageManager.MATCH_DEFAULT_ONLY);

            boolean resolved = false;
            for (ResolveInfo resolveInfo : resolvedInfoList) {
                if (resolveInfo.activityInfo.packageName.startsWith("com.twitter.android")) {
                    tweetIntent.setClassName(
                            resolveInfo.activityInfo.packageName,
                            resolveInfo.activityInfo.name);
                    resolved = true;
                    break;
                }
            }
            if (resolved) {
                startActivity(tweetIntent);
            } else {
                Intent i = new Intent();
                // i.putExtra(Intent.EXTRA_TEXT, "message");
                i.setAction(Intent.ACTION_VIEW);

                i.setData(Uri.parse("https://twitter.com/intent/tweet?text=" ));

                if (u != null) {
                    i.putExtra(Intent.EXTRA_STREAM, u);
                }

                //  i.setType("image/*");
// 4020024001738242   06/21
                startActivity(i);
                //Toast.makeText(this, "Twitter app isn't found", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            //Log.e("Exception posting on fb", "" + e);
            Toast.makeText(this, "Some problem occured", Toast.LENGTH_SHORT).show();
        } catch (Throwable th) {
            th.printStackTrace();
            Toast.makeText(this, "Some problem occured", Toast.LENGTH_SHORT).show();

        }


    }

    private void shareOnGp() {

        try {
            Intent gpIntent = new Intent(Intent.ACTION_SEND);
            gpIntent.putExtra(Intent.EXTRA_TEXT, ""+urlToShare);
            if (u != null) {
                gpIntent.putExtra(Intent.EXTRA_STREAM, u);
            }

            gpIntent.setType("text/plain");

            PackageManager packManager = getPackageManager();
            List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(gpIntent, PackageManager.MATCH_DEFAULT_ONLY);

            boolean resolved = false;
            for (ResolveInfo resolveInfo : resolvedInfoList) {

                if (resolveInfo.activityInfo.packageName.startsWith("com.google.android.apps.plus")) {
                    gpIntent.setClassName(
                            resolveInfo.activityInfo.packageName,
                            resolveInfo.activityInfo.name);
                    resolved = true;
                    break;
                }
            }
            if (resolved) {
                startActivity(gpIntent);
            } else {
                Intent intent = new Intent(Intent.ACTION_SEND);
                String sharerUrl = "https://plus.google.com/share?post=YOUR_URL_HERE" + "link of post";
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
                startActivity(intent);
                // Toast.makeText(ctx,"Facebook app not Available",Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            //Log.e("Exception posting on fb", "" + e);
            Toast.makeText(this, "Some problem occured", Toast.LENGTH_SHORT).show();
        } catch (Throwable th) {
            th.printStackTrace();
            Toast.makeText(this, "Some problem occured", Toast.LENGTH_SHORT).show();

        }


    }
}

