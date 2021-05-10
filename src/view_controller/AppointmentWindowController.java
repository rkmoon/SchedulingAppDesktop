package view_controller;

import DAO.AppointmentDAO;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;



public class AppointmentWindowController {

    @FXML
    private TableView<Appointment> appTable;

    @FXML
    private TableColumn<Appointment, Integer> appIDCol;

    @FXML
    private TableColumn<Appointment, String> appTitleCol;

    @FXML
    private TableColumn<Appointment, String> appDescCol;

    @FXML
    private TableColumn<Appointment, String> appLocCol;

    @FXML
    private TableColumn<Appointment, String> appContCol;

    @FXML
    private TableColumn<Appointment, String> appTypeCol;

    @FXML
    private TableColumn<Appointment, Date> appStartCol;

    @FXML
    private TableColumn<Appointment, Date> appEndCol;

    @FXML
    private TableColumn<Appointment, Integer> appCustIdCol;

    @FXML
    private Button addAppButton;

    @FXML
    private Button updateAppButton;

    @FXML
    private Button deleteAppButton;

    @FXML
    private Button custButton;


    public void initialize() throws SQLException {
        populateAppTable();
    }


    private void populateAppTable() throws SQLException {
        FilteredList<Appointment> appointments = new FilteredList<>(AppointmentDAO.getAllAppointments());
        appIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        appTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        appDescCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        appLocCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        appContCol.setCellValueFactory(new PropertyValueFactory<>("contactId"));
        appTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        appStartCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        appEndCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        appCustIdCol.setCellValueFactory(new PropertyValueFactory<>("custId"));

        appTable.setItems(appointments);
    }

    public void goToCustomers() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("CustomerWindow.fxml"));

        Scene scene = new Scene(root);
        stage.setTitle("Customers");
        stage.setScene(scene);
        stage.show();
        closeWindow();
    }

    private void closeWindow(){
        Stage stage = (Stage) custButton.getScene().getWindow();
        stage.close();
    }
}
