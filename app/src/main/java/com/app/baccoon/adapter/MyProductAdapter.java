package com.app.baccoon.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.app.baccoon.R;
import com.app.baccoon.activity.CreateSellActivity;
import com.app.baccoon.bean.MyProductBean;
import com.app.baccoon.bean.ProductBean;
import com.app.baccoon.fragment.MyProductFragment;
import com.app.baccoon.utils.API;
import com.app.baccoon.utils.Common;
import com.app.baccoon.utils.SpKey;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyProductAdapter extends BaseAdapter {

	ArrayList<MyProductBean> list;
	Resources res;
	Context CTX;
	MyProductFragment fragment;
	private PopupWindow popupWindow;
	private boolean isPopupShowing = false;
	private class ViewHolderGroup {
		ImageView productImage, imgSetting, imgSoldTag;
		TextView  productName;
		TextView  productPrice;
		TextView  productPlace;
		TextView btnSold;

	}

	public MyProductAdapter(Context context, ArrayList<MyProductBean> m, MyProductFragment fragment) {
		
		list = m;
		CTX=context;
		this.fragment=fragment;

	}

	@Override
	public View getView(final int groupPosition, View convertView, ViewGroup parent) {

		final ViewHolderGroup holder;
		if (convertView == null) {
			LayoutInflater inflator=(LayoutInflater)CTX.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflator.inflate(R.layout.myproduct_grid_item, null);
			holder = new ViewHolderGroup();
			holder.productName = (TextView) convertView.findViewById(R.id.txtProductName);
			holder.productPrice = (TextView) convertView.findViewById(R.id.txtProductPrice);
			holder.productPlace = (TextView) convertView.findViewById(R.id.txtProductPlace);
			holder.imgSoldTag = (ImageView) convertView.findViewById(R.id.imgSoldTag);
			holder.imgSetting = (ImageView) convertView.findViewById(R.id.imgSetting);
		//	holder.btnSold = (TextView) convertView.findViewById(R.id.btnSold);
			holder.productImage = (ImageView) convertView.findViewById(R.id.productImage);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolderGroup) convertView.getTag();
		}
		MyProductBean obj = list.get(groupPosition);
	holder.productName.setText("" + obj.getProd_name());

	holder.productPlace.setText("" + obj.getProd_location());
	holder.productPrice.setText("" + obj.getProd_price());
/*		if(obj.getIsSold().equalsIgnoreCase("1"))
		{
			holder.imgSoldTag.setVisibility(View.VISIBLE);
			holder.btnSold.setVisibility(View.GONE);
		}
		else
		{
holder.imgSoldTag.setVisibility(View.GONE);
			holder.btnSold.setVisibility(View.VISIBLE);
		}*/



		Ion.with(holder.productImage)
				.placeholder(R.mipmap.app_icon)

				.error(R.mipmap.app_icon)

				.load(obj.getProd_image());

		/*holder.btnSold.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//showSettingPopUp(groupPosition, holder.btnSetting);

			}
		});*/

		holder.imgSetting.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showSettingPopUp(groupPosition, holder.imgSetting);

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
					public MyProductBean getItem(int position) {
						return list.get(position);
					}

					@Override
					public long getItemId(int position) {
						// TODO Auto-generated method stub
						return position;
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
		TextView txtclose = (TextView) tmpView.findViewById(R.id.txtPerson_dash);
		txtclose.setVisibility(View.VISIBLE);
		txtEdit.setText("Edit");
		txtDel.setText("Delete");
		txtEdit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.e("edit click", "click");


				if (list.get(position).getIsSold().equalsIgnoreCase("0")) {
					Intent intent = new Intent(CTX, CreateSellActivity.class);
					intent.putExtra("MyProductBean", list.get(position));
					fragment.startActivityForResult(intent, 2);


				} else {
					Common.showToast(CTX, "You can not edit sold product");
				}
				popupWindow.dismiss();
				isPopupShowing = false;
			}
		});

		txtDel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub


				popupWindow.dismiss();
				isPopupShowing = false;
				showAlertDialog(position);


			}
		});


		txtclose.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.e("close click", "click");

				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("prod_id", list.get(position).getProd_id());
				jsonObject.addProperty("sellerId", list.get(position).getSellerId());


				if (Common.isConnected(CTX)) {
					Common.showProgress(CTX);
					Ion.with(CTX)
							.load(API.Product_Sold_Out)
							.setTimeout(60 * 1000)
							.setJsonObjectBody(jsonObject)
							.asString()
							.setCallback(new FutureCallback<String>() {


								@Override
								public void onCompleted(Exception e, String jsonString) {
									Log.d("call Sold Out", "callllll");
									Common.hideProgress(CTX);

									if (e == null) {
										if (jsonString != null && jsonString != "") {

											Log.e("json2", jsonString);

											try {
												JSONObject resultObj = new JSONObject(jsonString);


												if (resultObj.getBoolean("isSuccess")) {
													list.get(position).setIsSold("1");
													popupWindow.dismiss();
												}



											} catch (JSONException e1) {
												e1.printStackTrace();
												Toast.makeText(CTX, "Network Error", Toast.LENGTH_SHORT).show();
											}


										}

									} else {

										e.printStackTrace();
										Toast.makeText(CTX, "Network Error", Toast.LENGTH_SHORT).show();

									}


								}
							});
				}
			/*	if(list.get(position).getIsSold().equalsIgnoreCase("0")) {
					Intent intent = new Intent(CTX, CreateSellActivity.class);
					intent.putExtra("MyProductBean", list.get(position));
					fragment.startActivityForResult(intent, 2);


				}
				else {
					Common.showToast(CTX, "You can not edit sold product");
				}*/

				isPopupShowing = false;
			}
		});

	}

	private void showAlertDialog(final int position) {

		final android.support.v7.app.AlertDialog.Builder alertDialog= new android.support.v7.app.AlertDialog.Builder(CTX);
		alertDialog.setTitle("Confirm delete...");
		alertDialog.setMessage("Are you sure, do you want to delete this?");
		alertDialog.setPositiveButton("Yes'", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				deleteMyProduct(position);
			}
		})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				})
				.setIcon(R.drawable.flag_cr);

		alertDialog.show();



	}

	private void deleteMyProduct(final int position) {
/*
		{
			"prod_id" : "54",
				"sellerId" : "53",

		}*/

		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("sellerId", list.get(position).getSellerId());
		jsonObject.addProperty("prod_id", list.get(position).getProd_id());

		if (Common.isConnected(CTX)) {
			Common.showProgress(CTX);

			Ion.with(CTX)
					.load(API.DeleteMyProduct)
					.setTimeout(60 * 1000)
					.setJsonObjectBody(jsonObject)
					.asString()
					.setCallback(new FutureCallback<String>() {

						@Override
						public void onCompleted(Exception e, String jsonString) {

							Log.d("Delete My product", "api call");
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
