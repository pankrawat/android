package com.app.baccoon.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.baccoon.R;

public class DrawerItemHolder extends RecyclerView.ViewHolder {

        public ImageView itemIcon;
        public TextView itemText;
    public RelativeLayout itemLayout;
        public DrawerItemHolder(View itemView) {
            super(itemView);
            itemIcon= (ImageView) itemView.findViewById(R.id.drawer_icon);
            itemLayout= (RelativeLayout) itemView.findViewById(R.id.itemLayout);
            itemText= (TextView) itemView.findViewById(R.id.drawer_text);
        }
    }