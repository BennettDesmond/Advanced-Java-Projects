package edu.pdx.cs410J.bdesmond;

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

public class PrettyPrinter implements AppointmentBookDumper<AppointmentBook>{
    private String fileName;
    boolean printToFile;

    public PrettyPrinter() {
        fileName = "";
        printToFile = false;
    }

    public PrettyPrinter(String fileName, boolean printToFile) {
        this.fileName = fileName;
        this.printToFile = printToFile;
    }

    @Override
    public void dump(AppointmentBook appBook) throws IOException{
        if(printToFile) {
            if(!fileVerification()) {
                throw new IOException("There was an error creating or opening your file.");
            }
            if(!prettyPrintToFile(appBook)) {
                throw new IOException("There was an error writing to your file.");
            }
        } else {
            dumpToStandardOut(appBook);
        }
    }

    public static void  dumpToStandardOut(AppointmentBook appBook) {
        LinkedList appointments = (LinkedList) appBook.getAppointments();
        System.out.println("***************************************************************************");
        System.out.println(appBook.getOwnerName() + "'s Appointment Book (" + appointments.size() + " Appointment(s))");
        //System.out.println("'s Appointment Book (" + appointments.size() + " Appointment(s))\n");
        System.out.println("***************************************************************************");

        for (int i = 0; i < appointments.size(); i++) {
            Appointment app = (Appointment) appointments.get(i);
            System.out.println("--------------------------------------------------------------------------------");
            System.out.println("Timing: " + app.getBeginTimeString() + " -- " + app.getEndTimeString() + " (" + findDifference(app.getBeginTimeString(),app.getEndTimeString()) + ")");
            System.out.println("Description: " + app.getDescription());
            System.out.println("--------------------------------------------------------------------------------");
        }
    }

    public boolean fileVerification() {
        try {
            File file = new File(fileName);
            if(file.exists()) {
                return true;
            }
            if(fileName.equals("")) {
                return false;
            }
            file.createNewFile();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean prettyPrintToFile(AppointmentBook appBook) {
        try {
            FileWriter printer = new FileWriter(fileName);
            LinkedList appointments = (LinkedList) appBook.getAppointments();
            printer.write("***************************************************************************\n");
            printer.write(appBook.getOwnerName());
            printer.write("'s Appointment Book (" + appointments.size() + " Appointment(s))\n");
            printer.write("***************************************************************************\n");
            for (int i = 0; i < appointments.size(); i++) {
                Appointment app = (Appointment) appointments.get(i);
                printer.write("--------------------------------------------------------------------------------\n");
                printer.write("Timing: ");
                printer.write(app.getBeginTimeString() + " -- ");
                printer.write(app.getEndTimeString() + " (");
                printer.write(findDifference(app.getBeginTimeString(),app.getEndTimeString()) + ")\n");
                printer.write("Description: " + app.getDescription() + "\n");
                printer.write("--------------------------------------------------------------------------------\n");
            }
            printer.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

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


