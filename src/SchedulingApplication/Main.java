package SchedulingApplication;

import DAO.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("../view_controller/LogInWindow.fxml"));
        primaryStage.setTitle("SchedulingApp");
        //primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) throws SQLException {
        DBConnection.startConnection();
        AppointmentDAO.getAllAppointments();
        ContactDAO.getAllContacts();
        CountryDAO.getAllCountries();
        CustomerDAO.getAllCustomers();
        FLDivisionDAO.getAllFLDivisions();
        UserDAO.getAllUsers();

        launch(args);
    }
}
