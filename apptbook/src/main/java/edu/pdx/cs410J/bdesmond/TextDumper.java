package edu.pdx.cs410J.bdesmond;

import edu.pdx.cs410J.AbstractAppointmentBook;
import edu.pdx.cs410J.AppointmentBookDumper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public class TextDumper implements AppointmentBookDumper<AppointmentBook>{
    private String fileName;

    public TextDumper() {
        fileName = "";
    }

    public TextDumper(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void dump(AppointmentBook AppointmentBook) throws IOException{
        if(!fileVerification()) {
            throw new IOException("There was an error creating or opening your file.");
        }
        if(!writeToFile(AppointmentBook)) {
            throw new IOException("There was an error writing to your file.");
        }
    }

    public boolean fileVerification() {
        File file = new File(fileName);
        if(file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean writeToFile(AppointmentBook appBook) {
        try {
            FileWriter writer = new FileWriter(fileName);
            writer.write(appBook.getOwnerName());
            writer.write("\n");
            LinkedList appointments = new LinkedList<Appointment>();
            appointments = (LinkedList) appBook.getAppointments();
            for (int i = 0; i < appointments.size(); i++) {
                Appointment app = (Appointment) appointments.get(i);
                writer.write(app.getDescription());
                writer.write(",");
                writer.write(app.getBeginTimeString());
                writer.write(",");
                writer.write(app.getEndTimeString());
                writer.write("\n");
            }
            writer.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}
