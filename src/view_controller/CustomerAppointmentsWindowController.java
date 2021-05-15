package view_controller;

import DAO.AppointmentDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.MonthAppointment;
import model.TypeAppointment;

import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class controls the Customer Appointments report window. It displays the number of appointments by type and by
 * month.
 */
public class CustomerAppointmentsWindowController {
    @FXML
    private TableView<TypeAppointment> typeTable;

    @FXML
    private TableColumn<TypeAppointment, String> typeCol;

    @FXML
    private TableColumn<TypeAppointment, Integer> numTypeCol;

    @FXML
    private TableView<MonthAppointment> monthTable;

    @FXML
    private TableColumn<String, Integer> monthCol;

    @FXML
    private TableColumn<String, Integer> numMonthCol;

    @FXML
    public void initialize() throws SQLException {
        fillTables();
    }

    /**
     * Fills the table with appointment types, months, and the number of each
     * @throws SQLException error with DB
     */
    private void fillTables() throws SQLException {
        ObservableList<TypeAppointment> typeAppointments = getNumberOfEachType();
        ObservableList<MonthAppointment> monthAppointments = getNumberOfEachMonth();
        typeCol.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
        numTypeCol.setCellValueFactory(new PropertyValueFactory<>("number"));
        monthCol.setCellValueFactory(new PropertyValueFactory<>("month"));
        numMonthCol.setCellValueFactory(new PropertyValueFactory<>("number"));
        typeTable.setItems(typeAppointments);
        monthTable.setItems(monthAppointments);

    }

    /**
     * Closes the window
     */
    @FXML
    public void closeWindow(){
        Stage stage = (Stage) typeTable.getScene().getWindow();
        stage.close();
    }

    /**
     * Gets all appointments, and adds them to a list of AppointmentTypes and counts the number of each type of
     * appointment. The first lambda expression goes through each appointment and the second checks the current
     * list of appointments already gone through and compares the type of both, counting up by one if they match and
     * adding another AppointmentType to the list if no match is found
     * @return list of all types of appointments and the number of them
     * @throws SQLException error with the DB
     */
    private ObservableList<TypeAppointment> getNumberOfEachType() throws SQLException {
        ObservableList<Appointment> appointments = AppointmentDAO.getAllAppointments();
        ObservableList<TypeAppointment> appointmentTypes = FXCollections.observableArrayList();
        appointments.forEach(appointment -> {
            AtomicBoolean foundMatching = new AtomicBoolean(false);
            appointmentTypes.forEach(typeAppointment -> {
                if (typeAppointment.getAppointmentType().equals(appointment.getType())) {
                    typeAppointment.addOne();
                    foundMatching.set(true);
                }
            });
            if (foundMatching.get() == false) {
                appointmentTypes.add(new TypeAppointment(appointment.getType(), 1));
            }

        });
        return appointmentTypes;
    }

    /**
     * Gets all appointments, then counts the number of appointments per month. The first lambda expression is used to
     * go through all of the appointments, and the second is to check if there is already a MonthAppointment object
     * with that month. If there is, the number of appointments in that month is increased by one, if not, a new
     * MonthAppointment is added to the list.
     * @return returns a list of months and how many appointments in each
     * @throws SQLException error with the DB
     */
    private ObservableList<MonthAppointment> getNumberOfEachMonth() throws SQLException {
        ObservableList<Appointment> appointments = AppointmentDAO.getAllAppointments();
        ObservableList<MonthAppointment> monthAppointments = FXCollections.observableArrayList();
        appointments.forEach(appointment -> {
            AtomicBoolean foundMatching = new AtomicBoolean(false);
            String appointmentMonth = appointment.getStart().toLocalDateTime().getMonth().toString();
            monthAppointments.forEach(monthAppointment -> {
                if (monthAppointment.getMonth().equals(appointmentMonth)) {
                    monthAppointment.addOne();
                    foundMatching.set(true);
                }
            });
            if (foundMatching.get() == false) {
                monthAppointments.add(new MonthAppointment(appointmentMonth, 1));
            }

        });
        return monthAppointments;
    }

}
