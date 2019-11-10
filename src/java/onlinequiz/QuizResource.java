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
@Path("quiz")
public class QuizResource {
    
     
     String msg; 
 Date today=new Date();
 long timeStamp=today.getTime();
 JSONArray mainArray=new JSONArray();
 JSONObject mainObject = new JSONObject();
 
 DatabaseConnection databaseConn=new DatabaseConnection();


    @Context
    private UriInfo context;

    /**
     * Creates a new instance of QuizResource
     */
    public QuizResource() {
    }

    /**
     * Retrieves representation of an instance of onlinequiz.QuizResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    




@GET
            @Path("getQuizList")
    @Produces(MediaType.APPLICATION_JSON)
    public String getQuizList() {
        
        
        Connection conn = null;
              conn=  databaseConn.getConnection(conn);
              JSONObject singleJob=new JSONObject();
         
         
        try {
                   String sql;
                sql = "SELECT QUIZID,CATID,TITLE,PASSINGSCORE,DURATION FROM QUIZ";
    
             PreparedStatement stm = conn.prepareStatement(sql);
                ResultSet rs=stm.executeQuery();
           
            
       
    
    while(rs.next()){
//Retrieve by column name
    int quiz_id = rs.getInt("QUIZID");
    int cat_id = rs.getInt("CATID");
    String title = rs.getString("TITLE");
    int passingScore = rs.getInt("PASSINGSCORE");
    int duration = rs.getInt("DURATION");
   
//Display values

     singleJob.accumulate("QUIZID", quiz_id);
        singleJob.accumulate("CATID", cat_id);
        singleJob.accumulate("TITLE", title);
        singleJob.accumulate("PASSINGSCORE", passingScore);
        singleJob.accumulate("DURATION", duration+" minutes");
        mainArray.add(singleJob);
        singleJob.clear();

    }
     mainObject.accumulate("status", "ok");
        mainObject.accumulate("Timestamp", timeStamp);
    mainObject.accumulate("Quizes", mainArray);
    
    
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
            @Path("getSingleQuiz&{QUIZID}&{CATID}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getSingleQuiz(@PathParam("QUIZID") String quiz_id,@PathParam("CATID") String cat_id) {
        
        
        JSONObject singleQuiz =new JSONObject();
        mainObject.clear();
        mainArray.clear();
        
        
        Connection conn = null;
              conn=  databaseConn.getConnection(conn);
         
        try {          
            
            
            
              String sql;
    sql = "SELECT TITLE,PASSINGSCORE,DURATION FROM QUIZ WHERE QUIZID=? and CATID=?";
    
   
   
      PreparedStatement stm = conn.prepareStatement(sql);
                stm.setString(1,quiz_id);
                stm.setString(2,cat_id);

                ResultSet rs=stm.executeQuery();

                while(rs.next()) {
      String title = rs.getString("TITLE");
       int passingScore = rs.getInt("PASSINGSCORE");
    int duration = rs.getInt("DURATION");
      
   
     //String user_password = rs.getString("PASSWORD");
   
//Display values
    singleQuiz.accumulate("status", "ok");
        singleQuiz.accumulate("Timestamp", timeStamp);
        singleQuiz.accumulate("TITLE", title);
        singleQuiz.accumulate("PASSINGSCORE", passingScore);
        singleQuiz.accumulate("DURATION", duration+" minutes");
    
          //singleUser.accumulate("PASSWORD", user_password);
     
    }
                     databaseConn.closeConnection(conn,rs,stm);


        }
        catch (SQLException ex) {
                       msg=ex.getMessage();
                      
                    } catch (Exception ex) {
              msg=ex.getMessage();
          }
 
        if(singleQuiz.toString().equals("{}"))
        {
              if(msg==null)
               msg="QuizID - '"+quiz_id+ "' and CategoryID - '"+cat_id+"' Record not found";
                   
             singleQuiz.accumulate("Status", "error");
        singleQuiz.accumulate("Timestamp", timeStamp);
         //singleQuiz.accumulate("Quiz", quiz_id);
        singleQuiz.accumulate("Msg",  msg);
       }
        
         return singleQuiz.toString();
         
    }
    
}