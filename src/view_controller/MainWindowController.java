package view_controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MainWindowController {

    @FXML
    private Button customerButton;

    @FXML
    private Button appointmentButton;


    @FXML
    public void goToCustomers() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("CustomerWindow.fxml"));

        Scene scene = new Scene(root);
        stage.setTitle("Customers");
        stage.setScene(scene);
        stage.show();
        closeWindow();
    }

    @FXML
    public void goToAppointments() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("AppointmentWindow.fxml"));

        Scene scene = new Scene(root);
        stage.setTitle("Appointments");
        stage.setScene(scene);
        stage.show();
        closeWindow();
    }

    private void closeWindow(){
        Stage stage = (Stage) customerButton.getScene().getWindow();
        stage.close();
    }

}
