package com.borah.manjit.customcalendarview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CustomCalendarView extends LinearLayout {
    private View mainCustomCalendarView;
    private GridView mainCalendarGridView;
    private ArrayList<Date> dates;
    private ArrayList<String> markedDates;
    private Context context;
    private ColorStateList textViewOriginalColor;
    private final int MAX_DAYS=42;
    private int SUN_1_MON_2=1;



    private Calendar currentDisplayableCalendar,maxDate=null,minDate=null;

    private Button show_prev_month,show_next_month,show_current_month;




    public CustomCalendarView(Context context) {
        super(context);
        this.context=context;
        markedDates=new ArrayList<>();
        init();
    }

    public CustomCalendarView(Context context,AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        markedDates=new ArrayList<>();
        init();
    }

    public CustomCalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        markedDates=new ArrayList<>();
        init();
    }

    protected void init(){
        textViewOriginalColor=(new TextView(context)).getTextColors();
        LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mainCustomCalendarView=layoutInflater.inflate(R.layout.custom_calendar_view,this,true);
        setDayNames((GridView) mainCustomCalendarView.findViewById(R.id.gv_days));
        mainCalendarGridView=mainCustomCalendarView.findViewById(R.id.gv);
        setCalendar();
        setCalendarNavigationButtons();

    }


    public void markDate(View view,int pos){
        if(view.isEnabled()){
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy");
            String todayDateActual=simpleDateFormat.format(Calendar.getInstance().getTime());
            String todayDateAssumption=simpleDateFormat.format(dates.get(pos));

            if(todayDateActual.equalsIgnoreCase(todayDateAssumption)){
                view.setBackground(getResources().getDrawable(R.drawable.currentdatemarked));
                ((TextView)view).setTextColor(context.getResources().getColor(R.color.white));
            }
            else{
                view.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                ((TextView)view).setTextColor(getResources().getColor(android.R.color.white));
            }

            if(!this.markedDates.contains(todayDateAssumption)){
                this.markedDates.add(todayDateAssumption);
            }

        }

    }
    public void unmarkDate(View view,int pos){

        if(view.isEnabled()){
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy");
            String todayDateActual=simpleDateFormat.format(Calendar.getInstance().getTime());
            String todayDateAssumption=simpleDateFormat.format(dates.get(pos));

            if(todayDateActual.equalsIgnoreCase(todayDateAssumption)){
                view.setBackgroundColor(context.getResources().getColor(R.color.active));
                ((TextView)view).setTextColor(context.getResources().getColor(R.color.white));
            }
            else{
                view.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                ((TextView)view).setTextColor(textViewOriginalColor);
            }

            if(this.markedDates.contains(todayDateAssumption)){
                this.markedDates.remove(todayDateAssumption);
            }
        }

    }

    public void markDates(ArrayList<Date> dates){
        SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy");
        ArrayList<String> dates_string_list=new ArrayList<>();
        for(Date i : dates){
            dates_string_list.add(sdf.format(i));
        }
        for(String i : dates_string_list){
            if(!this.markedDates.contains(i)){
                this.markedDates.add(i);
            }
        }
        setCalendar(getCurrentDisplayableCalendar());
    }

    public void setOnDateClickedListener(OnItemClickListener onDateClickedListener) {
        mainCalendarGridView.setOnItemClickListener(onDateClickedListener);
    }

    public void setCalendar(){
        Calendar currentCalendar=getCurrentActualCalendar();

        Calendar calendarTemp= (Calendar) currentCalendar.clone();

        populateDatesList(calendarTemp);

        GridViewAdapter gridViewAdapter=new GridViewAdapter(context,dates,currentCalendar,this.maxDate,this.minDate,this.markedDates);

        mainCalendarGridView.setAdapter(gridViewAdapter);

        setNameOfMonthAndYear(currentCalendar);
        setCurrentDisplayableCalendar(currentCalendar);

    }

    public void setCalendar(Calendar calendar){
        Calendar currentCalendar=calendar;

        Calendar calendarTemp= (Calendar) currentCalendar.clone();

        populateDatesList(calendarTemp);

        GridViewAdapter gridViewAdapter=new GridViewAdapter(context,dates,currentCalendar,this.maxDate,this.minDate,this.markedDates);
        mainCalendarGridView.setAdapter(gridViewAdapter);

        setNameOfMonthAndYear(currentCalendar);
        setCurrentDisplayableCalendar(currentCalendar);
    }


    public Date getDate(int pos){
        Date date= dates.get(pos);
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY,getCurrentActualCalendar().get(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE,getCurrentActualCalendar().get(Calendar.MINUTE));
        calendar.set(Calendar.SECOND,getCurrentActualCalendar().get(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND,getCurrentActualCalendar().get(Calendar.MILLISECOND));
        return calendar.getTime();
    }


    private void setDayNames(GridView gridView){
        gridView.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        String days[]=new String[]{"SUN","MON","TUE","WED","THU","FRI","SAT"};
        AdapterToSetWeekDayNames adapter=new AdapterToSetWeekDayNames(context,days);
        gridView.setAdapter(adapter);
    }

    private void setNameOfMonthAndYear(Calendar calendar){
        TextView currentMonthDisplay=mainCustomCalendarView.findViewById(R.id.current_month_display);
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("MMMM, yyyy");
        String d=simpleDateFormat.format(calendar.getTime());
        currentMonthDisplay.setText(d);

        show_current_month=mainCustomCalendarView.findViewById(R.id.today_btn);
        show_current_month.setText(getCurrentActualCalendar("MMM\nyyyy"));

    }




    private void populateDatesList(Calendar calendarTemp){
        dates=new ArrayList<>();
        calendarTemp.set(Calendar.DAY_OF_MONTH,1);

        int startingDay=calendarTemp.get(Calendar.DAY_OF_WEEK)-1;

        calendarTemp.add(Calendar.DATE,-startingDay);


        for(int i=0;i<MAX_DAYS;i++){
            Date date=calendarTemp.getTime();
            dates.add(date);
            calendarTemp.add(Calendar.DATE,1);
        }
    }

    public void setCurrentDisplayableCalendar(Calendar currentDisplayableCalendar){
        this.currentDisplayableCalendar=currentDisplayableCalendar;
    }
    public Calendar getCurrentDisplayableCalendar(){
        return this.currentDisplayableCalendar;
    }
    public Calendar getCurrentActualCalendar(){
        return Calendar.getInstance();
    }
    public String getCurrentDisplayableCalendar(String pattern){
        SimpleDateFormat sdf=new SimpleDateFormat(pattern);
        String date=sdf.format(this.currentDisplayableCalendar.getTime());
        return date;
    }
    public String getCurrentActualCalendar(String pattern){
        SimpleDateFormat sdf=new SimpleDateFormat(pattern);
        String date=sdf.format(Calendar.getInstance().getTime());
        return date;
    }



    private void setCalendarNavigationButtons(){
        show_prev_month=mainCustomCalendarView.findViewById(R.id.prev_month_btn);
        show_next_month=mainCustomCalendarView.findViewById(R.id.next_month_btn);
        show_current_month=mainCustomCalendarView.findViewById(R.id.today_btn);

        show_prev_month.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c_calendar=getCurrentDisplayableCalendar();
                c_calendar.add(Calendar.MONTH,-1);
                setCalendar(c_calendar);
                show_current_month.setVisibility(View.VISIBLE);
                if(getCurrentActualCalendar("MMMM-yyyy").equalsIgnoreCase(getCurrentDisplayableCalendar("MMMM-yyyy"))){
                    show_current_month.setVisibility(View.GONE);
                }
            }
        });

        show_next_month.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c_calendar=getCurrentDisplayableCalendar();
                c_calendar.add(Calendar.MONTH,1);
                setCalendar(c_calendar);
                show_current_month.setVisibility(View.VISIBLE);
                if(getCurrentActualCalendar("MMMM-yyyy").equalsIgnoreCase(getCurrentDisplayableCalendar("MMMM-yyyy"))){
                    show_current_month.setVisibility(View.GONE);
                }
            }
        });

        show_current_month.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setCalendar();
                v.setVisibility(View.GONE);
            }
        });

    }

    public void setMaxDate(Calendar calendar){
        this.maxDate=calendar;
    }
    public void setMinDate(Calendar calendar){
        this.minDate=calendar;
    }

    public void setOnlySpecifiedMonth(Calendar calendar){
        setCalendar(calendar);
        disableNextButton();
        disablePrevButton();
    }

    private void disablePrevButton(){
        show_prev_month.setClickable(false);
        show_prev_month.setEnabled(false);
    }
    private void disableNextButton(){
        show_next_month.setClickable(false);
        show_next_month.setEnabled(false);
    }
    private void enablePrevButton(){
        show_prev_month.setClickable(true);
        show_prev_month.setEnabled(true);
    }
    private void enableNextButton(){
        show_next_month.setClickable(true);
        show_next_month.setEnabled(true);
    }

    public ArrayList<Date> getMarkedDates(){
        ArrayList<Date> markedDates=new ArrayList<>();
        SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy");
        try {
            for (String i : this.markedDates) {
                markedDates.add(sdf.parse(i));
            }
        }
        catch (ParseException ex){

        }
        return markedDates;
    }

    public ArrayList<String> getMarkedDatesStringList(){
        return (ArrayList<String>) this.markedDates.clone();
    }
}
