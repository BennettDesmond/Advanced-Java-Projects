package edu.pdx.cs410J.bdesmond;

import edu.pdx.cs410J.AbstractAppointmentBook;
import edu.pdx.cs410J.AppointmentBookDumper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

/**
 * This class implements the interface AppointmentBookDumper. This
 * class dumps text to files.
 */
public class TextDumper implements AppointmentBookDumper<AppointmentBook>{
    private String fileName;

    /**
     * This is the default constructor for the TextDumper class.
     */
    public TextDumper() {
        fileName = "";
    }

    /**
     * This is the constructor with parameters. This constructor
     * is passed a file name.
     * @param fileName
     *          This is the name of the file to dump to.
     */
    public TextDumper(String fileName) {
        this.fileName = fileName;
    }

    /**
     * This class is the main dumper class that calls helper
     * classes to dump information to a file. It takes care of
     * file verification and it can throw errors.
     * @param AppointmentBook
     *          This is the appointment book to dump to a file
     * @throws IOException
     *          This class can throw IOExceptions if there
     *          is an issue dumping to a file
     */
    @Override
    public void dump(AppointmentBook AppointmentBook) throws IOException{
        if(!fileVerification()) {
            throw new IOException("There was an error creating or opening your file.");
        }
        if(!writeToFile(AppointmentBook)) {
            throw new IOException("There was an error writing to your file.");
        }
    }

    /**
     * This class verifies that the file specified to dump to exists
     * and that it can be written to.
     * @return
     *          This method returns a bool flag that tells if
     *          an IOException should be thrown
     */
    public boolean fileVerification() {
        File file = new File(fileName);
        if(file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * This class deals with writing the AppointmentBook to the
     * specified file.
     * @param appBook
     *          This is the AppointmentBook that is written to the file
     * @return
     *          This method returns a bool flag that tells if
     *          an IOException should be thrown
     */
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
