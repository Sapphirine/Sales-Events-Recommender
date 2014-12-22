package dbAPI;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.*;

public class rds {

	/*
	 * retrieve *************************************************************************************************
	 */
    public synchronized JSONObject[] retrieve(String table_name, String select_attributes) throws JSONException {
    	init(); // initialize DB
    	String sql="";
    	JSONObject[] result;
    	JSONObject attributes=new JSONObject(select_attributes);
		PreparedStatement ps=null;
		ResultSet rs=null;
    	
    	switch (table_name){
    		case "retailer":
    			sql= "select * from " + table_name + " where retailer_name=?";
    			try{
    				ps=conn.prepareStatement(sql);
    				ps.setString(1, attributes.getString("name"));
    				System.out.println("ps" + ps);
    				rs=ps.executeQuery();
    
    				// encode result set to json array
    				//System.out.println("test result set, cursor 1: " + rs);
    				rs.last();
    				int rs_size=rs.getRow();
    				System.out.println("size of result set: " + rs_size);
    				result=new JSONObject[rs_size];
    				rs.beforeFirst(); 
    				//System.out.println("test result set, cursor 2: " + rs);
    				
    				int count=0;
    	            while(rs.next()){
    	            	String retailer_name = rs.getString("retailer_name");
    	                String password = rs.getString("password");             
    	                System.out.println("name: " + retailer_name + " password: " + password);
    	                result[count]=new JSONObject();
    					result[count].put("password", password);
    	                count++;
    	            }
    				return result;
    			}catch(Exception e){
    				e.printStackTrace();
    				return null;
    			}finally{
    				try{
    					if(rs!=null){
    						rs.close();
    						rs=null;
    					}
    					if(ps!=null){
    						ps.close();
    						ps=null;
    					}
    					if(conn!=null){
    						conn.close();
    						conn=null;
    					}
    				}catch(SQLException e){
    					e.printStackTrace();
    				}
    			}
    		case "twitter_user":
    			sql = "select * from " + table_name + " where user_id = ?";
    			try{
    				ps=conn.prepareStatement(sql);
    				ps.setString(1, attributes.getString("name"));
    				System.out.println("ps" + ps);
    				rs=ps.executeQuery();
    
    				// encode result set to json array
    				rs.last(); int rs_size=rs.getRow(); result=new JSONObject[rs_size]; rs.beforeFirst();
    				
    				int count=0;
    	            while(rs.next()){
    	            	Integer user_name = rs.getInt("user_id");
    	                String password = rs.getString("password");             
    	                System.out.println("name: " + user_name + " password: " + password);
    	                result[count]=new JSONObject();
    					result[count].put("password", password);
    	                count++;
    	            }
    				return result;
    				  				
    			}catch(Exception e){
    				e.printStackTrace();
    				return null;
    			}finally{
    				try{
    					if(rs!=null){
    						rs.close();
    						rs=null;
    					}
    					if(ps!=null){
    						ps.close();
    						ps=null;
    					}
    					if(conn!=null){
    						conn.close();
    						conn=null;
    					}
    				}catch(SQLException e){
    					e.printStackTrace();
    				}
    			}
    		case "product":
    			
    			
    			
    			
    		default: 
    			return null;
    	}    	
    }
    /*
     * insert *************************************************************************************************
     */
    public synchronized boolean insert(String table_name, String select_attributes) throws JSONException {
    	init();
    	String sql="";
    	JSONObject attributes=new JSONObject(select_attributes);
    	PreparedStatement ps=null;
    	switch(table_name){
    		case "retailer":
    			try{
    	    		sql="insert into " + table_name + " values (?, ?)";
    				ps=conn.prepareStatement(sql);
    				ps.setString(1, attributes.getString("name"));
    				ps.setString(2, attributes.getString("password"));
    				ps.executeUpdate();
    				ps.close();
    				return true;
    			}catch(Exception e){
    				e.printStackTrace();
    				return false;
    			}finally{
    				try{
    					if(ps!=null){
    						ps.close();
    						ps=null;
    					}
    					if(conn!=null){
    						conn.close();
    						conn=null;
    					}
    				}catch(SQLException e){
    					e.printStackTrace();
    				}
    			}
    		case "twitter_user":
    			try{
    	    		sql="insert into " + table_name + " values (?, ?)";
    				ps=conn.prepareStatement(sql);
    				ps.setInt(1, Integer.parseInt(attributes.getString("name")));
    				ps.setString(2, attributes.getString("password"));
    				ps.executeUpdate();
    				return true;
    			}catch(Exception e){
    				e.printStackTrace();
    				return false;
    			}finally{
    				try{
    					if(ps!=null){
    						ps.close();
    						ps=null;
    					}
    					if(conn!=null){
    						conn.close();
    						conn=null;
    					}
    				}catch(SQLException e){
    					e.printStackTrace();
    				}
    			}
    			default:
    				return false;
    	}
    }
    
    /*
     * update *************************************************************************************************
     */
    public synchronized boolean update(String table_name, String update_attributes) throws JSONException{
    	init();
    	String sql = null;
    	JSONObject attributes=new JSONObject(update_attributes);
    	PreparedStatement ps=null;
    	boolean r;
    	
    	switch(table_name){
    		case "retailer":
    			//update retailer set retailer.password='123456' where retailer.retailer_name='qifei';
    			sql="update retailer set retailer.password=? where retailer.retailer_name=?";
    			try{
    				ps=conn.prepareStatement(sql);
    				ps.setString(1, attributes.getString("password"));
    				ps.setString(2, attributes.getString("name"));
    				System.out.println("update sql: " + ps);
    				ps.executeUpdate();
    				r=true;
    				break;
    			}catch(Exception e){
    				e.printStackTrace();
    				r=false;
    			}finally{
    				try{
    					if(ps!=null){
    						ps.close();
    						ps=null;
    					}
    					if(conn!=null){
    						conn.close();
    						conn=null;
    					}
    				}catch(SQLException ex){
    					ex.printStackTrace();
    				}
    			}
    		default:
    			r=false;
    	}
    	return r;
    }
    
    /*
     * delete *************************************************************************************************
     */
    public synchronized void delete(Integer user_id){
    	String sql="delete from original_tweets where user_id=?";
    	PreparedStatement ps;
    	while(true){
    		try{
    			ps = conn.prepareStatement(sql);
    			ps.setInt(1, user_id);
    			ps.executeUpdate();
    			ps.close();
    			break;
    		}catch(Exception e){
    			e.printStackTrace();
    		}
    	}
    }
    
	
	private final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private final String DB_URL = "jdbc:mysql://tweetrecommendation.coy7nudqad9n.us-east-1.rds.amazonaws.com:3306/recommend2u";
    public Connection conn = null;
    private static rds instance = null;
    
    private rds() {
    	conn = null;
    }
    
    /*get instance of rds*/
    public synchronized static rds getInstance() {
    	if (instance == null)
    		instance = new rds();
    	return instance;
    }
    
    /*test connection*/
    public boolean isConnected() {
    	return conn != null;
    }
    /*initialize DB*/
    public synchronized void init() {
    	while (true) {
            try {
                Class.forName(JDBC_DRIVER);
                System.out.println("Connecting to database...");
                conn = DriverManager.getConnection(DB_URL, "winter", "2014winter");
                break;
            } catch (Exception e) {
            	e.printStackTrace();
            }
    	}
    }
}
