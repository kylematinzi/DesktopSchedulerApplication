package DesktopScheduler;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class is the controller of the home screen. The home screen is the main screen for the application and is where
 * a user may access the majority of other screens.
 *
 * @author Kyle Matinzi
 * @version Version 1.0
 */
public class HomeScreen implements Initializable {

    /**
     * Method opens the view all customer screen when the view all customers button is pressed.
     * @param actionEvent
     * @throws IOException
     */
    public void viewAllCustomersButtonPressed(ActionEvent actionEvent) throws IOException {
        StackPane viewAllCustomerParent = new StackPane();
        viewAllCustomerParent.getChildren().add(FXMLLoader.load(getClass().getResource("ViewsControllers/AllCustomersScreen.fxml")));
        Scene scene = new Scene(viewAllCustomerParent);
        Stage viewAllCustomerScene = new Stage();
        viewAllCustomerScene.setScene(scene);
        viewAllCustomerScene.initModality(Modality.WINDOW_MODAL);
        viewAllCustomerScene.initOwner(((((Button)actionEvent.getSource()).getScene().getWindow())));
        viewAllCustomerScene.sizeToScene();
        viewAllCustomerScene.setResizable(false);
        viewAllCustomerScene.setTitle("Desktop Scheduler");
        viewAllCustomerScene.show();
    }

    /**
     * Method opens the the create new customer screen when the add customer button is pressed.
     * @param actionEvent
     * @throws IOException
     */
    public void createNewCustomerButtonPressed(ActionEvent actionEvent) throws IOException {
        StackPane createNewCustomerParent = new StackPane();
        createNewCustomerParent.getChildren().add(FXMLLoader.load(getClass().getResource("ViewsControllers/AddCustomerScreen.fxml")));
        Scene scene = new Scene(createNewCustomerParent);
        Stage createNewCustomerScene = new Stage();
        createNewCustomerScene.setScene(scene);
        createNewCustomerScene.initModality(Modality.WINDOW_MODAL);
        createNewCustomerScene.initOwner(((((Button)actionEvent.getSource()).getScene().getWindow())));
        createNewCustomerScene.sizeToScene();
        createNewCustomerScene.setResizable(false);
        createNewCustomerScene.setTitle("Desktop Scheduler");
        createNewCustomerScene.show();
    }

    /**
     * Method opens the view all appointment screen when the view all appointments button is pressed.
     * @param actionEvent
     * @throws IOException
     */
    public void viewAllAppointmentsButtonPressed(ActionEvent actionEvent) throws IOException {
        StackPane viewAllApptParent = new StackPane();
        viewAllApptParent.getChildren().add(FXMLLoader.load(getClass().getResource("ViewsControllers/AllAppointmentScreen.fxml")));
        Scene scene = new Scene(viewAllApptParent);
        Stage viewAllApptScene = new Stage();
        viewAllApptScene.setScene(scene);
        viewAllApptScene.initModality(Modality.WINDOW_MODAL);
        viewAllApptScene.initOwner(((((Button)actionEvent.getSource()).getScene().getWindow())));
        viewAllApptScene.sizeToScene();
        viewAllApptScene.setResizable(false);
        viewAllApptScene.setTitle("Desktop Scheduler");
        viewAllApptScene.show();
    }

    /**
     * Method opens the create new appointment screen when the add appointment button is pressed.
     * @param actionEvent
     * @throws IOException
     */
    public void createNewAppointmentButtonPressed(ActionEvent actionEvent) throws IOException {
        StackPane createNewApptParent = new StackPane();
        createNewApptParent.getChildren().add(FXMLLoader.load(getClass().getResource("ViewsControllers/AddAppointmentScreen.fxml")));
        Scene scene = new Scene(createNewApptParent);
        Stage createNewApptScene = new Stage();
        createNewApptScene.setScene(scene);
        createNewApptScene.initModality(Modality.WINDOW_MODAL);
        createNewApptScene.initOwner(((((Button)actionEvent.getSource()).getScene().getWindow())));
        createNewApptScene.sizeToScene();
        createNewApptScene.setResizable(false);
        createNewApptScene.setTitle("Desktop Scheduler");
        createNewApptScene.show();
    }

    /**
     * Method opens the view contact schedule button when the contact schedule button is pressed.
     * @param actionEvent
     * @throws IOException
     */
    public void viewScheduleButtonPressed(ActionEvent actionEvent) throws IOException {
        StackPane viewScheduleParent = new StackPane();
        viewScheduleParent.getChildren().add(FXMLLoader.load(getClass().getResource("ViewsControllers/ContactScheduleScreen.fxml")));
        Scene scene = new Scene(viewScheduleParent);
        Stage viewScheduleParentScene = new Stage();
        viewScheduleParentScene.setScene(scene);
        viewScheduleParentScene.initModality(Modality.WINDOW_MODAL);
        viewScheduleParentScene.initOwner(((((Button)actionEvent.getSource()).getScene().getWindow())));
        viewScheduleParentScene.sizeToScene();
        viewScheduleParentScene.setResizable(false);
        viewScheduleParentScene.setTitle("Desktop Scheduler");
        viewScheduleParentScene.show();
    }

    /**
     * Method opens the who's working report screen when the who's working button is pressed.
     * @param actionEvent
     * @throws IOException
     */
    public void whosWorkingButtonPressed(ActionEvent actionEvent) throws IOException {
        StackPane whosWorkingParent = new StackPane();
        whosWorkingParent.getChildren().add(FXMLLoader.load(getClass().getResource("ViewsControllers/WhosWorking.fxml")));
        Scene scene = new Scene(whosWorkingParent);
        Stage whosWorkingParentScene = new Stage();
        whosWorkingParentScene.setScene(scene);
        whosWorkingParentScene.initModality(Modality.WINDOW_MODAL);
        whosWorkingParentScene.initOwner(((((Button)actionEvent.getSource()).getScene().getWindow())));
        whosWorkingParentScene.sizeToScene();
        whosWorkingParentScene.setResizable(false);
        whosWorkingParentScene.setTitle("Desktop Scheduler");
        whosWorkingParentScene.show();
    }

    /**
     * Method will close the current window when the exit button is pressed.
     * @param actionEvent
     * @throws IOException
     */
    public void exitButtonPressed(ActionEvent actionEvent) throws IOException {
        ((Stage)(((Button)actionEvent.getSource()).getScene().getWindow())).close();
    }

    /**
     * Method initializes the homescreen window.
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

}
