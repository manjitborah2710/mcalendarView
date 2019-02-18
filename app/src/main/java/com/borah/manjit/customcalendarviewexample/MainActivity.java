package com.borah.manjit.customcalendarviewexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.borah.manjit.customcalendarview.CustomCalendarView;
import com.borah.manjit.customcalendarview.GridViewAdapter;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;

public class MainActivity extends AppCompatActivity {

    CustomCalendarView customCalendarView;
    ArrayList<Date> markDates;
    ArrayList<String> markedDates;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        customCalendarView=findViewById(R.id.ccv);
        markDates=new ArrayList<>();
        final SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy");

        try {
            markDates.add(simpleDateFormat.parse("09-02-2019"));
            markDates.add(simpleDateFormat.parse("21-02-2019"));
            markDates.add(simpleDateFormat.parse("02-03-2019"));
            customCalendarView.markDates(markDates);
        }
        catch (Exception ex){

        }
        markedDates=customCalendarView.getMarkedDatesStringList();


        customCalendarView.setOnDateClickedListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s=simpleDateFormat.format(customCalendarView.getDate(position));
                if(!markedDates.contains(s)){
                    markedDates.add(s);
                    customCalendarView.markDate(view,position);
                }
                else{
                    markedDates.remove(s);
                    customCalendarView.unmarkDate(view,position);
                }
            }
        });


    }
}
