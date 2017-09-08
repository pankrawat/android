package com.app.baccoon.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.baccoon.R;
import com.app.baccoon.bean.ProductBean;
import com.app.baccoon.bean.ProductBean;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

public class FavGridAdapter extends BaseAdapter {

	ArrayList<ProductBean> list;
	Resources res;
Context CTX;
	private class ViewHolderGroup {
		ImageView productImage;
		TextView productName;
		TextView productPrice;
		TextView productPlace;







	}

	public FavGridAdapter(Context context, ArrayList<ProductBean> m) {
		
		list = m;
		CTX=context;
	}

	@Override
	public View getView(final int groupPosition, View convertView, ViewGroup parent) {

		ViewHolderGroup holder;
		if (convertView == null) {
			LayoutInflater inflator=(LayoutInflater)CTX.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflator.inflate(R.layout.fav_grid_item, null);
			holder = new ViewHolderGroup();
			holder.productName = (TextView) convertView.findViewById(R.id.txtProductName);
			holder.productPrice = (TextView) convertView.findViewById(R.id.txtProductPrice);
			holder.productPlace = (TextView) convertView.findViewById(R.id.txtProductPlace);

       holder.productImage = (ImageView) convertView.findViewById(R.id.productImage);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolderGroup) convertView.getTag();
		}
		ProductBean obj = list.get(groupPosition);
	holder.productName.setText("" + obj.getProductName());

	holder.productPlace.setText("" + obj.getProd_location());
	holder.productPrice.setText("" + obj.getProductPrice());



		Ion.with(holder.productImage)
				.placeholder(R.mipmap.app_icon)

				.error(R.mipmap.app_icon)

				.load(obj.getProductImageUrl());




						convertView.setTag(holder);

						return convertView;
					}

					@Override
					public int getCount() {
						return list.size();
					}

					@Override
					public ProductBean getItem(int position) {
						return list.get(position);
					}

					@Override
					public long getItemId(int position) {
						// TODO Auto-generated method stub
						return position;
					}

				}
