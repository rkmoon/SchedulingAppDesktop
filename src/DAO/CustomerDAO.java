package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;
import utils.DBGenerics;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDAO {

    private static final String tableName = "customers";

    public static ObservableList<Customer> getAllCustomers() throws SQLException {
        ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
        ResultSet rs = DBGenerics.queryAll(tableName);
        while(rs.next()){
            allCustomers.add(fillCustomer(rs));
        }

        return allCustomers;
    }

    private static Customer fillCustomer(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setId(rs.findColumn("Customer_ID"));
        customer.setName(rs.getString("Customer_Name"));
        customer.setAddress(rs.getString("Address"));
        customer.setPostCode(rs.getString("Postal_Code"));
        customer.setPhoneNum(rs.getString("Phone"));
        customer.setCreateDate(rs.getTimestamp("Create_Date"));
        customer.setCreateBy(rs.getString("Created_By"));
        customer.setLastUpdate(rs.getTimestamp("Last_Update"));
        customer.setLastUpdateBy(rs.getString("Last_Updated_By"));
        customer.setDivID(rs.findColumn("Division_ID"));
        return customer;
    }
}
