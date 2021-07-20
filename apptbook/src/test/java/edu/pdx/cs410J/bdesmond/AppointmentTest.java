package edu.pdx.cs410J.bdesmond;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for the {@link Appointment} class.
 *
 * You'll need to update these unit tests as you build out your program.
 */
public class AppointmentTest {

  static private Date setupDateObject(String date) {
    Date dateClassObj = new Date();
    DateFormat format = new SimpleDateFormat("MM/dd/yy hh:mm a");
    try {
      dateClassObj = format.parse(date);
    } catch(ParseException e) {
      System.err.println(e);
    }
    return dateClassObj;
  }

  @Disabled
  @Test
  void getBeginTimeStringNeedsToBeImplemented() {
    Appointment appointment = new Appointment();
    assertThrows(UnsupportedOperationException.class, appointment::getBeginTimeString);
  }

  @Disabled
  @Test
  void initiallyAllAppointmentsHaveTheSameDescription() {
    Appointment appointment = new Appointment();
    assertThat(appointment.getDescription(), containsString("not implemented"));
  }
  @Disabled
  @Test
  void forProject1ItIsOkayIfGetBeginTimeReturnsNull() {
    Appointment appointment = new Appointment();
    assertThat(appointment.getBeginTime(), (nullValue()));
  }

  @Test
  void getBeginTimeStringOutputsCorrectBeginTime() {
    Appointment appointment = new Appointment(setupDateObject("7/15/2021 12:00 am"),setupDateObject("7/15/2021 2:00 pm"),"Meeting with Bernice");
    assertThat(appointment.getBeginTimeString(), equalTo("7/15/21 12:00 AM"));
  }

  @Test
  void getBeginTimeStringOutputsCorrectBeginTimeWhenValIsNull() {
    Appointment appointment = new Appointment();
    Date date = new Date();
    assertThat(appointment.getBeginTimeString(), containsString(DateFormat.getDateInstance(DateFormat.SHORT).format(date)));
  }

  @Test
  void getEndTimeStringOutputsCorrectEndTime() {
    Appointment appointment = new Appointment(setupDateObject("7/15/2021 12:00 am"),setupDateObject("7/15/2021 2:00 pm"),"Meeting with Bernice");
    assertThat(appointment.getEndTimeString(),  containsString("7/15/21 2:00 PM"));
  }

  @Test
  void getEndTimeStringOutputsCorrectEndTimeWhenValIsNull() {
    Appointment appointment = new Appointment();
    Date date = new Date();
    assertThat(appointment.getEndTimeString(), containsString(DateFormat.getDateInstance(DateFormat.SHORT).format(date)));
  }

  @Test
  void getDescriptionStringOutputsCorrectDescription() {
    Appointment appointment = new Appointment(setupDateObject("7/15/2021 12:00 am"),setupDateObject("7/15/2021 2:00 pm"),"Meeting with Bernice");
    assertThat(appointment.getDescription(), equalTo("Meeting with Bernice"));
  }

  @Test
  void getDescriptionStringOutputsCorrectDescriptionWhenValIsNull() {
    Appointment appointment = new Appointment();
    assertThat(appointment.getDescription(), equalTo(""));
  }

  @Test
  void testGetBeginTimeWithInitialVal() {
    Date date = setupDateObject("7/15/2021 12:00 am");
    Appointment appointment = new Appointment(date,setupDateObject("7/15/2021 2:00 pm"),"Meeting with Bernice");
    assertThat(appointment.getBeginTime(), equalTo(date));
  }

  @Test
  void testGetEndTimeWithInitialVal() {
    Date date = setupDateObject("7/15/2021 2:00 pm");
    Appointment appointment = new Appointment(setupDateObject("7/15/2021 12:00 am"),date,"Meeting with Bernice");
    assertThat(appointment.getEndTime(), equalTo(date));
  }

  @Test
  void testCompareMethodWithDifferingStartValues() {
    Appointment appointment1 = new Appointment(setupDateObject("7/15/2021 9:00 am"), setupDateObject("7/15/2021 2:00 pm"), "Meeting with Bernice");
    Appointment appointment2 = new Appointment(setupDateObject("7/15/2021 1:00 pm"), setupDateObject("7/15/2021 2:00 pm"), "Meeting with Bernice");
    assertThat(appointment1.compareTo(appointment2), equalTo(-1));

    appointment1 = new Appointment(setupDateObject("7/15/2021 1:00 pm"), setupDateObject("7/15/2021 2:00 pm"), "Meeting with Bernice");
    appointment2 = new Appointment(setupDateObject("7/15/2021 9:00 am"), setupDateObject("7/15/2021 2:00 pm"), "Meeting with Bernice");
    assertThat(appointment1.compareTo(appointment2), equalTo(1));
  }

  @Test
  void testCompareMethodWithDifferingEndingValues() {
    Appointment appointment1 = new Appointment(setupDateObject("7/15/2021 9:00 am"), setupDateObject("7/15/2021 2:00 pm"), "Meeting with Bernice");
    Appointment appointment2 = new Appointment(setupDateObject("7/15/2021 9:00 am"), setupDateObject("7/15/2021 7:00 pm"), "Meeting with Bernice");
    assertThat(appointment1.compareTo(appointment2), equalTo(-1));

    appointment1 = new Appointment(setupDateObject("7/15/2021 9:00 am"), setupDateObject("7/15/2021 7:00 pm"), "Meeting with Bernice");
    appointment2 = new Appointment(setupDateObject("7/15/2021 9:00 am"), setupDateObject("7/15/2021 2:00 pm"), "Meeting with Bernice");
    assertThat(appointment1.compareTo(appointment2), equalTo(1));
  }

  @Test
  void testCompareMethodWithDifferingDescriptions() {
    Appointment appointment1 = new Appointment(setupDateObject("7/15/2021 9:00 am"), setupDateObject("7/15/2021 2:00 pm"), "abc");
    Appointment appointment2 = new Appointment(setupDateObject("7/15/2021 9:00 am"), setupDateObject("7/15/2021 2:00 pm"), "bcd");
    assertThat(appointment1.compareTo(appointment2), equalTo(-1));

    appointment1 = new Appointment(setupDateObject("7/15/2021 9:00 am"), setupDateObject("7/15/2021 2:00 pm"), "bcd");
    appointment2 = new Appointment(setupDateObject("7/15/2021 9:00 am"), setupDateObject("7/15/2021 2:00 pm"), "abc");
    assertThat(appointment1.compareTo(appointment2), equalTo(1));
  }

  @Test
  void testCompareMethodWithAllMatchingValues() {
    Appointment appointment1 = new Appointment(setupDateObject("7/15/2021 9:00 am"), setupDateObject("7/15/2021 2:00 pm"), "Meeting with Bernice");
    Appointment appointment2 = new Appointment(setupDateObject("7/15/2021 9:00 am"), setupDateObject("7/15/2021 2:00 pm"), "Meeting with Bernice");
    assertThat(appointment1.compareTo(appointment2), equalTo(0));
  }

}
