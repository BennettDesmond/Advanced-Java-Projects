package edu.pdx.cs410J.bdesmond;

import com.google.common.annotations.VisibleForTesting;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * This servlet ultimately provides a REST API for working with an
 * <code>AppointmentBook</code>.  However, in its current state, it is an example
 * of how to use HTTP and Java servlets to store simple dictionary of words
 * and their definitions.
 */
public class AppointmentBookServlet extends HttpServlet
{
    static final String OWNER_PARAMETER = "owner";
    static final String DESCRIPTION_PARAMETER = "description";
    static final String START_PARAMETER = "start";
    static final String END_PARAMETER = "end";

    private final Map<String, AppointmentBook> books = new HashMap<>();

    /**
     * Handles an HTTP GET request from a client by writing the definition of the
     * word specified in the "word" HTTP parameter to the HTTP response.  If the
     * "word" parameter is not specified, all of the entries in the dictionary
     * are written to the HTTP response.
     */
    @Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
        response.setContentType( "text/plain" );

        String owner = getParameter(OWNER_PARAMETER, request);
        String start = getParameter(START_PARAMETER, request);
        String end = getParameter(END_PARAMETER, request);

        if (owner == null) {
            missingRequiredParameter(response, OWNER_PARAMETER);
            return;
        }
        if((start != null) && (end != null)) {
            Date startDate = setupDateObject(start);
            Date endDate = setupDateObject(end);
            if(startDate == null) {
                response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, "The start date is not in the correct format");
            }
            if(endDate == null) {
                response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, "The end date is not in the correct format");
            }
            writeSpecifiedAppointments(owner, startDate, endDate, response);
        } else {
            writeAppointmentBook(owner, response);
        }
    }

    /**
     * Handles an HTTP POST request by storing the dictionary entry for the
     * "word" and "definition" request parameters.  It writes the dictionary
     * entry to the HTTP response.
     */
    @Override
    protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
        response.setContentType( "text/plain" );

        String owner = getParameter(OWNER_PARAMETER, request );
        if (owner == null) {
            missingRequiredParameter(response, OWNER_PARAMETER);
            return;
        }
        String description = getParameter(DESCRIPTION_PARAMETER, request );
        if ( description == null) {
            missingRequiredParameter( response, DESCRIPTION_PARAMETER );
            return;
        }
        String start = getParameter(START_PARAMETER, request );
        if ( start == null) {
            missingRequiredParameter( response, START_PARAMETER );
            return;
        }
        String end = getParameter(END_PARAMETER, request );
        if ( end == null) {
            missingRequiredParameter( response, END_PARAMETER );
            return;
        }

        AppointmentBook book = this.books.get(owner);
        if(book == null) {
            book = createAppointmentBook(owner);
        }
        Date startDate = setupDateObject(start);
        Date endDate = setupDateObject(end);
        if(startDate == null) {
            response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, "The start date is not in the correct format");
        }
        if(endDate == null) {
            response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, "The end date is not in the correct format");
        }
        Appointment appointment = new Appointment(startDate,endDate,description);
        book.addAppointment(appointment);

        /*
        this.dictionary.put(owner, description);
        PrintWriter pw = response.getWriter();
        pw.println(Messages.definedWordAs(owner, description));
        pw.flush();
        */

        response.setStatus( HttpServletResponse.SC_OK);
    }

    /**
     * Handles an HTTP DELETE request by removing all dictionary entries.  This
     * behavior is exposed for testing purposes only.  It's probably not
     * something that you'd want a real application to expose.
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");

        this.books.clear();

        PrintWriter pw = response.getWriter();
        pw.println(Messages.allDictionaryEntriesDeleted());
        pw.flush();

        response.setStatus(HttpServletResponse.SC_OK);

    }

    /**
     * Writes an error message about a missing parameter to the HTTP response.
     *
     * The text of the error message is created by {@link Messages#missingRequiredParameter(String)}
     */
    private void missingRequiredParameter( HttpServletResponse response, String parameterName )
        throws IOException
    {
        String message = Messages.missingRequiredParameter(parameterName);
        response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, message);
    }

    /**
     * Writes the definition of the given word to the HTTP response.
     *
     * The text of the message is formatted with
     * {@link Messages#formatDictionaryEntry(String, String)}
     */
    private void writeAppointmentBook(String owner, HttpServletResponse response) throws IOException {
        AppointmentBook book = this.books.get(owner);

        if (book == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } else {
            PrintWriter pw = response.getWriter();
            TextDumper dumper = new TextDumper(pw);
            dumper.dump(book);
            //pw.println(Messages.formatDictionaryEntry(owner, book));

            pw.flush();

            response.setStatus(HttpServletResponse.SC_OK);
        }
    }

    private void writeSpecifiedAppointments(String owner, Date start, Date end, HttpServletResponse response) throws IOException {
        AppointmentBook book = this.books.get(owner);
        AppointmentBook tempBook = new AppointmentBook(owner);

        if (book == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } else {
            PrintWriter pw = response.getWriter();
            TextDumper dumper = new TextDumper(pw);
            for (Appointment app : book.getAppointments()) {
                if((app.getBeginTime().compareTo(start) > 0) && (app.getBeginTime().compareTo(end) < 0)) {
                    //dumper.dumpAppointment(app);
                    tempBook.addAppointment(app);
                }
            }
            dumper.dump(tempBook);

            pw.flush();

            response.setStatus(HttpServletResponse.SC_OK);
        }
    }

    /**
     * Writes all of the dictionary entries to the HTTP response.
     *
     * The text of the message is formatted with
     * {@link Messages#formatDictionaryEntry(String, String)}
     */
    private void writeAllDictionaryEntries(HttpServletResponse response ) throws IOException
    {
        //PrintWriter pw = response.getWriter();
        //Messages.formatDictionaryEntries(pw, books);

        //pw.flush();

        //response.setStatus( HttpServletResponse.SC_OK );
    }

    /**
     * Returns the value of the HTTP request parameter with the given name.
     *
     * @return <code>null</code> if the value of the parameter is
     *         <code>null</code> or is the empty string
     */
    private String getParameter(String name, HttpServletRequest request) {
      String value = request.getParameter(name);
      if (value == null || "".equals(value)) {
        return null;

      } else {
        return value;
      }
    }

    private Date setupDateObject(String date) {
        Date dateClassObj = new Date();
        DateFormat format = new SimpleDateFormat("MM/dd/yy hh:mm a");
        try {
            dateClassObj = format.parse(date);
        } catch (ParseException e) {
            return null;
        }
        return dateClassObj;
    }

    //@VisibleForTesting
    //String getDefinition(String word) {
        //return this.dictionary.get(word);
    //}

    @VisibleForTesting
    AppointmentBook getAppointmentBook(String owner) {
        return this.books.get(owner);
    }

    public AppointmentBook createAppointmentBook(String owner) {
        AppointmentBook book = new AppointmentBook(owner);
        this.books.put(owner, book);
        return book;
    }
}
