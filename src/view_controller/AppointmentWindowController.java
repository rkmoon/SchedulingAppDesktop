package view_controller;

import DAO.AppointmentDAO;
import DAO.ContactDAO;
import DAO.CustomerDAO;
import javafx.collections.FXCollections;
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
import utils.TimeUtilities;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.concurrent.atomic.AtomicBoolean;

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
    private DatePicker dateDate;

    @FXML
    private ComboBox<String> startHourTime;

    @FXML
    private ComboBox<String> startMinuteTime;

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

    private boolean isOpenedFromCustomer;
    private CustomerViewWindowController customerViewWindowController;
    private AppointmentViewWindowController appointmentViewWindowController;
    private Appointment appointmentToUpdate;
    private boolean isUpdate;


    public void initialize() throws SQLException {
        fillComboBoxes();
        setNewAppointmentID();
    }

    public void setAppointmentToUpdate(Appointment appointment) {
        appointmentToUpdate = appointment;
        isUpdate = true;

    }

    @FXML
    void onCancelButton() {
        closeWindow();
    }

    @FXML
    void onConfirmButton() throws SQLException {
        if (checkFields() && checkBusinessHours()) {
            Appointment appointmentToAdd = createAppointment();
            if (!checkAppointmentOverlap(appointmentToAdd)) {
                if (isUpdate) {
                    AppointmentDAO.updateAppointment(appointmentToAdd);
                } else {
                    AppointmentDAO.insertAppointment(appointmentToAdd);

                }
                if (isOpenedFromCustomer) {
                    customerViewWindowController.updateTable();
                } else {
                    appointmentViewWindowController.updateTable();
                }
                closeWindow();
            }
            else{
                System.out.println("Schedule Overlap");
                //ADD ERROR BOX HERE
            }
        }
        else {
            System.out.println("Not all fields filled");
            //ADD POPUP WINDOW HERE
        }
    }

    private void closeWindow() {
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

    private Appointment createAppointment() {
        Appointment newAppointment = new Appointment();
        newAppointment.setId(Integer.parseInt(appIdText.getText()));
        newAppointment.setTitle(titleText.getText());
        newAppointment.setDescription(descriptionText.getText());
        newAppointment.setLocation(locationText.getText());
        newAppointment.setType(typeText.getText());
        newAppointment.setContactId(contactBox.getValue().getContactID());
        newAppointment.setStart(dateAndTimeToTimestamp(dateDate, startHourTime, startMinuteTime));
        newAppointment.setEnd(dateAndTimeToTimestamp(dateDate, endHourTime, endMinuteTime));
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

    private void fillHoursBox(ComboBox<String> comboBox) {
        for (int i = 0; i < 24; i++) {
            comboBox.getItems().add(String.valueOf(i));
        }
    }

    private void fillMinutesBox(ComboBox<String> comboBox) {
        for (int i = 0; i < 60; i += 15) {
            comboBox.getItems().add(String.valueOf(i));
        }
    }

    private boolean checkFields() {
        boolean fieldsFilled = !appIdText.getText().isEmpty() && !titleText.getText().isEmpty() && !descriptionText.getText().isEmpty() &&
                !locationText.getText().isEmpty() && !typeText.getText().isEmpty() && contactBox.getValue() != null &&
                dateDate.getValue() != null && !startHourTime.getValue().isEmpty() &&
                !startMinuteTime.getValue().isEmpty() && dateDate.getValue() != null &&
                !endHourTime.getValue().isEmpty() && !endMinuteTime.getValue().isEmpty() && customerBox.getValue() != null;

        return fieldsFilled;
    }

    private Timestamp dateAndTimeToTimestamp(DatePicker date, ComboBox<String> hourBox, ComboBox<String> minuteBox) {
        Timestamp timestamp = null;
        int year = date.getValue().getYear();
        Month month = date.getValue().getMonth();
        int day = date.getValue().getDayOfMonth();
        int hour = Integer.parseInt(hourBox.getValue());
        int minute = Integer.parseInt(minuteBox.getValue());
        LocalDateTime startTime = LocalDateTime.of(year, month, day, hour, minute);

        startTime = TimeUtilities.localToUTC(startTime);
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

    public void importCustomer(Customer customer) throws SQLException {
        fillCustomerBox();
        customerBox.setValue(customer);
        customerBox.setDisable(true);
    }

    private boolean checkBusinessHours() {
        boolean isValid;
        LocalDateTime startTime = dateAndTimeToTimestamp(dateDate, startHourTime, startMinuteTime).toLocalDateTime();
        LocalDateTime endTime = dateAndTimeToTimestamp(dateDate, endHourTime, endMinuteTime).toLocalDateTime();

        if (endTime.isBefore(startTime)) {
            isValid = false;
            System.out.println("end time before start time");
        } else if (TimeUtilities.checkBeforeOpenHours(startTime)) {
            isValid = false;
            System.out.println("before open");
        } else if (TimeUtilities.checkAfterCloseHours(endTime)) {
            isValid = false;
            System.out.println("after close");
        } else if (startTime.getDayOfYear() != endTime.getDayOfYear() && startTime.getYear() != endTime.getYear()) {
            isValid = false;
            System.out.println("spans multiple days");
        } else {
            isValid = true;
        }

        return isValid;
    }


    public void getCustomerMainWindowInstance(CustomerViewWindowController controller) {
        this.customerViewWindowController = controller;
        isOpenedFromCustomer = true;
    }

    public void getAppointmentMainWindowInstance(AppointmentViewWindowController controller) {
        this.appointmentViewWindowController = controller;
        isOpenedFromCustomer = false;
    }

    public void setFields() {
        appIdText.setText(String.valueOf(appointmentToUpdate.getId()));
        titleText.setText(appointmentToUpdate.getTitle());
        descriptionText.setText(appointmentToUpdate.getDescription());
        locationText.setText(appointmentToUpdate.getLocation());
        typeText.setText(appointmentToUpdate.getType());
        setContactBox(appointmentToUpdate.getContactId());
        setDateAndTime(appointmentToUpdate.getStart(), appointmentToUpdate.getEnd());
        setCustomerBox(appointmentToUpdate.getCustId());


    }

    private void setContactBox(int contactID) {
        ObservableList<Contact> contacts = contactBox.getItems();
        contacts.forEach(contact -> {
            if (contactID == contact.getContactID()) {
                contactBox.setValue(contact);
            }
        });
    }

    private void setCustomerBox(int customerID) {
        ObservableList<Customer> customers = customerBox.getItems();
        customers.forEach(customer -> {
            if (customerID == customer.getId()) {
                customerBox.setValue(customer);
            }
        });
    }

    private void setDateAndTime(Timestamp startTime, Timestamp endTime) {
        LocalDateTime startLDT = startTime.toLocalDateTime();
        LocalDateTime endLDT = endTime.toLocalDateTime();
        startLDT = TimeUtilities.utcToLocal(startLDT);
        endLDT = TimeUtilities.utcToLocal(endLDT);
        dateDate.setValue(startLDT.toLocalDate());
        startHourTime.setValue(String.valueOf(startLDT.getHour()));
        startMinuteTime.setValue(String.valueOf(startLDT.getMinute()));
        endHourTime.setValue(String.valueOf(endLDT.getHour()));
        endMinuteTime.setValue(String.valueOf(endLDT.getMinute()));
    }

    private boolean checkAppointmentOverlap(Appointment appointmentToAdd) throws SQLException {
        AtomicBoolean isOverlap = new AtomicBoolean(false);
        Customer customer = customerBox.getSelectionModel().getSelectedItem();
        LocalDateTime appointmentToAddTimeStart = appointmentToAdd.getStart().toLocalDateTime();
        LocalDateTime appointmentToAddTimeEnd = appointmentToAdd.getEnd().toLocalDateTime();
        ObservableList<Appointment> allAppointments = AppointmentDAO.getAllAppointments();
        ObservableList<Appointment> customerAppointments = FXCollections.observableArrayList();
        allAppointments.forEach(appointment -> {
            if (customer.getId() == appointment.getCustId()) {
                customerAppointments.add(appointment);
            }
        });

        System.out.println(customerAppointments);
        customerAppointments.forEach(appointment -> {
            LocalDateTime appointmentToCheckStartTime = appointment.getStart().toLocalDateTime();
            LocalDateTime appointmentToCheckEndTime = appointment.getEnd().toLocalDateTime();
            if (TimeUtilities.isOverlapping(appointmentToAddTimeStart, appointmentToCheckStartTime,
                    appointmentToAddTimeEnd, appointmentToCheckEndTime)) {
                isOverlap.set(true);
                System.out.println("found overlap");
            }
        });
        return isOverlap.get();

    }

}
