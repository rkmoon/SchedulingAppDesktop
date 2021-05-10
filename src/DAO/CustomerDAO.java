package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;

import java.sql.PreparedStatement;
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
        Customer customerToFind;
        ResultSet rs = DBQuery.selectFromTable(custIdName + " = " + id, tableName);

        if(!rs.next()){
            throw new Exception("Customer not found");
        }
        else {
            customerToFind = fillCustomer(rs);
        }
        return customerToFind;

    }

    public static void insertCustomer(Customer customer) throws SQLException {
        String insertString = "INSERT INTO customers (Customer_ID, Customer_Name, Address, Postal_Code, Phone, " +
                                                    "Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID)" +
                                                    " VALUES(?,?,?,?,?,now(),'test',now(),'test',?)";
        DBQuery.setPrepareStatement(DBConnection.getConnection(),insertString);
        PreparedStatement ps = DBQuery.getPrepareStatement();

        ps.setInt(1, customer.getId());
        ps.setString(2, customer.getName());
        ps.setString(3, customer.getAddress());
        ps.setString(4, customer.getPostCode());
        ps.setString(5, customer.getPhoneNum());
        ps.setInt(6, customer.getDivID());
        ps.execute();
    }

    public static void deleteCustomer(Customer customer) throws SQLException {
        String deleteString = "DELETE FROM customers WHERE (Customer_ID = " + customer.getId() + ")";
        //System.out.println(deleteString);
        DBQuery.setPrepareStatement(DBConnection.getConnection(), deleteString);
        PreparedStatement ps = DBQuery.getPrepareStatement();
        ps.execute();
    }

    public static void updateCustomer(Customer customer) throws SQLException{
        String updateString = "UPDATE customers set Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Last_Update = now()," +
                                                    "Last_Updated_By = 'test', Division_ID = ? where (Customer_ID = ?)";
        DBQuery.setPrepareStatement(DBConnection.getConnection(), updateString);
        PreparedStatement ps = DBQuery.getPrepareStatement();

        ps.setString(1,customer.getName());
        ps.setString(2, customer.getAddress());
        ps.setString(3, customer.getPostCode());
        ps.setString(4, customer.getPhoneNum());
        ps.setInt(5,customer.getDivID());
        ps.setInt(6,customer.getId());

        ps.execute();
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
