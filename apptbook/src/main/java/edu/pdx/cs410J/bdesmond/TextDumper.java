package edu.pdx.cs410J.bdesmond;

import edu.pdx.cs410J.AbstractAppointmentBook;
import edu.pdx.cs410J.AppointmentBookDumper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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
        try {
            File file = new File(fileName);
            file.createNewFile();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean writeToFile(AppointmentBook AppointmentBook) {
        try {
            FileWriter writer = new FileWriter(fileName);
            writer.write("Hi");
            writer.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}
