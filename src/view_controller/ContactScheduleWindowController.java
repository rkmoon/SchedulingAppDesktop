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

/**
 * This class controls the Contact Schedule Report window. It displays all appointments for a contact, with a drop down
 * box to select different contacts.
 */
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

    /**
     * Initializes the window by filling the contact combo box with a list of all contacts to choose from
     * @throws SQLException error with the DB
     */
    public void initialize() throws SQLException {
        fillContactComboBox();
    }

    /**
     * Fills the contact combo box with all contacts
     * @throws SQLException error with the DB
     */
    private void fillContactComboBox() throws SQLException {
        contactBox.setItems(ContactDAO.getAllContacts());

    }

    /**
     * Changes the contents of the table based on the contact selected in the combobox
     * @throws SQLException
     */
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

    /**
     * Closes the window when the close button is clicked
     */
    @FXML
    public void closeWindow(){
        Stage stage = (Stage) contactBox.getScene().getWindow();
        stage.close();
    }

    /**
     * Fills the table with all appointments that a contact has. The lambda expression is used to get all appointments
     * with the same contact ID. The lambda expression used here is for checking each appointment's contact ID against
     * the contact ID of the contact selected in the combobox and adding those to the list to fill the table.
     * @throws SQLException error with the DB
     */
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

    /**
     * Clears the table of all items
     */
    private void clearTable(){
        scheduleTable.getItems().clear();
    }
}
