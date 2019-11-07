/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package onlinequiz;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 *
 * @author kulartist
 */
public class DatabaseConnection {
    
     static String classs = "oracle.jdbc.OracleDriver";
     static String url = "jdbc:oracle:thin:@144.217.163.57:1521:XE";
     static String un = "mad312team1";
     static String password = "anypw";    
    
   
    public Connection getConnection(Connection conn) {
      
        try {
             Class.forName(classs);
            conn = DriverManager.getConnection(url, un, password);
        } catch (SQLException ex) {
            //Logger.getLogger(ShapesAny.class.getName()).log(Level.SEVERE, null, ex);
            //return ex.getMessage();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(QuizResource.class.getName()).log(Level.SEVERE, null, ex);
           // return ex.getMessage();
        }

         return conn;
        
    }
    
    
    
    
     public void closeConnection(Connection conn, ResultSet rs,PreparedStatement ps )  {
         
         
        try {
                        if (rs != null)
                            rs.close();
                        if (ps != null)
                            ps.close();
                        if (conn != null)
                            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(QuizResource.class.getName()).log(Level.SEVERE, null, ex);
        }
         
     }
    
    
    
    
    
    
    
    
}
