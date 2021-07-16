package edu.pdx.cs410J.bdesmond;

import edu.pdx.cs410J.AppointmentBookParser;
import edu.pdx.cs410J.ParserException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//import java.util.regex.Matcher.quoteReplacement(String);

public class TextParser implements AppointmentBookParser{
    private String fileName;

    public TextParser() {
        fileName = "";
    }

    public TextParser(String fileName) {
        this.fileName = fileName;
    }

    public AppointmentBook parse() throws ParserException{
        try {
            if(!fileVerification()) {
                return new AppointmentBook();
            }
            BufferedReader parser = new BufferedReader(new FileReader(fileName));
            if(!parser.ready()) {
                return new AppointmentBook();
                //throw new ParserException("Not able to read file");
            }
            //if(parser.readLine() == null) {
                //return new AppointmentBook();
            //}
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

    public boolean validateTime(String time) {
        String regex = "(0[0-9]|1[0-2]|[0-9])/([0-2][0-9]|3[01]?)/[0-9][0-9][0-9][0-9]\s([01]?[0-9]|2[0-3]|[0-9]):[0-5][0-9]";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(time);
        return m.matches();
    }

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