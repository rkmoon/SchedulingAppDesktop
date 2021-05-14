package view_controller;


import DAO.AppointmentDAO;
import DAO.CustomerDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.Customer;
import utils.Errors;
import utils.LoggedInUser;
import utils.TimeUtilities;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class controls the main window of the program, a list of customers and their information. It can bring up windows
 * to add or update a customer, it can delete customers, it can bring up a window of reports to look at, and bring up
 * a window to show appointments.
 */
public class CustomerViewWindowController {

    @FXML
    private TableView<Customer> custTable;

    @FXML
    private TableColumn<?, ?> custIdCol;

    @FXML
    private TableColumn<?, ?> custNameCol;

    @FXML
    private TableColumn<Customer, String> custAddressCol;

    @FXML
    private TableColumn<Customer, String> custPostCol;

    @FXML
    private TableColumn<Customer, String> custPhoneCol;

    @FXML
    private TableColumn<Customer, String> custDivisionCol;

    @FXML
    private TableColumn<Customer, String> custCountryCol;

    @FXML
    private Button addCustomerButton;

    @FXML
    private Button addAppointmentButton;

    @FXML
    private Button updateCustomerButton;

    @FXML
    private Button deleteCustomerButton;

    @FXML
    private Button appointmentButton;

    @FXML
    private Label appointmentAlertLabel;

    @FXML
    private Label appointmentInfoLabel;

    @FXML
    private Label appointmentTimeLabel;


    @FXML
    public void initialize() throws SQLException {
        populateCustTable();
        checkForAppointments();

    }

    /**
     * Fills the customer table with all customers from the database
     * @throws SQLException error with the DB
     */
    private void populateCustTable() throws SQLException {
        FilteredList<Customer> customers = new FilteredList<>(CustomerDAO.getAllCustomers());
        custIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        custNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        custAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        custPostCol.setCellValueFactory(new PropertyValueFactory<>("postCode"));
        custPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));
        custCountryCol.setCellValueFactory(new PropertyValueFactory<>("country"));
        custDivisionCol.setCellValueFactory(new PropertyValueFactory<>("division"));
        custTable.setItems(customers);
    }



    @FXML
    public void openAddCustomerWindow() throws IOException, SQLException {
        openCustomerWindow(false);
    }

    @FXML
    public void openUpdateCustomerWindow() throws IOException, SQLException {
        openCustomerWindow(true);
    }

    /**
     * Opens the customer window, checking for if the user is either adding or updating a customer
     * @param updatingCustomer if the user is updating a customer
     * @throws IOException error opening up window
     * @throws SQLException error with the DB
     */
    private void openCustomerWindow(boolean updatingCustomer) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomerWindow.fxml"));
        Parent root = loader.load();
        CustomerWindowController customerWindowController = loader.getController();
        customerWindowController.getMainWindowInstance(this);
        if(updatingCustomer){
            Customer custToUpdate = custTable.getSelectionModel().getSelectedItem();
            if (custToUpdate == null){
                Errors.openErrorMenu(Errors.getNoSelection());
                return;
            }
            customerWindowController.setUpdateCustomer(true);
            customerWindowController.setCustomerToUpdate(custToUpdate);
        }
        else {
            customerWindowController.setUpdateCustomer(false);
        }

        customerWindowController.setAddOrUpdate();

        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setTitle("Customer Window");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Opens the appointment window, passing the customer selected to add an appointment for. Opens an error popup if
     * no customer is selected.
     * @throws IOException error opening the window
     * @throws SQLException error with the DB
     */
    @FXML
    public void openAppointmentWindow() throws IOException, SQLException {
        Customer customerSelected = custTable.getSelectionModel().getSelectedItem();
        if(customerSelected != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AppointmentWindow.fxml"));
            Parent root = loader.load();
            AppointmentWindowController appointmentWindowController = loader.getController();
            appointmentWindowController.importCustomer(customerSelected);
            appointmentWindowController.getCustomerMainWindowInstance(this);

            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setTitle("Customer Window");
            stage.setScene(scene);
            stage.show();
        }
        else{
            Errors.openErrorMenu(Errors.getNoSelection());
        }
    }

    /**
     * Opens the appointment window
     * @throws IOException error opening the window
     */
    @FXML
    public void goToAppointments() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("AppointmentViewWindow.fxml")));

        Scene scene = new Scene(root);
        stage.setTitle("Appointments");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Deletes the customer from the database. Checks if a customer is selected and if the customer has no appointments.
     * Displays an error popup with the information if either are false
     * @throws SQLException error with the DB
     * @throws IOException error opening the error window
     */
    @FXML void deleteCustomer() throws SQLException, IOException {
        Customer custToDelete = custTable.getSelectionModel().getSelectedItem();
        if(custToDelete == null){
            Errors.openErrorMenu(Errors.getNoSelection());
        }
        else if(checkForAppointments(custToDelete)){
            Errors.openErrorMenu(Errors.getCustomerHasAppointments());
        }
        else {
            //ADD CONFIRMATION BOX
            CustomerDAO.deleteCustomer(custToDelete);
            updateTable();
        }
    }

    /**
     * Updates the table after a customer is added or updated from the Customer Window
     * @throws SQLException error with the DB
     */
    public void updateTable() throws SQLException {
        populateCustTable();
        checkForAppointments();
    }

//    private void closeWindow(){
//        Stage stage = (Stage) appointmentButton.getScene().getWindow();
//        stage.close();
//    }

    /**
     * Checks to see if there are any appointments for the user in the next 15 minutes after logging in. The first
     * lambda expression is to check all appointments that have the same User Id as the user. The second lambda expression
     * is to then go through each of those and see if it is within 15 minutes of logging in.
     *
     * @throws SQLException
     */
    private void checkForAppointments() throws SQLException {
        ObservableList<Appointment> appointments = AppointmentDAO.getAllAppointments();
        ObservableList<Appointment> userAppointments = FXCollections.observableArrayList();
        LocalDateTime nowTime = LocalDateTime.now();
        appointments.forEach(appointment -> {
            if(LoggedInUser.getUserID() == appointment.getUserId()){
                userAppointments.add(appointment);
            }
        });
        userAppointments.forEach(appointment -> {
            LocalDateTime appointmentTime = appointment.getStart().toLocalDateTime();
            appointmentTime = TimeUtilities.utcToLocal(appointmentTime);
            double timeUntil = nowTime.until(appointmentTime, ChronoUnit.MINUTES);
            if(timeUntil < 15 && timeUntil > 0){
                alertAboutAppointment(appointment, timeUntil, appointmentTime);
            }
        });
    }

    /**
     * Gives a message on the GUI for the user alerting them that there is an appointment within 15 minutes
     * @param appointment appointment that is upcoming
     * @param time time until the meeting
     * @param appointmentTime time of the appointment
     */
    private void alertAboutAppointment(Appointment appointment, double time, LocalDateTime appointmentTime){
        appointmentAlertLabel.setText("You have an appointment in " + (int)time + " minutes");
        DateTimeFormatter format = DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy h:mm a");
        appointmentInfoLabel.setText("ID: " + appointment.getId());
        appointmentTimeLabel.setText("Start Time: " + appointmentTime.format(format));
    }

    /**
     * Check to see if the customer has any appointments assigned to them. The lambda is used to go through each
     * appointment to see if it has the same customer ID as the customer
     * @param customer customer to check for appointments for
     * @return True if there are appoints, false if not
     * @throws SQLException error with the DB
     */
    private boolean checkForAppointments(Customer customer) throws SQLException {
        AtomicBoolean hasAppointments = new AtomicBoolean(false);
        ObservableList<Appointment> appointments = AppointmentDAO.getAllAppointments();
        appointments.forEach(appointment -> {
            if(customer.getId() == appointment.getCustId()){
                hasAppointments.set(true);
            }
            else{
                hasAppointments.set(false);
            }
        });
        return hasAppointments.get();
    }

    /**
     * Opens the Reports window
     * @throws IOException error with opening the window
     */
    @FXML
    public void openReports() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ReportsWindow.fxml")));

        Scene scene = new Scene(root);
        stage.setTitle("Reports");
        stage.setScene(scene);
        stage.show();
    }



}