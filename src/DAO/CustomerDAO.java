package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDAO {

    private static final String tableName = "customers";
    private static final String custIdName = "Customer_ID";

    public static ObservableList<Customer> getAllCustomers() throws SQLException {
        ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
        ResultSet rs = DBQuery.selectAllFromTable(tableName);
        while(rs.next()){
            allCustomers.add(fillCustomer(rs));
        }

        return allCustomers;
    }

    public static Customer findCustomer(int id) throws Exception {
        Customer customerToFind = new Customer();
        ResultSet rs = DBQuery.selectFromTable(custIdName + " = " + id, tableName);

        if(!rs.next()){
            throw new Exception("Customer not found");
        }
        else {
            customerToFind = fillCustomer(rs);
        }
        return customerToFind;

    }

    public static void insertCustomer(Customer customer){

    }

    private static Customer fillCustomer(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setId(rs.getInt("Customer_ID"));
        customer.setName(rs.getString("Customer_Name"));
        customer.setAddress(rs.getString("Address"));
        customer.setPostCode(rs.getString("Postal_Code"));
        customer.setPhoneNum(rs.getString("Phone"));
        customer.setCreateDate(rs.getTimestamp("Create_Date"));
        customer.setCreateBy(rs.getString("Created_By"));
        customer.setLastUpdate(rs.getTimestamp("Last_Update"));
        customer.setLastUpdateBy(rs.getString("Last_Updated_By"));
        customer.setDivID(rs.getInt("Division_ID"));
        customer.setCountry(rs.getString("Country"));
        customer.setDivision(rs.getString("Division"));
        return customer;
    }
}
