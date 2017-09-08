package com.app.baccoon.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.baccoon.R;
import com.app.baccoon.bean.SellerContactBean;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

public class SellerContactListAdapter extends BaseAdapter {

	ArrayList<SellerContactBean> list;
	Resources res;
Context CTX;
	private class ViewHolderGroup {
		ImageView sellerImage;
		TextView txtName, txtMobile;


	}

	public SellerContactListAdapter(Context context, ArrayList<SellerContactBean> m) {
		
		list = m;
		CTX=context;
	}

	@Override
	public View getView(final int groupPosition, View convertView, ViewGroup parent) {

		ViewHolderGroup holder;
		if (convertView == null) {
			LayoutInflater inflator=(LayoutInflater)CTX.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflator.inflate(R.layout.selller_contact_grid_item, null);
			holder = new ViewHolderGroup();
			holder.txtName = (TextView) convertView.findViewById(R.id.txtSellerName);
			holder.txtMobile = (TextView) convertView.findViewById(R.id.txtPhone);

			holder.sellerImage = (ImageView) convertView.findViewById(R.id.productImage);

				convertView.setTag(holder);

		} else {
			holder = (ViewHolderGroup) convertView.getTag();
		}
		SellerContactBean obj = list.get(groupPosition);
		holder.txtName.setText("" + (Html.fromHtml(obj.getName())));

		holder.txtMobile.setText("" + obj.getMobile());





		Ion.with(holder.sellerImage)
				.placeholder(R.mipmap.app_icon)
				.error(R.mipmap.app_icon)
				.load(obj.getImage());



						convertView.setTag(holder);

						return convertView;
					}

					@Override
					public int getCount() {
						return list.size();
					}

					@Override
					public SellerContactBean getItem(int position) {
						return list.get(position);
					}

					@Override
					public long getItemId(int position) {
						// TODO Auto-generated method stub
						return position;
					}

				}
