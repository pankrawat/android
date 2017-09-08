package com.app.baccoon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AutoLocationAdapter extends ArrayAdapter<String> {

	private Context ctx;
	private LayoutInflater inflater;
	private int resourceId;

	public AutoLocationAdapter(Context context, int resource) {
		super(context, resource);
		ctx=context;
		resourceId=resource;
		inflater=(LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView tv=(TextView)inflater.inflate(resourceId, null);
		return super.getView(position, tv, parent);
	}

}