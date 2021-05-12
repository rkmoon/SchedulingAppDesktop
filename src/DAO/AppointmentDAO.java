package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import utils.LoggedInUser;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AppointmentDAO {

    private static final String tableName = "appointments";

    public static ObservableList<Appointment> getAllAppointments() throws SQLException {
        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
        ResultSet rs = DBQuery.selectAllFromTable(tableName);
        while(rs.next()){
            allAppointments.add(fillAppointments(rs));
        }
        return allAppointments;
    }

    private static Appointment fillAppointments(ResultSet rs) throws SQLException {
        Appointment appointment = new Appointment();
        appointment.setId(rs.getInt("Appointment_ID"));
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

    public static void insertAppointment(Appointment appointment) throws SQLException {
        String insertString = "INSERT INTO appointments (`Appointment_ID`, `Title`, `Description`, `Location`, `Type`, " +
                "`Start`, `End`, `Create_Date`, `Created_By`, `Last_Update`, `Last_Updated_By`, `Customer_ID`, " +
                "`User_ID`, `Contact_ID`)" +
                " VALUES(?,?,?,?,?,?,?,now(), ?, now(), ?,?,?,?)";
        DBQuery.setPrepareStatement(DBConnection.getConnection(),insertString);
        PreparedStatement ps = DBQuery.getPrepareStatement();

        ps.setInt(1, appointment.getId());
        ps.setString(2, appointment.getTitle());
        ps.setString(3, appointment.getDescription());
        ps.setString(4, appointment.getLocation());
        ps.setString(5, appointment.getType());
        ps.setTimestamp(6, appointment.getStart());
        ps.setTimestamp(7,appointment.getEnd());
        ps.setString(8, LoggedInUser.getUserName());
        ps.setString(9, LoggedInUser.getUserName());
        ps.setInt(10, appointment.getCustId());
        ps.setInt(11, appointment.getUserId());
        ps.setInt(12, appointment.getContactId());

        ps.execute();
    }

    public static void updateAppointment(Appointment appointment) throws SQLException{
        String updateString = "UPDATE customers set Title = ?, Description = ?, Location = ?, Type = ?, " +
                "Start = ?, End = ?, Last_Update = now(), Last_Updated_By = ?" +
                "Customer_ID = ?, User_ID = ?, Contact_ID = ? where (Appointment_ID = ?)";
        DBQuery.setPrepareStatement(DBConnection.getConnection(),updateString);
        PreparedStatement ps = DBQuery.getPrepareStatement();

        ps.setString(1, appointment.getTitle());
        ps.setString(2, appointment.getDescription());
        ps.setString(3, appointment.getLocation());
        ps.setString(4, appointment.getType());
        ps.setTimestamp(5, appointment.getStart());
        ps.setTimestamp(6,appointment.getEnd());
        ps.setString(7, LoggedInUser.getUserName());
        ps.setInt(8, appointment.getCustId());
        ps.setInt(9, appointment.getUserId());
        ps.setInt(10, appointment.getContactId());

        ps.execute();
    }

    public static void deleteAppointment(Appointment appointment) throws SQLException{
        String deleteString = "DELETE FROM appointments WHERE (Appointment_ID = " + appointment.getId() + ")";
        DBQuery.setPrepareStatement(DBConnection.getConnection(), deleteString);
        PreparedStatement ps = DBQuery.getPrepareStatement();
        ps.execute();
    }
}
