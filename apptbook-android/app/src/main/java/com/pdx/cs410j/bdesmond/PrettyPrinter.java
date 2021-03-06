package com.pdx.cs410j.bdesmond;

import android.widget.Button;
import android.widget.TextView;

import edu.pdx.cs410J.AbstractAppointmentBook;
import edu.pdx.cs410J.AppointmentBookDumper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

/**
 * This class is an implementation of the AppointmentBookDumper. This
 * class has one job, print an AppointmentBook beautifully to some outside entity.
 */
public class PrettyPrinter implements AppointmentBookDumper<AppointmentBook>{
    private TextView outputField;

    public PrettyPrinter() {
        outputField = null;
    }

    public PrettyPrinter(TextView outputField) {
        this.outputField = outputField;
    }

    /**
     * This method dumps the AppointmentBook to the specified output source.
     * This method calls helper methods to do the main work.
     * @param appBook
     *       This is the AppointmentBook to pretty print
     * @throws IOException
     *        In certain cases, this method can through an IOException
     */
    @Override
    public void dump(AppointmentBook appBook) throws IOException{
        StringBuilder output = new StringBuilder();

        LinkedList appointments = (LinkedList) appBook.getAppointments();
        //output.append("*****************************************************\n");
        //output.append(appBook.getOwnerName() + "'s Appointment Book (" + appointments.size() + " Appointment(s))\n");
        //output.append("*****************************************************\n");
        output.append("------------------------------------------------------------------------------------------\n");

        for (int i = 0; i < appointments.size(); i++) {
            Appointment app = (Appointment) appointments.get(i);
            output.append("Timing: " + app.getBeginTimeString() + " -- " + app.getEndTimeString() + " (" + findDifference(app.getBeginTimeString(),app.getEndTimeString()) + ")\n");
            output.append("Description: " + app.getDescription()+"\n");
            output.append("------------------------------------------------------------------------------------------\n");
        }
        outputField.append(output);
    }


    /**
     * This method takes to dates in string form and it finds the difference in time.
     * It returns a string that tells how many years, days, hours, and minutes are
     * between the dates. If any of these values are zero, it doesnt mention that label.
     * @param start_date
     *        This is the first date for the event
     * @param end_date
     *        This is the second date for the event
     * @return
     *        This is the string returned that tells the difference in the time with the labels
     */
    static private String findDifference(String start_date, String end_date) {
        StringBuilder returnString = new StringBuilder();
        DateFormat format = new SimpleDateFormat("MM/dd/yy hh:mm a");

        try {
            Date date1 = format.parse(start_date);
            Date date2 = format.parse(end_date);

            long difference_In_Time = date2.getTime() - date1.getTime();
            long difference_In_Minutes = TimeUnit.MILLISECONDS.toMinutes(difference_In_Time) % 60;
            long difference_In_Hours = TimeUnit.MILLISECONDS.toHours(difference_In_Time) % 24;
            long difference_In_Days = TimeUnit.MILLISECONDS.toDays(difference_In_Time) % 365;
            long difference_In_Years = TimeUnit.MILLISECONDS.toDays(difference_In_Time) / 365L;

            if(difference_In_Years > 0) {
                returnString.append(" " + difference_In_Years + " " + "Years ");
            }
            if(difference_In_Days > 0) {
                returnString.append(" " + difference_In_Days + " " + "Days ");
            }
            if(difference_In_Hours > 0) {
                returnString.append(" " + difference_In_Hours + " " + "Hours ");
            }
            if(difference_In_Minutes > 0) {
                returnString.append(" " + difference_In_Minutes + " " + "Minutes ");
            }
            return returnString.toString();
        }
        catch (ParseException e) {
            System.err.println("Couldn't parse date in pretty printer.");
            return "error";
        }
    }

}
