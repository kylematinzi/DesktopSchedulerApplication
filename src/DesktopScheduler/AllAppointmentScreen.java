package DesktopScheduler;

import DesktopScheduler.Model.*;
import DesktopScheduler.Utility.DatabaseConnection;
import DesktopScheduler.Utility.DatabaseQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This class is the controller of the all appointment screen used to add functionality to the all appointment screen.
 * In this class all appointments in the database may be viewed, updated, and/or deleted.
 *
 * @author Kyle Matinzi
 * @version Version 1.0
 */
public class AllAppointmentScreen implements Initializable {

    @FXML
    private TextField appointmentIdTextBox;
    @FXML
    private ComboBox<Contact> contactIdComboBox;
    @FXML
    private ComboBox<User> userIdComboBox;
    @FXML
    private ComboBox<Customer> customerIdComboBox;
    @FXML
    private TextField appointmentTitleTextBox;
    @FXML
    private TextField locationTextBox;
    @FXML
    private ComboBox appointmentTypeComboBox;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<LocalTime> startTimeComboBox;
    @FXML
    private ComboBox<LocalTime> endTimeComboBox;
    @FXML
    private TableView<Appointment> allAppointmentTable;
    @FXML
    private TableColumn<Appointment, Integer> appointmentIdColumn;
    @FXML
    private TableColumn<Appointment, String> appointmentTitleColumn;
    @FXML
    private TableColumn<Appointment, String> appointmentDescriptionColumn;
    @FXML
    private TableColumn<Appointment, String> appointmentLocationColumn;
    @FXML
    private TableColumn<Appointment, String> appointmentContactIdColumn;
    @FXML
    private TableColumn<Appointment, String> appointmentTypeColumn;
    @FXML
    private TableColumn<Appointment, Date> appointmentStartDateColumn;
    @FXML
    private TableColumn<Appointment, Date> appointmentEndDateColumn;
    @FXML
    private TableColumn<Appointment, Integer> appointmentCustomerIdColumn;
    @FXML
    private TableColumn<Appointment, Time> appointmentStartTimeColumn;
    @FXML
    private TableColumn<Appointment, Time> appointmentEndTimeColumn;
    private static int appointmentId;
    public static int dayOfyear;
    public static int dayOfWeek;
    private static String appointmentIdName;
    private String appointmentTitle;
    private String appointmentDescription;
    private String appointmentLocation;
    private String appointmentType;
    private int appointmentCustomerId;
    private int appointmentUserId;
    private int appointmentContactId;
    private int appointmentUpdateId;
    private LocalTime appointmentStart;
    private LocalTime appointmentEnd;
    private LocalDate appointmentDate;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private static LocalDateTime existingSqlStartDateTime;
    private static LocalDateTime existingSqlEndDateTime;
    private ObservableList<User> userList = DatabaseAccess.getAllUsers();
    private ObservableList<Customer> customerList = DatabaseAccess.getAllCustomers();
    private ObservableList<Contact> contactsList = DatabaseAccess.getAllContacts();
    private ObservableList<String> appointmentTypes = DatabaseAccess.getAppointmentTypes();
    private static ObservableList<Appointment> customerAppointmentList = FXCollections.observableArrayList();
    private static ObservableList<Appointment> monthlyAppointmentList = FXCollections.observableArrayList();
    private static ObservableList<Appointment> weeklyAppointmentList = FXCollections.observableArrayList();
    private static ObservableList<Appointment> allAppointments = DatabaseAccess.getAllAppointments();

    /**
     * This method opens the appointment report screen when appointment report button is pressed.
     * @param actionEvent
     * @throws IOException
     */
    public void appointmentReportButtonPressed(ActionEvent actionEvent) throws IOException {
        StackPane appointmentReportParent = new StackPane();
        appointmentReportParent.getChildren().add(FXMLLoader.load(getClass().getResource("ViewsControllers/AppointmentReport.fxml")));
        Scene scene = new Scene(appointmentReportParent);
        Stage appointmentReportScene = new Stage();
        appointmentReportScene.setScene(scene);
        appointmentReportScene.initModality(Modality.WINDOW_MODAL);
        appointmentReportScene.initOwner(((((Button) actionEvent.getSource()).getScene().getWindow())));
        appointmentReportScene.sizeToScene();
        appointmentReportScene.setResizable(false);
        appointmentReportScene.setTitle("Desktop Scheduler");
        appointmentReportScene.show();
    }

    /**
     * Method updates a given appointment in the database based on user input when save button is pressed.
     * @param actionEvent
     * @throws SQLException
     */
    public void saveChangesButtonPressed(ActionEvent actionEvent) throws SQLException {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String updateStatement = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Customer_ID = ?, User_ID = ?, " +
                    "Contact_ID = ?, Start = ?, End = ? WHERE Appointment_ID = ?";
            DatabaseQuery.setPreparedStatement(conn, updateStatement);
            appointmentStart = LocalTime.parse(startTimeComboBox.getSelectionModel().getSelectedItem().toString());
            appointmentEnd = LocalTime.parse(endTimeComboBox.getSelectionModel().getSelectedItem().toString());
            appointmentDate = datePicker.getValue();
            startDateTime = LocalDateTime.of(appointmentDate, appointmentStart);
            endDateTime = LocalDateTime.of(appointmentDate, appointmentEnd);
            Appointment updateAppointment = allAppointmentTable.getSelectionModel().getSelectedItem();
            appointmentUpdateId = updateAppointment.getAppointmentId();
            appointmentTitle = appointmentTitleTextBox.getText();
            appointmentDescription = descriptionTextArea.getText();
            appointmentLocation = locationTextBox.getText();
            appointmentType = String.valueOf(appointmentTypeComboBox.getValue());
            appointmentCustomerId = customerIdComboBox.getSelectionModel().getSelectedItem().getCustomerID();
            appointmentUserId = userIdComboBox.getSelectionModel().getSelectedItem().getUserId();
            appointmentContactId = contactIdComboBox.getSelectionModel().getSelectedItem().getContactId();
            if( appointmentTitle.isEmpty() || appointmentDescription.isEmpty() || appointmentLocation.isEmpty() || appointmentType.isEmpty()
                    || customerIdComboBox.getSelectionModel().isEmpty() || userIdComboBox.getSelectionModel().isEmpty()
                    || contactIdComboBox.getSelectionModel().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please ensure all fields are filled.");
                alert.setTitle("Warning");
                Optional<ButtonType> result = alert.showAndWait();
            }
            else {
                if (timeCheck(appointmentStart, appointmentEnd) == true) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Start time may not be after or equal to End time.");
                    alert.setTitle("Warning");
                    Optional<ButtonType> result = alert.showAndWait();
                } else {
                    if (overLapTest(startDateTime, endDateTime, appointmentCustomerId, appointmentUpdateId) == true) {
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
                        ps.setString(10, String.valueOf(appointmentUpdateId));
                        ps.execute();
                        allAppointmentTable.setItems(DatabaseAccess.getAllAppointments());
                    }
                }
            }
            }catch(Exception e){
                Alert alert = new Alert(Alert.AlertType.WARNING, "No appointment selected.");
                alert.setTitle("Warning");
                Optional<ButtonType> result = alert.showAndWait();
        }
    }

    /**
     * Method will delete a selected appointment based on a table selection.
     * @param actionEvent
     * @throws SQLException
     */
    public void deleteButtonPressed(ActionEvent actionEvent) throws SQLException {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String deleteStatement = "DELETE FROM appointments WHERE Appointment_ID = ?";
            DatabaseQuery.setPreparedStatement(conn, deleteStatement);
            Appointment deletingAppointment = allAppointmentTable.getSelectionModel().getSelectedItem();
            appointmentId = deletingAppointment.getAppointmentId();
            appointmentIdName = deletingAppointment.getTitle();
            appointmentType = deletingAppointment.getType();
            PreparedStatement ps = DatabaseQuery.getPreparedStatement();
            if (deletingAppointment == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "No Appointment Selected for deletion.");
                alert.setTitle("Empty Selection");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You are about to delete: "+"\n"
                        +"Appointment ID: "+ appointmentId+"\n"+
                        "Appointment Type: "+appointmentType);
                alert.setTitle("Confirmation");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    ps.setInt(1, appointmentId);
                    ps.execute();
                }
            }
            allAppointmentTable.setItems(DatabaseAccess.getAllAppointments());
        }catch(Exception e){
            Alert alert = new Alert(Alert.AlertType.WARNING, "No appointment selected to delete.");
            alert.setTitle("Warning");
            Optional<ButtonType> result = alert.showAndWait();
        }
    }

    /**
     * Method will fill corresponding fields based on selected table row when user uses arrows to change selection.
     * @param keyEvent - arrow pressed
     */
    public void tableArrowSelection(KeyEvent keyEvent) {
        try {
            appointmentIdTextBox.setText(String.valueOf(allAppointmentTable.getSelectionModel().getSelectedItem().getAppointmentId()));
            appointmentTitleTextBox.setText(allAppointmentTable.getSelectionModel().getSelectedItem().getTitle());
            locationTextBox.setText(allAppointmentTable.getSelectionModel().getSelectedItem().getLocation());
            appointmentTypeComboBox.setValue(allAppointmentTable.getSelectionModel().getSelectedItem().getType());
            descriptionTextArea.setText(allAppointmentTable.getSelectionModel().getSelectedItem().getDescription());
            startTimeComboBox.setValue(allAppointmentTable.getSelectionModel().getSelectedItem().getStartTime());
            endTimeComboBox.setValue(allAppointmentTable.getSelectionModel().getSelectedItem().getEndTime());
            int contactId = allAppointmentTable.getSelectionModel().getSelectedItem().getContactId();
            int userId = allAppointmentTable.getSelectionModel().getSelectedItem().getUserId();
            int customerId = allAppointmentTable.getSelectionModel().getSelectedItem().getCustomerId();
            for (Contact c : contactIdComboBox.getItems()) {
                if (c.getContactId() == contactId) {
                    contactIdComboBox.setValue(c);
                    break;
                }
            }
            for (User u : userIdComboBox.getItems()) {
                if (u.getUserId() == userId) {
                    userIdComboBox.setValue(u);
                    break;
                }
            }
            for (Customer c : customerIdComboBox.getItems()) {
                if (c.getCustomerID() == customerId) {
                    customerIdComboBox.setValue(c);
                    break;
                }
            }
            LocalDate input = allAppointmentTable.getSelectionModel().getSelectedItem().getStartDate();
            datePicker.setValue(input);
//            startTimeComboBox.setItems(DatabaseAccess.getBusinessHours());
//            endTimeComboBox.setItems(DatabaseAccess.getBusinessHours());
        }catch (Exception e){}
    }

    /**
     * Method will fill corresponding fields based on selected table row when user uses a mouse click to change selection.
     * @param mouseEvent - mouse click
     */
    public void tableClicked(MouseEvent mouseEvent) {
        try {
            appointmentIdTextBox.setText(String.valueOf(allAppointmentTable.getSelectionModel().getSelectedItem().getAppointmentId()));
            appointmentTitleTextBox.setText(allAppointmentTable.getSelectionModel().getSelectedItem().getTitle());
            locationTextBox.setText(allAppointmentTable.getSelectionModel().getSelectedItem().getLocation());
            appointmentTypeComboBox.setValue(allAppointmentTable.getSelectionModel().getSelectedItem().getType());
            descriptionTextArea.setText(allAppointmentTable.getSelectionModel().getSelectedItem().getDescription());
            startTimeComboBox.setValue(allAppointmentTable.getSelectionModel().getSelectedItem().getStartTime());
            endTimeComboBox.setValue(allAppointmentTable.getSelectionModel().getSelectedItem().getEndTime());
            int contactId = allAppointmentTable.getSelectionModel().getSelectedItem().getContactId();
            int userId = allAppointmentTable.getSelectionModel().getSelectedItem().getUserId();
            int customerId = allAppointmentTable.getSelectionModel().getSelectedItem().getCustomerId();
            for (Contact c : contactIdComboBox.getItems()) {
                if (c.getContactId() == contactId) {
                    contactIdComboBox.setValue(c);
                    break;
                }
            }
            for (User u : userIdComboBox.getItems()) {
                if (u.getUserId() == userId) {
                    userIdComboBox.setValue(u);
                    break;
                }
            }
            for (Customer c : customerIdComboBox.getItems()) {
                if (c.getCustomerID() == customerId) {
                    customerIdComboBox.setValue(c);
                    break;
                }
            }
            LocalDate input = allAppointmentTable.getSelectionModel().getSelectedItem().getStartDate();
            datePicker.setValue(input);
//            startTimeComboBox.setItems(DatabaseAccess.getBusinessHours());
//            endTimeComboBox.setItems(DatabaseAccess.getBusinessHours());
        }catch (Exception e){
        }
    }

    /**
     * Method produces a list with appointments from the current month
     *
     * This lambda goes through a list of all appointments to find all appointments that correspond to
     * the given month.
     *
     * @return - list of appointments
     */
    public static ObservableList<Appointment> setTableMonthly(){

        monthlyAppointmentList = allAppointments.filtered(a -> {
            if(a.getStartDate().getMonth() == LocalDate.now().getMonth()){
                return true;
            }
            return false;
        });
        return monthlyAppointmentList;

    }

    /**
     * Method produces a list with appointments from the current seven day week.
     * @return - list of appointments
     */
    public static ObservableList<Appointment> setWeeklyTable(){
        weeklyAppointmentList.clear();
        dayOfWeek = LocalDate.now().getDayOfWeek().getValue();
        dayOfyear = LocalDate.now().getDayOfYear();
        if(dayOfWeek == 0){
            int count = 6;
            while( count >= 0) {
                for (Appointment a : allAppointments) {
                    if (a.getStartDate().getDayOfYear() == dayOfyear) {
                        weeklyAppointmentList.add(a);
                    }
                }
                dayOfyear += 1;
                count--;
            }
        }
        else if(dayOfWeek == 1){
            int count = 6;
            dayOfyear = dayOfyear-1;
            while( count >= 0) {
                for (Appointment a : allAppointments) {
                    if (a.getStartDate().getDayOfYear() == dayOfyear) {
                        weeklyAppointmentList.add(a);
                    }
                }
                dayOfyear += 1;
                count--;
            }
        }
        else if(dayOfWeek == 2){
            int count = 6;
            dayOfyear = dayOfyear-2;
            while( count >= 0) {
                for (Appointment a : allAppointments) {
                    if (a.getStartDate().getDayOfYear() == dayOfyear) {
                        weeklyAppointmentList.add(a);
                    }
                }
                dayOfyear += 1;
                count--;
            }
        }
        else if(dayOfWeek == 3){
            int count = 6;
            dayOfyear = dayOfyear-3;
            while( count >= 0) {
                for (Appointment a : allAppointments) {
                    if (a.getStartDate().getDayOfYear() == dayOfyear) {
                        weeklyAppointmentList.add(a);
                    }
                }
                dayOfyear += 1;
                count--;
            }
        }
        else if(dayOfWeek == 4){
            int count = 6;
            dayOfyear = dayOfyear-4;
            while( count >= 0) {
                for (Appointment a : allAppointments) {
                    if (a.getStartDate().getDayOfYear() == dayOfyear) {
                        weeklyAppointmentList.add(a);
                    }
                }
                dayOfyear += 1;
                count--;
            }
        }
        else if(dayOfWeek == 5){
            int count = 6;
            dayOfyear = dayOfyear-5;
            while( count >= 0) {
                for (Appointment a : allAppointments) {
                    if (a.getStartDate().getDayOfYear() == dayOfyear) {
                        weeklyAppointmentList.add(a);
                    }
                }
                dayOfyear += 1;
                count--;
            }
        }
        else if(dayOfWeek == 6){
            int count = 6;
            dayOfyear = dayOfyear-6;
            while( count >= 0) {
                for (Appointment a : allAppointments) {
                    if (a.getStartDate().getDayOfYear() == dayOfyear) {
                        weeklyAppointmentList.add(a);
                    }
                }
                dayOfyear += 1;
                count--;
            }
        }
        return weeklyAppointmentList;
    }

    /**
     * Method will display all appointment data in proper table when "All" radio button is selected.
     * @param actionEvent
     */
    public void allRadioButtonSelected(ActionEvent actionEvent) {
        allAppointmentTable.setItems(DatabaseAccess.getAllAppointments());
        appointmentIdColumn.setCellValueFactory((new PropertyValueFactory<>("appointmentId")));
        appointmentTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        appointmentDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        appointmentLocationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        appointmentContactIdColumn.setCellValueFactory(new PropertyValueFactory<>("contactId"));
        appointmentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        appointmentStartDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        appointmentEndDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        appointmentCustomerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        appointmentStartTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        appointmentEndTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
    }

    /**
     * Method will display only appointments from the current month when "Monthly" radio button is selected.
     * @param actionEvent
     */
    public void monthlyRadioButtonSelected(ActionEvent actionEvent) {
        allAppointmentTable.setItems(setTableMonthly());
        appointmentIdColumn.setCellValueFactory((new PropertyValueFactory<>("appointmentId")));
        appointmentTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        appointmentDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        appointmentLocationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        appointmentContactIdColumn.setCellValueFactory(new PropertyValueFactory<>("contactId"));
        appointmentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        appointmentStartDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        appointmentEndDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        appointmentCustomerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        appointmentStartTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        appointmentEndTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
    }

    /**
     * Method will display only appointments from current week when "Weekly" radio button is selected.
     * @param actionEvent
     */
    public void weeklyRadioButtonSelected(ActionEvent actionEvent) {
        allAppointmentTable.setItems(setWeeklyTable());
        appointmentIdColumn.setCellValueFactory((new PropertyValueFactory<>("appointmentId")));
        appointmentTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        appointmentDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        appointmentLocationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        appointmentContactIdColumn.setCellValueFactory(new PropertyValueFactory<>("contactId"));
        appointmentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        appointmentStartDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        appointmentEndDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        appointmentCustomerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        appointmentStartTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        appointmentEndTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
    }

    /**
     * This method checks to see if a new appointment overlaps with an existing appointment for a given customer ID.
     * @param proposedStartDateTime - appointment start time
     * @param proposedEndDateTime - appointment end time
     * @param appointmentCustomerId - customer ID
     * @return - true if there is an overlap, false if there is no overlap.
     */
    public static boolean overLapTest(LocalDateTime proposedStartDateTime, LocalDateTime proposedEndDateTime, int appointmentCustomerId, int appointmentId){
        customerAppointmentList.clear();
        for(Appointment a: allAppointments){
            if(a.getCustomerId() == appointmentCustomerId){
                customerAppointmentList.add(a);
            }
        }
        for(Appointment b: customerAppointmentList){
            if(b.getAppointmentId() == appointmentId){
                continue;
            }
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
        ((Stage) (((Button) actionEvent.getSource()).getScene().getWindow())).close();
    }

    /**
     * Method initializes all appointment screen.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        startTimeComboBox.setItems(DatabaseAccess.getBusinessHours());
        endTimeComboBox.setItems(DatabaseAccess.getBusinessHours());
        contactIdComboBox.setItems(contactsList);
        userIdComboBox.setItems(userList);
        customerIdComboBox.setItems(customerList);
        appointmentTypeComboBox.setItems(appointmentTypes);
        allAppointmentTable.setItems(DatabaseAccess.getAllAppointments());
        appointmentIdColumn.setCellValueFactory((new PropertyValueFactory<>("appointmentId")));
        appointmentTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        appointmentDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        appointmentLocationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        appointmentContactIdColumn.setCellValueFactory(new PropertyValueFactory<>("contactId"));
        appointmentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        appointmentStartDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        appointmentEndDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        appointmentCustomerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        appointmentStartTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        appointmentEndTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
    }
}