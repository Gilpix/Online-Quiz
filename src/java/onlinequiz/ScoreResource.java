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
@Path("score")
public class ScoreResource {
    
    
     String msg; 
 Date today=new Date();
 long timeStamp=today.getTime();
 JSONArray mainArray=new JSONArray();
 JSONObject mainObject = new JSONObject();
 
 DatabaseConnection databaseConn=new DatabaseConnection();

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of ScoreResource
     */
    public ScoreResource() {
    }

    /**
     * Retrieves representation of an instance of onlinequiz.ScoreResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

   




@GET
            @Path("getScoreList")
    @Produces(MediaType.APPLICATION_JSON)
    public String getScoreList() {
        
        
        Connection conn = null;
              conn=  databaseConn.getConnection(conn);
              JSONObject singleScore=new JSONObject();
         
         
        try {
                   String sql;
                sql = "SELECT USERID,QUIZID,MARKS FROM SCORE";
    
             PreparedStatement stm = conn.prepareStatement(sql);
                ResultSet rs=stm.executeQuery();
           
            
       
    
    while(rs.next()){
//Retrieve by column name
    int quiz_id = rs.getInt("QUIZID");
    String user_id = rs.getString("USERID");
    int marks = rs.getInt("MARKS");
   
//Display values

     singleScore.accumulate("USERID", user_id);
        singleScore.accumulate("QUIZID", quiz_id);
        singleScore.accumulate("MARKS", marks);
        mainArray.add(singleScore);
        singleScore.clear();

    }
     mainObject.accumulate("status", "ok");
        mainObject.accumulate("Timestamp", timeStamp);
    mainObject.accumulate("Scores", mainArray);
    
    
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
            @Path("getSingleQuizScore&{USERID}&{QUIZID}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getSingleQuizScore(@PathParam("USERID") String user_id,@PathParam("QUIZID") int quiz_id) {
        
        
        JSONObject singleScore =new JSONObject();
        mainObject.clear();
        mainArray.clear();
        
        
        Connection conn = null;
              conn=  databaseConn.getConnection(conn);
         
        try {          
            
            
            
              String sql;
    sql = "SELECT MARKS FROM SCORE WHERE USERID=? and QUIZID=?";
    
   
   
      PreparedStatement stm = conn.prepareStatement(sql);
                stm.setInt(2,quiz_id);
                stm.setString(1,user_id);

                ResultSet rs=stm.executeQuery();

                while(rs.next()) {
    int marks = rs.getInt("MARKS");
      
   
     //String user_password = rs.getString("PASSWORD");
   
//Display values
    singleScore.accumulate("status", "ok");
        singleScore.accumulate("Timestamp", timeStamp);
        singleScore.accumulate("MARKS", marks);
    
          //singleUser.accumulate("PASSWORD", user_password);
     
    }
                     databaseConn.closeConnection(conn,rs,stm);


        }
        catch (SQLException ex) {
                       msg=ex.getMessage();
                      
                    } catch (Exception ex) {
              msg=ex.getMessage();
          }
 
        if(singleScore.toString().equals("{}"))
        {
              if(msg==null)
               msg="UserID - '"+user_id+ "' and QuizID - '"+quiz_id+"' Record not found";
                   
             singleScore.accumulate("Status", "error");
        singleScore.accumulate("Timestamp", timeStamp);
         //singleQuiz.accumulate("Quiz", quiz_id);
        singleScore.accumulate("Msg",  msg);
       }
        
         return singleScore.toString();
         
    }
    
}