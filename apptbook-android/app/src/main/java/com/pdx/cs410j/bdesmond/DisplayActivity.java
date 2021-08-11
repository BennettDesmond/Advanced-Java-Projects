package com.pdx.cs410j.bdesmond;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import edu.pdx.cs410J.ParserException;

public class DisplayActivity extends AppCompatActivity {

    private TextView displayTextField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        setupUI(findViewById(R.id.parent));

        displayTextField = findViewById(R.id.displayResults);

        Button displayAppointmentBook = findViewById(R.id.displayButton);
        displayAppointmentBook.setOnClickListener(view -> displayAppointmentBook());

        ImageButton launchMainPage = findViewById(R.id.backButton);
        launchMainPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DisplayActivity.this, MainActivity.class);
                startActivity(intent);
                //startActivityForResult(intent, GET_SUM_FROM_CALCULATOR);
            }
        });
    }

    public void displayAppointmentBook() {
        EditText nameText = findViewById(R.id.nameText);
        String name = nameText.getText().toString();

        nameText.setText("");

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

        if(appBook.getAppointments().size() == 0) {
            displayTextField.setText("ERROR: "+name+" does not have an Appointment Book");
            return;
        }
        boolean nameComparison = name.equals(appBook.getOwnerName());
        if(!nameComparison) {
            displayTextField.setText("ERROR: There is a database issue with "+name+"'s Appointment Book");
            return;
        }

        PrettyPrinter printer = new PrettyPrinter(displayTextField);
        try {
            displayTextField.setText(appBook.getOwnerName() + "'s Appointment Book (" + appBook.getAppointments().size() + " Appointment(s))\n");
            printer.dump(appBook);
        } catch(IOException e) {
            displayTextField.setText("ERROR: A problem occurred while trying to display the Appointment Book");
        }
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
                    hideSoftKeyboard(DisplayActivity.this);
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