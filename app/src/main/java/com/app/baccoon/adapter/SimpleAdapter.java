package com.app.baccoon.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.baccoon.R;
import com.app.baccoon.bean.ProductBean;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.SimpleViewHolder> {


    private final Context mContext;
    private final ArrayList<ProductBean> mItems;
    private int mCurrentItemId = 0;

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        // public final TextView title;
        ImageView productImage, fav, imgSoldTag;
        TextView productName;
        TextView productPrice;
        TextView productPlace;
        TextView productFav;

        View convertView;

        public SimpleViewHolder(View convertView) {
            super(convertView);
            //title = (TextView) view.findViewById(R.id.title);

            productName = (TextView) convertView.findViewById(R.id.txtProductName);
            productPrice = (TextView) convertView.findViewById(R.id.txtProductPrice);
            productPlace = (TextView) convertView.findViewById(R.id.txtProductPlace);
            productFav = (TextView) convertView.findViewById(R.id.txtFav);
            productImage = (ImageView) convertView.findViewById(R.id.productImage);
            imgSoldTag = (ImageView) convertView.findViewById(R.id.imgSoldTag);
            fav = (ImageView) convertView.findViewById(R.id.fav);
            this.convertView = convertView;




        }
    }

    public SimpleAdapter(Context context, ArrayList<ProductBean> list) {
        mContext = context;
        mItems = list;

    }

    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.home_grid_item, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, final int position) {
        // holder.title.setText(mItems.get(position).toString());

        ProductBean obj = mItems.get(position);
        holder.productName.setText("" + obj.getProductName());
        holder.productFav.setText("" + obj.getNoOfLikes());
        holder.productPlace.setText("" + obj.getProd_location());
        holder.productPrice.setText("" + obj.getProductPrice());



        if(obj.getIsSold().equalsIgnoreCase("1"))
        {
            holder.imgSoldTag.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.imgSoldTag.setVisibility(View.GONE);
        }

        if (obj.getIsLiked() == 1) {
            holder.fav.setImageResource(R.mipmap.heart_red);
        } else {
            holder.fav.setImageResource(R.mipmap.heart);
        }




        Ion.with(holder.productImage)
                .placeholder(R.mipmap.app_icon)

                .error(R.mipmap.app_icon)

                .load(obj.getProductImageUrl());

        holder.convertView.setTag(position);

    }


    @Override
    public int getItemCount() {
        return mItems.size();
    }
}