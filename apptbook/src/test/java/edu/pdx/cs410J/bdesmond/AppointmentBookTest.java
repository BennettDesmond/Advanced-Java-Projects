package edu.pdx.cs410J.bdesmond;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

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
        Appointment appointment = new Appointment("7/15/202112:00","7/15/202113:00","Meeting with Bernice");
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
        Appointment appointment = new Appointment("7/15/202112:00","7/15/202113:00","Meeting with Bernice");
        LinkedList<Appointment> appointments = new LinkedList<Appointment>();
        appointments.add(appointment);
        AppointmentBook appointmentBook = new AppointmentBook("Jake",appointments);
        AppointmentBook otherAppointmentBook = new AppointmentBook();
        otherAppointmentBook.addAppointment(appointment);
        assertThat(appointmentBook.getAppointments(), equalTo(otherAppointmentBook.getAppointments()));
    }

}
