package edu.pdx.cs410J.bdesmond;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for the {@link Appointment} class.
 *
 * You'll need to update these unit tests as you build out your program.
 */
public class AppointmentBookTest {

    static private Date setupDateObject(String date) {
        Date dateClassObj = new Date();
        DateFormat format = new SimpleDateFormat("MM/dd/yy hh:mm a");
        try {
            dateClassObj = format.parse(date);
        } catch (ParseException e) {
            System.err.println(e);
        }
        return dateClassObj;
    }

    @Test
    void getOwnerNameCorrectStringReturned() {
        LinkedList<Appointment> appointments = new LinkedList<Appointment>();
        AppointmentBook appointmentBook = new AppointmentBook("John",appointments);
        assertThat(appointmentBook.getOwnerName(), equalTo("John"));
    }

    @Test
    void getOwnerNameCorrectOutputWithNull() {
        AppointmentBook appointmentBook = new AppointmentBook();
        assertThat(appointmentBook.getOwnerName(), equalTo(""));
    }

    @Test
    void getAppointmentsTestWithAppointmentBookPassed() {
        Appointment appointment = new Appointment(setupDateObject("7/15/2021 12:00 am"),setupDateObject("7/15/2021 12:00 pm"),"Meeting with Bernice");
        LinkedList<Appointment> appointments = new LinkedList<Appointment>();
        appointments.add(appointment);
        AppointmentBook appointmentBook = new AppointmentBook("Jake",appointments);
        assertThat(appointmentBook.getAppointments(), equalTo(appointments));
    }

    @Test
    void getAppointmentsTestWithNullValue() {
        AppointmentBook appointmentBook = new AppointmentBook();
        LinkedList<Appointment> appointments = new LinkedList<Appointment>();
        assertThat(appointmentBook.getAppointments(), equalTo(appointments));
    }

    @Test
    void addAppointmentTestWithNewAppointment() {
        Appointment appointment = new Appointment(setupDateObject("7/15/2021 12:00 am"),setupDateObject("7/15/2021 12:00 pm"),"Meeting with Bernice");
        LinkedList<Appointment> appointments = new LinkedList<Appointment>();
        appointments.add(appointment);
        AppointmentBook appointmentBook = new AppointmentBook("Jake",appointments);
        AppointmentBook otherAppointmentBook = new AppointmentBook();
        otherAppointmentBook.addAppointment(appointment);
        assertThat(appointmentBook.getAppointments(), equalTo(otherAppointmentBook.getAppointments()));
    }

}
