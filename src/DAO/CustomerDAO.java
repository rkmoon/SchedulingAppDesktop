package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDAO {

    public static ObservableList<Customer> getAllCustomers() throws SQLException {
        ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
        String getAllCustomersString = "SELECT * FROM customers";
        DBQuery.setPrepareStatement(DBConnection.getConnection(), getAllCustomersString);
        PreparedStatement ps = DBQuery.getPrepareStatement();
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            allCustomers.add(fillCustomer(rs));
            /*Customer newCustomer = new Customer(rs.getInt(1), rs.getString(2),
                    rs.getString(3), rs.getString(4),
                    rs.getString(5));*/

            //ObjectArrays.addCustomer(newCustomer);
        }

        DBConnection.closeConnection();
        return allCustomers;
    }

    public static Customer fillCustomer(ResultSet rs) throws SQLException {
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
