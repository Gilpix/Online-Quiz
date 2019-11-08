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
 * @author anjupaul
 */
@Path("questions")
public class QuestionsResource {
    
    
    
     String msg; 
 Date today=new Date();
 long timeStamp=today.getTime();
 JSONArray mainArray=new JSONArray();
 JSONObject mainObject = new JSONObject();
 
 DatabaseConnection databaseConn=new DatabaseConnection();

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of QuestionsResource
     */
    public QuestionsResource() {
    }

    /**
     * Retrieves representation of an instance of onlinequiz.QuestionsResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public String getXml() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

   




@GET
            @Path("getQuestionList")
    @Produces(MediaType.APPLICATION_JSON)
    public String getQuestionList() {
        
        
        Connection conn = null;
              conn=  databaseConn.getConnection(conn);
              JSONObject singleJob=new JSONObject();
         
         
        try {
                   String sql;
                sql = "SELECT QUESTIONID,QUESTIONTEXT FROM QUESTION";
    
             PreparedStatement stm = conn.prepareStatement(sql);
                ResultSet rs=stm.executeQuery();
           
            
       
    
    while(rs.next()){
//Retrieve by column name
    int cat_id = rs.getInt("QUESTIONID");
    String cat_name = rs.getString("QUESTIONTEXT");
   
//Display values

     singleJob.accumulate("QUESTIONID", cat_id);
        singleJob.accumulate("QUESTIONTEXT", cat_name);
        mainArray.add(singleJob);
        singleJob.clear();

    }
     mainObject.accumulate("status", "ok");
        mainObject.accumulate("Timestamp", timeStamp);
    mainObject.accumulate("Questions", mainArray);
    
    
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
            @Path("getSingleQuestion&{QUESTIONID}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getSingleQuestion(@PathParam("QUESTIONID") String que_id) {
        
        
        JSONObject singleUser =new JSONObject();
        mainObject.clear();
        mainArray.clear();
        
        
        Connection conn = null;
              conn=  databaseConn.getConnection(conn);
         
        try {          
            
            
            
              String sql;
    sql = "SELECT QUESTIONTEXT FROM QUESTION WHERE QUESTIONID=?";
    
   
   
      PreparedStatement stm = conn.prepareStatement(sql);
                stm.setString(1,que_id);

                ResultSet rs=stm.executeQuery();

                while(rs.next()) {
      String cat_name = rs.getString("QUESTIONTEXT");
   
     //String user_password = rs.getString("PASSWORD");
   
//Display values
    singleUser.accumulate("status", "ok");
        singleUser.accumulate("Timestamp", timeStamp);
  
     singleUser.accumulate("QUESTIONTEXT", cat_name);
    
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
               msg="ID - '"+que_id+"' Record not found";
                   
             singleUser.accumulate("Status", "error");
        singleUser.accumulate("Timestamp", timeStamp);
         singleUser.accumulate("QUESTIONTEXT", que_id);
        singleUser.accumulate("Msg",  msg);
       }
        
         return singleUser.toString();
         
    }
    
}
