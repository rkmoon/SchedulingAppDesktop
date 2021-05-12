package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBQuery {
    private static PreparedStatement statement;

    public static void setPrepareStatement(Connection conn, String sqlStatement) throws SQLException {
        statement = conn.prepareStatement(sqlStatement);
    }

    public static PreparedStatement getPrepareStatement(){
        return statement;
    }


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

    public static ResultSet selectFromTable(String selection, String table) throws SQLException {
        String getResults = "SELECT * FROM " + table + " WHERE " + selection;
        setPrepareStatement(DBConnection.getConnection(), getResults);
        PreparedStatement ps = getPrepareStatement();
        return ps.executeQuery();
    }
}
