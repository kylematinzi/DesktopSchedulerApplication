package DesktopScheduler;

import DesktopScheduler.Model.Appointment;
import DesktopScheduler.Model.Contact;
import DesktopScheduler.Model.DatabaseAccess;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

/**
 * This class is the controller of the contact schedule screen. This class is used to display a selected contacts schedule.
 *
 * @author Kyle Matinzi
 * @version Version 1.0
 */
public class ContactScheduleScreen implements Initializable {

    @FXML
    private TableView<Appointment> scheduleTable;
    @FXML
    private TableColumn<Appointment, Integer> appointmentIdColumn;
    @FXML
    private TableColumn<Appointment, String> appointmentTitleColumn;
    @FXML
    private TableColumn<Appointment, String> appointmentDescriptionColumn;
    @FXML
    private TableColumn<Appointment, String> appointmentTypeColumn;
    @FXML
    private TableColumn<Appointment, java.util.Date> startDateColumn;
    @FXML
    private TableColumn<Appointment, java.util.Date> endDateColumn;
    @FXML
    private TableColumn<Appointment, Integer> customerIdColumn;
    @FXML
    private TableColumn<Appointment, Time> startTimeColumn;
    @FXML
    private TableColumn<Appointment, Time> endTimeColumn;
    @FXML
    private ComboBox<Contact> contactsComboBox;
    private ObservableList<Contact> contactsList = DatabaseAccess.getAllContacts();
    private static ObservableList<Appointment> contactAppointmentsList = FXCollections.observableArrayList();
    private int selectedContactId;


    /**
     * Method will fill the schedule table with the appointment information of a selected contact.
     * @param actionEvent
     */
    public void fillScheduleTable(ActionEvent actionEvent) {
        try {
            selectedContactId = contactsComboBox.getSelectionModel().getSelectedItem().getContactId();

            scheduleTable.setItems(getAllAppointments(selectedContactId));
            appointmentIdColumn.setCellValueFactory((new PropertyValueFactory<>("appointmentId")));
            appointmentTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
            appointmentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
            appointmentDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
            startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
            endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
            customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
            startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
            endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        }catch(Exception e){
        }

    }

    /**
     * Method will fill a list with all appointments of a given contact.
     *
     * This lambda goes through appointment list and filters by the contact ID to add it to a list of
     * contacts.
     *
     * @param selectedContactId - the contact whos appointments are being looked for
     * @return - list of contact associated appointments
     */
    public static ObservableList<Appointment> getAllAppointments(int selectedContactId){
        ObservableList<Appointment> aList = DatabaseAccess.getAllAppointments();
        contactAppointmentsList = aList.filtered(a -> {
            if(a.getContactId() == selectedContactId){
                return true;
            }
            return false;
        });
        return contactAppointmentsList;
    }

    /**
     * Method closes the current window when the exit button is pressed.
     * @param actionEvent
     * @throws IOException
     */
    public void exitButtonPressed(ActionEvent actionEvent) throws IOException {
        ((Stage)(((Button)actionEvent.getSource()).getScene().getWindow())).close();
    }

    /**
     * Method initializes the contact schedule screen.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        contactsComboBox.setItems(contactsList);
    }
}