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

/**
 * This class is an implementation of the AppointmentBookDumper. This
 * class has one job, print an AppointmentBook beautifully to some outside entity.
 */
public class PrettyPrinter implements AppointmentBookDumper<AppointmentBook>{
    private String fileName;
    boolean printToFile;

    /**
     * This is the default constructor for the PrettyPrinter class
     */
    public PrettyPrinter() {
        fileName = "";
        printToFile = false;
    }

    /**
     * This is the constructor with parameters for the PrettyPrinter class
     * @param fileName
     *       This is the name of the file to be printed to
     * @param printToFile
     *       This is a flag telling the printer to either print to a file or standard out
     */
    public PrettyPrinter(String fileName, boolean printToFile) {
        this.fileName = fileName;
        this.printToFile = printToFile;
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

    /**
     * This is the main code to write the AppointmentBook to standard out
     * @param appBook
     *       This is the AppointmentBook that is printed
     */
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

    /**
     * This method is tasked with the job of checking if a file
     * exists, and creating one if it doesn't exist.
     * @return
     *        This method returns true if a file is ready to go,
     *        and false if not
     */
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

    /**
     * This is the method that prints the AppointmentBook to a file
     * @param appBook
     *       This is the AppointmentBook to be printed
     * @return
     *        A boolean flag is returned to tell if an exception should be thrown or not
     */
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