/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package onlinequiz;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;


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
 * @author padda
 */
@Path("quizCategories")
public class CategoryResource {
    
      
    String msg; 
 Date today=new Date();
 long timeStamp=today.getTime();
 JSONArray mainArray=new JSONArray();
 JSONObject mainObject = new JSONObject();
 
 DatabaseConnection databaseConn=new DatabaseConnection();

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of CategoryResource
     */
    public CategoryResource() {
    }

    /**
     * Retrieves representation of an instance of onlinequiz.CategoryResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

   










@GET
            @Path("getCategoryList")
    @Produces(MediaType.APPLICATION_JSON)
    public String getCategoryList() {
        
        
        Connection conn = null;
              conn=  databaseConn.getConnection(conn);
              JSONObject singleJob=new JSONObject();
              String userName="";
         
         
        try {
                   String sql;
                sql = "SELECT CATID,CATNAME FROM CATEGORY";
    
             PreparedStatement stm = conn.prepareStatement(sql);
                ResultSet rs=stm.executeQuery();
           
            
       
    
    while(rs.next()){
//Retrieve by column name
    int cat_id = rs.getInt("CATID");
    String cat_name = rs.getString("CATNAME");
   
//Display values

     singleJob.accumulate("CATID", cat_id);
        singleJob.accumulate("CATNAME", cat_name);
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
            @Path("getSingleCategory&{CATID}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getSingleCategory(@PathParam("CATID") String cat_id) {
        
        
        JSONObject singleUser =new JSONObject();
        mainObject.clear();
        mainArray.clear();
        
        
        Connection conn = null;
              conn=  databaseConn.getConnection(conn);
         
        try {          
            
            
            
              String sql;
    sql = "SELECT CATNAME FROM CATEGORY WHERE CATID=?";
    
   
   
      PreparedStatement stm = conn.prepareStatement(sql);
                stm.setString(1,cat_id);

                ResultSet rs=stm.executeQuery();

                while(rs.next()) {
      String cat_name = rs.getString("CATNAME");
   
     //String user_password = rs.getString("PASSWORD");
   
//Display values
    singleUser.accumulate("status", "ok");
        singleUser.accumulate("Timestamp", timeStamp);
  
     singleUser.accumulate("CATNAME", cat_name);
    
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
               msg="ID - '"+cat_id+"' Record not found";
                   
             singleUser.accumulate("Status", "error");
        singleUser.accumulate("Timestamp", timeStamp);
         singleUser.accumulate("CATID", cat_id);
        singleUser.accumulate("Msg",  msg);
       }
        
         return singleUser.toString();
         
    }
    
}
