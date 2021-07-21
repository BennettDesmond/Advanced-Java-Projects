package edu.pdx.cs410J.bdesmond;

import edu.pdx.cs410J.ParserException;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The main class for the CS410J appointment book Project.
 * This class contains all of the command line parsing.
 */
public class Project3 {
  public static final String USAGE_MESSAGE = "usage: This is a command line program that needs an appointment book owner," +
          "and an appointment. An appointment needs a description and a start and end time. This program also has options." +
          "-pretty: prints the AppointmentBook to the file specified after the argument or to standard out if \"-\" is passed in." +
          "-textFile: reads an AppointmentBook from a file and writes back to it. A file needs to be provided." +
          "-print: prints a description of the new Appointment." +
          "-README: This option prints the README for the project and exits";
  static final String MISSING_DESCRIPTION = "No description was given.";
  static final String MISSING_BEGINDATE = "No starting date was given.";
  static final String MISSING_BEGINTIME = "No starting time was given.";
  static final String MISSING_STARTPERIOD = "No starting period was given.";
  static final String MISSING_ENDDATE = "No ending date was given";
  static final String MISSING_ENDTIME = "No ending time was given";
  static final String MISSING_ENDPERIOD = "No ending period was given";
  static final String INCORRECT_DATE_FORMAT = "Incorrect Date format; correct: MM/dd/yyyy";
  static final String INCORRECT_TIME_FORMAT = "Incorrect Time format; correct: 24 hour time";
  static final String ERROR_LOADING_FILE = "There was an error creating or opening your file.";
  static final String ERROR_WRITING_TO_FILE = "There was an error writing to your file.";
  static final String ERROR_PARSING_DATE = "There was a problem parsing your date. The format is incorrect";
  static final int NUMOFARGUMENTS = 8;

  /**
   * Main program that parses the command line, creates an
   * <code>AppointmentBook</code> with an <code>Appointment</code>,
   * and prints either a description of the Appointment Book class
   * or a description of the appointment to
   * standard out by invoking its <code>toString</code> method. If
   * the -textFile option is set, the file will be parsed and the
   * new appointment will be added to the book and the entire
   * appointment book will be dumped to the file.
   * The -print option decides what will be printed.
   * @param args
   *        This is the array of arguments passed in from the command line.
   */
  public static void main(String[] args) {
    String name = "";
    String description = "";
    String startDate = "";
    String startTime = "";
    String startPeriod = "";
    //String begin = "";
    String endDate = "";
    String endTime = "";
    String endPeriod = "";
    //String end = "";
    String fileNameText = "";
    String fileNamePretty = "";
    int numOfOptions = 0;
    boolean printFlag = false;
    boolean fileFlag = false;
    boolean fileNameTextFlag = false;
    boolean prettyFlag = false;
    boolean prettyFileFlag = false;
    boolean fileNamePrettyFlag = false;
    Date begin;
    Date end;

    if(args.length == 0) {
      printErrorAndExit(USAGE_MESSAGE);
    }
    for (String arg : args) {
      if(fileNameTextFlag || fileNamePrettyFlag) {
        if(fileNamePrettyFlag) {
          fileNamePretty = arg;
          fileNamePrettyFlag = false;
        } else {
          fileNameText = arg;
          fileNameTextFlag = false;
        }
        numOfOptions++;
      } else if(arg.equals("-README")) {
        readMe();
        numOfOptions++;
      } else if(arg.equals("-print")) {
        printFlag = true;
        numOfOptions++;
      } else if(arg.equals("-textFile")) {
        fileNameTextFlag = true;
        fileFlag = true;
        numOfOptions++;
      } else if (arg.equals("-pretty")) {
        fileNamePrettyFlag = true;
        prettyFlag = true;
        numOfOptions++;
      } else if(name.equals("")) {
        name = arg;
      } else if (description.equals("")) {
        description = arg;
      } else if (startDate.equals("")) {
        startDate = arg;
      } else if (startTime.equals("")) {
        startTime = arg;
      } else if (startPeriod.equals("")) {
        startPeriod = arg;
      } else if (endDate.equals("")) {
        endDate = arg;
      } else if (endTime.equals("")){
        endTime = arg;
      } else if (endPeriod.equals("")) {
        endPeriod = arg;
      }
    }
    if(args.length > (NUMOFARGUMENTS+numOfOptions)) {
      System.err.println("Too many arguments");
      printErrorAndExit(USAGE_MESSAGE);
    }
    if(!validate12HourTime(startTime) || !validate12HourTime(endTime)) {
      printErrorAndExit("The time given needs to be in the 12 hour format");
    }
    validateInput(name,description,startDate,startTime,startPeriod,endDate,endTime,endPeriod,fileNameTextFlag,fileNamePrettyFlag);
    begin = validateDate(startDate, startTime, startPeriod);
    end = validateDate(endDate, endTime, endPeriod);
    if(begin.compareTo(end) != -1) {
      printErrorAndExit("The start time cannot be after the end time");
    }
    Appointment appointment = new Appointment(begin,end,description);
    AppointmentBook appointmentBook = new AppointmentBook(name);
    appointmentBook.addAppointment(appointment);

    AppointmentBook appBook = new AppointmentBook();
    appBook = parseFileAndDump(name, fileNameText, fileFlag, appointment, appointmentBook, appBook);
    printAppointmentOrAppointmentBookInfo(printFlag,fileFlag,appointmentBook,appBook);
    prettyPrinter(fileNamePretty, prettyFlag, appBook);

    System.exit(0);
  }

  private static void prettyPrinter(String fileNamePretty, boolean prettyFlag, AppointmentBook appBook) {
    if(prettyFlag) {
      if(fileNamePretty.equals("-")) {
        PrettyPrinter printer = new PrettyPrinter(fileNamePretty,false);
        try {
          printer.dump(appBook);
        } catch (IOException e) {
          printErrorAndExit("Trouble printing an AppointmentBook using pretty print");
        }
      } else {
        PrettyPrinter printer = new PrettyPrinter(fileNamePretty,true);
        try {
          printer.dump(appBook);
        } catch (IOException e) {
          printErrorAndExit("Trouble printing an AppointmentBook using pretty print");
        }
      }
    }
  }

  private static void printAppointmentOrAppointmentBookInfo(boolean printFlag,boolean fileFlag,AppointmentBook appointmentBook, AppointmentBook appBook) {
    if(printFlag) {
      System.out.println(appointmentBook.getAppointments());
    } else {
      if(fileFlag) {
        System.out.println(appBook);
      } else {
        System.out.println(appointmentBook);
      }
    }
  }

  private static AppointmentBook parseFileAndDump(String name, String fileName, boolean fileFlag, Appointment appointment, AppointmentBook appointmentBook, AppointmentBook appBook) {
    if(fileFlag) {
      try {
        TextParser parser = new TextParser(fileName);
        appBook = parser.parse();
      } catch (ParserException e) {
        System.err.println(e);
        printErrorAndExit("The file cannot be parsed");
      }
      boolean nameComparison = name.equals(appBook.getOwnerName());
      if(!nameComparison && (!appBook.getOwnerName().equals(""))) {
        printErrorAndExit("The name on the file does not match the name passed through the command line");
      }
      appBook.addAppointment(appointment);
      try {
        TextDumper dumper = new TextDumper(fileName);
        if(appBook.getOwnerName().equals("")) {
          dumper.dump(appointmentBook);
        } else {
          dumper.dump(appBook);
        }
      } catch(IOException e) {
        System.err.println(e);
        printErrorAndExit("The file cannot be written to");
      }
    } else {
      appBook.addAppointment(appointment);
    }
    return appBook;
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
  private static void validateInput(String name,String description,String startDate,String startTime,String startPeriod,String endDate,String endTime,String endPeriod,boolean fileNameTextFlag,boolean fileNamePrettyFlag) {
    if(name.equals("")) {
      printErrorAndExit(USAGE_MESSAGE);
    } else if(description.equals("")) {
      //printErrorAndExit(MISSING_DESCRIPTION);
      printErrorAndExit(USAGE_MESSAGE);
    } else if(startDate.equals("")) {
      //printErrorAndExit(MISSING_BEGINDATE);
      printErrorAndExit(USAGE_MESSAGE);
    } else if(startTime.equals("")) {
      //printErrorAndExit(MISSING_BEGINTIME);
      printErrorAndExit(USAGE_MESSAGE);
    } else if(startPeriod.equals("")) {
      //printErrorAndExit(MISSING_STARTPERIOD);
      printErrorAndExit(USAGE_MESSAGE);
    } else if(endDate.equals("")) {
      //printErrorAndExit(MISSING_ENDDATE);
      printErrorAndExit(USAGE_MESSAGE);
    } else if(endTime.equals("")) {
      //printErrorAndExit(MISSING_ENDTIME);
      printErrorAndExit(USAGE_MESSAGE);
    } else if(endPeriod.equals("")) {
      //printErrorAndExit(MISSING_ENDPERIOD);
      printErrorAndExit(USAGE_MESSAGE);
    }
    //else if(fileNameTextFlag) {
      //printErrorAndExit("The file option was selected but a file was not provided");
    //} else if(fileNamePrettyFlag) {
      //printErrorAndExit("The pretty print option was selected but a file was not provided");
    //}
  }

  private static Date validateDate(String date, String time, String period) {
    String dateString = date + " " + time + " " + period;
    Date dateClassObj = new Date();
    DateFormat format = new SimpleDateFormat("MM/dd/yy hh:mm a");
    try {
      dateClassObj = format.parse(dateString);
    } catch(ParseException e) {
      printErrorAndExit(ERROR_PARSING_DATE);
    }
    return dateClassObj;
  }

  /**
   * This function validates the date to make sure that the format is correct.
   * @param date
   *        This is the date that needs to be validated
   * @return
   *        This function returns a boolean value telling the caller
   *        if the date is valid.
   */
  /*
  private static boolean validateDate(String date) {
    DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    sdf.setLenient(false);
    try {
      sdf.parse(date);
    } catch (ParseException e) {
      return false;
    }
    return true;
  }
  */

  /**
   * This function validates the time to make sure that it
   * is in the correct format
   * @param time
   *        This is the time to be validated
   * @return
   *        This function returns a boolean flag telling if
   *        the time is in the correct format or not.
   */
  /*
  private static boolean validateTime(String time) {
    String regex = "([01]?[0-9]|2[0-3]):[0-5][0-9]";
    Pattern p = Pattern.compile(regex);
    Matcher m = p.matcher(time);
    return m.matches();
  }
  */

  private static boolean validate12HourTime(String time) {
    String regex1 = "(0[0-9]|[0-9]|1[0-2]):[0-5][0-9]";
    String regex2 = "(0[0-9]|[0-9]|1[0-9]|2[0-4]):[0-5][0-9]";
    Pattern p1 = Pattern.compile(regex1);
    Pattern p2 = Pattern.compile(regex2);
    Matcher m1 = p1.matcher(time);
    Matcher m2 = p2.matcher(time);
    if(!m1.matches() && m2.matches()) {
      return false;
    }
    return true;
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
  /*
  private static void validateEventDates(String date, String time) {
    if(!validateDate(date)) {
      printErrorAndExit(INCORRECT_DATE_FORMAT);
    }
    if(!validateTime(time)) {
      printErrorAndExit(INCORRECT_TIME_FORMAT);
    }
  }
  */

  /**
   * This is the readme for the program. It simply prints out a
   * brief overview of the project.
   */
  private static void readMe() {
    System.out.println("Bennett Desmond\n");
    System.out.println("Project 3\n");
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