package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;
import utils.LoggedInUser;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class to handle all database operations dealing with customers
 */
public class CustomerDAO {

    private static final String tableName = "customers";
    private static final String custIdName = "Customer_ID";

    /**
     * Retrieves all customers from the database
     * @return list of all customers
     * @throws SQLException error with the DB
     */
    public static ObservableList<Customer> getAllCustomers() throws SQLException {
        ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
        ResultSet rs = DBQuery.selectAllFromTable(tableName);
        while(rs.next()){
            allCustomers.add(fillCustomer(rs));
        }

        return allCustomers;
    }


    /**
     * Inserts customer into the database
     * @param customer customer to insert
     * @throws SQLException error with the DB
     */
    public static void insertCustomer(Customer customer) throws SQLException {
        String insertString = "INSERT INTO customers (Customer_ID, Customer_Name, Address, Postal_Code, Phone, " +
                                                    "Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID)" +
                                                    " VALUES(?,?,?,?,?,now(),?,now(),?,?)";
        DBQuery.setPrepareStatement(DBConnection.getConnection(),insertString);
        PreparedStatement ps = DBQuery.getPrepareStatement();

        ps.setInt(1, customer.getId());
        ps.setString(2, customer.getName());
        ps.setString(3, customer.getAddress());
        ps.setString(4, customer.getPostCode());
        ps.setString(5, customer.getPhoneNum());
        ps.setString(6, LoggedInUser.getUserName());
        ps.setString(7, LoggedInUser.getUserName());
        ps.setInt(8, customer.getDivID());
        ps.execute();
    }

    /**
     * Deletes a customer from the database using the unique customer ID to find the correct customer to delete
     * @param customer customer to delete
     * @throws SQLException error with the DB
     */
    public static void deleteCustomer(Customer customer) throws SQLException {
        String deleteString = "DELETE FROM customers WHERE (Customer_ID = " + customer.getId() + ")";
        DBQuery.setPrepareStatement(DBConnection.getConnection(), deleteString);
        PreparedStatement ps = DBQuery.getPrepareStatement();
        ps.execute();
    }

    /**
     * Updates a customer in the database using the unique customer ID to identify which customer to update
     * @param customer customer to update
     * @throws SQLException error with the DB
     */
    public static void updateCustomer(Customer customer) throws SQLException{
        String updateString = "UPDATE customers set Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Last_Update = now()," +
                                                    "Last_Updated_By = ?, Division_ID = ? where (Customer_ID = ?)";
        DBQuery.setPrepareStatement(DBConnection.getConnection(), updateString);
        PreparedStatement ps = DBQuery.getPrepareStatement();

        ps.setString(1,customer.getName());
        ps.setString(2, customer.getAddress());
        ps.setString(3, customer.getPostCode());
        ps.setString(4, customer.getPhoneNum());
        ps.setString(5,LoggedInUser.getUserName());
        ps.setInt(6,customer.getDivID());
        ps.setInt(7,customer.getId());

        ps.execute();
    }

    /**
     * Creates a Customer object using the information from the resultset
     * @param rs resultset to retrieve information from
     * @return filled customer object
     * @throws SQLException error with the DB
     */
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
