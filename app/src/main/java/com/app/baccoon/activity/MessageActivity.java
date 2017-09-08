package com.app.baccoon.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.baccoon.R;
import com.app.baccoon.adapter.MessageAdapter;
import com.app.baccoon.bean.ChatBean;
import com.app.baccoon.bean.MessgeBean;
import com.app.baccoon.utils.API;
import com.app.baccoon.utils.Common;
import com.app.baccoon.utils.SharedPreference;
import com.app.baccoon.utils.SpKey;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.vk.sdk.api.model.VKApiUserFull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by admin1 on 3/5/16.
 */
public class MessageActivity extends Activity {

    SharedPreference sharedPreference;
    ListView messageList;
    ArrayList<ChatBean> chatList;
    ImageView btnBack;
    MessageAdapter messageAdapter;
    private SwipyRefreshLayout mSwipyRefreshLayout;
    int count=0;
    private int me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        sharedPreference = SharedPreference.getInstance(this);
        initView();
    }

    private void initView() {

        chatList = new ArrayList<>();
        messageList = (ListView) findViewById(R.id.messages);
        btnBack = (ImageView) findViewById(R.id.btnBack);
        mSwipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.refresh);
        mSwipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.TOP);
        mSwipyRefreshLayout.setDistanceToTriggerSync(15);
        // getData();


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mSwipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {


            @Override
            public void onRefresh(SwipyRefreshLayoutDirection swipyRefreshLayoutDirection) {

                // Log.e("Layout Direction",""+swipyRefreshLayoutDirection);
                if (swipyRefreshLayoutDirection.toString().equals("TOP")) {
                    Log.e("Layout Direction", "This is in Top");
                    if (chatList.size() > 0)
                        chatList.clear();
                  if(sharedPreference.getBoolean(SpKey.isRefreshRequired,false))
                      getData(true);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (chatList.size() > 0)
            chatList.clear();
        Log.e("In Resume", "true");
        getData(false);
    }

    public void getData(final boolean fromSwipe) {

        count++;
        JsonObject jsonObject = new JsonObject();
        final String uid = sharedPreference.getString(SpKey.UID, "");

        jsonObject.addProperty("user_id", uid);
        Log.e("user id", "" + uid);

       /* "isSuccess": true,
                "message": "Message displayed successfully!",
                "Result": [
        {
            "chat_id": "1",
                "product_id": "3",
                "seller_id": "2",
                "buyer_id": "1",
                "offer_price": "200",
                "chat_date": "2016-05-03 18:25:56",
                "status": "0",
                "buyerName": "Abhishek Tyagi",
                "profileImage": "http://ec2-52-205-20-98.compute-1.amazonaws.com/baccoon/profileImages/",
                "unread_msg": 3,
                "last_unread_msg": "testing2",
                "msg_data": [
            {
                "chat_msg": "testing",
                    "user_type": "2",
                    "chat_date": "2016-05-03 18:25:56"
            },
            {
                "chat_msg": "testing2",
                    "user_type": "1",
                    "chat_date": "2016-05-03 18:29:03"
            },
            {
                "chat_msg": "testing2",
                    "user_type": "1",
                    "chat_date": "2016-05-03 18:29:24"
            },
            {
                "chat_msg": "testing2",
                    "user_type": "2",
                    "chat_date": "2016-05-03 18:29:29"
            },
            {
                "chat_msg": "testing2",
                    "user_type": "2",
                    "chat_date": "2016-05-03 18:33:03"
            }
            ]
        }
*/


       if(!fromSwipe)
       {Common.showProgress(this);}
        Ion.with(this)
                .load(API.Get_All_Msg)
                .setTimeout(60 * 1000)
                .setJsonObjectBody(jsonObject)
                .asString()

                .setCallback(new FutureCallback<String>() {

                                 @Override
                                 public void onCompleted(Exception e, String jsonString) {
                                     mSwipyRefreshLayout.setRefreshing(false);

                                     Log.d("calllll", "callllll");
                                     if(!fromSwipe) {
                                         Common.hideProgress(MessageActivity.this);
                                     }

                                     if (e == null) {
                                         if (jsonString != null && jsonString != "") {
                                             Log.e("json2", jsonString);
                                             try {

                                                 JSONObject js = new JSONObject(jsonString);
                                                 if (js.getString("isSuccess").equals("true")) {
                                                     sharedPreference.putBoolean(SpKey.isRefreshRequired,false);

                                                     JSONArray root = js.getJSONArray("Result");

                                                     for (int i = 0; i < root.length(); i++) {

                                                         JSONObject jobj = root.getJSONObject(i);
                                                         ArrayList<MessgeBean> msgList = new ArrayList<MessgeBean>();

                                                         ChatBean chatBean = new ChatBean();

                                                         chatBean.setChat_Id(jobj.getString("chat_id"));
                                                         chatBean.setProductId(jobj.getString("product_id"));
                                                         chatBean.setSellerId(jobj.getString("seller_id"));
                                                         chatBean.setBuyerId(jobj.getString("buyer_id"));
                                                         chatBean.setOffer(jobj.getString("offer_price"));
                                                         chatBean.setChat_date(jobj.getString("chat_date"));
                                                         chatBean.setStatus(jobj.getString("status"));
                                                         chatBean.setBuyerName(jobj.getString("buyerName"));
                                                         chatBean.setProfileImage(jobj.getString("profileImage"));
                                                         chatBean.setUnread_msg(jobj.getString("unread_msg"));
                                                         chatBean.setLast_unread_msg(jobj.getString("last_unread_msg"));

                                                         if (uid.equals(jobj.getString("seller_id"))) {
                                                             chatBean.setUser_type("1");

                                                         } else if (uid.equals(jobj.getString("buyer_id"))) {
                                                             chatBean.setUser_type("2");

                                                         }


                                                         JSONArray msgs = jobj.getJSONArray("msg_data");
                                                         for (int j = 0; j < msgs.length(); j++) {

                                                             JSONObject jobj1 = msgs.getJSONObject(j);
                                                             MessgeBean bean = new MessgeBean();
                                                             bean.setChat_date(jobj1.getString("chat_date"));
                                                             bean.setChat_msg(jobj1.getString("chat_msg"));
                                                             bean.setUser_type(jobj1.getString("user_type"));
                                                             msgList.add(bean);
                                                         }
                                                         chatBean.setMsgList(msgList);

                                                         /*if(count==1)
                                                         {
                                                             Log.e("count if",""+count);
                                                             chatList.add(chatBean);
                                                         }
                                                         else {
                                                             Log.e("count else",""+count);
                                                             for (int k = 0; k < chatList.size(); k++) {
                                                                 if (chatBean.getChat_Id().equals(chatList.get(i).getChat_Id())) {
                                                                     break;
                                                                 } else {
                                                                     chatList.add(i, chatBean);
                                                                 }
                                                             }
                                                         }*/

                                                         chatList.add(chatBean);
                                                     }


                                                     if (chatList != null && chatList.size() > 0) {
                                                         if (messageAdapter == null) {
                                                             messageAdapter = new MessageAdapter(MessageActivity.this, chatList, me);
                                                             messageList.setAdapter(messageAdapter);
                                                         } else {
                                                             messageAdapter.notifyDataSetChanged();
                                                         }

                                                     }

                                                 } else {
                                                     Common.showToast(MessageActivity.this, (js.getString("message")));
                                                 }
                                             } catch (JSONException e1) {
                                                 e1.printStackTrace();
                                                 mSwipyRefreshLayout.setRefreshing(false);
                                                 Common.showToast(MessageActivity.this, "Network Error");
                                             }
                                         }
                                     } else {
                                         e.printStackTrace();
                                         mSwipyRefreshLayout.setRefreshing(false);
                                         Log.e("Exception", "" + e);
                                         Common.showToast(MessageActivity.this, "Network Error");
                                     }
                                 }
                             }

                );
    }
}

