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
import com.app.baccoon.bean.ItemSoldBean;
import com.koushikdutta.ion.Ion;


import org.w3c.dom.Text;

import java.util.ArrayList;

public class ItemSoldListAdapter extends BaseAdapter {

	ArrayList<ItemSoldBean> list;
	Resources res;
    Context CTX;
	private class ViewHolderGroup {
		ImageView productImage;
		TextView txtTitle, txtDetails,txtBuyer, txtMobile, txtPrice, txtRu;


	}

	public ItemSoldListAdapter(Context context, ArrayList<ItemSoldBean> m) {
		
		list = m;
		CTX=context;
	}

	@Override
	public View getView(final int groupPosition, View convertView, ViewGroup parent) {

		ViewHolderGroup holder;
		if (convertView == null) {
			LayoutInflater inflator=(LayoutInflater)CTX.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflator.inflate(R.layout.itemsold_row_item, null);
			holder = new ViewHolderGroup();
			holder.txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);
			holder.txtMobile = (TextView) convertView.findViewById(R.id.txtMobile);
			holder.txtBuyer = (TextView) convertView.findViewById(R.id.txtBuyer);
			holder.txtDetails = (TextView) convertView.findViewById(R.id.txtDetails);
			holder.txtPrice = (TextView) convertView.findViewById(R.id.txtPrice);
			holder.txtRu = (TextView) convertView.findViewById(R.id.txtRu);
			holder.productImage = (ImageView) convertView.findViewById(R.id.productImage);

				convertView.setTag(holder);

		} else {
			holder = (ViewHolderGroup) convertView.getTag();
		}
		ItemSoldBean obj = list.get(groupPosition);
		holder.txtTitle.setText("" + (Html.fromHtml(obj.getTitle())));
		holder.txtDetails.setText("" + obj.getDescription());
		holder.txtBuyer.setText("" + obj.getBuyer());
		holder.txtMobile.setText("" + obj.getMobile());
		holder.txtPrice.setText("" + obj.getPrice());




		Ion.with(holder.productImage)
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
					public ItemSoldBean getItem(int position) {
						return list.get(position);
					}

					@Override
					public long getItemId(int position) {
						// TODO Auto-generated method stub
						return position;
					}

				}
