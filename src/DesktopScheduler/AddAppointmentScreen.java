package DesktopScheduler;

import DesktopScheduler.Model.*;
import DesktopScheduler.Utility.DatabaseConnection;
import DesktopScheduler.Utility.DatabaseQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This class is the controller of the add appointment screen. This class is used to add appointments to a given
 * database.
 *
 * @author Kyle Matinzi
 * @version Version 1.0
 */
public class AddAppointmentScreen implements Initializable {

    @FXML
    private TextField appointmentTitleTextBox;
    @FXML
    private TextField locationTextBox;
    @FXML
    private ComboBox<String> appointmentTypeComboBox;
    @FXML
    private ComboBox<Contact> contactIdComboBox;
    @FXML
    private ComboBox<User> userIdComboBox;
    @FXML
    private ComboBox<Customer> customerIdComboBox;
    @FXML
    private ComboBox startTimeComboBox;
    @FXML
    private ComboBox endTimeComboBox;
    @FXML
    private DatePicker appointmentDateBox;
    @FXML
    private TextArea appointmentDescriptionArea;

    private String appointmentTitle;
    private String appointmentDescription;
    private String appointmentLocation;
    private String appointmentType;
    private LocalTime appointmentStart;
    private LocalTime appointmentEnd;
    private LocalDate appointmentDate;
    private int appointmentCustomerId;
    private int appointmentUserId;
    private int appointmentContactId;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private static LocalDateTime existingSqlStartDateTime;
    private static LocalDateTime existingSqlEndDateTime;
    private ObservableList<User> userList = DatabaseAccess.getAllUsers();
    private ObservableList<Customer> customerList = DatabaseAccess.getAllCustomers();
    private ObservableList<String> appointmentTypesList = DatabaseAccess.getAppointmentTypes();
    private ObservableList<Contact> contactsList = DatabaseAccess.getAllContacts();
    private ObservableList<LocalTime> businessHoursList = DatabaseAccess.getBusinessHours();
    private static ObservableList<Appointment> allAppointments = DatabaseAccess.getAllAppointments();
    private static ObservableList<Appointment> customerAppointmentList = FXCollections.observableArrayList();

    /**
     * This method adds a new appointment to a given database based on given user input.
     * @param actionEvent
     * @throws SQLException
     */
    public void saveAppointmentButtonPressed(ActionEvent actionEvent) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        String insertStatement = "INSERT INTO appointments(Title, Description, Location, Type, " +
                "Customer_ID, User_ID, Contact_ID, Start, End) VALUES (?,?,?,?,?,?,?,?,?)";
        DatabaseQuery.setPreparedStatement(conn, insertStatement);
        try {
            appointmentStart = LocalTime.parse(startTimeComboBox.getSelectionModel().getSelectedItem().toString());
            appointmentEnd = LocalTime.parse(endTimeComboBox.getSelectionModel().getSelectedItem().toString());
            appointmentDate = appointmentDateBox.getValue();
            appointmentTitle = appointmentTitleTextBox.getText();
            appointmentDescription = appointmentDescriptionArea.getText();
            appointmentLocation = locationTextBox.getText();
            appointmentType = appointmentTypeComboBox.getSelectionModel().getSelectedItem();
            startDateTime = LocalDateTime.of(appointmentDate, appointmentStart);
            endDateTime = LocalDateTime.of(appointmentDate, appointmentEnd);
            appointmentCustomerId = customerIdComboBox.getSelectionModel().getSelectedItem().getCustomerID();
            appointmentUserId = userIdComboBox.getSelectionModel().getSelectedItem().getUserId();
            appointmentContactId = contactIdComboBox.getSelectionModel().getSelectedItem().getContactId();
            if (startTimeComboBox.getSelectionModel().isEmpty() || endTimeComboBox.getSelectionModel().isEmpty() || appointmentDateBox.getValue() == null
                    || appointmentTitle.isEmpty() || appointmentDescription.isEmpty() || appointmentLocation.isEmpty() || appointmentType.isEmpty()
                    || customerIdComboBox.getSelectionModel().isEmpty() || userIdComboBox.getSelectionModel().isEmpty()
                    || contactIdComboBox.getSelectionModel().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please ensure all fields are filled.");
                alert.setTitle("Warning");
                Optional<ButtonType> result = alert.showAndWait();

            } else {
                if (timeCheck(appointmentStart, appointmentEnd) == true) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Start time may not be after or equal to End time.");
                    alert.setTitle("Warning");
                    Optional<ButtonType> result = alert.showAndWait();
                } else {
                    if (overLapTest(startDateTime, endDateTime, appointmentCustomerId) == true) {
                        Alert alert = new Alert(Alert.AlertType.WARNING, "Customer appointment times may not overlap.");
                        alert.setTitle("Warning");
                        Optional<ButtonType> result = alert.showAndWait();
                    } else {
                        PreparedStatement ps = DatabaseQuery.getPreparedStatement();
                        ps.setString(1, appointmentTitle);
                        ps.setString(2, appointmentDescription);
                        ps.setString(3, appointmentLocation);
                        ps.setString(4, appointmentType);
                        ps.setInt(5, appointmentCustomerId);
                        ps.setInt(6, appointmentUserId);
                        ps.setInt(7, appointmentContactId);
                        ps.setTimestamp(8, Timestamp.valueOf(startDateTime));
                        ps.setTimestamp(9, Timestamp.valueOf(endDateTime));
                        ps.execute();
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Appointment: " + appointmentTitle + " has been added.");
                        alert.setTitle("Confirmation");
                        Optional<ButtonType> result = alert.showAndWait();
                        ((Stage) (((Button) actionEvent.getSource()).getScene().getWindow())).close();
                    }
                }
            }
        }catch(NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please ensure all fields are filled.");
            alert.setTitle("Warning");
            Optional<ButtonType> result = alert.showAndWait();
        }
    }

    /**
     * This method checks to see if a new appointment overlaps with an existing appointment for a given customer ID.
     * @param proposedStartDateTime - appointment start time
     * @param proposedEndDateTime - appointment end time
     * @param appointmentCustomerId - customer ID
     * @return - true if there is an overlap, false if there is no overlap.
     */
    public static boolean overLapTest(LocalDateTime proposedStartDateTime, LocalDateTime proposedEndDateTime, int appointmentCustomerId){
        customerAppointmentList.clear();
        for(Appointment a: allAppointments){
            if(a.getCustomerId() == appointmentCustomerId){
                customerAppointmentList.add(a);
            }
        }
        for(Appointment b: customerAppointmentList){
            existingSqlStartDateTime = LocalDateTime.of(b.getStartDate(), b.getStartTime());
            existingSqlEndDateTime = LocalDateTime.of(b.getEndDate(),b.getEndTime());
            //starts during another appointment
            if((proposedStartDateTime.isEqual(existingSqlStartDateTime)|| proposedStartDateTime.isAfter(existingSqlStartDateTime) &&
                    proposedStartDateTime.isBefore(existingSqlEndDateTime)))
                return true;
            //ends during another appointment
            if((proposedEndDateTime.isEqual(existingSqlEndDateTime) || proposedEndDateTime.isBefore(existingSqlEndDateTime)
                    && proposedEndDateTime.isAfter(existingSqlStartDateTime))){
                return true;
            }
            //encompasses another entire appointment
            if((proposedStartDateTime.isEqual(existingSqlStartDateTime) || proposedStartDateTime.isBefore(existingSqlStartDateTime))
                    && (proposedEndDateTime.isEqual(existingSqlEndDateTime) || proposedEndDateTime.isAfter(existingSqlEndDateTime))){
                return true;
            }
        }
        return false;
    }

    /**
     * This method checks user entry to make sure that the start time is not after or equal to the end time.
     * @param startTime - inputted start time
     * @param endTime - inputted end time
     * @return - true if times are equal or start is after end, false if they are not equal and start is before end.
     */
    public static boolean timeCheck(LocalTime startTime, LocalTime endTime){
        if(startTime.isAfter(endTime) || startTime == endTime){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * This method is used to close the current window when the exit button is pressed.
     * @param actionEvent
     * @throws IOException
     */
    public void exitButtonPressed(ActionEvent actionEvent) throws IOException {
        ((Stage)(((Button)actionEvent.getSource()).getScene().getWindow())).close();
    }

    /**
     * Method initializes the add appointment screen
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userIdComboBox.setItems(userList);
        customerIdComboBox.setItems(customerList);
        appointmentTypeComboBox.setItems(appointmentTypesList);
        contactIdComboBox.setItems(contactsList);
        startTimeComboBox.setItems(businessHoursList);
        endTimeComboBox.setItems(businessHoursList);
    }
}
