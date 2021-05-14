package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Generalized query Class. Used by all DAO classes.
 */
public class DBQuery {
    private static PreparedStatement statement;

    /**
     * Sets the prepared statement to use for queries to the database
     * @param conn connection to use for the query
     * @param sqlStatement statement to prepare
     * @throws SQLException error with the DB
     */
    public static void setPrepareStatement(Connection conn, String sqlStatement) throws SQLException {
        statement = conn.prepareStatement(sqlStatement);
    }

    public static PreparedStatement getPrepareStatement(){
        return statement;
    }

    /**
     * Retrieves all the data from the table passed to it. Has joins for both customers and appointments for finding
     * country ID for customers from the division ID, and for getting the Contact name for the appointments
     * @param table name of the table to query
     * @return resultset of the table
     * @throws SQLException error with the DB
     */
    public static ResultSet selectAllFromTable(String table) throws SQLException {
        String getAllString;
        if(table == "customers"){
            getAllString = "select * from customers inner join first_level_divisions on " +
                    "customers.Division_ID = first_level_divisions.Division_ID " +
                    "left join countries on first_level_divisions.COUNTRY_ID = countries.Country_ID";
        }
        else if(table == "appointments"){
            getAllString = "select * from appointments inner join contacts on " +
                    "appointments.Contact_ID = contacts.Contact_ID";
        }
        else {
            getAllString = "SELECT * FROM " + table;
        }
        setPrepareStatement(DBConnection.getConnection(), getAllString);
        PreparedStatement ps = getPrepareStatement();
        return ps.executeQuery();
    }

    /**
     * Queries the table for a specific condition.
     * @param selection string to look for in table. Example: (Customer_ID = 1)
     * @param table name of the table to query
     * @return resultset of the query
     * @throws SQLException error with the DB
     */
    public static ResultSet selectFromTable(String selection, String table) throws SQLException {
        String getResults = "SELECT * FROM " + table + " WHERE " + selection;
        setPrepareStatement(DBConnection.getConnection(), getResults);
        PreparedStatement ps = getPrepareStatement();
        return ps.executeQuery();
    }
}
