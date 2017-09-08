package com.app.baccoon.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.baccoon.HelpData;
import com.app.baccoon.R;
import com.app.baccoon.utils.SharedPreference;

/**
 * Created by admin1 on 3/5/16.
 */
public class AboutDetailsActivity extends Activity {

    SharedPreference sharedPreference;
    ImageView btnBack;
    TextView txtData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_details);
        sharedPreference = SharedPreference.getInstance(this);
        initView();
        txtData.setText(new HelpData().getAnswers(2)[2]);
    }

    private void initView() {

        txtData = (TextView) findViewById(R.id.txtData);
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

