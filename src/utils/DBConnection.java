package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String ipAddress = "//wgudb.ucertify.com/WJ06D2w";
    private static final String JDBCUrl = protocol + vendor + ipAddress;

    private static final String mySQLJDBCDriver = "com.mysql.cj.jdbc.Driver";
    private static Connection conn = null;

    private static final String username = "U06D2w";
    private static final String password = "53688729783";

    public static Connection startConnection(){
        try {
            Class.forName(mySQLJDBCDriver);
            conn = DriverManager.getConnection(JDBCUrl, username, password);
            System.out.println("Connection Successful");
        }
        catch (ClassNotFoundException | SQLException e){
            System.out.print(e.getMessage());


        }
        return conn;
    }

    public static Connection getConnection(){
        return conn;
    }

    public static void closeConnection(){
        try {
            conn.close();
        } catch (SQLException e) {
            System.out.print(e.getMessage());
        }
    }

}
