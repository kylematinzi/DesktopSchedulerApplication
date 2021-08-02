package DesktopScheduler;

import DesktopScheduler.Model.*;
import DesktopScheduler.Utility.DatabaseConnection;
import DesktopScheduler.Utility.DatabaseQuery;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This class is the controller of the all customer screen used to add functionality to the all customer screen. This
 * class is where all customers in the database may be viewed, updated, and/or deleted.
 *
 * @author Kyle Matinzi
 * @version Version 1.0
 */
public class AllCustomersScreen implements Initializable {

    @FXML
    private TextField customerIdTextBox;
    @FXML
    private TextField customerNameTextBox;
    @FXML
    private TextField customerPhoneNumberTextBox;
    @FXML
    private TextField customerAddressTextBox;
    @FXML
    private TextField customerPostCodeTextBox;
    @FXML
    private ComboBox<Country> countryComboBox;
    @FXML
    private ComboBox<Divisions> stateComboBox;
    @FXML
    private TableView<Customer> allCustomersTableView;
    @FXML
    private TableColumn<Customer,Integer> customerIdColumn;
    @FXML
    private TableColumn<Customer,String> customerNameColumn;
    @FXML
    private TableColumn<Customer,String> phoneNumberColumn;
    @FXML
    private TableColumn<Customer,String> customerAddressColumn;
    @FXML
    private TableColumn<Customer,String> postalCodeColumn;
    @FXML
    private TableColumn<Customer, Integer> divisionIdColumn;
    private static int customerId;
    private static int customerDivisionId;
    private static String customerName;
    private static String customerPhoneNumber;
    private static String customerAddress;
    private static String customerPostalCode;
    private ObservableList<Country> countryList = DatabaseAccess.getAllCountries();

    /**
     * Method populates corresponding fields when a user clicks on a table row.
     */
    public void tableClicked(){
        customerIdTextBox.setText(String.valueOf(allCustomersTableView.getSelectionModel().getSelectedItem().getCustomerID()));
        customerNameTextBox.setText(allCustomersTableView.getSelectionModel().getSelectedItem().getCustomerName());
        customerPhoneNumberTextBox.setText(allCustomersTableView.getSelectionModel().getSelectedItem().getPhoneNumber());
        customerAddressTextBox.setText(allCustomersTableView.getSelectionModel().getSelectedItem().getAddress());
        customerPostCodeTextBox.setText(allCustomersTableView.getSelectionModel().getSelectedItem().getPostalCode());
        int divisionId = allCustomersTableView.getSelectionModel().getSelectedItem().getDivisionId();
        int countryId = allCustomersTableView.getSelectionModel().getSelectedItem().getCountryId();
        for(Country c : countryComboBox.getItems()){
            if (c.getCountryId() == countryId){
                countryComboBox.setValue(c);
                break;
            }
        }
        for (Divisions d : stateComboBox.getItems()){
            if(d.getDivisionId() == divisionId){
                stateComboBox.setValue(d);
                break;
            }
        }
    }

    /**
     * Method populates corresponding fields when a user navigates to a given table row via arrow keys.
     */
    public void tableArrowSelection(){
        customerIdTextBox.setText(String.valueOf(allCustomersTableView.getSelectionModel().getSelectedItem().getCustomerID()));
        customerNameTextBox.setText(allCustomersTableView.getSelectionModel().getSelectedItem().getCustomerName());
        customerPhoneNumberTextBox.setText(allCustomersTableView.getSelectionModel().getSelectedItem().getPhoneNumber());
        customerAddressTextBox.setText(allCustomersTableView.getSelectionModel().getSelectedItem().getAddress());
        customerPostCodeTextBox.setText(allCustomersTableView.getSelectionModel().getSelectedItem().getPostalCode());
        int divisionId = allCustomersTableView.getSelectionModel().getSelectedItem().getDivisionId();
        int countryId = allCustomersTableView.getSelectionModel().getSelectedItem().getCountryId();
        for(Country c : countryComboBox.getItems()){
            if (c.getCountryId() == countryId){
                countryComboBox.setValue(c);
                break;
            }
        }
        for (Divisions d : stateComboBox.getItems()){
            if(d.getDivisionId() == divisionId){
                stateComboBox.setValue(d);
                break;
            }
        }
    }

    /**
     * Method will update a selected customers data via the corresponding field contents when the save button is pressed.
     * @throws SQLException
     */
    public void saveChangesButtonPressed() throws SQLException {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String updateStatement = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Division_ID = ? WHERE Customer_ID = ? ";
            DatabaseQuery.setPreparedStatement(conn, updateStatement);
            Customer updatedCustomer = allCustomersTableView.getSelectionModel().getSelectedItem();
            customerId = updatedCustomer.getCustomerID();
            customerName = customerNameTextBox.getText();
            customerPhoneNumber = customerPhoneNumberTextBox.getText();
            customerAddress = customerAddressTextBox.getText();
            customerPostalCode = customerPostCodeTextBox.getText();
            customerDivisionId = stateComboBox.getSelectionModel().getSelectedItem().getDivisionId();
            if (customerName.isEmpty() || customerPhoneNumber.isEmpty() || customerAddress.isEmpty() || customerPostalCode.isEmpty() ||
                    stateComboBox.getSelectionModel().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please ensure all fields are filled.");
                alert.setTitle("Empty Entry");
                alert.showAndWait();
            } else {
                PreparedStatement ps = DatabaseQuery.getPreparedStatement();
                ps.setString(1, customerName);
                ps.setString(2, customerAddress);
                ps.setString(3, customerPostalCode);
                ps.setString(4, customerPhoneNumber);
                ps.setInt(5, customerDivisionId);
                ps.setString(6, String.valueOf(customerId));
                ps.execute();
                allCustomersTableView.setItems(DatabaseAccess.getAllCustomers());
            }
        }catch (NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please ensure all fields are filled.");
            alert.setTitle("Empty Entry");
            alert.showAndWait();
        }
    }

    /**
     * Method will delete a selected customer from the given database.
     * @throws SQLException
     */
    public void deleteCustomerButtonPressed() throws SQLException {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String deleteStatement = "DELETE FROM customers WHERE Customer_ID = ? ";
            DatabaseQuery.setPreparedStatement(conn, deleteStatement);
            Customer deletingCustomer = allCustomersTableView.getSelectionModel().getSelectedItem();
            customerId = deletingCustomer.getCustomerID();
            customerName = deletingCustomer.getCustomerName();
            PreparedStatement ps = DatabaseQuery.getPreparedStatement();
            if (allCustomersTableView.getSelectionModel().getSelectedItem() == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "No Customer Selected for deletion.");
                alert.setTitle("Empty Selection");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You are about to delete all customer information for: " + "\n" + "Customer ID: " +
                        customerId + "\n" + "Customer Name: " + customerName);
                alert.setTitle("Confirmation");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    deleteAssociatedAppointments(customerId);
                    ps.setInt(1, customerId);
                    ps.execute();
                }
            }
            allCustomersTableView.setItems(DatabaseAccess.getAllCustomers());
        }catch (NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.WARNING, "No Customer Selected for deletion.");
            alert.setTitle("Empty Selection");
            alert.showAndWait();
        }
    }

    /**
     * Method will set the items in the state combo box based on the selected country.
     * @param actionEvent
     */
    public void onCountrySelect(ActionEvent actionEvent) {
        Country c = countryComboBox.getValue();
        if(c == null){
            return;
        }
        stateComboBox.setItems(DatabaseAccess.getSelectedStates(c.getCountryId()));
    }

    /**
     * Method will delete appointments associated with a given customer id.
     * @param customerId - given customer id
     * @throws SQLException
     */
    public void deleteAssociatedAppointments(int customerId) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        String deleteStatement = "DELETE FROM appointments WHERE Customer_ID = ?";
        DatabaseQuery.setPreparedStatement(conn, deleteStatement);
        PreparedStatement ps = DatabaseQuery.getPreparedStatement();
        ps.setInt(1, customerId);
        ps.execute();
    }

    /**
     * Method will close the current window when the exit button is pressed.
     * @param actionEvent - exit button pressed
     * @throws IOException
     */
    public void exitButtonPressed(ActionEvent actionEvent) throws IOException {
        ((Stage)(((Button)actionEvent.getSource()).getScene().getWindow())).close();
    }

    /**
     * Method initializes the all customers screen.
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        countryComboBox.setItems(countryList);
        allCustomersTableView.setItems(DatabaseAccess.getAllCustomers());
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("CustomerName"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        customerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        postalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        divisionIdColumn.setCellValueFactory(new PropertyValueFactory<>("divisionId"));
    }
}