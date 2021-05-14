package view_controller;

import DAO.AppointmentDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import utils.Errors;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Locale;

/**
 * This class controls the Appointment View window. It shows all the appointments for either the week or month, and
 * allows modification or deletion of each appointment shown.
 *
 */


public class AppointmentViewWindowController {

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
    private TableColumn<Appointment, String> appStartCol;

    @FXML
    private TableColumn<Appointment, String> appEndCol;

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

    @FXML
    private RadioButton weekRadio;

    @FXML
    private RadioButton monthRadio;



    public void initialize() throws SQLException {
        populateAppTable();
        monthRadioClicked();
    }

    /**
     * Populates the table with appointments for both week and month, depending on which radio button is checked
     * @throws SQLException If there is an error connected to the DB
     */
    private void populateAppTable() throws SQLException {

        FilteredList<Appointment> appointments = new FilteredList<>(AppointmentDAO.getAllAppointments());
        // Uncomment this if the user is only supposed to see their own appointments
        // appointments = new FilteredList<>(getUserAppointments(appointments));
        if(monthRadio.isSelected()){
            appointments = new FilteredList<>(getAllAppointmentsInMonth(appointments));
        }
        else{
            appointments = new FilteredList<>(getAllAppointmentsInWeek(appointments));
        }

        appIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        appTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        appDescCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        appLocCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        appContCol.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        appTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        appStartCol.setCellValueFactory(new PropertyValueFactory<>("formattedLocalStartTime"));
        appEndCol.setCellValueFactory(new PropertyValueFactory<>("formattedLocalEndTime"));
        appCustIdCol.setCellValueFactory(new PropertyValueFactory<>("custId"));

        appTable.setItems(appointments);
    }

/*
    // Uncomment if there is a need to have a button to return to customers
    @FXML
    public void goToCustomers() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("CustomerViewWindow.fxml")));

        Scene scene = new Scene(root);
        stage.setTitle("Customers");
        stage.setScene(scene);
        stage.show();
        closeWindow();
    }

 */

//    Uncomment if there is a need to add an app appointment to the appointment window
//    @FXML
//    public void addAppointment() throws IOException {
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("AppointmentWindow.fxml"));
//        Parent root = loader.load();
//        AppointmentWindowController appointmentWindowController = loader.getController();
//        appointmentWindowController.getAppointmentMainWindowInstance(this);
//
//        Stage stage = new Stage();
//        Scene scene = new Scene(root);
//        stage.setTitle("Appointments");
//        stage.setScene(scene);
//        stage.show();
//    }

    /**
     * Populates the table with this week's appointments.Executes when the week radio button is chosen. Disables the
     * week button so it cannot be unchecked and enables the month button.
     * @throws SQLException Error connecting to DB
     */
    @FXML
    public void weekRadioClicked() throws SQLException {
        monthRadio.setSelected(false);
        weekRadio.setDisable(true);
        monthRadio.setDisable(false);
        populateAppTable();
    }

    /**
     * Populates the table with this month's appointments. Executes when the month radio button is chosen. Disables the
     * month button so it cannot be unchecked and enables the week button.
     * @throws SQLException
     */
    @FXML
    public void monthRadioClicked() throws SQLException {
        weekRadio.setSelected(false);
        monthRadio.setDisable(true);
        weekRadio.setDisable(false);
        populateAppTable();
    }

    /**
     * Deletes the currently selected appointment. Gives an error popup if no appointment is selected.
     * @throws SQLException Error connecting to DB
     * @throws IOException Error opening window
     */
    @FXML
    public void deleteAppointment() throws SQLException, IOException {
        Appointment appointmentToDelete = appTable.getSelectionModel().getSelectedItem();
        if(appointmentToDelete == null){
            Errors.openErrorMenu(Errors.getNoSelection());
        }
        else {
            AppointmentDAO.deleteAppointment(appointmentToDelete);
            updateTable();
        }

    }

    /**
     * Takes a list of all appointments and returns only the appointments in the current month
     * @param appointments list of all appointments to filter by month
     * @return filtered list of appointments by current month
     */
    private ObservableList<Appointment> getAllAppointmentsInMonth(FilteredList<Appointment> appointments){
        ObservableList<Appointment> filteredAppointments = FXCollections.observableArrayList();
        appointments.forEach(appointment -> {
            LocalDateTime appointmentTime = appointment.getStart().toLocalDateTime();
            LocalDateTime nowTime = LocalDateTime.now();
            if(nowTime.getMonth() == appointmentTime.getMonth() && nowTime.getYear() == appointmentTime.getYear()){
                filteredAppointments.add(appointment);
            }
        });
        return filteredAppointments;
    }
    /**
     * Takes a list of all appointments and returns only the appointments in the current week
     * @param appointments list of all appointments to filter by week
     * @return filtered list of appointments by current week
     */
    private ObservableList<Appointment> getAllAppointmentsInWeek(FilteredList<Appointment> appointments) {
        ObservableList<Appointment> filteredAppointments = FXCollections.observableArrayList();
        TemporalField weekOfYear = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
        appointments.forEach(appointment -> {
                    LocalDateTime appointmentTime = appointment.getStart().toLocalDateTime();
                    LocalDateTime nowTime = LocalDateTime.now();
                    if (nowTime.get(weekOfYear) == appointmentTime.get(weekOfYear) &&
                            nowTime.getYear() == appointmentTime.getYear()) {
                        filteredAppointments.add(appointment);
                    }
                }

        );
        return filteredAppointments;
    }

    /**
     * Takes the currently selected appointment opens the appointment window, and sends the information to the new
     * window to populate the update form. Will open an error pop up if no appointment is selected.
     * @throws IOException Error opening the window
     */
    @FXML
    public void updateAppointmentWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AppointmentWindow.fxml"));
        Parent root = loader.load();
        AppointmentWindowController appointmentWindowController = loader.getController();
        appointmentWindowController.getAppointmentMainWindowInstance(this);
        if(appTable.getSelectionModel().getSelectedItem() != null) {
            appointmentWindowController.setAppointmentToUpdate(appTable.getSelectionModel().getSelectedItem());
            appointmentWindowController.setFields();

            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setTitle("Appointment Window");
            stage.setScene(scene);
            stage.show();
        }
        else{
            Errors.openErrorMenu(Errors.getNoSelection());
        }
    }

    /**
     * Updates the table from other windows, such as when an appointment is added or updated
     * @throws SQLException
     */
    public void updateTable() throws SQLException {
        populateAppTable();
    }
//    Uncomment if there needs to be a close window button
//    private void closeWindow(){
//        Stage stage = (Stage) custButton.getScene().getWindow();
//        stage.close();
//    }

      // Uncomment if table needs to be filled by only the current users appointments
//    private ObservableList<Appointment> getUserAppointments(ObservableList<Appointment> appointments){
//        int userID = LoggedInUser.getUserID();
//        ObservableList<Appointment> filteredAppointments = FXCollections.observableArrayList();
//        appointments.forEach(appointment -> {
//            if (userID == appointment.getUserId()) {
//                filteredAppointments.add(appointment);
//            }
//        });
//        return filteredAppointments;
//    }
}
