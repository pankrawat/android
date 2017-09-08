package com.app.baccoon.activity;

import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.baccoon.HelpData;
import com.app.baccoon.R;
import com.app.baccoon.utils.SharedPreference;
import com.app.baccoon.utils.SpKey;
import com.vk.sdk.api.model.VKApiUserFull;

import java.util.ArrayList;

/**
 * Created by admin1 on 3/5/16.
 */
public class HelpQuestionActivity extends Activity {

    SharedPreference sharedPreference;
    ListView listView;
  String[] questionsList;
  String[] answerList;
    HelpData helpData=new HelpData();

    ImageView btnBack;
    ArrayAdapter<String> listAdapter;
    int topicIndex;
    String title;
    private TextView txtHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        sharedPreference = SharedPreference.getInstance(this);
        initView();

        topicIndex= getIntent().getIntExtra(SpKey.TopicPosition, -1);
        title=getIntent().getStringExtra(SpKey.Topic);
txtHeader.setText(title);

        questionsList=helpData.getQuestions(topicIndex);
        answerList=helpData.getAnswers(topicIndex);




        listAdapter=new ArrayAdapter<String>(this,R.layout.textview_layout_list,  R.id.spinerText, questionsList);
listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("position ans", "pos=" + position+" ans="+answerList[position]);
                showAnswerDialog(questionsList[position], answerList[position]);

            }
        });


    }

    private void showAnswerDialog(String question, String answer) {


        final Dialog dialog= new Dialog(this, R.style.myDialogTheme);

        dialog.setContentView(R.layout.popup_help_layout);
        TextView txtQuestion, txtAnswer;
        ImageView imgCross;
        RelativeLayout parent;

        txtQuestion= (TextView)dialog.findViewById(R.id.txtQuestion);
        txtAnswer= (TextView)dialog.findViewById(R.id.txtAnswer);
        imgCross= (ImageView)dialog.findViewById(R.id.imgCross);
        parent= (RelativeLayout) dialog.findViewById(R.id.parent);


        txtQuestion.setText(""+question);
        txtAnswer.setText("" + answer);
        imgCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });


dialog.getWindow().getAttributes().windowAnimations= R.style.DialogAnimation;

        dialog.show();


    }

    private void initView() {
        txtHeader = (TextView) findViewById(R.id.txtHeader);

        listView = (ListView) findViewById(R.id.listview);
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

