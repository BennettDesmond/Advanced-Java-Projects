package com.pdx.cs410j.bdesmond;

import edu.pdx.cs410J.AbstractAppointmentBook;
import edu.pdx.cs410J.AppointmentBookDumper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.LinkedList;

/**
 * This class implements the interface AppointmentBookDumper. This
 * class dumps text to files.
 */
public class TextDumper implements AppointmentBookDumper<AppointmentBook>{
    private final PrintWriter writer;

    /**
     * This is the default constructor for the TextDumper class.
     */
    public TextDumper() {
        writer = null;
    }

    public TextDumper(PrintWriter writer) {
        this.writer = writer;
    }

    /**
     * This class is the main dumper class that calls helper
     * classes to dump information to a file. It takes care of
     * file verification and it can throw errors.
     * @param appBook
     *          This is the appointment book to dump to a file
     * @throws IOException
     *          This class can throw IOExceptions if there
     *          is an issue dumping to a file
     */
    @Override
    public void dump(AppointmentBook appBook) throws IOException{
        writer.println(appBook.getOwnerName());
        for (Appointment app : appBook.getAppointments()) {
            writer.println(app.getDescription()+","+app.getBeginTimeString()+","+app.getEndTimeString());
        }
        writer.flush();
    }

    public void dumpAppointment(Appointment app) throws IOException{
        writer.println(app.getDescription()+","+app.getBeginTimeString()+","+app.getEndTimeString());
    }
}