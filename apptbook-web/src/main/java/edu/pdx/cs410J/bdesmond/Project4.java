package edu.pdx.cs410J.bdesmond;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

/**
 * The main class that parses the command line and communicates with the
 * Appointment Book server using REST.
 */
public class Project4 {

    public static final String MISSING_ARGS = "Missing command line arguments";
    public static final String TOO_MANY_ARGS = "Too many command line arguments were provided for the option that it looks like you want";

    public static void main(String... args) {
        //AppointmentBook Variables
        String owner = "";
        String description = "";
        String start = "";
        String end = "";
        //Options Variables
        String hostName = "";
        String portString = "";
        boolean searchFlag = false;
        boolean printFlag = false;
        String definition = ""; //TODO REPLACE

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-README")) {
                readMe();
            } else if (args[i].equals("-host")) {
                if((i+1) < args.length) {
                    hostName = args[++i];
                }
            } else if (args[i].equals("-port")) {
                if((i+1) < args.length) {
                    portString = args[++i];
                }
            } else if (args[i].equals("-search")) {
                if(args.length < 12) {
                    usage(MISSING_ARGS);
                } else if(args.length > 12) {
                    usage(TOO_MANY_ARGS);
                }
                searchFlag = true;
            } else if (args[i].equals("-print")) {
                if(args.length < 13) {
                    usage(MISSING_ARGS);
                } else if(args.length > 13) {
                    usage(TOO_MANY_ARGS);
                }
                printFlag = true;
            } else if (owner.equals("")) {
                owner = args[i];
            } else if (description.equals("") && !searchFlag) {
                description = args[i];
            } else if (start.equals("")) {
                if((i+2) < args.length) {
                    start = args[i] + " " + args[++i] + " " + args[++i];
                } else {
                    usage("The start date is missing an element");
                }
            } else if (end.equals("")) {
                if((i+2) < args.length) {
                    start = args[i] + " " + args[++i] + " " + args[++i];
                } else {
                    usage("The end date is missing an element");
                }
            }
        }

        if (hostName.equals("")) {
            usage( MISSING_ARGS );
        } else if ( portString.equals("")) {
            usage( "Missing port" );
        }

        //TODO Make sure that the date format is correct

        int port;
        try {
            port = Integer.parseInt( portString );

        } catch (NumberFormatException ex) {
            usage("Port \"" + portString + "\" must be an integer");
            return;
        }

        AppointmentBookRestClient client = new AppointmentBookRestClient(hostName, port);

        String message = "";
        try {
            if(searchFlag) { //Search for appointments in the range
                //TODO Search for Appointments that fall within the range
            } else if (printFlag) { //Print the added appointment to the screen and add appointment
                //TODO Print the added appointment to the screen and add appointment
            } else if (description.equals("") && !owner.equals("")) { //Print all appointments for the specified owner
                //message = Messages.for
                //TODO print all appointments for the specified owner

                // Print all dictionary entries
                message = Messages.formatDictionaryEntry(owner, client.getAppointments(owner));

            } else if (args.length == 12){ //Add an appointment to an appointment book
                //TODO add an appointment to the book
            } else {
                if(args.length < 5) {
                    usage(MISSING_ARGS);
                } else {
                    usage(TOO_MANY_ARGS);
                }

                // Post the word/definition pair
                //client.createAppointment(owner, definition);
                //message = Messages.definedWordAs(owner, definition);
            }

        } catch ( IOException ex ) {
            error("While contacting server: " + ex);
            return;
        }

        System.out.println(message);

        System.exit(0);
    }

    private static void error( String message )
    {
        PrintStream err = System.err;
        err.println("** " + message);

        System.exit(1);
    }

    /**
     * Prints usage information for this program and exits
     * @param message An error message to print
     */
    private static void usage( String message )
    {
        PrintStream err = System.err;
        err.println("** " + message);
        err.println();
        err.println("usage: java edu.pdx.cs410J.<login-id>.Project4 [options] <args>");
        err.println("args are (in this order):");
        err.println("  owner            The person who owns the appt book");
        err.println("  description      A description of the appointment");
        err.println("  begin            When the appt begins");
        err.println("  end              When the appt ends");
        err.println("options are (options may appear in any order):");
        err.println("  -host hostname   Host computer on which the server runs");
        err.println("  -port port       Port on which the server is listening");
        err.println("  -search          Appointments should be searched for");
        err.println("  -print           Prints a description of the new appointment");
        err.println("  -README          Prints a README for this project and exits");
        err.println();
        err.println("This is a simple appointment book program that stores multiple");
        err.println("appointment books on a server.");
        err.println("Other than the main options mentioned above, there are two main other options.");
        err.println("(1) If you want all the appointments for a particular owner printer out to the ");
        err.println("console, simply run the program with the host, port, and the owners name.");
        err.println("(2) In order to add an appointment to an existing or new appointment book;");
        err.println(" simply run the program with the host, the port, and all the args.");
        err.println();

        System.exit(1);
    }

    private static void readMe() {
        System.out.println("Bennett Desmond\n");
        System.out.println("Project 4\n");
        System.out.println("This program is an appointment book program. New appointments" +
                "can be added to existing appointment books, and new appointment books can" +
                "be created. The appointments can be queried and all the appointments for " +
                "a specified owner can be listed. This program needs to be passed the port and the" +
                "host in order to connect to the website that hold the data.\n");
        System.exit(0);
    }
}