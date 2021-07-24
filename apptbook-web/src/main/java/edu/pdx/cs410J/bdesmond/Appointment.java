package edu.pdx.cs410J.bdesmond;

import edu.pdx.cs410J.AbstractAppointment;

import java.text.DateFormat;
import java.util.Date;

/**
 * This class extends the abstractAppointment class.
 * It holds the data for an appointment and the necessary
 * methods to manage the data.
 */
public class Appointment extends AbstractAppointment implements Comparable<Appointment>{
    private Date beginTime;
    private Date endTime;
    private String description;

    /**
     * This is the constructor without parameters for the Appointment class.
     */
    public Appointment() {
        super();
        beginTime = new Date();
        endTime = new Date();
        this.description = "";
    }

    public Appointment(String description) {
        super();
        this.description = description;
    }

    /**
     * This is the constructor with parameters for the appointment class.
     * @param beginTime
     *        This is when the event begins
     * @param endTime
     *        This is when the event ends
     * @param description
     *        This parameter gives a description of the appointment
     */
    public Appointment(Date beginTime, Date endTime, String description) {
        super();
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.description = description;
    }

    /**
     * This function is the getter for the beginning time of the appointment.
     * @return
     *        The beginning time of the appointment
     */
    @Override
    public String getBeginTimeString() {
        return DateFormat.getDateInstance(DateFormat.SHORT).format(beginTime) + " " + DateFormat.getTimeInstance(DateFormat.SHORT).format(beginTime);
    }

    /**
     * This function is the getter for the end time of the appointment
     * @return
     *        The end time of the appointment
     */
    @Override
    public String getEndTimeString() {
        return DateFormat.getDateInstance(DateFormat.SHORT).format(endTime) + " " + DateFormat.getTimeInstance(DateFormat.SHORT).format(endTime);
    }

    /**
     * This function returns the beginning date in the Date format
     * @return
     *        The begin date Date object
     */
    public Date getBeginTime() {
        return beginTime;
    }

    /**
     * This is the getter for the end time
     * @return
     *        The end time in a Date object
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * This function is the getter for the description of the appointment
     * @return
     *        The description of the appointment
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * This is the overriden function from the comparable class. This
     * method defines the way two Appointment objects are compared.
     * @param app
     *        This method is passed an Appointment object to be compared to the callers object
     * @return
     *        An int value representing how the Appointments compare
     */
    @Override
    public int compareTo(Appointment app) {
        Date begin1 = this.getBeginTime();
        Date begin2 = app.getBeginTime();
        if (begin1.compareTo(begin2) > 0) {
            return 1; //This is larger than app also known as later chronologically.
        } else if (begin1.compareTo(begin2) < 0) {
            return -1; //This is smaller than app aldo know as app is after this.
        }

        Date end1 = this.getEndTime();
        Date end2 = app.getEndTime();
        if (end1.compareTo(end2) > 0) {
            return 1; //This is larger than app also known as later chronologically.
        } else if (end1.compareTo(end2) < 0) {
            return -1; //This is smaller than app aldo know as app is after this.
        }

        String desc1 = this.getDescription();
        String desc2 = app.getDescription();
        if (desc1.compareTo(desc2) > 0) {
            return 1; //This is larger than app also known as later chronologically.
        } else if (desc1.compareTo(desc2) < 0) {
            return -1; //This is smaller than app aldo know as app is after this.
        } else {
            return 0;
        }
    }
}
