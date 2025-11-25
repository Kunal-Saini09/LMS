package bmt.Library;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBHelper {
    static final String URL = "jdbc:mysql://localhost:3306/library";
    static final String USER = "root";
    static final String PASS = "Kunal@0906";
    public static final DriverManager DriverManagerConnectionSource = null;

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}