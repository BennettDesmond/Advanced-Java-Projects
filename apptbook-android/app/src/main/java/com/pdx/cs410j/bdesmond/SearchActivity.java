package com.pdx.cs410j.bdesmond;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    Button timeButton;
    int hour, minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initDatePicker();
        dateButton = findViewById(R.id.startDatePickerButton);
        dateButton.setText(getTodaysDate());

        timeButton = findViewById(R.id.startTimePickerButton);

        Button searchAppointmentBook = findViewById(R.id.localSearchButton);
        searchAppointmentBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Add implementation to search through the specified Appointment Book

                //Intent intent = new Intent(SearchActivity.this, AddActivity.class);
                //startActivity(intent);
                //startActivityForResult(intent, GET_SUM_FROM_CALCULATOR);
            }
        });

        ImageButton launchMainPage = findViewById(R.id.backButton);
        launchMainPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                startActivity(intent);
                //startActivityForResult(intent, GET_SUM_FROM_CALCULATOR);
            }
        });
    }
    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day,month,year);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateButton.setText(date);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(this,style,dateSetListener,year,month,day);
    }

    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month) {
        if(month == 1) {
            return "JAN";
        } else if(month == 2) {
            return "FEB";
        } else if(month == 3) {
            return "MAR";
        } else if(month == 4) {
            return "APR";
        } else if(month == 5) {
            return "MAY";
        } else if(month == 6) {
            return "JUN";
        } else if(month == 7) {
            return "JUL";
        } else if(month == 8) {
            return "AUG";
        } else if(month == 9) {
            return "SEP";
        } else if(month == 10) {
            return "OCT";
        } else if(month == 11) {
            return "NOV";
        } else if(month == 12) {
            return "DEC";
        } else {
            return "Jan";
        }

    }

    public void openDatePicker(View view) {
        datePickerDialog.show();
    }

    public void popTimePicker(View view) {
        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String ending = "AM";
                hour = selectedHour;
                minute = selectedMinute;
                if(hour > 12) {
                    hour = hour - 12;
                    ending = "PM";
                }
                timeButton.setText(String.format(Locale.getDefault(), "%02d:%02d %s",hour,minute, ending));
            }
        };
        int style = AlertDialog.THEME_HOLO_LIGHT;
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,style,listener,hour,minute,false);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }
}