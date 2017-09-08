package com.app.baccoon.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.app.baccoon.R;
import com.app.baccoon.adapter.MessageAdapter;
import com.app.baccoon.bean.ChatBean;
import com.app.baccoon.bean.MessgeBean;
import com.app.baccoon.fragment.MyProductFragment;
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
public class MyProductActivity extends FragmentActivity{
    SharedPreference sharedPreference;

    RelativeLayout btnBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_product);
        sharedPreference = SharedPreference.getInstance(this);
        btnBack = (RelativeLayout) findViewById(R.id.btnBack);
        getSupportFragmentManager().beginTransaction().add(R.id.flContent,new MyProductFragment()).commit();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }



    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK && data.getBooleanExtra("edited", false))
        {
            startActivity(new Intent(MyProductActivity.this, MyProductActivity.class));
            finish();
        }

    }


}

