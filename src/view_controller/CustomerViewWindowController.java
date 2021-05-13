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
import utils.LoggedInUser;
import utils.TimeUtilities;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

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


    private void openCustomerWindow(boolean updatingCustomer) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomerWindow.fxml"));
        Parent root = loader.load();
        CustomerWindowController customerWindowController = loader.getController();
        customerWindowController.getMainWindowInstance(this);
        if(updatingCustomer){
            Customer custToUpdate = custTable.getSelectionModel().getSelectedItem();
            if (custToUpdate == null){
                System.out.println("No customer selected");
                // ADD POPUP WINDOW HERE
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
            System.out.println("No customer Selected");
            //ADD ERROR HERE
        }
    }

    @FXML
    public void goToAppointments() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("AppointmentViewWindow.fxml")));

        Scene scene = new Scene(root);
        stage.setTitle("Appointments");
        stage.setScene(scene);
        stage.show();
    }

    @FXML void deleteCustomer() throws SQLException {
        Customer custToDelete = custTable.getSelectionModel().getSelectedItem();
        if(custToDelete == null){
            System.out.println("No Customer Selected");
            //ADD ERROR BOX
        }
        else if(checkForAppointments(custToDelete)){
            System.out.println("Customer has appointments");
            //ADD ERROR BOX
        }
        else {
            //ADD CONFIRMATION BOX
            CustomerDAO.deleteCustomer(custToDelete);
            updateTable();
        }
    }

    public void updateTable() throws SQLException {
        populateCustTable();
        checkForAppointments();
    }

    private void closeWindow(){
        Stage stage = (Stage) appointmentButton.getScene().getWindow();
        stage.close();
    }

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

    private void alertAboutAppointment(Appointment appointment, double time, LocalDateTime appointmentTime){
        appointmentAlertLabel.setText("You have an appointment in " + (int)time + " minutes");
        DateTimeFormatter format = DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy h:mm a");
        appointmentInfoLabel.setText("ID: " + appointment.getId());
        appointmentTimeLabel.setText("Start Time: " + appointmentTime.format(format));
    }

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