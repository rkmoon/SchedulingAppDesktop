package utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBQuery {
    private static PreparedStatement statement;

    public static void setPrepareStatement(Connection conn, String sqlStatement) throws SQLException {
        statement = conn.prepareStatement(sqlStatement);
    }

    public static PreparedStatement getPrepareStatement(){
        return statement;
    }







    /*public static boolean CheckLogIn(String username, String password){

        String sql = "SELECT User_Name, Password FROM users WHERE User_Name='" + username + "' AND Password='" + password + "'";
        try{
            statement = conn.createStatement();
            result = statement.executeQuery(sql);
            return result.next();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

        return false;
    }*/
}
