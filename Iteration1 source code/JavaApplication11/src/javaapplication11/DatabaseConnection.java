/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication11;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author sunny
 */
public class DatabaseConnection {
    private static String url = "jdbc:derby://localhost:1527/Music";    
    private static String driverName = "org.apache.derby.jdbc.ClientDriver";   
    private static String username = "project";   
    private static String password = "sunny1166";
    private static Connection con;
    private static String urlstring;

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName(driverName);
            try {
                con = DriverManager.getConnection(url, username, password);
            } catch (SQLException ex) {
                // log an exception. fro example:
                System.out.println("Failed to create the database connection."); 
            }
        } catch (ClassNotFoundException ex) {
            // log an exception. for example:
            System.out.println("Driver not found."); 
        }
        return con;
    }
}
