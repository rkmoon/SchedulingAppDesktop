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
import utils.Errors;
import utils.LoggedInUser;
import utils.TimeUtilities;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class controls the adding or updating of appointments. It uses the fields to create or update an appointment and add
 * or update it in the database.
 */

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

    /**
     * Fills the combo boxes with the relevant data and sets a new appointment ID
     * @throws SQLException Error connecting to DB
     */
    public void initialize() throws SQLException {
        fillComboBoxes();
        setNewAppointmentID();
    }

    /**
     * Used from other windows to notify that this is an appointment being updated instead of an appointment added
     * @param appointment appointment being updated
     */
    public void setAppointmentToUpdate(Appointment appointment) {
        appointmentToUpdate = appointment;
        isUpdate = true;

    }

    @FXML
    void onCancelButton() {
        closeWindow();
    }

    /**
     * Calls the AppointmentDAO with the information stored in the fields. Checks for all forms filled, and checks that
     * all times are valid, including business hours, same day start and end times, and ensuring start time is before
     * end time. Will display an informational error popup box if there are any errors.
     * @throws SQLException error with the DB
     * @throws IOException error opening error windows
     */
    @FXML
    void onConfirmButton() throws SQLException, IOException {
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
                Errors.openErrorMenu(Errors.getOverlap());
            }
        }
        else {
            Errors.openErrorMenu(Errors.getFieldsIncomplete());
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Fills the combo boxes on the window with the relevant information to choose from.
     * @throws SQLException Error with the DB
     */
    private void fillComboBoxes() throws SQLException {
        fillContactBox();
        fillCustomerBox();
        fillHoursBox(startHourTime);
        fillHoursBox(endHourTime);
        fillMinutesBox(startMinuteTime);
        fillMinutesBox(endMinuteTime);
    }

    /**
     * Takes the information inside the different forms and creates an Appointment object to send to the DAO for
     * inserting or updating in the DB
     * @return Appointment to be inserted or updated
     */
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

    /**
     * Fills the contact combo box with all contacts
     * @throws SQLException error with the DB
     */
    private void fillContactBox() throws SQLException {
        contactBox.setItems(ContactDAO.getAllContacts());
    }

    /**
     * Filles the customer combo box with all customers
     * @throws SQLException error with the DB
     */
    private void fillCustomerBox() throws SQLException {
        customerBox.setItems(CustomerDAO.getAllCustomers());
    }

    /**
     * Fills the hours combo box with integers ranging from 0 to 23 for the different hours of the day
     * @param comboBox combo box to fill
     */
    private void fillHoursBox(ComboBox<String> comboBox) {
        for (int i = 0; i < 24; i++) {
            comboBox.getItems().add(String.valueOf(i));
        }
    }

    /**
     * Fills the minutes combo box with the valid minutes available for appointments: 0, 15, 30, 45.
     * @param comboBox combo box to fill
     */
    private void fillMinutesBox(ComboBox<String> comboBox) {
        for (int i = 0; i < 60; i += 15) {
            comboBox.getItems().add(String.valueOf(i));
        }
    }

    /**
     * Checks to make sure none of the fields are empty
     * @return True if all are filled, false if any are missing
     */
    private boolean checkFields() {
        boolean fieldsFilled = !appIdText.getText().isEmpty() && !titleText.getText().isEmpty() && !descriptionText.getText().isEmpty() &&
                !locationText.getText().isEmpty() && !typeText.getText().isEmpty() && contactBox.getValue() != null &&
                dateDate.getValue() != null && !startHourTime.getValue().isEmpty() &&
                !startMinuteTime.getValue().isEmpty() && dateDate.getValue() != null &&
                !endHourTime.getValue().isEmpty() && !endMinuteTime.getValue().isEmpty() && customerBox.getValue() != null;

        return fieldsFilled;
    }

    /**
     * Converts the date and time information in the selectors in the window to a Timestamp for insertion into the DB
     * @param date date chosen by the DatePicker
     * @param hourBox hour chosen by the hour ComboBox
     * @param minuteBox minute chosen by the minute ComboBox
     * @return Timestamp created by the information in the selectors
     */
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

    /**
     * Creates a new unique AppointmentID. The lambda expression here is used to sort appointment ids in different lists
     * in descending order for highest ID+1 to make sure it is unique.
     * @throws SQLException error with the DB
     */
    private void setNewAppointmentID() throws SQLException {
        ObservableList<Appointment> appointments = AppointmentDAO.getAllAppointments();
        appointments.sort(((o1, o2) -> (o2.getId() - o1.getId())));
        int newAppointmentID = appointments.get(0).getId() + 1;
        appIdText.setText(String.valueOf(newAppointmentID));
    }

    /**
     * Imports the customer from the Customer View Window. It is used to pass information about the customer selected
     * in that window over to this window. It is then disabled so to allow the user to see which customer they are
     * creating an appointment for, but not able to switch customers.
     * @param customer customer to make the appointment for
     * @throws SQLException error with the DB
     */
    public void importCustomer(Customer customer) throws SQLException {
        fillCustomerBox();
        customerBox.setValue(customer);
        customerBox.setDisable(true);
    }

    /**
     * Takes the start time and end time from the combo boxes, converts from local time to EST, and then checks if
     * that appointment takes place within the business hours of 8AM EST to 10PM EST.
     * @return True if appointment time is within business hours, False if not
     * @throws IOException error opening error window
     */
    private boolean checkBusinessHours() throws IOException {
        boolean isValid;
        LocalDateTime startTime = dateAndTimeToTimestamp(dateDate, startHourTime, startMinuteTime).toLocalDateTime();
        LocalDateTime endTime = dateAndTimeToTimestamp(dateDate, endHourTime, endMinuteTime).toLocalDateTime();

        if (endTime.isBefore(startTime)) {
            isValid = false;
            Errors.openErrorMenu(Errors.getEndBeforeStart());
        } else if (TimeUtilities.checkBeforeOpenHours(startTime)) {
            isValid = false;
            Errors.openErrorMenu(Errors.getBusinessHours());
        } else if (TimeUtilities.checkAfterCloseHours(endTime)) {
            isValid = false;
            Errors.openErrorMenu(Errors.getBusinessHours());
        } else if (startTime.getDayOfYear() != endTime.getDayOfYear() && startTime.getYear() != endTime.getYear()) {
            isValid = false;
            Errors.openErrorMenu(Errors.getMultipleDays());
        } else {
            isValid = true;
        }

        return isValid;
    }

    /**
     * Used to get the instance of the Customer Window Controller if opened from there.
     * @param controller controller of the customer window
     */
    public void getCustomerMainWindowInstance(CustomerViewWindowController controller) {
        this.customerViewWindowController = controller;
        isOpenedFromCustomer = true;
    }

    /**
     * Used to get the instance of the Appointment Window Controller if opened from there.
     * @param controller controller of the appointment window.
     */
    public void getAppointmentMainWindowInstance(AppointmentViewWindowController controller) {
        this.appointmentViewWindowController = controller;
        isOpenedFromCustomer = false;
    }

    /**
     * Populates the fields with the currently selected appointment's information if updating an appointment
     */
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

    /**
     * Used as a helper method to check through all the contacts for the specific ID stored, then displays the name
     * in the combo box for convenience of the user. The lambda expression is used to perform the ID check on each
     * contact in the combobox.
     * @param contactID ID of the contact assigned to the appointment
     */
    private void setContactBox(int contactID) {
        ObservableList<Contact> contacts = contactBox.getItems();
        contacts.forEach(contact -> {
            if (contactID == contact.getContactID()) {
                contactBox.setValue(contact);
            }
        });
    }

    /**
     * Used as a helper method to check through all the customers for the specific ID stored, then displays the name
     * in the combo box for convenience of the user. The lambda expression is used to perform the ID check on each
     * customer in the combobox.
     * @param customerID ID of the customer assigned to the appointment
     */
    private void setCustomerBox(int customerID) {
        ObservableList<Customer> customers = customerBox.getItems();
        customers.forEach(customer -> {
            if (customerID == customer.getId()) {
                customerBox.setValue(customer);
            }
        });
    }

    /**
     * Takes the start time and end time from the appointment, converts it to local time, then inserts into the combo
     * boxes when updating an appointment.
     * @param startTime Timestamp start time from the appointment
     * @param endTime Timestamp end time from the appointment
     */
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

    /**
     * Check if the appointment overlaps with any of the other customer's appointments. The first lambda expression is
     * used to find any appointments the customer has. The second lambda expression is to check each of those appointment
     * times against the appointment to be added to the customer
     * @param appointmentToAdd added appointment to check against other appointments
     * @return True if there is an overlap, false if there is no overlap
     * @throws SQLException error with DB
     */
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
        customerAppointments.forEach(appointment -> {
            LocalDateTime appointmentToCheckStartTime = appointment.getStart().toLocalDateTime();
            LocalDateTime appointmentToCheckEndTime = appointment.getEnd().toLocalDateTime();
            if(!(appointment.getId() == appointmentToAdd.getId())) {
                if (TimeUtilities.isOverlapping(appointmentToAddTimeStart, appointmentToCheckStartTime,
                        appointmentToAddTimeEnd, appointmentToCheckEndTime)) {
                    isOverlap.set(true);
                    try {
                        Errors.openErrorMenu(Errors.getOverlap());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        return isOverlap.get();

    }

}
