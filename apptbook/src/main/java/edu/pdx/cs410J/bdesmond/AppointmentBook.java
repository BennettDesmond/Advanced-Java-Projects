package edu.pdx.cs410J.bdesmond;

import edu.pdx.cs410J.AbstractAppointment;
import edu.pdx.cs410J.AbstractAppointmentBook;

import java.util.Collection;
import java.util.LinkedList;

/**
 * This class extends the AbstractAppointmentBook class.
 * All of the data and methods for an appointment book are
 * contained in this class.
 */
public class AppointmentBook extends AbstractAppointmentBook {
    private String owner;
    private LinkedList<Appointment> appointments;

    /**
     * This is the default constructor for the AppointmentBook class.
     */
    public AppointmentBook() {
        super();
        owner = "";
        appointments = new LinkedList<Appointment>();
    }

    /**
     * This is the constructor with a parameter for the
     * AppointmentBook class. This constructor accepts the owner name
     * and initializes the appointment list.
     * @param owner
     *        Appointment book owner
     */
    public AppointmentBook(String owner) {
        super();
        this.owner = owner;
        appointments = new LinkedList<Appointment>();
    }

    /**
     * This is the fully parameterized constructor for the AppointmentBook class.
     * @param owner
     *        Appointment book owner
     * @param appointments
     *        A linked list of appointments
     */
    public AppointmentBook(String owner, Collection<Appointment> appointments) {
        super();
        this.owner = owner;
        this.appointments = (LinkedList) appointments;
    }

    /**
     * This is the getter for the owner's name
     * @return
     *        Appointment book owner's name
     */
    @Override
    public String getOwnerName() {
        return owner;
    }

    /**
     * This method is a getter for the linked list of Appointments
     * @return
     *        This function returns the linked list of Appointments.
     */
    @Override
    public Collection<Appointment> getAppointments() {
        return appointments;
    }

    /**
     * This function adds an appointment to the linked list of Appointments.
     * @param abstractAppointment
     *        This is the appointment to be added.
     */
    @Override
    public void addAppointment(AbstractAppointment abstractAppointment) {
        appointments.add((Appointment) abstractAppointment);
    }
}