/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 *
 * @author tw
 */
public class DbConnectionPools extends Object {
    
    DbConnectionPools() throws NamingException {
//        InitialContext ctx = new InitialContext();
//        ds = (DataSource)ctx.lookup("jdbc/login");
    }
    public static Connection getPoolConnection(){
        try{
           Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ehr", "ehr", "123456");
//            Connection connection = DriverManager.getConnection("jdbc:mysql://74.208.128.169:3306/PHR", "root", "Rr7TNS7PrR");
//            Connection connection = DriverManager.getConnection("jdbc:mysql://74.208.128.169:3306/ehr", "root", "Rr7TNS7PrR");

            return connection; 
            }
            catch(Exception e){
                System.out.println("ERROR connection " + e);
                e.printStackTrace();
                return null;
            }
        }
    public static void closeResources(Connection conn, PreparedStatement stmt) {
         try {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        } 
        catch (SQLException e) {}
    }
    
}
