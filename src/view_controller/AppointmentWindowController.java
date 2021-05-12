package view_controller;

import DAO.AppointmentDAO;
import DAO.ContactDAO;
import DAO.CustomerDAO;
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
import utils.LoggedInUser;

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
    private TextField typeText;

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
    void onConfirmButton() throws SQLException {
        if(checkFields()) {
            AppointmentDAO.insertAppointment(createAppointment());
        }
        else{
            System.out.println("Not all fields filled");
            //ADD POPUP WINDOW HERE
        }
    }

    private void closeWindow(){
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
    }

    private void fillComboBoxes() throws SQLException {
        fillContactBox();
        fillCustomerBox();
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
        newAppointment.setType(typeText.getText());
        newAppointment.setContactId(contactBox.getValue().getContactID());
        newAppointment.setStart(dateAndTimeToTimestamp(startDate,startHourTime,startMinuteTime));
        newAppointment.setEnd(dateAndTimeToTimestamp(endDate,endHourTime,endMinuteTime));
        newAppointment.setCustId(customerBox.getValue().getId());
        newAppointment.setUserId(LoggedInUser.getLoggedIn().getUserID());
        return newAppointment;
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

    private boolean checkFields(){
        boolean fieldsFilled = !appIdText.getText().isEmpty() && !titleText.getText().isEmpty() && !descriptionText.getText().isEmpty() &&
                !locationText.getText().isEmpty() && !typeText.getText().isEmpty() && contactBox.getValue() != null &&
                startDate.getValue() != null && !startHourTime.getValue().isEmpty() &&
                !startMinuteTime.getValue().isEmpty() && endDate.getValue() != null &&
                !endHourTime.getValue().isEmpty() && !endMinuteTime.getValue().isEmpty() && customerBox.getValue() != null;

        return fieldsFilled;
    }

    private Timestamp dateAndTimeToTimestamp(DatePicker date, ComboBox<String> hourBox, ComboBox<String> minuteBox){
        Timestamp timestamp = null;
        int year = date.getValue().getYear();
        Month month = date.getValue().getMonth();
        int day = date.getValue().getDayOfMonth();
        int hour = Integer.parseInt(hourBox.getValue());
        int minute = Integer.parseInt(minuteBox.getValue());
        LocalDateTime startTime = LocalDateTime.of(year, month, day, hour, minute);
        timestamp = Timestamp.valueOf(startTime);
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
