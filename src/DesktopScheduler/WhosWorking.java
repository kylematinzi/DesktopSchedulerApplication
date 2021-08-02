package DesktopScheduler;

import DesktopScheduler.Model.DatabaseAccess;
import DesktopScheduler.Model.User;
import DesktopScheduler.Utility.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * This class is the controller of the whos working screen. This class will display a report that shows a breakdown of
 * how many appointments each user is scheduling and what percent total that accounts for.
 *
 * @author Kyle Matinzi
 * @version Version 1.0
 */
public class WhosWorking implements Initializable {

    @FXML
    private PieChart userDataPieChart;
    private static String userName;
    private static int userId;
    private static int count;
    private static int totalAppointments;
    private static String percentageTotal;
    private static ObservableList<User> userList = DatabaseAccess.getAllUsers();
    private static ObservableList<PieChart.Data> userPieData = FXCollections.observableArrayList();

    /**
     * Method produces a list with pie chart data consisting of users and total amount of appointments scheduled per user.
     * @return - list of users and number of appointments
     * @throws SQLException
     */
    public static ObservableList<PieChart.Data> fillUserPieChart() throws SQLException {
        userPieData.clear();
        totalAppointments = getTotalAppointments();
        for(User u: userList){
            userId = u.getUserId();
            String sqlCommand = "SELECT COUNT(*) from appointments where User_ID = ? ";
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sqlCommand);
            ps.setInt(1, userId);
            ResultSet results = ps.executeQuery();
            while(results.next()){
                userName = u.getUserName();
                count = results.getInt("Count(*)");
            }
            percentageTotal = count+" , "+(count *100.0f)/totalAppointments;
            userPieData.add(new PieChart.Data(userName+" "+percentageTotal+"%",(count *100.0f)/totalAppointments));
        }
        return userPieData;
    }

    /**
     * Method finds how many total appointments are stored in the database.
     * @return - total number of appointments
     * @throws SQLException
     */
    public static int getTotalAppointments() throws SQLException {
        String sqlCommand = "SELECT COUNT(*) from appointments";
        PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sqlCommand);
        ResultSet results = ps.executeQuery();
        while (results.next()) {
            totalAppointments = results.getInt("Count(*)");
        }
        return totalAppointments;
    }

    /**
     * Method will close current window when the exit button is pressed.
     * @param actionEvent
     * @throws IOException
     */
    public void exitButtonPressed(ActionEvent actionEvent) throws IOException {
        ((Stage)(((Button)actionEvent.getSource()).getScene().getWindow())).close();
    }

    /**
     * Method initializes the who's working report screen.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            userDataPieChart.setData(fillUserPieChart());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}