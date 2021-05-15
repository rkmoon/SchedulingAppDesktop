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

import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class controls the Customer Appointments report window. It displays the number of appointments by type and by
 * month.
 */
public class CustomerAppointmentsWindowController {

    @FXML
    private TableColumn<MonthAppointment, String> typeCol;

    @FXML
    private TableColumn<MonthAppointment, Integer> numCol;

    @FXML
    private TableView<MonthAppointment> monthTable;

    @FXML
    private TableColumn<String, Integer> monthCol;


    @FXML
    public void initialize() throws SQLException {
        fillTables();
    }

    /**
     * Fills the table with appointment types, months, and the number of each
     *
     * @throws SQLException error with DB
     */
    private void fillTables() throws SQLException {
        ObservableList<MonthAppointment> monthAppointments = getNumberOfEachMonth();
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        numCol.setCellValueFactory(new PropertyValueFactory<>("number"));
        monthCol.setCellValueFactory(new PropertyValueFactory<>("month"));
        monthTable.setItems(monthAppointments);

    }

    /**
     * Closes the window
     */
    @FXML
    public void closeWindow() {
        Stage stage = (Stage) monthTable.getScene().getWindow();
        stage.close();
    }

    /**
     * Gets all appointments and numbers them by how many types are in each month. The first lambda expression is to loop
     * through all appointments and to get the month of that appointment. The second lambda expression is to then loop
     * through the list of Month appointments and check to see if there is already an appointment in a month, and if there
     * is check to see if there is one of that type in that month. If there is, one is added to the total number of appointments
     * of that type in that month.
     * @return list of MonthAppointment objects
     * @throws SQLException error with the DB
     */
    private ObservableList<MonthAppointment> getNumberOfEachMonth() throws SQLException {
        ObservableList<Appointment> appointments = AppointmentDAO.getAllAppointments();
        ObservableList<MonthAppointment> monthAppointments = FXCollections.observableArrayList();
        appointments.forEach(appointment -> {
            AtomicBoolean foundMatchingMonth = new AtomicBoolean(false);
            AtomicBoolean foundMatchingType = new AtomicBoolean(false);
            String appointmentMonth = appointment.getStart().toLocalDateTime().getMonth().toString();
            if (monthAppointments.isEmpty()) {
                monthAppointments.add(new MonthAppointment(appointmentMonth, appointment.getType(), 1));
            } else {
                monthAppointments.forEach(monthAppointment -> {
                    if (monthAppointment.getMonth().equals(appointmentMonth)) {
                        if (monthAppointment.getType().equals(appointment.getType())) {
                            monthAppointment.addOne();
                            foundMatchingType.set(true);
                        }
                        foundMatchingMonth.set(true);
                    }
                });
                if (foundMatchingMonth.get() && !foundMatchingType.get()) {
                    monthAppointments.add(new MonthAppointment(appointmentMonth, appointment.getType(), 1));
                } else if (!foundMatchingMonth.get() && !foundMatchingType.get()) {
                    monthAppointments.add(new MonthAppointment(appointmentMonth, appointment.getType(), 1));
                }
            }
        });
        System.out.println(monthAppointments);

        return monthAppointments;
    }



}
