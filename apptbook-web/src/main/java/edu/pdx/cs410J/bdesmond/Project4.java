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
            } else if (description.equals("")) {
                description = args[i];
            } else if (start.equals("")) {
                if((i+2) < args.length) {
                    start = args[i] + " " + args[++i] + " " + args[++i];
                }
            } else if (end.equals("")) {
                if((i+2) < args.length) {
                    start = args[i] + " " + args[++i] + " " + args[++i];
                }
            }
        }

        if (hostName == null) {
            usage( MISSING_ARGS );
        } else if ( portString == null) {
            usage( "Missing port" );
        }

        //

        int port;
        try {
            port = Integer.parseInt( portString );

        } catch (NumberFormatException ex) {
            usage("Port \"" + portString + "\" must be an integer");
            return;
        }

        AppointmentBookRestClient client = new AppointmentBookRestClient(hostName, port);

        String message;
        try {
            if(searchFlag) {
                //TODO Search for Appointments that fall within the range
            } else if (printFlag) {
                //TODO Print the added appointment to the screen and add appointment
            } else if (description.equals("") && !owner.equals("")) {

                //TODO print all appointments for the specified owner

                // Print all dictionary entries
                //message = Messages.formatDictionaryEntry(owner, client.getAppointments(owner));

            } else if (args.length == 12){
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
        err.println("usage: java Project4 host port [word] [definition]");
        err.println("  host         Host of web server");
        err.println("  port         Port of web server");
        err.println("  owner        Owner in dictionary");
        err.println("  definition   Definition of word");
        err.println();
        err.println("This simple program posts words and their definitions");
        err.println("to the server.");
        err.println("If no definition is specified, then the word's definition");
        err.println("is printed.");
        err.println("If no word is specified, all dictionary entries are printed");
        err.println();

        System.exit(1);
    }

    private static void readMe() {
        System.out.println("Bennett Desmond\n");
        System.out.println("Project 4\n");
        System.out.println("This program accepts parameters at the command line and makes" +
                "an appointment book and an appointment. This program only accepts one appointment" +
                "All parameters must be present for the project to run. The appointment book has" +
                "a name and an array of appointments, and the appointments have a description, a start time" +
                "and an end time. If the -textFile option is set and a file is provided" +
                ", then the appointment book is read from the file and the new appointment" +
                "introduced on the command line is added to the file. There is also a -print" +
                "option that will print out the Appointment that was passed to the program." +
                "There is also a -pretty option that prints the AppointmentBook to a file when" +
                "a file is provided and it prints the AppointmentBook to standard out when \"-\"" +
                "is passed after the option.\n");
        System.exit(0);
    }
}