package edu.pdx.cs410J.bdesmond;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The main class for the CS410J appointment book Project.
 * This class contains all of the command line parsing.
 */
public class Project1 {
  public static final String USAGE_MESSAGE = "usage: This is a command line program that needs an appointment book owner, and an appointment. An appointment needs a description and a start and end time.";
  static final String MISSING_DESCRIPTION = "No description was given.";
  static final String MISSING_BEGINDATE = "No starting date was given.";
  static final String MISSING_BEGINTIME = "No starting time was given.";
  static final String MISSING_ENDDATE = "No ending date was given";
  static final String MISSING_ENDTIME = "No ending time was given";
  static final String INCORRECT_DATE_FORMAT = "Incorrect Date format; correct: MM/dd/yyyy";
  static final String INCORRECT_TIME_FORMAT = "Incorrect Time format; correct: 24 hour time";

  /**
   * Main program that parses the command line, creates an
   * <code>AppointmentBook</code> with an <code>Appointment/code>,
   * and prints either a description of the Appointment Book class
   * or a description of the appointment to
   * standard out by invoking its <code>toString</code> method.
   * The -print option decides what will be printed.
   * @param args
   *        This is the array of arguments passed in from the command line.
   */
  public static void main(String[] args) {
    String name = null;
    String description = null;
    String startDate = null;
    String startTime = null;
    String begin = null;
    String endDate = null;
    String endTime = null;
    String end = null;
    int numOfOptions = 0;
    boolean printFlag = false;
    if(args.length == 0) {
      printErrorAndExit(USAGE_MESSAGE);
    }
    for (String arg : args) {
      if(arg.equals("-README")) {
        readMe();
        numOfOptions++;
      } else if(arg.equals("-print")) {
        printFlag = true;
        numOfOptions++;
      } else if(name == null) {
        name = arg;
      } else if (description == null) {
        description = arg;
      } else if (startDate == null) {
        startDate = arg;
      } else if (startTime == null) {
        startTime = arg;
      } else if (endDate == null) {
        endDate = arg;
      } else {
        endTime = arg;
      }
    }
    if(args.length > (6+numOfOptions)) {
      System.err.println("Too many arguments");
      printErrorAndExit(USAGE_MESSAGE);
    }
    validateInput(name,description,startDate,startTime,endDate,endTime);
    begin = startDate + " " + startTime;
    end = endDate + " " + endTime;
    Appointment appointment = new Appointment(begin,end,description);
    AppointmentBook appointmentBook = new AppointmentBook(name);
    appointmentBook.addAppointment(appointment);
    if(printFlag) {
      System.out.println(appointmentBook.getAppointments());
    } else {
      System.out.println(appointmentBook);
    }

    System.exit(0);
  }

  /**
   * This function sends out a message to standard error and
   * then exits.
   * @param message
   *        Message is printed to standard error
   */
  private static void printErrorAndExit(String message) {
    System.err.println(message);
    System.exit(1);
  }

  /**
   * This function validates input. It makes sure that all the data
   * members that are supposed to be filled have information.This
   * function also validates the dates and times to make sure that they
   * are in the correct format.
   * @param name
   *        This is the name of the appointment book owner
   * @param description
   *        Description of the appointment
   * @param startDate
   *        Start date of the appointment
   * @param startTime
   *        Start Time of the appointment
   * @param endDate
   *        End date of the appointment
   * @param endTime
   *        End time of the appointment
   */
  private static void validateInput(String name,String description,String startDate,String startTime,String endDate,String endTime) {
    if(name == null) {
      printErrorAndExit(USAGE_MESSAGE);
    } else if(description == null) {
      //printErrorAndExit(MISSING_DESCRIPTION);
      printErrorAndExit(USAGE_MESSAGE);
    } else if(startDate == null) {
      //printErrorAndExit(MISSING_BEGINDATE);
      printErrorAndExit(USAGE_MESSAGE);
    } else if(startTime == null) {
      //printErrorAndExit(MISSING_BEGINTIME);
      printErrorAndExit(USAGE_MESSAGE);
    } else if(endDate == null) {
      //printErrorAndExit(MISSING_ENDDATE);
      printErrorAndExit(USAGE_MESSAGE);
    } else if(endTime == null) {
      //printErrorAndExit(MISSING_ENDTIME);
      printErrorAndExit(USAGE_MESSAGE);
    }
    validateEventDates(startDate,startTime);
    validateEventDates(endDate,endTime);
  }

  /**
   * This function validates the date to make sure that the format is correct.
   * @param date
   *        This is the date that needs to be validated
   * @return
   *        This function returns a boolean value telling the caller
   *        if the date is valid.
   */
  private static boolean validateDate(String date) {
    String regex = "([0-9]|0[0-9]|1[0-2])/([0-9]|[0-2][0-9]|3[0-2])/([0-9][0-9]|[0-9][0-9][0-9][0-9])";
    Pattern p = Pattern.compile(regex);
    Matcher m = p.matcher(date);
    return m.matches();
  }

  /**
   * This function validates the time to make sure that it
   * is in the correct format
   * @param time
   *        This is the time to be validated
   * @return
   *        This function returns a boolean flag telling if
   *        the time is in the correct format or not.
   */
  private static boolean validateTime(String time) {
    String regex = "([01]?[0-9]|2[0-3]):[0-5][0-9]";
    Pattern p = Pattern.compile(regex);
    Matcher m = p.matcher(time);
    return m.matches();
  }

  /**
   * This function takes dates and times and sends
   * them both to their specific validators. This
   * function calls a function to end the program
   * if either are incorrectly formatted.
   * @param date
   *        This is the date for the event
   * @param time
   *        This is the time for the event
   */
  private static void validateEventDates(String date, String time) {
    if(!validateDate(date)) {
      printErrorAndExit(INCORRECT_DATE_FORMAT);
    }
    if(!validateTime(time)) {
      printErrorAndExit(INCORRECT_TIME_FORMAT);
    }
  }

  /**
   * This is the readme for the program. It simply prints out a
   * brief overview of the project.
   */
  private static void readMe() {
    System.out.println("Bennett Desmond\n");
    System.out.println("Project 1\n");
    System.out.println("This program accepts parameters at the command line and makes" +
            "an appointment book and an appointment. This program only accepts one appointment" +
            "All parameters must be present for the project to run. The appointment book has" +
            "a name and an array of appointments, and the appointments have a description, a start time" +
            "and an end time.\n");
    System.exit(0);
  }

}