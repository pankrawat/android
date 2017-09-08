package com.app.baccoon.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.baccoon.R;

import com.app.baccoon.bean.ChatBean;
import com.app.baccoon.bean.MessgeBean;
import com.app.baccoon.utils.CircleImageView;
import com.app.baccoon.utils.SharedPreference;
import com.app.baccoon.utils.SpKey;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

/**
 * Created by admin1 on 3/5/16.
 */
public class ChatAdapter extends BaseAdapter
{
    ArrayList<MessgeBean> msgList;
    ChatBean obj;
    Resources res;
    Context ctx;
    SharedPreference sharedPreference;
    private class ViewHolderGroup {
       // ImageView profileImage;
        TextView txtMsg1,txtMsg2;
        TextView txtTime1,txtTime2;
        TextView txtName1,txtName2;
        LinearLayout layoutChat1,layoutChat2;
        CircleImageView imgprofile1, imgprofile2;

    }

    public ChatAdapter(Context context, ChatBean obj) {

        this.obj=obj;
        msgList=obj.getMsgList();
        ctx =context;
        sharedPreference=SharedPreference.getInstance(ctx);
    }

    @Override
    public View getView(final int groupPosition, View convertView, ViewGroup parent) {

        final ViewHolderGroup holder;
        if (convertView == null) {
            LayoutInflater inflator=(LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflator.inflate(R.layout.adapter_chat1, null);
            holder = new ViewHolderGroup();
            holder.txtMsg1 = (TextView) convertView.findViewById(R.id.txtmssg1);
            holder.txtMsg2 = (TextView) convertView.findViewById(R.id.txtmssg2);
            holder.txtTime1=(TextView)convertView.findViewById(R.id.txttime1);
            holder.txtTime2 = (TextView)convertView.findViewById(R.id.txttime2);
            holder.txtName1=(TextView)convertView.findViewById(R.id.txtname1);
            holder.txtName2=(TextView)convertView.findViewById(R.id.txtname2);
            holder.layoutChat1=(LinearLayout) convertView.findViewById(R.id.layoutChat1);
            holder.layoutChat2=(LinearLayout) convertView.findViewById(R.id.layoutChat2);
            holder.imgprofile1=(CircleImageView) convertView.findViewById(R.id.imgprofile1);
            holder.imgprofile2=(CircleImageView) convertView.findViewById(R.id.imgprofile2);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolderGroup) convertView.getTag();
        }

        MessgeBean bean=msgList.get(groupPosition);
        String date=bean.getChat_date().substring(11, 16);
       // Log.e("Real Date",bean.getChat_date());
       // Log.e("Calculated date",date);
        String date1[]=date.split(":");
        int hr=Integer.parseInt(date1[0]);
        String Am_Pm="am";
        if(hr!=12 && hr>12)
        {
            hr=hr-12;
            Am_Pm="pm";
        }
        else if(hr==12)
        {
            Am_Pm="pm";
        }
        else
        {
         Am_Pm="am";
        }

        if(! bean.getUser_type().equals(obj.getUser_type()))
        {
            holder.txtMsg1.setText(bean.getChat_msg());
            holder.txtTime1.setText(""+hr+":"+date1[1]+" "+Am_Pm);
            holder.txtName1.setText(obj.getBuyerName());
            holder.layoutChat1.setVisibility(View.VISIBLE);
            holder.layoutChat2.setVisibility(View.GONE);

            Ion.with(ctx)
                    .load(obj.getProfileImage())
                    .asBitmap()
                    .setCallback(new FutureCallback<Bitmap>() {
                        @Override
                        public void onCompleted(Exception e, Bitmap result) {

                            if(e==null)
                            {
                                if(result!=null) {
                                    holder.imgprofile1.setImageBitmap(result);
                                }

                            }
                            else
                            {
                                e.printStackTrace();

                            }


                        }
                    });




        }
        else if(bean.getUser_type().equals(obj.getUser_type()))
        {
            holder.txtMsg2.setText(bean.getChat_msg());
            holder.txtTime2.setText(""+hr+":"+date1[1]+Am_Pm);
            holder.txtName2.setText(sharedPreference.getString(SpKey.FIRST_NAME,""));
            holder.layoutChat2.setVisibility(View.VISIBLE);
            holder.layoutChat1.setVisibility(View.GONE);


            Ion.with(ctx)
                    .load(sharedPreference.getString(SpKey.ImageUlr,"")
                    )
                    .asBitmap()
                    .setCallback(new FutureCallback<Bitmap>() {
                        @Override
                        public void onCompleted(Exception e, Bitmap result) {

                            if(e==null)
                            {
                                if(result!=null) {
                                    holder.imgprofile2.setImageBitmap(result);
                                }

                            }
                            else
                            {
                                e.printStackTrace();

                            }


                        }
                    });


        }

        convertView.setTag(holder);

        return convertView;
    }

    @Override
    public int getCount() {
        return msgList.size();
    }

    @Override
    public MessgeBean getItem(int position) {
        return msgList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

}

