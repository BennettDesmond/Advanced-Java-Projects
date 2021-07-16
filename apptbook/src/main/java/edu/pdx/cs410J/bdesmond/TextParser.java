package edu.pdx.cs410J.bdesmond;

import edu.pdx.cs410J.AppointmentBookParser;
import edu.pdx.cs410J.ParserException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The TextParser class implements the AppointmentBookParser
 * interface. This class has the job of parsing an AppointmentBook
 * from a file.
 */
public class TextParser implements AppointmentBookParser{
    private String fileName;

    public TextParser() {
        fileName = "";
    }

    public TextParser(String fileName) {
        this.fileName = fileName;
    }

    /**
     * This method is the main function that parses the information.
     * This function calls helper functions. When this function runs
     * into a parsing error, it throws a ParserException.
     * @return
     *          An AppointmentBook is returned
     * @throws ParserException
     *          This function will throw exceptions when a parsing
     *          issue occurs
     */
    public AppointmentBook parse() throws ParserException{
        try {
            if(!fileVerification()) {
                return new AppointmentBook();
            }
            BufferedReader parser = new BufferedReader(new FileReader(fileName));
            if(!parser.ready()) {
                return new AppointmentBook();
            }
            String owner = parser.readLine();
            String appointmentStr = parser.readLine();
            AppointmentBook appBook = new AppointmentBook(owner);
            Appointment appointmentObj = new Appointment();
            while(appointmentStr != null) {
                appointmentObj = parseAppointmentString(appointmentStr);
                if (appointmentObj == null) {
                    throw new ParserException("There was a problem with reading an appointment from the file");
                }
                appBook.addAppointment(appointmentObj);
                appointmentStr = parser.readLine();
            }
            return appBook;
        } catch (IOException e) {
            throw new ParserException("Problem while parsing",e);
        }
    }

    /**
     * This method parses strings that contain appointments.
     * It also calls an additional helper to verify the validity
     * of several inputs.
     * @param appointment
     *          The string to be parsed into an appointment
     * @return
     *          An Appointment object that contains the appointment
     *          that was just read in
     */
    private Appointment parseAppointmentString(String appointment) {
        String description = null;
        String start = null;
        String end = null;
        String [] data = appointment.split(",");
        for (String arg : data) {
            if (description == null) {
                description = arg;
            } else if (start == null) {
                start = arg;
            } else if (end == null) {
                end = arg;
            }
        }
        if((description == null) || (start == null) || (end == null)) {
            return null;
        }
        if(!validateTime(start)) {
            return null;
        }
        if(!validateTime(end)) {
            return null;
        }
        Appointment app = new Appointment(start,end,description);
        return app;
    }

    /**
     * This method validates the time and makes sure that
     * the date and time are in the correct format.
     * @param time
     *          The time is passed in as a string
     * @return
     *          A boolean flag telling if the format is valid
     */
    public boolean validateTime(String time) {
        String regex = "(0[0-9]|1[0-2]|[0-9])/([0-2][0-9]|3[01]?)/[0-9][0-9][0-9][0-9]\s([01]?[0-9]|2[0-3]|[0-9]):[0-5][0-9]";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(time);
        return m.matches();
    }

    /**
     * This method verifies if the file is reachable and
     * if it is not it will create a new one
     * @return
     *          A boolean flag telling the caller to throw
     *          a ParserException on a false return
     */
    public boolean fileVerification() {
        try {
            File file = new File(fileName);
            if(file.exists()) {
                return true;
            }
            file.createNewFile();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}