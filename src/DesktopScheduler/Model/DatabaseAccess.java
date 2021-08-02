package DesktopScheduler.Model;

import DesktopScheduler.Utility.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.time.*;

/**
 * This method is used to access the given database
 *
 * @author Kyle Matinzi
 * @version 1.0
 */
public class DatabaseAccess {

    private static ObservableList<Customer> allCustomersList = FXCollections.observableArrayList();
    private static ObservableList<Appointment> allAppointmentList = FXCollections.observableArrayList();
    private static ObservableList<Contact> allContactsList = FXCollections.observableArrayList();
    private static ObservableList<Country> allCountriesList = FXCollections.observableArrayList();
    private static ObservableList<Divisions> allDivisionsList = FXCollections.observableArrayList();
    private static ObservableList<User> allUsersList = FXCollections.observableArrayList();
    private static ObservableList<String> appointmentTypes = FXCollections.observableArrayList();
    private static ObservableList<LocalTime> businessHours = FXCollections.observableArrayList();
    private static ObservableList<String> monthsList = FXCollections.observableArrayList();
    private static ObservableList<Divisions> selectedStateList = FXCollections.observableArrayList();

    /**
     * This method will build a list of appointments from a given database
     * @return - List of appointments
     */
    public static ObservableList<Appointment> getAllAppointments(){
        allAppointmentList.clear();
        try{
            String sqlCommand = "SELECT * from appointments";
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sqlCommand);
            ResultSet results = ps.executeQuery();
            while(results.next()){
                int appointmentId = results.getInt("Appointment_ID");
                String title = results.getString("Title");
                String description = results.getString("Description");
                String location = results.getString("Location");
                String type = results.getString("Type");
                LocalDate start = results.getTimestamp("Start").toLocalDateTime().toLocalDate();
                LocalDate end = results.getTimestamp("End").toLocalDateTime().toLocalDate();
                int customerId = results.getInt("Customer_ID");
                int userId = results.getInt("User_ID");
                int contactId = results.getInt("Contact_ID");
                LocalTime startTime = results.getTimestamp("Start").toLocalDateTime().toLocalTime();
                LocalTime endTime = results.getTimestamp("End").toLocalDateTime().toLocalTime();
                //create and add a new appointment object
                Appointment appointment = new Appointment(appointmentId, title, description, location, type, start, end,
                        customerId, userId, contactId,startTime, endTime);
                allAppointmentList.add(appointment);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return allAppointmentList;
    }

    /**
     * This method will build a list of contacts from a given database
     * @return - list of contacts
     */
    public static ObservableList<Contact> getAllContacts(){
        allContactsList.clear();
        try{
            String sqlCommand = "SELECT * from contacts";
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sqlCommand);
            ResultSet results = ps.executeQuery();
            while(results.next()){
                allContactsList.add(new Contact(
                        results.getInt("Contact_ID"),
                        results.getString("Contact_Name"),
                        results.getString("Email")
                ));
            }
            results.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return allContactsList;
    }

    /**
     * This method will build a list of all the Countries in a given database
     * @return - List of countries
     */
    public static ObservableList<Country> getAllCountries(){
        allCountriesList.clear();
        try{
            String sqlCommand = "SELECT * from countries";
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sqlCommand);
            ResultSet results = ps.executeQuery();
            while(results.next()){
                allCountriesList.add(new Country(
                        results.getInt("Country_ID"),
                        results.getString("Country")
                ));
            }
            results.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return allCountriesList;
    }

    /**
     * This method will build a list of Customers from a given database
     * @return - List of customers
     */
    public static ObservableList<Customer> getAllCustomers(){
        allCustomersList.clear();
        try{
            String sqlCommand = "select customers.*, Country_ID FROM customers, first_level_divisions where customers.Division_ID = first_level_divisions.Division_ID";
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sqlCommand);
            ResultSet results = ps.executeQuery();
            while(results.next()){
                allCustomersList.add(new Customer(
                        results.getInt("Customer_ID"),
                        results.getString("Customer_Name"),
                        results.getString("Address"),
                        results.getString("Postal_Code"),
                        results.getString("Phone"),
                        results.getInt("Division_ID"),
                        results.getInt("Country_ID")
                ));
            }
            results.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return allCustomersList;
    }

    /**
     * This method will build a list of divisions from a given database
     * @return - List of divisions
     */
    public static ObservableList<Divisions> getAllDivisions(){
        allDivisionsList.clear();
        try{
            String sqlCommand = "SELECT * from first_level_divisions";
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sqlCommand);
            ResultSet results = ps.executeQuery();
            while(results.next()){
                allDivisionsList.add(new Divisions(
                        results.getInt("Division_ID"),
                        results.getString("Division"),
                        results.getInt("Country_ID")
                ));
            }
            results.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return allDivisionsList;
    }

    /**
     * This method is used to create a list of states according to which country is selected.
     * @param countryId - id of selected country
     * @return - list of states
     */
    public static ObservableList<Divisions> getSelectedStates(int countryId){
        selectedStateList.clear();
        try{
            String sqlCommand = "SELECT * from first_level_divisions where COUNTRY_ID = ? ";
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sqlCommand);
            ps.setInt(1,countryId);
            ResultSet results = ps.executeQuery();
            while(results.next()){
                selectedStateList.add(new Divisions(
                        results.getInt("Division_ID"),
                        results.getString("Division"),
                        results.getInt("Country_ID")
                ));
            }
            results.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return selectedStateList;
    }

    /**
     * This method will build a list of users from a given database
     * @return - List of users
     */
    public static ObservableList<User> getAllUsers(){
        allUsersList.clear();
        try{
            String sqlCommand = "SELECT * from users";
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sqlCommand);
            ResultSet results = ps.executeQuery();
            while(results.next()){
                allUsersList.add(new User(
                        results.getInt("User_ID"),
                        results.getString("User_Name"),
                        results.getString("Password")
                ));
            }
            results.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return allUsersList;
    }

    /**
     * This method creates a list of allowable appointments types.
     * @return - list of appointments types
     */
    public static ObservableList<String> getAppointmentTypes(){
        appointmentTypes.clear();
        appointmentTypes.add("Consultation");
        appointmentTypes.add("De-Briefing");
        appointmentTypes.add("Planning Session");
        appointmentTypes.add("Weekly Meeting");
        appointmentTypes.add("Monthly Meeting");
        appointmentTypes.add("Annual Meeting");
        return appointmentTypes;
    }

    /**
     * This method creates a list of business hours.
     * @return - list of business hours
     */
    public static ObservableList<LocalTime> getBusinessHours(){
        businessHours.clear();
        LocalTime startEst = LocalTime.of(8,0);
        LocalDateTime estLdt = LocalDateTime.of(LocalDate.now(), startEst);
        ZonedDateTime estZdt = estLdt.atZone(ZoneId.of("America/New_York"));
        ZonedDateTime localZdt = estZdt.withZoneSameInstant(ZoneId.systemDefault());
        LocalTime start = localZdt.toLocalTime();

        LocalTime endEst = LocalTime.of(22,0);
         estLdt = LocalDateTime.of(LocalDate.now(), endEst);
         estZdt = estLdt.atZone(ZoneId.of("America/New_York"));
         localZdt = estZdt.withZoneSameInstant(ZoneId.systemDefault());
         LocalTime end = localZdt.toLocalTime();

         while(start.isBefore(end.plusSeconds(1))){
             businessHours.add(start);
             start = start.plusMinutes(30);
         }

        return businessHours;
    }

    /**
     * This method creates a list of months by number.
     * @return - list of months
     */
    public static ObservableList<String> getMonths(){
        monthsList.clear();
        monthsList.add("01");
        monthsList.add("02");
        monthsList.add("03");
        monthsList.add("04");
        monthsList.add("05");
        monthsList.add("06");
        monthsList.add("07");
        monthsList.add("08");
        monthsList.add("09");
        monthsList.add("10");
        monthsList.add("11");
        monthsList.add("12");
        return monthsList;
    }
}
