package DesktopScheduler;

import DesktopScheduler.Model.Country;
import DesktopScheduler.Model.DatabaseAccess;
import DesktopScheduler.Model.Divisions;
import DesktopScheduler.Utility.DatabaseConnection;
import DesktopScheduler.Utility.DatabaseQuery;
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
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This class is the controller of the add customer screen used to add functionality to the add customer screen. This
 * class is used to add a new customer to a given database.
 *
 * @author Kyle Matinzi
 * @version Version 1.0
 */
public class AddCustomerScreen implements Initializable {

    @FXML
    private TextField customerNameBox;
    @FXML
    private TextField customerPhoneNumberBox;
    @FXML
    private TextField customerAddressBox;
    @FXML
    private TextField customerPostalCodeBox;
    @FXML
    private ComboBox<Country> countryComboBox;
    @FXML
    private ComboBox<Divisions> stateComboBox;
    private String customerName;
    private String customerPhoneNumber;
    private String customerAddress;
    private String customerPostalCode;
    private int customerDivisionId;
    private ObservableList<Country> countryList = DatabaseAccess.getAllCountries();

    /**
     * Method creates a new customer in a given database based on user input on new customer screen
     * @param actionEvent
     * @throws SQLException
     */
    public void saveNewCustomerButtonPressed(ActionEvent actionEvent) throws SQLException {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String insertStatement = "INSERT INTO customers(Customer_Name, Address, Postal_Code, Phone, Division_ID) VALUES (?,?,?,?,?)";
            DatabaseQuery.setPreparedStatement(conn, insertStatement);
            customerName = customerNameBox.getText();
            customerPhoneNumber = customerPhoneNumberBox.getText();
            customerAddress = customerAddressBox.getText();
            customerPostalCode = customerPostalCodeBox.getText();
            customerDivisionId = stateComboBox.getValue().getDivisionId();
            if (customerName.isEmpty() || customerPhoneNumber.isEmpty() || customerAddress.isEmpty() || customerPostalCode.isEmpty()
            || stateComboBox.getSelectionModel().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please ensure all fields are filled.");
                alert.setTitle("Warning");
                Optional<ButtonType> result = alert.showAndWait();
            } else {
                PreparedStatement ps = DatabaseQuery.getPreparedStatement();
                ps.setString(1, customerName);
                ps.setString(2, customerAddress);
                ps.setString(3, customerPostalCode);
                ps.setString(4, customerPhoneNumber);
                ps.setInt(5, customerDivisionId);
                ps.execute();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, customerName + " has been added to the roster.");
                alert.setTitle("Confirmation");
                Optional<ButtonType> result = alert.showAndWait();
                ((Stage) (((Button) actionEvent.getSource()).getScene().getWindow())).close();
            }
        }catch (NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please ensure all fields are filled.");
            alert.setTitle("Warning");
            Optional<ButtonType> result = alert.showAndWait();
        }
    }

    /**
     * Method will override string method format to increase readability.
     * @return - formatted country strings
     */
    @Override
    public String toString(){
        return countryList.toString();
    }

    /**
     * Method fills state combo box based on a selected country.
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
     * This method is used to close the current window when the exit button is pressed.
     * @param actionEvent
     * @throws IOException
     */
    public void exitButtonPressed(ActionEvent actionEvent) throws IOException {
        ((Stage)(((Button)actionEvent.getSource()).getScene().getWindow())).close();
    }

    /**
     * Method initializes the add customer screen.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        countryComboBox.setItems(countryList);
    }

}
