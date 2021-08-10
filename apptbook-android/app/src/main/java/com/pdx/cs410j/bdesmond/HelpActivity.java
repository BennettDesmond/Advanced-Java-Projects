package com.pdx.cs410j.bdesmond;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        ImageButton launchMainPage = findViewById(R.id.backButton);
        launchMainPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HelpActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        TextView helpView = findViewById(R.id.helpInfo);
        helpView.setText("This program is a basic Appointment Book App written by Bennett Desmond. This app was built for Portland State's Advanced Java class and this is project 5. This app has the following three functionalities:\n\n[Add/Create] This page will add an appointment to an existing appointment book, or create a new appointment book. An appointment consists of a name, a description, a start date, and an end date.\n\n[Display] This option prints out all the appointments for the specified user.\n\n[Search] This option searches for appointments that start between the two entered dates for the specified user.");
    }
}