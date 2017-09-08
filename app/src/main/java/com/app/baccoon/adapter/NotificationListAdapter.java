package com.app.baccoon.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
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
import com.app.baccoon.bean.NotificationBean;
import com.app.baccoon.utils.API;
import com.app.baccoon.utils.Common;
import com.app.baccoon.utils.SharedPreference;
import com.app.baccoon.utils.SpKey;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by admin1 on 3/5/16.
 */
public class NotificationListAdapter extends BaseAdapter {
    ArrayList<NotificationBean> list;
    Resources res;
    Context ctx;
    SharedPreference sharedPreference;

    private class ViewHolderGroup {
        ImageView profileImage;
        TextView txtNotification;


    }

    public NotificationListAdapter(Context context, ArrayList<NotificationBean> m) {

        list = m;
        ctx = context;
        sharedPreference = SharedPreference.getInstance(ctx);

    }

    @Override
    public View getView(final int groupPosition, View convertView, ViewGroup parent) {

        final ViewHolderGroup holder;
        if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflator.inflate(R.layout.notification_row_item, null);
            holder = new ViewHolderGroup();
            holder.txtNotification = (TextView) convertView.findViewById(R.id.txtNotification);

            holder.profileImage = (ImageView) convertView.findViewById(R.id.imgProfile);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolderGroup) convertView.getTag();
        }

    try {
        final NotificationBean obj = list.get(groupPosition);


        holder.txtNotification.setText("" + obj.getMessage());

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




    }
    catch (Exception e)
    {
        e.printStackTrace();
    }



        return convertView;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public NotificationBean getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

}

