package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import utils.DBGenerics;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AppointmentDAO {

    private static final String tableName = "appointments";

    public static ObservableList<Appointment> getAllAppointments() throws SQLException {
        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
        ResultSet rs = DBGenerics.queryAll(tableName);
        while(rs.next()){
            allAppointments.add(fillAppointments(rs));
        }
        return allAppointments;
    }

    private static Appointment fillAppointments(ResultSet rs) throws SQLException {
        Appointment appointment = new Appointment();
        appointment.setId(rs.findColumn("Appointment_ID"));
        appointment.setTitle(rs.getString("Title"));
        appointment.setDescription(rs.getString("Description"));
        appointment.setLocation(rs.getString("Location"));
        appointment.setType(rs.getString("Type"));
        appointment.setStart(rs.getTimestamp("Start"));
        appointment.setEnd(rs.getTimestamp("End"));
        appointment.setCreateDate(rs.getTimestamp("Create_Date"));
        appointment.setCreatedBy(rs.getString("Created_By"));
        appointment.setLastUpdate(rs.getTimestamp("Last_Update"));
        appointment.setLastUpdateBy(rs.getString("Last_Updated_By"));
        appointment.setCustId(rs.getInt("Customer_ID"));
        appointment.setUserId(rs.getInt("User_ID"));
        appointment.setContactId(rs.getInt("Contact_ID"));
        return appointment;
    }
}
