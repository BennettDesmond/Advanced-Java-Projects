package com.pdx.cs410j.bdesmond;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

        AppointmentBook appBook;

        File contextDirectory = getApplicationContext().getDataDir();
        File file = new File(contextDirectory, (name+".txt"));
        if (file.exists()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                TextParser parser = new TextParser(br);
                appBook = parser.parse();
            } catch (IOException | ParserException e) {
                appBook = new AppointmentBook();
            }
        } else {
            appBook = new AppointmentBook();
        }
        /*
        File file = new File(DisplayActivity.this.getFilesDir()+name+".txt");
        try {
            TextParser parser = new TextParser(new BufferedReader(new FileReader(file)));
            appBook = parser.parse();
        } catch (ParserException | FileNotFoundException e) {
            displayTextField.setText("ERROR: There was a problem reading from the database");
            return;
        }

         */

        if(appBook.getOwnerName().equals("")) {
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
            printer.dump(appBook);
        } catch(IOException e) {
            displayTextField.setText("ERROR: A problem occurred while trying to display the Appointment Book");
        }
    }
}