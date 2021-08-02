package DesktopScheduler;

import DesktopScheduler.Model.DatabaseAccess;
import DesktopScheduler.Utility.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * This class is the controller of the appointment report screen. This class is used to view a breakdown of how many of
 * a selected appointment type occur in a selected month.
 *
 * @author Kyle Matinzi
 * @version Version 1.0
 */
public class AppointmentReport implements Initializable {

    @FXML
    private Label appointmentTotalLabel;
    @FXML
    private ComboBox<String> monthComboBox;
    @FXML
    private ComboBox<String> typeComboBox;
    private String appointmentType;
    private String appointmentMonth;


    /**
     * Method will display the total amount of a selected type of appointment during a selected month.
     * @param actionEvent
     * @throws SQLException
     */
    public void findTotalButtonPressed(ActionEvent actionEvent) throws SQLException {
        try {
            appointmentType = typeComboBox.getValue();
            appointmentMonth = monthComboBox.getValue();
            String sqlCommand = "SELECT COUNT(*) from appointments where Type = ? AND month(Start) = ? ";
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sqlCommand);
            ps.setString(1, appointmentType);
            ps.setString(2, appointmentMonth);
            ResultSet results = ps.executeQuery();
            while (results.next()) {
                appointmentTotalLabel.setText(String.valueOf(results.getInt("Count(*)")));
            }
        }catch(Exception e){}
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
     * Method initializes the appointment report window.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        monthComboBox.setItems(DatabaseAccess.getMonths());
        typeComboBox.setItems(DatabaseAccess.getAppointmentTypes());

    }
}
