package com.example.ultimatemydailylog;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.StringTokenizer;

public class CalendarActivity extends AppCompatActivity {

    Button btnMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        btnMain = findViewById(R.id.btn_gomain);

        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePicker datePicker = (DatePicker)findViewById(R.id.simple_datepicker);
        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear += 1;
                String sDate = year + "/" + monthOfYear + "/" + dayOfMonth;

                SharedPreferences sharedPreferences = getSharedPreferences("info", MODE_PRIVATE);
                String userName = sharedPreferences.getString("setName", null);
                String startDate = sharedPreferences.getString("setDate", null);

                Snackbar.make(view, userName + "님의 운동 시작 일은 " + startDate + "입니다.", Snackbar.LENGTH_SHORT).show();


                //Toast.makeText(getApplicationContext(),sDate + startDate, Toast.LENGTH_LONG).show();

            }
        });

        btnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}