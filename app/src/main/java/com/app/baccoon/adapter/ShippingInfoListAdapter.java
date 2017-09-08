package com.app.baccoon.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.app.baccoon.R;
import com.app.baccoon.bean.ShippingInfoBean;
import com.app.baccoon.fragment.ShippingInfoFragment;
import com.app.baccoon.utils.API;
import com.app.baccoon.utils.Common;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ShippingInfoListAdapter extends BaseAdapter {

    ArrayList<ShippingInfoBean> list;
    private OnSettingClickListener listener;
    private PopupWindow popupWindow;
    private boolean isPopupShowing = false;
    LayoutInflater inflator;

    Context CTX;
    private JsonObject jsonObject;
    private ShippingInfoFragment fragment;

    public void setListener(OnSettingClickListener listener) {
        this.listener = listener;
    }


    private class ViewHolderGroup {

        TextView txtTitle, txtPhone, txtAddressLine1, txtAddressLine2, txtAddressLine3;
        ImageView btnSetting;
    }

    public ShippingInfoListAdapter(Context context, ArrayList<ShippingInfoBean> m, ShippingInfoFragment fragment) {
        this.CTX = context;
        inflator  = LayoutInflater.from(CTX);
        list = m;
        this.fragment = fragment;
    }

    @Override
    public View getView(final int groupPosition, View convertView, ViewGroup parent) {

        final ViewHolderGroup holder;
        if (convertView == null) {

            convertView = inflator.inflate(R.layout.shipping_info_row_item,null);
            holder = new ViewHolderGroup();
            holder.txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);
            holder.txtPhone = (TextView) convertView.findViewById(R.id.txtPhone);
            holder.txtAddressLine1 = (TextView) convertView.findViewById(R.id.txtAddressLine1);
            holder.txtAddressLine2 = (TextView) convertView.findViewById(R.id.txtAddressLine2);
            holder.txtAddressLine3 = (TextView) convertView.findViewById(R.id.txtAddressLine3);
            holder.btnSetting = (ImageView) convertView.findViewById(R.id.btnSetting);


            convertView.setTag(holder);

        } else {
            holder = (ViewHolderGroup) convertView.getTag();
        }
        ShippingInfoBean obj = list.get(groupPosition);

        holder.txtTitle.setText("" + obj.getShipTo());
        holder.txtPhone.setText("" + obj.getShipPhone());
        holder.txtAddressLine1.setText("" + obj.getShipAddress());
        holder.txtAddressLine2.setText("" + obj.getShipCity());
        holder.txtAddressLine3.setText("" + obj.getShipState() + ", " + obj.getShipPincode() + ", " + obj.getShipCountry());

        holder.btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSettingPopUp(groupPosition, holder.btnSetting);

            }
        });


        convertView.setTag(holder);

        return convertView;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public ShippingInfoBean getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }


    public interface OnSettingClickListener {
        void onSettingClick(int position);
    }


    private void showSettingPopUp(final int position, ImageView btnSetting) {

        popupWindow = new PopupWindow(CTX);

        LayoutInflater inflator = (LayoutInflater) CTX.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View tmpView = inflator.inflate(R.layout.spiner_popup_layout, null);

//		View tmpView = Context.getLayoutInflater().inflate(R.layout.spiner_popup_layout, null);


        popupWindow.setFocusable(true);
        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(tmpView);
        popupWindow.showAsDropDown(btnSetting, 0, 0);
        isPopupShowing = true;

        TextView txtEdit = (TextView) tmpView.findViewById(R.id.txtCompany);
        TextView txtDel = (TextView) tmpView.findViewById(R.id.txtPerson);
        txtEdit.setText("Edit");
        txtDel.setText("Delete");
        txtEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.e("edit click", "click");

                if (listener != null) {
                    listener.onSettingClick(position);
                }

                popupWindow.dismiss();
                isPopupShowing = false;
            }
        });

        txtDel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub


                deleteAddressCall(position);

                popupWindow.dismiss();
                isPopupShowing = false;
            }
        });


    }

    private void deleteAddressCall(final int position) {


        jsonObject = new JsonObject();
        jsonObject.addProperty("user_id", list.get(position).getUser_id());
        jsonObject.addProperty("ship_id", list.get(position).getShip_id());


        if (Common.isConnected(CTX)) {
            Common.showProgress(CTX);

            Ion.with(CTX)
                    .load(API.Delete_Shiping_Address)
                    .setJsonObjectBody(jsonObject)
                    .asString()
                    .setCallback(new FutureCallback<String>() {

                        @Override
                        public void onCompleted(Exception e, String jsonString) {

                            Log.d("Delete address", "api call");
                            Common.hideProgress(CTX);

                            if (e == null) {
                                if (jsonString != null && jsonString != "") {
                                    Log.e("json2", jsonString);

                                    try {
                                        JSONObject resultObj = new JSONObject(jsonString);
                                        if (resultObj.getBoolean("isSuccess")) {
//											"isSuccess": true,
//													"message": "Shipping address has been deleted successfully",

                                            Common.showToast(CTX, "" + resultObj.getString("message"));
                                            list.remove(position);
                                            notifyDataSetChanged();
                                            fragment.updateAddressCountCallBack(list.size());


                                        }


                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                    }


                                }

                            } else {

                                e.printStackTrace();

                            }


                        }
                    });


        }

    }


}
