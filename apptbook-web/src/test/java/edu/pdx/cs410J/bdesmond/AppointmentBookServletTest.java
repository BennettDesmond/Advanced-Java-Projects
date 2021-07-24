package edu.pdx.cs410J.bdesmond;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

/**
 * A unit test for the {@link AppointmentBookServlet}.  It uses mockito to
 * provide mock http requests and responses.
 */
public class AppointmentBookServletTest {

  @Test
  void gettingAppointmentBookReturnsTextFormat() throws ServletException, IOException {
    String owner = "John";
    String description = "Teach Java";

    AppointmentBookServlet servlet = new AppointmentBookServlet();
    AppointmentBook book = servlet.createAppointmentBook(owner);
    book.addAppointment(new Appointment(description));

    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameter("owner")).thenReturn(owner);
    HttpServletResponse response = mock(HttpServletResponse.class);

    StringWriter sw = new StringWriter();
    when(response.getWriter()).thenReturn(new PrintWriter(sw));

    servlet.doGet(request, response);

    //int expectedWords = 0;
    //verify(new PrintWriter(sw)).println(Messages.formatWordCount(expectedWords));
    verify(response).setStatus(HttpServletResponse.SC_OK);

    String text = sw.toString();
    assertThat(text, containsString(owner));
    assertThat(text, containsString(description));
  }

  @Test
  void addAppointment() throws ServletException, IOException {
    AppointmentBookServlet servlet = new AppointmentBookServlet();

    String owner = "John";
    String description = "Teach Java";

    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameter("owner")).thenReturn(owner);
    when(request.getParameter("description")).thenReturn(description);

    HttpServletResponse response = mock(HttpServletResponse.class);

    // Use a StringWriter to gather the text from multiple calls to println()
    //StringWriter stringWriter = new StringWriter();
    //PrintWriter pw = new PrintWriter(stringWriter, true);

    //when(response.getWriter()).thenReturn(pw);

    servlet.doPost(request, response);

    //assertThat(stringWriter.toString(), containsString(Messages.definedWordAs(word, definition)));

    // Use an ArgumentCaptor when you want to make multiple assertions against the value passed to the mock
    ArgumentCaptor<Integer> statusCode = ArgumentCaptor.forClass(Integer.class);
    verify(response).setStatus(statusCode.capture());

    assertThat(statusCode.getValue(), equalTo(HttpServletResponse.SC_OK));

    AppointmentBook book = servlet.getAppointmentBook(owner);
    assertThat(book, notNullValue());
    assertThat(book.getOwnerName(), equalTo(owner));

    Collection<Appointment> appointments = book.getAppointments();
    assertThat(appointments, hasSize(1));

    Appointment appointment = appointments.iterator().next();
    assertThat(appointment.getDescription(), equalTo(description));

    //assertThat(servlet.getDefinition(word), equalTo(definition));
  }

}
