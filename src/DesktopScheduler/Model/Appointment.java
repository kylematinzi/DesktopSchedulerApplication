package DesktopScheduler.Model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * This class creates an appointment object.
 *
 * @author Kyle Matinzi
 * @version 1.0
 */
public class Appointment {

    private int appointmentId;
    private String title;
    private String description;
    private String location;
    private String type;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private int customerId;
    private int userId;
    private int contactId;


    /**
     * This method is used to create an appointment object.
     * @param appointmentId - unique number for each appointment
     * @param title - title of the appointment
     * @param description - description of what the appointment is for
     * @param location - where the appointment will take place
     * @param type - what kind of appointment
     * @param startDate - day the appointment starts
     * @param endDate - day the appointment ends
     * @param customerId - id of associated customer
     * @param userId - id of associated user
     * @param contactId - id of associated contact
     * @param startTime - time the appointments starts
     * @param endTime - time the appointment ends
     */
    public Appointment(int appointmentId, String title, String description, String location, String type, LocalDate startDate, LocalDate endDate, int customerId, int userId, int contactId, LocalTime startTime, LocalTime endTime){
        this.appointmentId = appointmentId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.customerId = customerId;
        this.userId = userId;
        this.contactId = contactId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    //setters/getters
    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public LocalTime getStartTime(){
        return startTime;
    }

    public void setStartTime(LocalTime startTime){
        this.startTime = startTime;
    }

    public LocalTime getEndTime(){
        return endTime;
    }

    public void setEndTime(LocalTime endTime){
        this.endTime = endTime;
    }
}
