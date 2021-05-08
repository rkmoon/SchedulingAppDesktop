package view_controller;


import DAO.AppointmentDAO;
import DAO.CustomerDAO;
import DAO.DBConnection;
import DAO.DBQuery;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Appointment;
import model.Customer;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerWindowController {

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
    public void initialize() throws SQLException {
        populateCustTable();
        populateAppTable();
    }


    private void populateCustTable() throws SQLException {
        FilteredList<Customer> customers = new FilteredList<>(CustomerDAO.getAllCustomers());
        custIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        custNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        custAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        custPostCol.setCellValueFactory(new PropertyValueFactory<>("postCode"));
        custPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));
        custTable.setItems(customers);
    }

    private void populateAppointments() throws SQLException{
        String getAllAppString = "SELECT * FROM appointments";
        DBQuery.setPrepareStatement(DBConnection.getConnection(), getAllAppString);
        PreparedStatement ps = DBQuery.getPrepareStatement();
        ResultSet rs = ps.executeQuery();
        //while(rs.next()){
            /*Appointment newAppointment = new Appointment(rs.getInt(1), rs.getString(2),
                                                        rs.getString(3), rs.getString(4),
                                                        rs.getString(14), rs.getString(5),
                                                        rs.getDate(6), rs.getDate(7),
                                                        rs.getInt(12));*/
            //ObjectArrays.addAppointment(newAppointment);

        //}
    }

    private void populateAppTable() throws SQLException {
        FilteredList<Appointment> appointments = new FilteredList<>(AppointmentDAO.getAllAppointments());
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

}