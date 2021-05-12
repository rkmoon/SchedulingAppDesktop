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

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.Objects;


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

    @FXML
    private RadioButton weekRadio;

    @FXML
    private RadioButton monthRadio;


    public void initialize() throws SQLException {
        populateAppTable();
    }


    private void populateAppTable() throws SQLException {

        FilteredList<Appointment> appointments = new FilteredList<>(AppointmentDAO.getAllAppointments());
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
        appContCol.setCellValueFactory(new PropertyValueFactory<>("contactId"));
        appTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        appStartCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        appEndCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        appCustIdCol.setCellValueFactory(new PropertyValueFactory<>("custId"));

        appTable.setItems(appointments);
    }
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

    @FXML
    public void addAppointment() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AppointmentWindow.fxml"));
        Parent root = loader.load();
        AppointmentWindowController appointmentWindowController = loader.getController();
        appointmentWindowController.getAppointmentMainWindowInstance(this);

        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setTitle("Appointments");
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void weekRadioClicked() throws SQLException {
        monthRadio.setSelected(false);
        populateAppTable();
    }
    @FXML
    public void monthRadioClicked() throws SQLException {
        weekRadio.setSelected(false);
        populateAppTable();
    }

    @FXML
    public void deleteAppointment() throws SQLException {
        Appointment appointmentToDelete = appTable.getSelectionModel().getSelectedItem();
        if(appointmentToDelete == null){
            System.out.println("No Appointment Selected");
            //ADD POPUP WINDOW HERE
        }
        else {
            AppointmentDAO.deleteAppointment(appointmentToDelete);
            updateTable();
        }

    }

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


//        LocalDate date = LocalDate.now();
//        TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
//        int weekNumber = date.get(woy);


    public void updateTable() throws SQLException {
        populateAppTable();
    }

    private void closeWindow(){
        Stage stage = (Stage) custButton.getScene().getWindow();
        stage.close();
    }
}
