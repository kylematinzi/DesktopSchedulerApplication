package DesktopScheduler;

import DesktopScheduler.Utility.DatabaseConnection;
import DesktopScheduler.Utility.DatabaseQuery;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * This is the main method used to run the program.
 *
 * @author Kyle Matinzi
 * @version 1.0
 *
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        StackPane stackRoot = new StackPane();
        stackRoot.getChildren().add(FXMLLoader.load(getClass().getResource("ViewsControllers/LogInScreen.fxml")));
        Scene scene = new Scene(stackRoot);
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.setResizable(false);
        primaryStage.setTitle("Desktop Scheduler");
        primaryStage.show();
    }

    public static void main(String[] args) throws SQLException {
        DatabaseConnection.startConnection();
        Connection conn = DatabaseConnection.getConnection();
        String insertStatement = "INSERT INTO users (User_Name, Password, Created_By,Last_Updated_By) Values(?,?,?,?)";
        DatabaseQuery.setPreparedStatement(conn, insertStatement);
        launch(args);
        DatabaseConnection.closeConnection();
    }
}
