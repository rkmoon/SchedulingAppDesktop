package view_controller;

import DAO.AppointmentDAO;
import DAO.ContactDAO;
import DAO.CustomerDAO;
import DAO.UserDAO;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Appointment;
import model.Contact;
import model.Customer;
import model.User;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.Month;

public class AppointmentWindowController {

    @FXML
    private TextField appIdText;

    @FXML
    private TextField titleText;

    @FXML
    private TextField descriptionText;

    @FXML
    private TextField locationText;

    @FXML
    private ComboBox<Contact> contactBox;

    @FXML
    private DatePicker startDate;

    @FXML
    private ComboBox<String> startHourTime;

    @FXML
    private ComboBox<String> startMinuteTime;

    @FXML
    private DatePicker endDate;

    @FXML
    private ComboBox<String> endHourTime;

    @FXML
    private ComboBox<String> endMinuteTime;

    @FXML
    private ComboBox<Customer> customerBox;

    @FXML
    private ComboBox<User> userBox;

    @FXML
    private Button confirmButton;

    @FXML
    private Button cancelButton;


    @FXML
    public void initialize() throws SQLException {
        fillComboBoxes();
        setNewAppointmentID();
    }
    @FXML
    void onCancelButton() {
        closeWindow();
    }

    @FXML
    void onConfirmButton() {

    }

    private void closeWindow(){
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
    }

    private void fillComboBoxes() throws SQLException {
        fillContactBox();
        fillCustomerBox();
        fillUserBox();
        fillHoursBox(startHourTime);
        fillHoursBox(endHourTime);
        fillMinutesBox(startMinuteTime);
        fillMinutesBox(endMinuteTime);
    }

    private Appointment createAppointment(){
        Appointment newAppointment = new Appointment();
        newAppointment.setId(Integer.parseInt(appIdText.getText()));
        newAppointment.setTitle(titleText.getText());
        newAppointment.setDescription(descriptionText.getText());
        newAppointment.setLocation(locationText.getText());
        newAppointment.setContactId(contactBox.getValue().getContactID());
        newAppointment.setStart(convertStartToTimestamp());
        newAppointment.setEnd(convertEndToTimestamp());
        newAppointment.setCustId(customerBox.getValue().getId());
        newAppointment.setUserId(userBox.getValue().getUserID());
        return newAppointment;
    }

    private void fillUserBox() throws SQLException {
        userBox.setItems(UserDAO.getAllUsers());
    }

    private void fillContactBox() throws SQLException {
        contactBox.setItems(ContactDAO.getAllContacts());
    }

    private void fillCustomerBox() throws SQLException {
        customerBox.setItems(CustomerDAO.getAllCustomers());
    }

    private void fillHoursBox(ComboBox<String> comboBox){
        for(int i = 0; i < 24; i++){
            comboBox.getItems().add(String.valueOf(i));
        }
    }

    private void fillMinutesBox(ComboBox<String> comboBox){
        for (int i = 0; i < 60; i += 15){
            comboBox.getItems().add(String.valueOf(i));
        }
    }

    private Timestamp convertStartToTimestamp(){
        Timestamp timestamp = null;
        LocalDateTime startTime = null;
        int year = startDate.getValue().getYear();
        Month month = startDate.getValue().getMonth();
        int day = startDate.getValue().getDayOfMonth();
        //LocalDateTime dateTime = startTime.of(year, month, day, startHourTime.getValue(), startMinuteTime.getValue());

        return timestamp;
    }

    private Timestamp convertEndToTimestamp(){
        Timestamp timestamp = null;
        return timestamp;
    }

    //lambda expression to sort appointment ids in descending order for highest ID+1 to make sure it is unique
    private void setNewAppointmentID() throws SQLException {
        ObservableList<Appointment> appointments = AppointmentDAO.getAllAppointments();
        appointments.sort(((o1, o2) -> (o2.getId() - o1.getId())));
        int newAppointmentID = appointments.get(0).getId() + 1;
        appIdText.setText(String.valueOf(newAppointmentID));
    }

}
