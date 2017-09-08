package com.app.baccoon.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.baccoon.R;
import com.app.baccoon.bean.DrawerItem;

import java.util.ArrayList;
import java.util.List;



public class DrawerItemAdapter extends RecyclerView.Adapter<DrawerItemHolder> {




    // slide menu items
    private ArrayList<DrawerItem> items;

    public DrawerItemAdapter(ArrayList<DrawerItem>  items) {
        super();
        this.items = items;


    }

    @Override
    public DrawerItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.left_pannel_item, parent, false);

        return new DrawerItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DrawerItemHolder holder, int position) {
        DrawerItem obj = items.get(position);
        holder.itemText.setText(items.get(position).getTitle());

        if (obj.isSelected())
        {
            holder.itemLayout.setBackgroundColor(Color.parseColor("#ededed"));
            holder.itemIcon.setVisibility(View.VISIBLE);
            holder.itemText.setTextColor(Color.parseColor("#8AC443"));

        }
        else
        {
            holder.itemLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            holder.itemIcon.setVisibility(View.GONE);
            holder.itemText.setTextColor(Color.parseColor("#000000"));

        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}