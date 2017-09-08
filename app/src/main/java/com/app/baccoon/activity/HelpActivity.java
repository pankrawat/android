package com.app.baccoon.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.app.baccoon.R;
import com.app.baccoon.adapter.NotificationListAdapter;
import com.app.baccoon.adapter.RecyclerItemClickListener;
import com.app.baccoon.bean.NotificationBean;
import com.app.baccoon.utils.API;
import com.app.baccoon.utils.Common;
import com.app.baccoon.utils.SharedPreference;
import com.app.baccoon.utils.SpKey;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by admin1 on 3/5/16.
 */
public class HelpActivity extends Activity {

    SharedPreference sharedPreference;
    ListView listView;
  String[] helpTopicList={
          "Getting Started", " Selling on Baccoon", "Buying on Baccoon", "Tailor your Experiance", "Payment", "Faq" };

    ImageView btnBack;
    ArrayAdapter<String> listAdapter;

Intent intent;
    private TextView txtHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        sharedPreference = SharedPreference.getInstance(this);
        initView();
        intent= new Intent(HelpActivity.this, HelpQuestionActivity.class);
        listAdapter=new ArrayAdapter<String>(this,R.layout.textview_layout_list,  R.id.spinerText, helpTopicList);
listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("click", "pos=" + position);
intent.putExtra(SpKey.TopicPosition,position);
intent.putExtra(SpKey.Topic,helpTopicList[position]);
                startActivity(intent);


            }
        });


    }

    private void initView() {
     listView = (ListView) findViewById(R.id.listview);
        txtHeader = (TextView) findViewById(R.id.txtHeader);
        btnBack = (ImageView) findViewById(R.id.btnBack);
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


}

