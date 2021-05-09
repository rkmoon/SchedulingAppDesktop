package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.User;
import utils.DBGenerics;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    private static final String tableName = "users";

    public static ObservableList<User> getAllUsers() throws SQLException {
        ObservableList<User> users = FXCollections.observableArrayList();
        ResultSet rs = DBGenerics.queryAll(tableName);
        while (rs.next()) {
            users.add(fillUsers(rs));
        }
        return users;
    }

    private static User fillUsers(ResultSet rs) throws SQLException {
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
}
