package com.borah.manjit.customcalendarview;

import android.content.Context;
import android.view.CollapsibleActionView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class GridViewAdapter extends BaseAdapter {

    private Context context;
    private List<Date> data;
    private ArrayList<String> markedDates;
    private Calendar currentCalendar;
    private Calendar maxCalendar;
    private Calendar minCalendar;
    private Calendar maxSet=null,minSet=null;

    private TextView tv;

    public GridViewAdapter(Context context, List<Date> items, Calendar currentCalendar,Calendar maxSet,Calendar minSet,ArrayList<String> markedDates) {
        this.context=context;
        this.data=items;
        this.currentCalendar=currentCalendar;
        this.markedDates=markedDates;
        setCalendarInView();

        if(maxSet !=null ){
            this.maxSet= (Calendar) maxSet.clone();
        }
        if(minSet != null){
            this.minSet= (Calendar) minSet.clone();
        }


    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=LayoutInflater.from(context).inflate(R.layout.grid_view_item,parent,false);
        tv=(TextView)view;
        Date date=data.get(position);
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd");
        String s=simpleDateFormat.format(date);
        tv.setText(s);
        if((date.getTime() > maxCalendar.getTimeInMillis() || date.getTime() < minCalendar.getTimeInMillis()) || (date.getTime() > maxSet.getTimeInMillis() || date.getTime() < minSet.getTimeInMillis()) ){
            view.setEnabled(false);
            view.setClickable(false);
        }

        simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy");
        String todayDateActual=simpleDateFormat.format(Calendar.getInstance().getTime());
        String todayDateAssumption=simpleDateFormat.format(date);

        if(todayDateActual.equalsIgnoreCase(todayDateAssumption) && view.isEnabled()){
            view.setBackgroundColor(context.getResources().getColor(R.color.active));
            tv.setTextColor(context.getResources().getColor(R.color.white));
        }

        if((!markedDates.isEmpty()) && markedDates.contains(todayDateAssumption)){
            mark(view,position);
        }


        return view;
    }


    private void setCalendarInView(){
        maxCalendar= (Calendar) currentCalendar.clone();
        maxCalendar.set(Calendar.DAY_OF_MONTH,currentCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        maxCalendar.set(Calendar.HOUR_OF_DAY,23);
        maxCalendar.set(Calendar.MINUTE,59);
        maxCalendar.set(Calendar.SECOND,59);
        maxCalendar.set(Calendar.MILLISECOND,999);

        minCalendar= (Calendar) currentCalendar.clone();
        minCalendar.set(Calendar.DAY_OF_MONTH,currentCalendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        minCalendar.set(Calendar.HOUR_OF_DAY,00);
        minCalendar.set(Calendar.MINUTE,00);
        minCalendar.set(Calendar.SECOND,00);
        minCalendar.set(Calendar.MILLISECOND,0000);

        this.maxSet= (Calendar) maxCalendar.clone();
        this.minSet= (Calendar) minCalendar.clone();
    }

    private void mark(View view,int pos){
        if(view.isEnabled()){
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy");
            String todayDateActual=simpleDateFormat.format(Calendar.getInstance().getTime());
            String todayDateAssumption=simpleDateFormat.format(getItem(pos));

            if(todayDateActual.equalsIgnoreCase(todayDateAssumption)){
                view.setBackground(context.getResources().getDrawable(R.drawable.currentdatemarked));
                ((TextView)view).setTextColor(context.getResources().getColor(R.color.white));
            }
            else{
                view.setBackgroundColor(context.getResources().getColor(android.R.color.holo_red_light));
                ((TextView)view).setTextColor(context.getResources().getColor(android.R.color.white));
            }

        }
    }
    private void unmark(View view){

    }


}
