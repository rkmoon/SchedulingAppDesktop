package view_controller;

import DAO.AppointmentDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Appointment;
import model.MonthAppointment;
import model.TypeAppointment;

import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicBoolean;

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

    private void fillTables() throws SQLException {
        ObservableList<TypeAppointment> typeAppointments = getNumberofEachType();
        ObservableList<MonthAppointment> monthAppointments = getNumberOfEachMonth();
        typeCol.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
        numTypeCol.setCellValueFactory(new PropertyValueFactory<>("number"));
        monthCol.setCellValueFactory(new PropertyValueFactory<>("month"));
        numMonthCol.setCellValueFactory(new PropertyValueFactory<>("number"));
        typeTable.setItems(typeAppointments);
        monthTable.setItems(monthAppointments);

    }

    private ObservableList<TypeAppointment> getNumberofEachType() throws SQLException {
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
