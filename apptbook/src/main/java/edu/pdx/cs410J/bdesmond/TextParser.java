package edu.pdx.cs410J.bdesmond;

import edu.pdx.cs410J.AppointmentBookParser;
import edu.pdx.cs410J.ParserException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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
            BufferedReader parser = new BufferedReader(new FileReader(fileName));
            if(!parser.ready()) {
                throw new ParserException("Not able to read file");
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

    private Appointment parseAppointmentString(String appointment) {
        String description = null;
        String start = null;
        String end = null;
        String [] data = appointment.split(",");
        Appointment app = new Appointment();
        for (String arg : data) {
            if (description == null) {
                description = arg;
            } else if (start == null) {
                start = arg;
            } else if (end == null) {
                end = arg;
            }
        }
        return true;
    }

}
