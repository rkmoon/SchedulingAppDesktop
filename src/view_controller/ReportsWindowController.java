package view_controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * This class controls the window and opens the report window according to the button pressed.
 */
public class ReportsWindowController {

    @FXML
    public void openCustomerAppointments() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("CustomersAppointmentsWindow.fxml")));

        Scene scene = new Scene(root);
        stage.setTitle("Customer Appointments");
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void openContactSchedule() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ContactScheduleWindow.fxml")));

        Scene scene = new Scene(root);
        stage.setTitle("Contact Schedule");
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void openCustomersPerCountry() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("CustomersPerCountryWindow.fxml")));

        Scene scene = new Scene(root);
        stage.setTitle("Customers Per Country");
        stage.setScene(scene);
        stage.show();
    }
}
