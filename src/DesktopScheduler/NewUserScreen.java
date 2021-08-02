package DesktopScheduler;

import DesktopScheduler.Utility.DatabaseQuery;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

/**
 * This class is the controller of the new user screen. This class is used to add a new user to the database.
 *
 * @author Kyle Matinzi
 * @version Version 1.0
 */
public class NewUserScreen {

    @FXML
    private TextField nameTextBox;
    @FXML
    private TextField usernameTextBox;
    @FXML
    private TextField passwordTextBox;
    private String name;
    private String username;
    private String password;

    /**
     * Method will close the current window when the exit button is pressed.
     * @param actionEvent
     * @throws IOException
     */
    public void cancelButtonPressed (ActionEvent actionEvent) throws IOException {
            ((Stage) (((Button) actionEvent.getSource()).getScene().getWindow())).close();
        }

    /**
     * Method will add a new user to the given database according to the user inputted information whe the create new
     * user button is pressed.
     * @param actionEvent
     * @throws SQLException
     */
    public void createNewUserButtonPressed (ActionEvent actionEvent) throws SQLException {
        name = nameTextBox.getText();
        username = usernameTextBox.getText();
        password = passwordTextBox.getText();
        if (name.isEmpty() || username.isEmpty() || password.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please ensure all fields are filled.");
            alert.setTitle("Warning");
            Optional<ButtonType> result = alert.showAndWait();
        }
        else {
            PreparedStatement ps = DatabaseQuery.getPreparedStatement();
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, name);
            ps.setString(4, name);
            ps.execute();
            ((Stage) (((Button) actionEvent.getSource()).getScene().getWindow())).close();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, username + " Has been added to the database.");
            alert.setTitle("Information");
            Optional<ButtonType> result = alert.showAndWait();
        }
    }
}
