package com.pdx.cs410j.bdesmond;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import edu.pdx.cs410J.ParserException;

public class SearchActivity extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;
    private Button startDateButton,endDateButton,startTimeButton,endTimeButton;
    int hour,minute;
    TextView searchTextField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setupUI(findViewById(R.id.parent));
        startDateButton = findViewById(R.id.startDateButton);
        startDateButton.setText(getTodaysDate());
        endDateButton = findViewById(R.id.endDateButton);
        endDateButton.setText(getTodaysDate());
        startTimeButton = findViewById(R.id.startTimeButton);
        endTimeButton = findViewById(R.id.endTimeButton);
        searchTextField = findViewById(R.id.searchResults);

        Button addAppointment = findViewById(R.id.searchButton);
        addAppointment.setOnClickListener(view -> searchAppointmentBooks());

        ImageButton launchMainPage = findViewById(R.id.backButton);
        launchMainPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                startActivity(intent);
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

    public void startDatePicker(View view) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                startDateButton.setText(date);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(this,style,dateSetListener,year,month,day);
        datePickerDialog.show();
    }

    public void endDatePicker(View view) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                endDateButton.setText(date);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(this,style,dateSetListener,year,month,day);
        datePickerDialog.show();
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

    public void startTimePicker(View view) {
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
                startTimeButton.setText(String.format(Locale.getDefault(), "%02d:%02d %s",hour,minute, ending));
            }
        };
        int style = AlertDialog.THEME_HOLO_LIGHT;
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,style,listener,hour,minute,false);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    public void endTimePicker(View view) {
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
                endTimeButton.setText(String.format(Locale.getDefault(), "%02d:%02d %s",hour,minute, ending));
            }
        };
        int style = AlertDialog.THEME_HOLO_LIGHT;
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,style,listener,hour,minute,false);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    public void searchAppointmentBooks() {
        EditText nameText = findViewById(R.id.nameText);
        String name = nameText.getText().toString();
        String startDate = startDateButton.getText().toString();
        String endDate = endDateButton.getText().toString();
        String startTime = startTimeButton.getText().toString();
        String endTime = endTimeButton.getText().toString();
        Date start = convertStringToDate(startDate + " " + startTime);
        Date end = convertStringToDate(endDate + " " + endTime);
        AppointmentBook appBook;
        AppointmentBook tempBook = new AppointmentBook(name);

        resetUIFields(nameText);

        if ((start == null) || (end == null)) {
            searchTextField.setText("ERROR: The Date is formatted incorrectly");
            return;
        }
        if(start.compareTo(end) > 0) {
            searchTextField.setText("ERROR: The first date must be before the second date");
            return;
        }

        appBook = parseAppointmentBook(name);
        if (errorCheckAppBook(name, appBook)) return;

        for (Appointment app : appBook.getAppointments()) {
            if((app.getBeginTime().compareTo(start) > 0) && (app.getBeginTime().compareTo(end) < 0)) {
                tempBook.addAppointment(app);
            }
        }

        if(appointmentBookIsEmpty(tempBook)) {
            searchTextField.setText("No Appointments from "+name+"'s Appointment Book fall between "+start+" and "+end);
            return;
        }

        PrettyPrinter printer = new PrettyPrinter(searchTextField);
        try {
            searchTextField.setText(tempBook.getAppointments().size()+" match(es) found in "+appBook.getOwnerName() + "'s Appointment Book\n");
            printer.dump(tempBook);
        } catch(IOException e) {
            searchTextField.setText("ERROR: A problem occurred while trying to display the Appointment Book");
        }
    }

    private boolean errorCheckAppBook(String name, AppointmentBook appBook) {
        if(appBook == null) {
            searchTextField.setText("ERROR: The Appointment Book file's name does not match the name of the person entered");
            return true;
        }
        if(appointmentBookIsEmpty(appBook)) {
            searchTextField.setText("ERROR: There is no Appointment Book for "+ name);
            return true;
        }
        return false;
    }

    private void resetUIFields(EditText nameText) {
        //startDateButton.setText(getTodaysDate());
        //endDateButton.setText(getTodaysDate());
        //startTimeButton.setText("11:59 PM");
        //endTimeButton.setText("11:59 PM");
        nameText.setText("");
    }

    private Date convertStringToDate(String dateString) {
        Date dateClassObj = new Date();
        DateFormat format = new SimpleDateFormat("MMM dd yyyy hh:mm a");
        try {
            dateClassObj = format.parse(dateString);
        } catch(ParseException e) {
            return null;
        }
        return dateClassObj;
    }

    private AppointmentBook parseAppointmentBook(String name) {
        AppointmentBook appBook;

        File contextDirectory = getApplicationContext().getDataDir();
        File file = new File(contextDirectory, (name+".txt"));
        if (file.exists()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                if(!br.ready()) {
                    appBook = new AppointmentBook(name);
                } else {
                    TextParser parser = new TextParser(br);
                    appBook = parser.parse();
                }
            } catch (IOException | ParserException e) {
                appBook = new AppointmentBook(name);
            }
        } else {
            appBook = new AppointmentBook(name);
        }

        boolean nameComparison = name.equals(appBook.getOwnerName());
        if(!nameComparison && (!appBook.getOwnerName().equals(""))) {
            searchTextField.setText("ERROR: There is a database issue with "+name+"'s Appointment Book");
            return null;
        }
        return appBook;
    }

    private boolean appointmentBookIsEmpty(AppointmentBook appBook) {
        if(appBook.getAppointments().size() == 0) {
            searchTextField.setText("ERROR: "+appBook.getOwnerName()+" does not have an Appointment Book");
            return true;
        }
        return false;
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if(inputMethodManager.isAcceptingText()){
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(),
                    0
            );
        }
    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(SearchActivity.this);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }
}