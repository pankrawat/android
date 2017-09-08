package com.app.baccoon.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.baccoon.R;
import com.app.baccoon.activity.ChatActivity;
import com.app.baccoon.bean.ChatBean;
import com.app.baccoon.bean.MessgeBean;
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

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by admin1 on 3/5/16.
 */
public class MessageAdapter extends BaseAdapter {
    ArrayList<ChatBean> list;
    Resources res;
    Context ctx;
    SharedPreference sharedPreference;
    int me;

    private class ViewHolderGroup {
        ImageView profileImage,msgImage;
        TextView txtName, txtMessage;
        TextView txtUnreadMsg;
        RelativeLayout layoutMsg;

    }

    public MessageAdapter(Context context, ArrayList<ChatBean> m, int me) {

        list = m;
        ctx = context;
        sharedPreference = SharedPreference.getInstance(ctx);
        this.me=me;
    }

    @Override
    public View getView(final int groupPosition, View convertView, ViewGroup parent) {

        final ViewHolderGroup holder;
        if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflator.inflate(R.layout.adapter_messages, null);
            holder = new ViewHolderGroup();
            holder.txtName = (TextView) convertView.findViewById(R.id.txtmssg2);
            holder.txtMessage = (TextView) convertView.findViewById(R.id.txtmsg);
            holder.txtUnreadMsg = (TextView) convertView.findViewById(R.id.txtunreadmsg);
            holder.profileImage = (ImageView) convertView.findViewById(R.id.imgprofile);
            holder.msgImage=(ImageView)convertView.findViewById(R.id.imgmsg);
            holder.layoutMsg = (RelativeLayout) convertView.findViewById(R.id.layoutMessage);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolderGroup) convertView.getTag();
        }

    try {
        final ChatBean obj = list.get(groupPosition);

        // holder.txtName.setText("" + (Html.fromHtml(obj.getTitle())));
        holder.txtName.setText("" + obj.getBuyerName());
        if (!obj.getUnread_msg().equals("0")) {
            holder.txtUnreadMsg.setText("" + obj.getUnread_msg());
            holder.msgImage.setImageResource(R.mipmap.green_message);
            obj.setIs_read("false");
        } else {
            holder.txtUnreadMsg.setVisibility(View.INVISIBLE);
            holder.msgImage.setImageResource(R.mipmap.grey_message);
            obj.setIs_read("true");
        }
        holder.txtMessage.setText(obj.getLast_unread_msg());

        Ion.with(ctx)
                .load(obj.getProfileImage())
                .asBitmap()
                .setCallback(new FutureCallback<Bitmap>() {
                    @Override
                    public void onCompleted(Exception e, Bitmap result) {

                        if (e == null) {
                            if (result != null) {
                                holder.profileImage.setImageBitmap(result);
                            }

                        } else {
                            e.printStackTrace();

                        }


                    }
                });


        holder.layoutMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (obj.getIs_read().equals("false"))
                    makeChatRead(obj.getChat_Id(), obj.getUser_type(), obj, holder);
                else {
                    //holder.msgImage.setImageResource(R.mipmap.grey_message);
                    Intent i = new Intent(ctx, ChatActivity.class);
                    i.putExtra("chatbean", obj);
                    ctx.startActivity(i);
                    Log.e("click", "successfull in else");
                }
            }
        });

    }
    catch (Exception e)
    {
        e.printStackTrace();
    }


        /*Ion.with(holder.profileImage)
                .placeholder(R.mipmap.app_icon)
                .error(R.mipmap.app_icon)
                .load(obj.getProfileImage());*/


        convertView.setTag(holder);

        return convertView;
    }

    private void makeChatRead(String chat_id, String user_type, final ChatBean obj, final ViewHolderGroup holder) {

        JsonObject jsonObject = new JsonObject();
        final String uid = sharedPreference.getString(SpKey.UID, "");

        jsonObject.addProperty("chat_id", chat_id);
        int type=0;
        if(user_type.equals("2"))
        {
            type=1;
        }
        else
        {
            type=2;
        }

        jsonObject.addProperty("user_type", ""+type);
        Log.e("user_type, me", "" +user_type+", "+me+" ,"+type+", "+obj.getUser_type());

        Common.showProgress(ctx);
        Ion.with(ctx)
                .load(API.Make_Chat_Read)
                .setTimeout(60 * 1000)
                .setJsonObjectBody(jsonObject)
                .asString()

                .setCallback(new FutureCallback<String>() {

                                 @Override
                                 public void onCompleted(Exception e, String jsonString) {

                                     Log.d("calllll", "callllll");
                                     Common.hideProgress(ctx);

                                     if (e == null) {
                                         if (jsonString != null && jsonString != "") {
                                             Log.e("json2", jsonString);
                                             try {

                                                 JSONObject js = new JSONObject(jsonString);
                                                 if (js.getString("isSuccess").equals("true")) {

                                                     obj.setIs_read("true");
                                                     holder.msgImage.setImageResource(R.mipmap.grey_message);
                                                     Intent i = new Intent(ctx, ChatActivity.class);
                                                     i.putExtra("chatbean", obj);
                                                     ctx.startActivity(i);
                                                     Log.e("click", "succesfull");

                                                 } else {
                                                     Common.showToast(ctx, (js.getString("message")));
                                                 }
                                             } catch (JSONException e1) {
                                                 e1.printStackTrace();
                                                 Common.showToast(ctx, "Network Error");
                                             }
                                         }
                                     } else {
                                         e.printStackTrace();
                                         Log.e("Exception", "" + e);
                                         Common.showToast(ctx, "Network Error");
                                     }
                                 }
                             }

                );
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public ChatBean getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

}

