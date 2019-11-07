/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package onlinequiz;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * REST Web Service
 *
 * @author kulartist
 */
@Path("quizuser")
public class QuizUserResource {
    
    
    String msg; 
 Date today=new Date();
 long timeStamp=today.getTime();
 JSONArray mainArray=new JSONArray();
 JSONObject mainObject = new JSONObject();
 
 DatabaseConnection databaseConn=new DatabaseConnection();
 
    

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of QuizUserResource
     */
    public QuizUserResource() {
    }

    /**
     * Retrieves representation of an instance of onlinequiz.QuizUserResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public String getXml() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }
    
    
    
    
    
    
    @GET
    @Path("addUser&{USERID}&{FNAME}&{LNAME}&{PASSWORD}")
    @Produces(MediaType.APPLICATION_JSON)
    public String addUser(@PathParam("USERID") String user_id, @PathParam("FNAME") String fname, @PathParam("LNAME") String lname, @PathParam("PASSWORD") String password) {
        
        Connection conn = null;
              conn=  databaseConn.getConnection(conn);
              int qRes=0;
         
        try {           
            
              String sql;
    sql = "INSERT INTO QUIZUSER VALUES(?,?,?,?)";
    
   
      PreparedStatement stm = conn.prepareStatement(sql);
                stm.setString(1,user_id);
                stm.setString(2,fname);
                stm.setString(3,lname);
                stm.setString(4,password);
                

                  qRes=stm.executeUpdate();
                  if(qRes==1)
                  {
                   mainObject.accumulate("Status", "ok");
                    mainObject.accumulate("Timestamp", timeStamp);
                  mainObject.accumulate("Msg", "Sucessfully Added User");
                  }

    
                  databaseConn.closeConnection(conn,null,stm);
            
        } catch (SQLException ex) {
            msg=ex.getMessage();
        }
        
         if(qRes!=1)
        {
            mainObject.clear();
        mainObject.accumulate("Status", "error");
        mainObject.accumulate("Timestamp", timeStamp);
        mainObject.accumulate("Msg", "Not Inserted - "+ msg);
       }
         
         return mainObject.toString();
         
    }
    
    
    
    
    
    
    
    
     @GET
            @Path("getUserList")
    @Produces(MediaType.APPLICATION_JSON)
    public String getUserList() {
        
        
        Connection conn = null;
              conn=  databaseConn.getConnection(conn);
              JSONObject singleJob=new JSONObject();
              String userName="";
         
         
        try {
                   String sql;
                sql = "SELECT USERID,FNAME,LNAME,PASSWORD FROM QUIZUSER";
    
             PreparedStatement stm = conn.prepareStatement(sql);
                ResultSet rs=stm.executeQuery();
           
            
       
    
    while(rs.next()){
//Retrieve by column name
    String user_id = rs.getString("USERID");
    String user_fname = rs.getString("FNAME");
    String user_lname = rs.getString("LNAME");
     String user_password = rs.getString("PASSWORD");
     userName=user_fname.substring(0,1)+user_lname;
//Display values

      singleJob.accumulate("USERNAME", userName);
     singleJob.accumulate("USERID", user_id);
        singleJob.accumulate("FNAME", user_fname);
          singleJob.accumulate("LNAME", user_lname);
          singleJob.accumulate("PASSWORD",user_password.substring(0, 1)+"*****"+user_password.substring(user_password.length()-1));
        mainArray.add(singleJob);
        singleJob.clear();

    }
     mainObject.accumulate("status", "ok");
        mainObject.accumulate("Timestamp", timeStamp);
    mainObject.accumulate("Users", mainArray);
    
    
     databaseConn.closeConnection(conn,rs,stm);
            
        } catch (Exception ex) {
            msg=ex.getMessage();
        }
        
        if(mainArray.isEmpty()||msg!=null)
        {
            mainObject.clear();
              
               if(mainArray.isEmpty() && msg==null)
                  msg="Main array is empty";
                   
             mainObject.accumulate("Status", "error");
        mainObject.accumulate("Timestamp", timeStamp);
        mainObject.accumulate("Msg",  msg);
       }
       
 
         return mainObject.toString();
         
    }
    
    
    
    
    
    
      @GET
            @Path("getSingleUser&{USERID}&{PASSWORD}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getSingleUser(@PathParam("USERID") String usr_id,@PathParam("PASSWORD") String usr_password) {
        
        
        JSONObject singleUser =new JSONObject();
        mainObject.clear();
        mainArray.clear();
        
        
        Connection conn = null;
              conn=  databaseConn.getConnection(conn);
         
        try {          
            
            
            
              String sql;
    sql = "SELECT USERID,FNAME,LNAME FROM QUIZUSER WHERE USERID=? and PASSWORD=?";
    
   
   
      PreparedStatement stm = conn.prepareStatement(sql);
                stm.setString(1,usr_id);
                 stm.setString(2,usr_password);

                ResultSet rs=stm.executeQuery();

                while(rs.next()) {
      String user_id = rs.getString("USERID");
    String user_fname = rs.getString("FNAME");
    String user_lname = rs.getString("LNAME");
     //String user_password = rs.getString("PASSWORD");
     String userName=user_fname.substring(0,1)+user_lname;
//Display values
    singleUser.accumulate("status", "ok");
        singleUser.accumulate("Timestamp", timeStamp);
          singleUser.accumulate("USERNAME", userName);
     singleUser.accumulate("USERID", user_id);
        singleUser.accumulate("FNAME", user_fname);
          singleUser.accumulate("LNAME", user_lname);
          //singleUser.accumulate("PASSWORD", user_password);
     
    }
                     databaseConn.closeConnection(conn,rs,stm);


        }
        catch (SQLException ex) {
                       msg=ex.getMessage();
                      
                    } catch (Exception ex) {
              msg=ex.getMessage();
          }
 
        if(singleUser.toString().equals("{}"))
        {
              if(msg==null)
               msg="ID - '"+usr_id+"' Record not found";
                   
             singleUser.accumulate("Status", "error");
        singleUser.accumulate("Timestamp", timeStamp);
         singleUser.accumulate("USERID", usr_id);
        singleUser.accumulate("Msg",  msg);
       }
        
         return singleUser.toString();
         
    }
    
}
