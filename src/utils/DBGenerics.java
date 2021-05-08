package utils;

import DAO.DBConnection;
import DAO.DBQuery;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBGenerics {

    public static ResultSet queryAll(String table) throws SQLException {
        String getAllString = "SELECT * FROM " + table;
        DBQuery.setPrepareStatement(DBConnection.getConnection(), getAllString);
        PreparedStatement ps = DBQuery.getPrepareStatement();
        return ps.executeQuery();
    }
}
