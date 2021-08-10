package com.pdx.cs410j.bdesmond;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import edu.pdx.cs410J.ParserException;

public class AddActivity extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;
    private Button startDateButton,endDateButton;
    private Button startTimeButton,endTimeButton;
    private TextView printTextField;
    int startHour,startMinute,endHour,endMinute;
    boolean printResults = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        //initDatePicker();
        startDateButton = findViewById(R.id.startDatePickerButton);
        startDateButton.setText(getTodaysDate());
        endDateButton = findViewById(R.id.endDatePickerButton);
        endDateButton.setText(getTodaysDate());
        startTimeButton = findViewById(R.id.startTimePickerButton);
        endTimeButton = findViewById(R.id.endTimePickerButton);
        printTextField = findViewById(R.id.printResults);

        Button addAppointment = findViewById(R.id.addButton);
        addAppointment.setOnClickListener(view -> addAppointment());

        ImageButton launchMainPage = findViewById(R.id.backButton);
        launchMainPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddActivity.this, MainActivity.class);
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

    /*
    public void openDatePicker(View view) {
        datePickerDialog.show();
    }

     */

    public void startTimePicker(View view) {
        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String ending = "AM";
                startHour = selectedHour;
                startMinute = selectedMinute;
                if(startHour > 12) {
                    startHour = startHour - 12;
                    ending = "PM";
                }
                startTimeButton.setText(String.format(Locale.getDefault(), "%02d:%02d %s",startHour,startMinute, ending));
            }
        };
        int style = AlertDialog.THEME_HOLO_LIGHT;
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,style,listener,startHour,startMinute,false);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    public void endTimePicker(View view) {
        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String ending = "AM";
                startHour = selectedHour;
                startMinute = selectedMinute;
                if(startHour > 12) {
                    startHour = startHour - 12;
                    ending = "PM";
                }
                endTimeButton.setText(String.format(Locale.getDefault(), "%02d:%02d %s",startHour,startMinute, ending));
            }
        };
        int style = AlertDialog.THEME_HOLO_LIGHT;
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,style,listener,startHour,startMinute,false);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    public void addAppointment() {
        EditText nameText = findViewById(R.id.nameText);
        String name = nameText.getText().toString();
        EditText descriptionText = findViewById(R.id.descriptionText);
        String description = descriptionText.getText().toString();
        //Button startDateText = findViewById(R.id.startDatePickerButton);
        String startDate = startDateButton.getText().toString();
        //Button endDateText = findViewById(R.id.endDatePickerButton);
        String endDate = endDateButton.getText().toString();
        //Button startTimeText = findViewById(R.id.startTimePickerButton);
        String startTime = startTimeButton.getText().toString();
        //Button endTimeText = findViewById(R.id.endTimePickerButton);
        String endTime = endTimeButton.getText().toString();

        if((name.equals(""))||(description.equals(""))) {
            printTextField.setText("ERROR: An Appointment Cannot be created without having both a name and a description");
            return;
        }

        startDateButton.setText(getTodaysDate());
        endDateButton.setText(getTodaysDate());
        startTimeButton.setText("11:59");
        endTimeButton.setText("11:59");
        nameText.setText("");
        descriptionText.setText("");

        Date start = convertStringToDate(startDate+" "+startTime);
        Date end = convertStringToDate(endDate+" "+endTime);
        if ((start == null)||(end == null))
            printTextField.setText("ERROR: The Date is formatted incorrectly");
        Appointment newApp = new Appointment(start,end,description);
        parseFileAndDump(name,newApp);

        if(printResults) {
            printTextField.setText(newApp.toString());
        }
    }

    public void printUpdate(View view) {
        printResults = ((CheckBox) view).isChecked();
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

    private void parseFileAndDump(String name, Appointment appointment) {
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

        /*
        File file = new File(AddActivity.this.getFilesDir()+name+".txt");
        try {
            TextParser parser = new TextParser(new BufferedReader(new FileReader(file)));
            appBook = parser.parse();
        } catch (ParserException | FileNotFoundException e) {
            //printTextField.setText("ERROR: There was a problem reading from the database");
            //return;
            appBook = new AppointmentBook(name);
        }

         */


        /*
        try {
            TextParser parser = new TextParser(name);
            appBook = parser.parse();
        } catch (ParserException e) {
            printTextField.setText("ERROR: There was a problem reading from the database");
            return;
        }

         */
        boolean nameComparison = name.equals(appBook.getOwnerName());
        if(!nameComparison && (!appBook.getOwnerName().equals(""))) {
            printTextField.setText("ERROR: There is a database issue with "+name+"'s Appointment Book");
            return;
        }

        appBook.addAppointment(appointment);

        //File contextDirectory = getApplicationContext().getDataDir();
        //File sumsFile = new File(contextDirectory, "sums.txt");
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(file));
            //TextDumper textDumper = new TextDumper(pw);
            //textDumper.dump(appBook);
            pw.println("This file is a pain");
            pw.flush();
        } catch (IOException e) {
            printTextField.setText("ERROR: There was a problem saving information to the database");
        }

        /*
        try (FileOutputStream outputStream = openFileOutput(appBook.getOwnerName() + ".txt", MODE_PRIVATE)) {
            TextDumper textDumper = new TextDumper(new OutputStreamWriter(outputStream));
            textDumper.dump(appBook);
        } catch(IOException e) {
            printTextField.setText("ERROR: There was a problem saving information to the database");
        }

         */

        /*
        try {
            FileOutputStream fileout = openFileOutput(appBook.getOwnerName() + ".csv", MODE_PRIVATE);
            TextDumper dumper = new TextDumper(name);
            dumper.dump(appBook); //TODO make sure this line works with add and create for APP BOOKS
        } catch(IOException e) {
            printTextField.setText("ERROR: There was a problem saving information to the database");
        }

         */
    }
}