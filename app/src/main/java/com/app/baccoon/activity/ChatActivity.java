package com.app.baccoon.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.app.baccoon.R;
import com.app.baccoon.adapter.ChatAdapter;
import com.app.baccoon.bean.ChatBean;
import com.app.baccoon.bean.MessgeBean;
import com.app.baccoon.utils.API;
import com.app.baccoon.utils.Common;
import com.app.baccoon.utils.SharedPreference;
import com.app.baccoon.utils.SpKey;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by admin1 on 16/5/16.
 */
public class ChatActivity extends Activity implements View.OnClickListener
{

    Intent i;
    SharedPreference sharedPreference;
    ListView chatList;
    EditText txtMsg;
    TextView btnSend;
    ArrayList<MessgeBean> msgList;
    ChatAdapter adapter;
    ChatBean obj=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat);
        sharedPreference = SharedPreference.getInstance(this);
        chatList=(ListView)findViewById(R.id.chatlist);
        txtMsg=(EditText)findViewById(R.id.txtmsg);
        btnSend=(TextView)findViewById(R.id.btnsend);
        msgList=new ArrayList<>();
        i=getIntent();
        btnSend.setOnClickListener(this);

        if(i.hasExtra("chatbean"))
        {
            obj= (ChatBean) i.getSerializableExtra("chatbean");
        }

        if(obj!=null)
        {
           msgList= obj.getMsgList();
            adapter=new ChatAdapter(this,obj);
            chatList.setAdapter(adapter);
            chatList.smoothScrollToPosition(msgList.size());
        }
        else
        {
            Log.e("In Else","Obj null");
        }
    }

    @Override
    public void onClick(View v) {

        if(txtMsg.getText().toString().replaceAll(" ","").equals(""))
        {
            Common.showToast(ChatActivity.this, "Enter any message");
        }
        else
        {
            sendMsgToServer(txtMsg.getText().toString());
        }

    }

    private void sendMsgToServer(final String s) {

        JsonObject jsonObject = new JsonObject();
        final String uid = sharedPreference.getString(SpKey.UID, "2");

        jsonObject.addProperty("chat_id", obj.getChat_Id());
        jsonObject.addProperty("user_type",obj.getUser_type());
        jsonObject.addProperty("chat_msg",s);


        Common.showProgress(ChatActivity.this);
        Ion.with(ChatActivity.this)
                .load(API.Chat_Service_Reply)
                .setTimeout(60 * 1000)
                .setJsonObjectBody(jsonObject)
                .asString()

                .setCallback(new FutureCallback<String>() {

                                 @Override
                                 public void onCompleted(Exception e, String jsonString) {

                                     Log.d("calllll", "callllll");
                                     Common.hideProgress(ChatActivity.this);

                                     if (e == null) {
                                         if (jsonString != null && jsonString != "") {
                                             Log.e("json2", jsonString);
                                             try {

                                                 JSONObject js = new JSONObject(jsonString);
                                                 if (js.getString("isSuccess").equals("true")) {

                                                    // Common.showToast(ChatActivity.this,"Msg Sent Successfully");
                                                     sharedPreference.putBoolean(SpKey.isRefreshRequired,true);
                                                     MessgeBean bean=new MessgeBean();
                                                     bean.setChat_msg(s);
                                                     bean.setUser_type(obj.getUser_type());

                                                     DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                     Date date = new Date();
                                                     bean.setChat_date(dateFormat.format(date));
                                                     msgList.add(bean);
                                                     txtMsg.setText("");

                                                     if(adapter!=null)
                                                     {
                                                         adapter.notifyDataSetChanged();
                                                     }
                                                     else
                                                     {
                                                         adapter=new ChatAdapter(ChatActivity.this,obj);
                                                         chatList.setAdapter(adapter);
                                                     }

                                                 } else {
                                                     Common.showToast(ChatActivity.this, (js.getString("message")));
                                                 }
                                             } catch (JSONException e1) {
                                                 e1.printStackTrace();
                                                 Common.showToast(ChatActivity.this, "Network Error");
                                             }
                                         }
                                     } else {
                                         e.printStackTrace();
                                         Log.e("Exception", "" + e);
                                         Common.showToast(ChatActivity.this, "Network Error");
                                     }
                                 }
                             }

                );
    }
}
