package view_controller;

import DAO.AppointmentDAO;
import DAO.ContactDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.Contact;

import java.sql.SQLException;

public class ContactScheduleWindowController {

    @FXML
    private ComboBox<Contact> contactBox;

    @FXML
    private TableView<Appointment> scheduleTable;

    @FXML
    private TableColumn<Appointment, Integer> appIdCol;

    @FXML
    private TableColumn<Appointment, String> titleCol;

    @FXML
    private TableColumn<Appointment, String> typeCol;

    @FXML
    private TableColumn<Appointment, String> descCol;

    @FXML
    private TableColumn<Appointment, String> startCol;

    @FXML
    private TableColumn<Appointment, String> endCol;

    @FXML
    private TableColumn<Appointment, Integer> custIDCol;

    private Contact contactSelected;


    public void initialize() throws SQLException {
        fillContactComboBox();
    }

    private void fillContactComboBox() throws SQLException {
        contactBox.setItems(ContactDAO.getAllContacts());

    }

    @FXML
    public void onContactSelected() throws SQLException {
        if(contactBox.getValue()== null){
            clearTable();
        }
        else{
            contactSelected = contactBox.getValue();
            fillTable();
        }
    }

    @FXML
    public void closeWindow(){
        Stage stage = (Stage) contactBox.getScene().getWindow();
        stage.close();
    }

    private void fillTable() throws SQLException {
        ObservableList<Appointment> appointments = AppointmentDAO.getAllAppointments();
        ObservableList<Appointment> appointmentsToAdd = FXCollections.observableArrayList();
        appointments.forEach(appointment -> {
            if(appointment.getContactId() == contactSelected.getContactID()){
                appointmentsToAdd.add(appointment);
            }
        });

        appIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        custIDCol.setCellValueFactory(new PropertyValueFactory<>("custId"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("formattedLocalStartTime"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("formattedLocalEndTime"));
        scheduleTable.setItems(appointmentsToAdd);

    }

    private void clearTable(){
        scheduleTable.getItems().clear();
    }
}
