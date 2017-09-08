package com.app.baccoon.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.app.baccoon.R;

import com.app.baccoon.adapter.NotificationListAdapter;
import com.app.baccoon.bean.NotificationBean;
import com.app.baccoon.bean.MessgeBean;
import com.app.baccoon.bean.NotificationBean;
import com.app.baccoon.utils.API;
import com.app.baccoon.utils.Common;
import com.app.baccoon.utils.SharedPreference;
import com.app.baccoon.utils.SpKey;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by admin1 on 3/5/16.
 */
public class NotificationListActivity extends Activity {

    SharedPreference sharedPreference;
    ListView listView;
    ArrayList<NotificationBean> notificationList;
    ImageView btnBack;
    NotificationListAdapter custAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        sharedPreference = SharedPreference.getInstance(this);
        initView();
        getNotificationList();
    }

    private void initView() {

        notificationList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listview);
        btnBack = (ImageView) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("click", "pos="+position);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void getNotificationList() {


        JsonObject jsonObject = new JsonObject();
        final String uid = sharedPreference.getString(SpKey.UID, "");

        jsonObject.addProperty("userId", uid);
        Log.e("user id", "" + uid);


        {
            Common.showProgress(this);
        }
        Ion.with(this)
                .load(API.GetNotificationList)
                .setTimeout(60 * 1000)
                .setJsonObjectBody(jsonObject)
                .asString()

                .setCallback(new FutureCallback<String>() {

                                 @Override
                                 public void onCompleted(Exception e, String jsonString) {


                                     Log.d("calllll", "callllll");

                                     Common.hideProgress(NotificationListActivity.this);


                                     if (e == null) {
                                         if (jsonString != null && jsonString != "") {
                                             Log.e("json2", jsonString);
                                             try {

                                                 JSONObject js = new JSONObject(jsonString);
                                                 if (js.getBoolean("isSuccess")) {


                                                     JSONArray root = js.getJSONObject("result").getJSONArray("notification");

                                                     /**
                                                      * buyerId : 57
                                                      * product_id : 58
                                                      * status : 1
                                                      * message : Ali Sheikh put gopal product to favourite
                                                      * prod_name : Footwear product
                                                      * name : Ali Sheikh
                                                      * profileImage :
                                                      */
                                                     for (int i = 0; i < root.length(); i++) {

                                                         JSONObject obj = root.getJSONObject(i);

                                                         notificationList.add(new NotificationBean(obj.getString("buyerId"), obj.getString("product_id")
                                                                 , obj.getString("status")
                                                                 , obj.getString("message"), obj.getString("prod_name"), obj.getString("name"), obj.getString("profileImage")));
                                                     }
                                                     custAdapter = new NotificationListAdapter(NotificationListActivity.this, notificationList);
                                                     listView.setAdapter(custAdapter);


                                                 } else {
                                                     Common.showToast(NotificationListActivity.this, (js.getString("message")));
                                                 }
                                             } catch (JSONException e1) {
                                                 e1.printStackTrace();

                                                 Common.showToast(NotificationListActivity.this, "Network Error");
                                             }
                                         }
                                     } else {
                                         e.printStackTrace();

                                         Log.e("Exception", "" + e);
                                         Common.showToast(NotificationListActivity.this, "Network Error");
                                     }
                                 }
                             }

                );
    }
}

