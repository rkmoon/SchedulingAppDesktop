package SchedulingApplication;

import DAO.DBConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main class. Starts the program, opens the login window, and opens a database connection.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("../view_controller/LogInWindow.fxml"));
        primaryStage.setTitle("SchedulingApp");
        //primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) throws Exception {
        DBConnection.startConnection();
        System.out.println(com.sun.javafx.runtime.VersionInfo.getRuntimeVersion());
        launch(args);
    }
}
