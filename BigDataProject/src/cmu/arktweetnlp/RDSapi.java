package cmu.arktweetnlp;

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

public class RDSapi {
	public static String table = "original_tweets"; // table name
	private final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private final String DB_URL = "jdbc:mysql://tweetrecommendation.coy7nudqad9n.us-east-1.rds.amazonaws.com:3306/tweets";
    public Connection conn = null;
    private String password = null;
    
    private static RDSapi instance = null;
	public static void main(String[] args){
		RDSapi api=getInstance();
		api.init();
//		api.createTable(table);
//		api.insert(111, "12 Dec 04 01 00 2014", "I had a great day! I'm so happy!");
		api.select();
//		api.update(111, "I love Columbia!");
//		api.select();
//		api.delete(111);
//		api.select();
	}
  
    private RDSapi() {
    	conn = null;
    }
    
    public synchronized static RDSapi getInstance() {
    	if (instance == null)
    		instance = new RDSapi();
    	return instance;
    }
    
    public boolean isConnected() {
    	return conn != null;
    }
    
    public void setPassword(String password) {
    	this.password = password;
    	if (conn == null)
    		init();
    }
    
    public boolean isPasswordSet() {
    	return this.password != null;
    }
    
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
    
    public synchronized void createTable(String name) {
    	this.table = name;
        System.out.println("Creating table in given database...");
        Statement stmt;
        try {
            stmt = conn.createStatement();
            /*String sql = "CREATE TABLE IF NOT EXISTS " +name+ " " +
                    "(id_str VARCHAR(255) not NULL, " +
                    " keyword VARCHAR(20), " +
                    " user VARCHAR(255), " +
                    " text VARCHAR(2000), " +
                    " latitude VARCHAR(255), " +
                    " longitude VARCHAR(255), " +
                    " created_at VARCHAR(255), " +
                    " sentiment_exist TINYINT(1), " +
                    " sentiment DOUBLE(4,3), " +
                    " PRIMARY KEY ( id_str ))";*/
            String sql="create table if not exists " + name + " " +
                    "( user_id integer not null, " +
            		"timestamp varchar(255), "+
            		"content varchar(255));";
            System.out.println("sql statement: " + sql);
            stmt.executeUpdate(sql);
            stmt.close();
            System.out.println("Finished creating table");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public synchronized void deleteTable(String name) {
        System.out.println("Deleting table in given database...");
        Statement stmt;
        try {
            stmt = conn.createStatement();
            String sql = "DROP TABLE " + name+ " ;";
            stmt.executeUpdate(sql);
            stmt.close();
            System.out.println("Finished deleting table");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    HashMap<String, String> map = new HashMap<String, String>();

    private void createMap() {
        map.put("Jan", "01");
        map.put("Feb", "02");
        map.put("Mar", "03");
        map.put("Apr", "04");
        map.put("May", "05");
        map.put("Jun", "06");
        map.put("Jul", "07");
        map.put("Aug", "08");
        map.put("Sep", "09");
        map.put("Oct", "10");
        map.put("Nov", "11");
        map.put("Dec", "12");
    }
    private String convertTime(String date) {
        String processed = null;

        if(map.size()==0){
            createMap();
        }

        // hard coded according to tweet format
        String[] s = date.split(" ");
        String year = s[5];
        String month = s[1];
        String day = s[2];
        String time = s[3];
        processed = year+"-"+map.get(month)+"-"+day+" "+time;

        Timestamp timestamp = Timestamp.valueOf(processed);
        return String.valueOf(timestamp.getTime());
    }

    //public synchronized String select() {
    public synchronized void select() {
        String sql = "SELECT * FROM "+table;
        //  String selectExpression = "select * from " + table + " where created_at > '"+start+"' and created_at < '"+end+"'";
        //StringBuilder sb = new StringBuilder();
        Statement stmt;
        int count = 0;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
            	String user_id = rs.getString("user_id");
                String content = rs.getString("content");             
                System.out.println("user_id: " + user_id + " content: " + content);
                count++;
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(count);
        //return sb.toString();
    }

    /*public synchronized List<TweetRequest> select (String key, String start) {
    	String sql = "SELECT * FROM "+table+" WHERE created_at > '"+start+"' and keyword = '"
    			+ key + "'";
    	StringBuilder sb = new StringBuilder();
        Statement stmt;
        List<TweetRequest> res = new ArrayList<TweetRequest> ();
        int count = 0;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
            	
            	Boolean sen = rs.getBoolean("sentiment_exist");
            	if (sen) {
            	String id = rs.getString("id_str");

            	String text = rs.getString("text");
            	String keyword = rs.getString("keyword");
            	String user = rs.getString("user");
            	String c1 = rs.getString("latitude");
            	String c2 = rs.getString("longitude");
            	String time = rs.getString("created_at");
            	String sentiment = rs.getString("sentiment");

            	res.add(new TweetRequest(id, time, text, user, Double.valueOf(c2), Double.valueOf(c1), Double.valueOf(sentiment)));

                count++;
            	}
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return res;
    }
    */
    
    /*
    public synchronized String selectTimeRange(String table, String start, String end) {
        String sql = "SELECT * FROM "+table+" WHERE created_at < '"+end+"' AND created_at > '"+start+"'";
//    	String selectExpression = "select * from " + table + " where created_at > '"+start+"' and created_at < '"+end+"'";
        StringBuilder sb = new StringBuilder();
        Statement stmt;
        int count = 0;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
            	String id = rs.getString("id_str");

            	String text = rs.getString("text");
            	String keyword = rs.getString("keyword");
            	String user = rs.getString("user");
            	String c1 = rs.getString("latitude");
            	String c2 = rs.getString("longitude");
            	String time = rs.getString("created_at");
            	Boolean sen = rs.getBoolean("sentiment_exist");

            	System.out.println("Keyword:" + keyword + "User:" + user + " Text:" + text+ " Created_at:"+ time + " id:"+id+ " sen:" + sen + " C1:" + c1);

                count++;
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println(count);
        return sb.toString();

    }*/

    public synchronized void insert(Integer user_id, String time, String content) {
        System.out.println("Inserting into table " +table );
        String sql = "INSERT INTO " + table + " VALUES (?,?,?)";
        PreparedStatement ps;
        while (true) {
	        try {
	            ps = conn.prepareStatement(sql);
	            //String timestamp = convertTime(time);
	            String timestamp = time;
	            ps.setInt(1, user_id);
	            ps.setString(2, timestamp);
	            ps.setString(3, content);
	            ps.executeUpdate();
	            ps.close();
	            break;
	            
	        } catch (SQLException e) {
            	System.err.println("Reconnect to database.");
            	init();
	            e.printStackTrace();
	        }
        }
    }
    
    public synchronized void update(Integer user_id, String content){
    	String sql = "update original_tweets set content = ? where user_id = ?";
    	//System.out.println("update: " + sql);
    	PreparedStatement ps;
    	while (true) {
			 try {
			     ps = conn.prepareStatement(sql);
			     ps.setString(1, content);
			     ps.setInt(2, user_id);
		
			     ps.executeUpdate();
			
			     ps.close();
			     break;
			     
			 } catch (SQLException e) {
            	System.err.println("Reconnect to database.");
            	init();
			     e.printStackTrace();
			 }
    	}

    	
    }
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
    
}
