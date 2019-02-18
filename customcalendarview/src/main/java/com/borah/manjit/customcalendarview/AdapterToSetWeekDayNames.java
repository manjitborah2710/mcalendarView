package com.borah.manjit.customcalendarview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class AdapterToSetWeekDayNames extends BaseAdapter {

    Context context;
    String data[];
    public AdapterToSetWeekDayNames(Context context, String names[]) {
        this.context=context;
        this.data=names;
    }

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=LayoutInflater.from(context).inflate(R.layout.grid_view_item,parent,false);
        TextView textView=(TextView)view;
        textView.setText((String)getItem(position));
        textView.setTextColor(context.getResources().getColor(android.R.color.black));
        return view;
    }
}
