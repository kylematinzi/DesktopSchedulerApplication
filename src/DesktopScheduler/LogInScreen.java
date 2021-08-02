package DesktopScheduler;

import DesktopScheduler.Utility.DatabaseConnection;
import DesktopScheduler.Utility.DatabaseQuery;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This class is the controller of the login screen. This is the first screen a user will see when opening the application.
 * This class is used to add new users, verify login credentials and track login attempts.
 *
 * @author Kyle Matinzi
 * @version Version 1.0
 */
public class LogInScreen implements Initializable {

    @FXML
    private Button signInButton;
    @FXML
    private Button newUserButton;
    @FXML
    private TextField usernameTextBox;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label locationLabel;
    @FXML
    private Label locationTitleLabel;
    @FXML
    private Label userNameTitleLabel;
    @FXML
    private Label passwordTitleLabel;
    @FXML
    private Label loginLabel;
    private String userName;
    private String passWord;
    private boolean usernamePasswordFound = false;
    private Statement stmt = null;
    private ZoneId userLocation;
    private LocalDateTime loginDateTime;
    private String dateTimeFormat = "yyyy-MM-dd, HH:mm:ss";
    private static int userIdCheck;
    private static LocalTime currentTime;
    private static LocalTime startTime;
    private static long timeDifference;
    private static boolean timeFlag = false;
    private int alertAppointmentId;
    private LocalDate alertStartDate;
    private LocalTime alertStartTime;

    /**
     * Method will verify username and password of inputted information and open the the home screen if the verification
     * passes.
     * @param actionEvent
     * @throws IOException
     * @throws SQLException
     */
    public void signInButtonPressed(ActionEvent actionEvent) throws IOException, SQLException {
        Connection conn = DatabaseConnection.getConnection();
        stmt = conn.createStatement();
        userName = usernameTextBox.getText();
        passWord = passwordField.getText();
        String sqlfinder = "SELECT User_Name, Password, User_ID FROM users";
        ResultSet rs = stmt.executeQuery(sqlfinder);
        while(rs.next()){
            String UserName = rs.getString("User_Name");
            String PassWord = rs.getString("Password");
            if(UserName.equals(userName) && PassWord.equals(passWord)){
                userIdCheck = rs.getInt("user_ID");
                usernamePasswordFound = true;
            }
        }
        rs.close();
        PreparedStatement ps = DatabaseQuery.getPreparedStatement();
        if(usernamePasswordFound == true){
            //15 minute alert
            appointmentTimeAlert(userIdCheck);
            StackPane loginScreenParent = new StackPane();
            loginScreenParent.getChildren().add(FXMLLoader.load(getClass().getResource("ViewsControllers/HomeScreen.fxml")));
            Scene scene = new Scene(loginScreenParent);
            Stage loginScreenScene = new Stage();
            loginScreenScene.setScene(scene);
            loginScreenScene.initModality(Modality.WINDOW_MODAL);
            loginScreenScene.initOwner(((((Button) actionEvent.getSource()).getScene().getWindow())));
            loginScreenScene.sizeToScene();
            loginScreenScene.setResizable(false);
            loginScreenScene.setTitle("Desktop Scheduler");
            loginScreenScene.show();
            loginDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimeFormat);
            String formatDateTime = loginDateTime.format(formatter);
            String fileName = "login_activity.txt";
            FileWriter writer = new FileWriter(fileName, true);
            writer.write("User Name: "+userName+", " + formatDateTime+", "+ "Attempt: Pass\n");
            writer.close();
            usernamePasswordFound = false;
        }
        else{
            loginDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimeFormat);
            String formatDateTime = loginDateTime.format(formatter);
            String fileName = "login_activity.txt";
            FileWriter writer = new FileWriter(fileName, true);
            writer.write("User Name: "+userName+", " + formatDateTime+", "+ "Attempt: Fail\n");
            writer.close();
            if(Locale.getDefault().getLanguage().equals("fr")) {
                Alert alert = new Alert(Alert.AlertType.WARNING,"Tentative de connexion incorrecte");
                alert.setTitle("Avertissement");
                Optional<ButtonType> result = alert.showAndWait();
            }
            else{
                Alert alert = new Alert(Alert.AlertType.WARNING, "Incorrect Username Password Combination. Please try again.");
                alert.setTitle("Warning");
                Optional<ButtonType> result = alert.showAndWait();
            }
        }
    }

    /**
     * Method opens the create new user screen when the create new user button is pressed.
     * @param actionEvent
     * @throws IOException
     */
    public void newUserButtonPressed(ActionEvent actionEvent) throws IOException {
        StackPane loginScreenParent = new StackPane();
        loginScreenParent.getChildren().add(FXMLLoader.load(getClass().getResource("ViewsControllers/NewUserScreen.fxml")));
        Scene scene = new Scene(loginScreenParent);
        Stage loginScreenScene = new Stage();
        loginScreenScene.setScene(scene);
        loginScreenScene.initModality(Modality.WINDOW_MODAL);
        loginScreenScene.initOwner(((((Button)actionEvent.getSource()).getScene().getWindow())));
        loginScreenScene.sizeToScene();
        loginScreenScene.setResizable(false);
        loginScreenScene.setTitle("Desktop Scheduler");
        loginScreenScene.show();
    }

    /**
     * Method checks to see if the user signing into the appointment has any upcoming appointments in the next 15 minutes.
     * If the user does it displays a message notifying the user of the given appointment. If the user does not it will
     * notify the user that they have no upcoming appointments.
     * @param userId - user to check for appointments
     * @throws SQLException
     */
    public void appointmentTimeAlert(int userId) throws SQLException {
        String sqlCommand = "SELECT * from appointments where User_ID = ? ";
        PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sqlCommand);
        ps.setInt(1,userId);
        ResultSet results = ps.executeQuery();
        while(results.next()){
            startTime = results.getTimestamp("start").toLocalDateTime().toLocalTime();
            currentTime = LocalTime.now();
            timeDifference = ChronoUnit.MINUTES.between(currentTime, startTime);
            if (timeDifference >0 && timeDifference <= 15 ){
                 alertAppointmentId = results.getInt("Appointment_ID");
                 alertStartDate = results.getTimestamp("Start").toLocalDateTime().toLocalDate();
                 alertStartTime = results.getTimestamp("Start").toLocalDateTime().toLocalTime();
                 timeFlag = true;
            }
        }
        if(Locale.getDefault().getLanguage().equals("fr")) {
            if (timeFlag == true) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Rendez-vous en 15 minutes!" + "\n" +
                        "Identifiant de rendez-vous: " + alertAppointmentId + "\n" +
                        "Date de rendez-vous: " + alertStartDate + "\n" +
                        "Heure de rendez-vous: " + alertStartTime);
                ;
                alert.setTitle("Avis de rendez-vous");
                Optional<ButtonType> result = alert.showAndWait();
                timeFlag = false;
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Pas de rendez-vous dans les 15 minutes.");
                alert.setTitle("Avis de rendez-vous");
                Optional<ButtonType> result = alert.showAndWait();
            }
        }
        else {
            if (timeFlag == true) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "You have an appointment within 15 minutes." + "\n" +
                        "Appointment ID: " + alertAppointmentId + "\n" +
                        "Appointment Date: " + alertStartDate + "\n" +
                        "Appointment Time: " + alertStartTime);
                ;
                alert.setTitle("Appointment Notice");
                Optional<ButtonType> result = alert.showAndWait();
                timeFlag = false;
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "You have no appointments within 15 minutes.");
                alert.setTitle("Appointment Notice");
                Optional<ButtonType> result = alert.showAndWait();

            }
        }
    }

    /**
     * Method initializes the login screen.
     * @param location
     * @param resources
     */
    public void initialize(URL location, ResourceBundle resources){
        userLocation = ZoneId.systemDefault();
        locationLabel.setText(userLocation.toString());
        if(Locale.getDefault().getLanguage().equals("fr")){
            loginLabel.setText("Connexion");
            locationTitleLabel.setText("Emplacement:");
            userNameTitleLabel.setText("Nom d'utilisateur: ");
            passwordTitleLabel.setText("Mot de passe: ");
            usernameTextBox.setPromptText("Nom d'utilisateur");
            passwordField.setPromptText("Mot de passe");
            signInButton.setText("S'identifier");
            newUserButton.setText("Créer un nouvel utilisateur");
        }
    }
}
