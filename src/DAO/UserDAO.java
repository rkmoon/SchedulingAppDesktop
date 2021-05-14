package DAO;

import model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class to handle all database operations with the user table
 */
public class UserDAO {

    private static final String tableName = "users";

    /**
     * Creats a user object using the resultset data
     * @param rs resultset to retrieve data from
     * @return filled user object
     * @throws SQLException error with the DB
     */
    public static User fillUsers(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserID(rs.getInt("User_ID"));
        user.setUsername(rs.getString("User_Name"));
        user.setPassword(rs.getString("Password"));
        user.setCreateDate(rs.getTimestamp("Create_Date"));
        user.setCreatedBy(rs.getString("Created_By"));
        user.setLastUpdate(rs.getTimestamp("Last_Update"));
        user.setLastUpdatedBy(rs.getString("Last_Updated_By"));
        return user;
    }

    /**
     * Gets user from the database using the username
     * @param username username of the user to get
     * @return User selected
     * @throws SQLException error with the DB
     */
    public static User getUser(String username) throws SQLException {
        ResultSet rs = DBQuery.selectFromTable("User_Name = '" + username + "'", tableName);
        rs.next();
        return fillUsers(rs);
    }
}
