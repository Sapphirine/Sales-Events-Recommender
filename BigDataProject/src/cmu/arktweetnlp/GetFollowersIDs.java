package cmu.arktweetnlp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import cmu.arktweetnlp.RunTagger.Decoder;
import cmu.arktweetnlp.Sentiment.Map;
import cmu.arktweetnlp.Sentiment.Reduce;
import cmu.arktweetnlp.impl.features.WordClusterPaths;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.CreateDomainRequest;

import sun.rmi.runtime.Log;
import twitter4j.GeoLocation;
import twitter4j.IDs;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.RequestToken;
//import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Lists followers' ids
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public final class GetFollowersIDs {
	
	public static int Key_Size;
	public static String[] ConsumeKey;
	public static String[] ConsumerSecret;
	public static String[] AccessToken;
	public static String[] AccessTokenSecret;
	
	public static String[] keywords;
	public static int Query_Size;// table name
	private static String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static String DB_URL = "jdbc:mysql://tweetrecommendation.coy7nudqad9n.us-east-1.rds.amazonaws.com:3306/webdb";
    public static Connection conn = null;
    private String password = null;
	public static ConfigurationBuilder cb = new ConfigurationBuilder();
	private static Date startdate; 
  
	public static ArrayList<Double> beautiList = new ArrayList<Double>();
	public static ArrayList<Double> toyList = new ArrayList<Double>();
	public static ArrayList<Double> computerList = new ArrayList<Double>();
	public static ArrayList<Double> clothingList = new ArrayList<Double>();
	public static ArrayList<Double> shoesList = new ArrayList<Double>();
	public static ArrayList<String> dic2 = new ArrayList<String>();
    
	 
	
    /**
     * Usage: java twitter4j.examples.friendsandfollowers.GetFollowersIDs [screen name]
     *
     * @param args message
     * @throws TwitterException 
     */
	
	public static void Initial() 
	
	{
		Date date = new Date(); 
		System.out.println("==============API_KEY & Queries fetch process starts=============\n");
		Properties prop = new Properties();
        String filename = "config.properties";

		try {
			
			
			//Load
			prop.load(GetFollowersIDs.class.getResourceAsStream(filename));

			
			ConsumeKey = prop.getProperty("ConsumerKey").split(";");
			ConsumerSecret = prop.getProperty("ConsumerSecret").split(";");
			AccessToken = prop.getProperty("AccessToken").split(";");
			AccessTokenSecret = prop.getProperty("AccessTokenSecret").split(";");
			keywords = prop.getProperty("Keywords").split(";");
		} catch (Exception e) {
			//System.out.println("Get twitter API Key error");
			// TODO: handle exception
		}finally {
			
		}
		//Record the key size and keyword size
		Key_Size = ConsumeKey.length;
		Query_Size = keywords.length;
		//Verbose informations
	//	System.out.println("Getting "+Key_Size+" API_KEY:"+ConsumeKey[0]);
		//System.out.println(Query_Size +" Queries are:" + Arrays.toString(keywords));
		
	//	System.out.println("==============API_KEY & Queries fetch process Ends=============\n");
	}	
	
    public static void main(String[] args) throws TwitterException, ParseException, InterruptedException, IOException, ClassNotFoundException {
		Initial();
   
            
            		

	        ConfigurationBuilder cb = new ConfigurationBuilder();
	        cb.setDebugEnabled(true)
	            .setOAuthConsumerKey(ConsumeKey[0])
	            .setOAuthConsumerSecret(ConsumerSecret[0])
	            .setOAuthAccessToken(AccessToken[0])
	            .setOAuthAccessTokenSecret(AccessTokenSecret[0]);

	        
	        BufferedReader br10 = new BufferedReader(new FileReader("/Users/qianyizhong/Documents/beauti.txt"));
	        BufferedReader br12 = new BufferedReader(new FileReader("/Users/qianyizhong/Documents/toy.txt"));
	        BufferedReader br14 = new BufferedReader(new FileReader("/Users/qianyizhong/Documents/computer.txt"));
	      	BufferedReader br15 = new BufferedReader(new FileReader("/Users/qianyizhong/Documents/clothing.txt"));
	      	BufferedReader br16 = new BufferedReader(new FileReader("/Users/qianyizhong/Documents/shoes.txt"));
	      	BufferedReader br13 = new BufferedReader(new FileReader("/Users/qianyizhong/Downloads/dictionary.txt"));
	      	
	      	
	      	try {
	 	       
		        String line = br10.readLine();
	      
		        while (line != null) {
		       // 	System.out.println(Double.parseDouble(line));
		     
		            beautiList.add(Double.parseDouble(line));
		            line = br10.readLine();
		        }
		     
		       
		    } finally {
		        br10.close();
		    }
		 
		 try {
		       
		        String line = br12.readLine();
	   
		        while (line != null) {
		        //	System.out.println(Double.parseDouble(line));
		           
		            toyList.add(Double.parseDouble(line));
		            line = br12.readLine();
		        }
		      
		       
		    } finally {
		        br12.close();
		    }
		 
		 
		 try {
		       
		        String line = br13.readLine();

		        while (line != null) {
		        //	System.out.println();
		           
		            dic2.add(line);
		            line = br13.readLine();
		        }
		      
		       
		    } finally {
		        br13.close();
		    }
		
		 try {
		       
		        String line = br14.readLine();

		        while (line != null) {
		        	//System.out.println(line);
		           
		            computerList.add(Double.parseDouble(line));
		            line = br14.readLine();
		        }
		      
		       
		    } finally {
		        br14.close();
		    }
		 

		 try {
		       
		        String line = br15.readLine();

		        while (line != null) {
		        //	System.out.println();
		           
		            clothingList.add(Double.parseDouble(line));
		            line = br15.readLine();
		        }
		      
		       
		    } finally {
		        br15.close();
		    }
		 
		 
		 try {
		       
		        String line = br16.readLine();

		        while (line != null) {
		        //	System.out.println();
		           
		            shoesList.add(Double.parseDouble(line));
		            line = br16.readLine();
		        }
		      
		       
		    } finally {
		        br16.close();
		    }
	      	
	      	
	        
	        TwitterFactory tf = new TwitterFactory(cb.build());
	        while (true) {
	            try {
	                Class.forName(JDBC_DRIVER);
	                System.out.println("Connecting to database...");
	                conn = DriverManager.getConnection(DB_URL,"winter", "2014winter");
	                break;
	            } catch (Exception e) {
	            //	e.printStackTrace();
	            }
	    	}

	       Twitter twitter = tf.getInstance();
			Query query = new Query("iphone6");   
			query.setResultType(Query.RECENT);
		    query.setCount(10);   
		    QueryResult result = twitter.search(query);  
		    for (Status status : result.getTweets()) {              
		    //  System.out.println("@" + status.getUser().getScreenName() + ":" + status.getText());
		    }
	        long cursor = -1;
            twitter.getHomeTimeline();
            for (Status status:  twitter.getHomeTimeline()){
            	//System.out.println("status is :" + status.getId());
            	//System.out.println("status is :" + status.getInReplyToStatusId());
            }
            
            String s=  "Sun Nov 30 22:01:08 EST 2014";
        	 startdate = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy", Locale.ENGLISH).parse(s);
// 	        Calendar cal2 = Calendar.getInstance();
// 	    	cal2.getTime();
// 	    	SimpleDateFormat sdf2 = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy", Locale.ENGLISH);
// 	    	startdate=new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy", Locale.ENGLISH).parse(sdf2.format(cal2.getTime()));
            
            while (true){
	        IDs ids = twitter.getFollowersIDs(cursor);
	     //   System.out.println(ids);
	        
	        do {
	        
	            for (long id : ids.getIDs()) {    
	            	
	            	/*****************add new user ***************/
	            	 System.out.println(id+"mmmmmm"+twitter.showUser(id).getScreenName());
	            	 String sql6 = "SELECT * FROM twitter_user where user_id = '"+id+"'";
	            	 System.out.println(sql6);
	                 //  String selectExpression = "select * from " + table + " where created_at > '"+start+"' and created_at < '"+end+"'";
	                 //StringBuilder sb = new StringBuilder();
	                 Statement stmt6;
	                 int count = 0;
	                 ResultSet rs6=null;
	                 try {
	                     stmt6 = conn.createStatement();
	                      rs6 = stmt6.executeQuery(sql6);
	                      
	   	                   while(rs6.next()){
	      	                	

	      	                    count++;
	      	                }
	      	                      
	         
	                     rs6.close();
	                     stmt6.close();
	                 } catch (SQLException e) {
	                   //  e.printStackTrace();
	                 }    
                    if (count==0){
                    	 String sql7 = "INSERT INTO twitter_user VALUES (?,?,?)";
                         PreparedStatement ps7;
                      
                 	        try {
                 	            ps7 = conn.prepareStatement(sql7);
                 	            //String timestamp = convertTime(time);
                 	           
                 	            ps7.setFloat(1, id);
                 	            ps7.setString(2, "123456");
                 	            ps7.setString(3,  twitter.showUser(id).getScreenName());
                 	            ps7.executeUpdate();
                 	            ps7.close();
                 	           
                 	            
                 	        } catch (SQLException e) {
                             //	System.err.println("Reconnect to database.");
                             
                 	          //  e.printStackTrace();
                 	        }
                         
                    }
                    
                    
                    /*********************************************************/
	                String ID = "followers ID #" + id;
	                String[] firstname = ID.split("#");
	                String first_Name = firstname[0];
	                String Id = firstname[1];

	              //  Log.i("split...........", first_Name + Id);

	                String Name = twitter.showUser(id).getName();
	                String screenName = twitter.showUser(id).getScreenName();
                  //  System.out.println(Name);
                    final List<Status> statuses = twitter.getUserTimeline(screenName);
                    
                    System.out.println("&&&&&&&&&&&&&&&&&&&&&&"+Name);
                    String tmp="10";
                    for (Status status : statuses) {
                    //	System.out.println("status is: !!!!!!!"+status.getId());
                    	//System.out.println("response status is :!!!!!!!!!"+status.getInReplyToStatusId());

                    	/**********check whether a tweet is a review tweet**********************/
                    	Long checkTwitterId = status.getId();
                    	Long cTwiIdRplyId = status.getInReplyToStatusId();
                    	
                    //	String sentiText= "I am happy with this cookie";
                    	
                    	File f = new File("/Users/qianyizhong/Documents/workspace/BigDataProject/input/1.txt");
                    	boolean success = f.delete();
                    	String originalText = status.getText();//get original twitter text
                   
                    	/***********process tweet text********************/
                    	/***********write to example.txt******************/

                	    try {
                	      File file = new File("/Users/qianyizhong/Documents/workspace/BigDataProject/input/1.txt");
                	      BufferedWriter output = new BufferedWriter(new FileWriter(file));
                	      output.write(originalText);
                	      output.close();
                	    } catch ( IOException e ) {
                	     //  e.printStackTrace();
                	    }
                    	
                    	
                    	
                    	File index2 = new File("/Users/qianyizhong/Documents/workspace/BigDataProject/output");
                    	
                    	String[]entries = index2.list();
                    	if (entries.length != 0&& entries!=null){
                    	for(String s2: entries){
                    	    File currentFile = new File(index2.getPath(),s2);
                    	    currentFile.delete();
                    	}
                    	}
                    	index2.delete();
                    	
                    	
                    	
                    	
                    	Sentiment sentiment = new Sentiment();
                   	 sentiment.initialize();
                        Configuration conf = new Configuration();
                       
                        Job job = new Job(conf, "WordCount");
                        job.setJarByClass(Sentiment.class);
                        job.setOutputKeyClass(Text.class);
                        job.setOutputValueClass(DoubleWritable.class);

                        job.setMapperClass(Map.class);
                        job.setCombinerClass(Reduce.class);
                        job.setReducerClass(Reduce.class);

                        job.setInputFormatClass(TextInputFormat.class);
                        job.setOutputFormatClass(TextOutputFormat.class);

                        FileInputFormat.addInputPath(job, new Path(args[0]));
                        FileOutputFormat.setOutputPath(job, new Path(args[1]));

                        job.waitForCompletion(true);
                    	
                     //   BufferedReader br1 = new BufferedReader(new FileReader("/Users/qianyizhong/Documents/workspace/BigDataProject/output/part-r-00000"));
                        BufferedReader br1 = new BufferedReader(new FileReader("/Users/qianyizhong/Desktop/examplett.txt"));
                        
                        String words="";
                    	
                    			
                    			 try {
                    			        StringBuilder sb1 = new StringBuilder();
                    			        String line = br1.readLine();

                    			        while (line != null) {
                    			            sb1.append(line);
                    			            sb1.append(System.lineSeparator());
                    			            line = br1.readLine();
                    			        }
                    			        words = sb1.toString();
                    			       
                    			    } finally {
                    			        br1.close();
                    			    }
                    			 
                    			 Double rating = (Double.parseDouble(words)+1)*2.5;
                    			 
                    			 System.out.println("rating is&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&"+rating);
   	            	 String sql8 = "SELECT * FROM recommend_tweet where tweet_id = '"+cTwiIdRplyId+"'";
   	                 //  String selectExpression = "select * from " + table + " where created_at > '"+start+"' and created_at < '"+end+"'";
   	                 //StringBuilder sb = new StringBuilder();
   	                 Statement stmt8;
   	                 int count3= 0;
   	                 ResultSet rs8=null;
   	                 try {
   	                     stmt8 = conn.createStatement();
   	                      rs8 = stmt8.executeQuery(sql8);

   	                   while(rs8.next()){
   	                	

   	                    count3++;
   	                }
   	                      
   	                      
   	                      
   	                      
   	                     rs8.close();
   	                     stmt8.close();
   	                 } catch (SQLException e) {
   	                    // e.printStackTrace();
   	                 }    
   	                 System.out.println("result 8 is:" +rs8.toString());
                       if (count3!=0||count3 == 0){
                       	 String sql9 = "INSERT INTO review_tweet VALUES (?,?,?,?)";
                            PreparedStatement ps9;
                         
                    	        try {
                    	        	System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
                    	            ps9 = conn.prepareStatement(sql9);
                    	            //String timestamp = convertTime(time);
                    	           
                    	            ps9.setFloat(1, checkTwitterId);
                    	            ps9.setDouble(2, rating);
                    	            ps9.setFloat(3,  id);
                    	            ps9.setFloat(4, cTwiIdRplyId );
                    	            ps9.executeUpdate();
                    	            ps9.close();
                    	           
                    	            
                    	        } catch (SQLException e) {
                                //	System.err.println("Reconnect to database.");
                                
                    	         //   e.printStackTrace();
                    	        }
                       }
                    //	String s=  "Sun Nov 30 22:01:08 EST 2014";
                    	//Date date = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy", Locale.ENGLISH).parse(s);
                    	//String s2 = "Wed Nov 05 20:41:18 EST 2014";
                    	//Date date2 = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy", Locale.ENGLISH).parse(s2);
                    //	 System.out.println("++++++++++++++"+date);
                      //  System.out.println( status.getCreatedAt() );
                       // System.out.println("Compare Result!!!!!!" + date2.compareTo(date));
                    	/************Delete file example.txt*************/
               //  
                    	File f1 = new File("/Users/qianyizhong/Desktop/example.txt");
                    	boolean success1 = f1.delete();
                    	String originalText1 = status.getText();//get original twitter text
                   
                    	/***********process tweet text********************/
                    	/***********write to example.txt******************/

                	    try {
                	      File file = new File("/Users/qianyizhong/Desktop/example.txt");
                	      BufferedWriter output = new BufferedWriter(new FileWriter(file));
                	      output.write(originalText1);
                	      output.close();
                	    } catch ( IOException e ) {
                	   //    e.printStackTrace();
                	    }
                	    
                	    
                	    
                	    /************Run NLP ******************/
                	    

                		System.err.println("1.here~~~~~~~");
                		if (status.getCreatedAt().compareTo(startdate)==1) {
                		RunTagger tagger;
						try {
							tagger = new RunTagger();
							tagger.inputFilename="/Users/qianyizhong/Desktop/example.txt";
							try {
								tagger.runTagger();
							} catch (ClassNotFoundException e) {
								// TODO Auto-generated catch block
								//e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								//e.printStackTrace();
							}		
						} catch (UnsupportedEncodingException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						   AnalyzeNLP a = new AnalyzeNLP();
						   ArrayList<String> str = a.getNoun();
						   System.out.println("string list is"+str);
						   String product = "";
						   if (str.size() != 0) {
                         for (int i = 0; i < str.size()-1; i++) {
                        	 product = product + str.get(i) + " ";
                         }
                             product = product + str.get(str.size()-1);
						   }
					//	   String product = str.toString();
						   System.out.println("product is"+product);
                	    
                	    if (product != null && product.length() != 0)  {
                    	Long userId = id;
                    	Long twitterId = status.getId();
                    	String content = status.getText();
                    	 String sql = "INSERT INTO user_tweet VALUES (?,?,?)";
                         PreparedStatement ps;
                        
                 	        try {
                 	            ps = conn.prepareStatement(sql);
                 	            //String timestamp = convertTime(time);
                 	          
                 	            ps.setFloat(1, twitterId);
                 	            ps.setString(2, content);
                 	            ps.setFloat(3, userId);
                 	            ps.executeUpdate();
                 	            ps.close();
                 	           
                 	            
                 	        } catch (SQLException e) {
                             //	System.err.println("Reconnect to database.");
                 	          //  e.printStackTrace();
                 	        }
                         
                 	       System.out.println("db works!!!!!!!!!!!!");
                    	
                    	try{
                    		
                    		/**********************classifying into a category*****************/
                    		
                    		 String test=status.getText();
                    		 String stringToBeTokenized = test.replace('#', ' ');
                    			stringToBeTokenized = stringToBeTokenized.replace('?', ' ');
                    			stringToBeTokenized = stringToBeTokenized.replace('<', ' ');
                    			stringToBeTokenized = stringToBeTokenized.replace('>', ' ');
                    			stringToBeTokenized = stringToBeTokenized.replace('[', ' ');
                    			stringToBeTokenized = stringToBeTokenized.replace(']', ' ');
                    			stringToBeTokenized = stringToBeTokenized.replace('{', ' ');
                    			stringToBeTokenized = stringToBeTokenized.replace('}', ' ');
                    			stringToBeTokenized = stringToBeTokenized.replace('@', ' ');
                    			stringToBeTokenized = stringToBeTokenized.replace('(', ' ');
                    			stringToBeTokenized = stringToBeTokenized.replace(')', ' ');
                    			stringToBeTokenized = stringToBeTokenized.replace(',', ' ');
                    			stringToBeTokenized = stringToBeTokenized.replace('!', ' ');
                    			stringToBeTokenized = stringToBeTokenized.replace('&', ' ');
                    			stringToBeTokenized = stringToBeTokenized.replace(':', ' ');
                    			stringToBeTokenized = stringToBeTokenized.replace('?', ' ');
                    			stringToBeTokenized = stringToBeTokenized.replace('/', ' ');
                    			stringToBeTokenized = stringToBeTokenized.replace('\\', ' ');
                    			stringToBeTokenized = stringToBeTokenized.replace('\'', ' ');
                    			stringToBeTokenized = stringToBeTokenized.replace('"', ' ');
                    			stringToBeTokenized = stringToBeTokenized.replace('+', ' ');
                    			stringToBeTokenized = stringToBeTokenized.replace('=', ' ');
                    			stringToBeTokenized = stringToBeTokenized.replace('_', ' ');
                    			stringToBeTokenized = stringToBeTokenized.replace('-', ' ');
                    			stringToBeTokenized = stringToBeTokenized.replace('*', ' ');
                    			stringToBeTokenized = stringToBeTokenized.replace('$', ' ');
                    			stringToBeTokenized = stringToBeTokenized.replace('^', ' ');
                    			stringToBeTokenized = stringToBeTokenized.replace('%', ' ');
                    			stringToBeTokenized = stringToBeTokenized.replace('.', ' ');
                    			stringToBeTokenized = stringToBeTokenized.replace(';', ' ');
                    			stringToBeTokenized = stringToBeTokenized.replace('1', ' ');
                    			stringToBeTokenized = stringToBeTokenized.replace('2', ' ');
                    			stringToBeTokenized = stringToBeTokenized.replace('3', ' ');
                    			stringToBeTokenized = stringToBeTokenized.replace('4', ' ');
                    			stringToBeTokenized = stringToBeTokenized.replace('5', ' ');
                    			stringToBeTokenized = stringToBeTokenized.replace('6', ' ');
                    			stringToBeTokenized = stringToBeTokenized.replace('7', ' ');
                    			stringToBeTokenized = stringToBeTokenized.replace('8', ' ');
                    			stringToBeTokenized = stringToBeTokenized.replace('9', ' ');
                    			stringToBeTokenized = stringToBeTokenized.replace('0', ' ');
                    			stringToBeTokenized = stringToBeTokenized.replace('\n', ' ');
                    			stringToBeTokenized = stringToBeTokenized.replace('\t', ' ');
                    			stringToBeTokenized = stringToBeTokenized.replace('`', ' ');
                    			stringToBeTokenized = stringToBeTokenized.replace('~', ' ');

                    			String stopwords="	a about above above across after afterwards again against all almost alone along already "
                    					+ "also although always am among amongst amoungst amount  an and another any anyhow anyone"
                    					+ " anything anyway anywhere are around as  at back be became because become becomes becoming"
                    					+ " been before beforehand behind being below beside besides between beyond both bottom but by "
                    					+ "call can cannot cant co con could couldnt cry de describe detail do done down due during each "
                    					+ "eg eight either eleven else elsewhere empty enough etc even ever every everyone everything"
                    					+ " everywhere except few fifteen fify fill find fire first five for former formerly forty found "
                    					+ "four from front full further get give go had has hasnt have he hence her here hereafter hereby "
                    					+ "herein hereupon hers herself him himself his how however hundred ie if in inc indeed interest into "
                    					+ "is it its itself keep last latter latterly least less ltd made many may me meanwhile might mill mine"
                    					+ " more moreover most mostly move much must my myself name namely neither never nevertheless next new nine"
                    					+ " no nobody none noone nor not nothing now nowhere of off often on once one only onto or other others "
                    					+ "otherwise our ours ourselves out over own part per perhaps please put rather re same see seem seemed seeming "
                    					+ "seems serious several she should show side since sincere six sixty so some somehow someone something sometime"
                    					+ " sometimes somewhere still such system take ten than that the their them themselves then thence there"
                    					+ " thereafter thereby therefore therein thereupon these they thickv thin third this those though three through"
                    					+ " throughout thru thus to together too top toward towards twelve twenty two un under until up upon us very via"
                    					+ " was we well were what whatever when whence whenever where whereafter whereas whereby wherein whereupon"
                    					+ " wherever whether which while whither who whoever whole whom whose why will with within without would yet"
                    					+ " you your yours yourself yourselves the 1 2 3 4 5 6 7 8 9 0 00 000 0000 00000 000000 0000000 \\ / j ya di tu " 
                    					+ "tr da q http apa uu ? fa ik nya";

                    		
                    			String[] tokens = stringToBeTokenized.split(" |-|,|\"|\\.|\\(|\\)|\\||&|\\?|:\n\t");
                    	        ArrayList<String> testString = new ArrayList<String>();
                    			for (int i = 0; i < tokens.length; i++) {
                    				String token = tokens[i].trim().toLowerCase();
                    				if (stopwords.contains(token)) {

                    				} else {
                    					testString.add(token);
                    				}
                    			}
                    			Double p1 = 1.0;
                    			Double p2 = 1.0;
                    			Double p3 = 1.0;
                    			Double p4 = 1.0;
                    			Double p5 = 1.0;
                    			for (String s2:testString){
                    				int index = dic2.indexOf(s2);
                    				if (index>-1){
                    					p1=p1*beautiList.get(index);
                    					p2=p2*toyList.get(index);
                    					p3=p3*computerList.get(index);
                    					p4=p4*clothingList.get(index);
                    					p5=p5*shoesList.get(index);
                    				}
                    			}
                    			
                    			ArrayList<Double> result2 = new ArrayList<Double>();
                    			result2.add(p1);
                    			result2.add(p2);
                    			result2.add(p3);
                    			result2.add(p4);
                    			result2.add(p5);
                    			
                    			Double max = p1;
                    			int maxPosition = 0; 
                    			for (int i = 1; i<result2.size();i++){
                    				
                    				if (result2.get(i)>=max){
                    					max = result2.get(i);
                    					maxPosition = i;
                    				}
                    				
                    			}
                    			String category =" "; 
                    			if (maxPosition == 0) {
                    				category = "Beauty";
                    			} else if (maxPosition == 1) {
                    				category = "Toys";
                    			} else if (maxPosition == 2) {
                    				category = "Electronics";
                    			} else if (maxPosition == 3) {
                    				category = "Apparel";
                    			} else {
                    				category = "Shoes";
                    			}
                    		
                    		
                    		
                    		System.out.println("works here now!!!!!!!!!!!!");
                        ArrayList<String> list = ItemSearch.getProductInfo(product, category);
                        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!###########################the recommendation url is"+list.get(0));
                        System.out.println(list.get(1));
                        tmp=list.get(1);
                      //  tmp="test";
                    //    System.out.println(status.getUser().getScreenName());
                        StatusUpdate st = new StatusUpdate("@"+status.getUser().getScreenName()+" "+tmp);
                   //     System.out.println("statusid!!!!!!!!!"+status.getId());
                     //   st.inReplyToStatusId(status.getId()).getStatus();
                      //  System.out.println(st.inReplyToStatusId(status.getId()).inReplyToStatusId(status.getId()));
                        st.setInReplyToStatusId(status.getId());
                     
 
                   //     st.notify();
                        GeoLocation location= new GeoLocation(-35.9281897,26.0819792);
                        st.setLocation(location);
                        try {
                          
                          Status replyStatus = twitter.updateStatus(st);
                          Long tId = replyStatus.getId();
                          System.out.println("reply id is"+tId);
                          Long replyId = replyStatus.getInReplyToStatusId();
                          String url = tmp;
                          /*******update recommend_tweet**************************/
                          String sql2 = "INSERT INTO recommend_tweet VALUES (?,?)";
                          PreparedStatement ps2;
                         
                  	        try {
                  	            ps2 = conn.prepareStatement(sql2);
                  	            //String timestamp = convertTime(time);
                  	          
                  	            ps2.setFloat(1, tId);
                  	            ps2.setString(2, url);
                  	            ps2.executeUpdate();
                  	            ps2.close();
                  	           
                  	            
                  	        } catch (SQLException e) {
                              //	System.err.println("Reconnect to database.");
                  	           // e.printStackTrace();
                  	        }
                          
                  	      /*******update recommend_to**************************/
                  	      String sql3 = "INSERT INTO recommend_to VALUES (?,?)";
                          PreparedStatement ps3;
                         
                  	        try {
                  	            ps3 = conn.prepareStatement(sql3);
                  	            //String timestamp = convertTime(time);
                  	          
                  	            ps3.setFloat(1, replyId);
                  	            ps3.setFloat(2, tId);
                  	            ps3.executeUpdate();
                  	            ps3.close();
                  	           
                  	            
                  	        } catch (SQLException e) {
                              //	System.err.println("Reconnect to database.");
                  	           // e.printStackTrace();
                  	        }
                          
                    	      /*******update product**************************/
                    	      String sql4 = "INSERT INTO product (name, url) VALUES (?,?)";
                            PreparedStatement ps4;
                           
                    	        try {
                    	            ps4 = conn.prepareStatement(sql4);
                    	            //String timestamp = convertTime(time);                    	          
                    	            ps4.setString(1,product);
                    	            ps4.setString(2, url);
                    	            ps4.executeUpdate();
                    	            ps4.close();
                    	           
                    	            
                    	        } catch (SQLException e) {
                                //	System.err.println("Reconnect to database.");
                    	          //  e.printStackTrace();
                    	        }
                            
                  	        
                  	        /***************has************************/
                    	      
                      	      String sql5 = "INSERT INTO has VALUES (?,?)";
                              PreparedStatement ps5;
                             
                      	        try {
                      	            ps5 = conn.prepareStatement(sql5);
                      	            //String timestamp = convertTime(time);
                      	          
                      	            ps5.setString(1,product);
                      	            ps5.setFloat(2, tId);
                      	            ps5.executeUpdate();
                      	            ps5.close();
                      	           
                      	            
                      	        } catch (SQLException e) {
                                  //	System.err.println("Reconnect to database.");
                      	            //e.printStackTrace();
                      	        }
                              
                          
                        //  System.out.println("reply to id is"+replyId);
                          Thread.sleep(10000);
                        } catch (TwitterException e) {
                          // TODO Auto-generated catch block
                       //   e.printStackTrace();
                        }
                        
                    	} catch (Exception e){
                    	//	 e.printStackTrace();
                    	}
                    }
                    }
	            }
                    
                   // twitter.showUser(id).getStatus().getText();
//                    System.out.println(twitter.showUser(id).getStatus().getText());
//                    System.out.println(twitter.showUser(id).getStatus().getText());
//                    System.out.println(twitter.showUser(id).getStatus().getText());
	  //            Log.i("id.......", "followers ID #" + id);
	    //          Log.i("Name..", mTwitter.mTwitter.showUser(id).getName());
	    //          Log.i("Screen_Name...", mTwitter.mTwitter.showUser(id).getScreenName());
	    //          Log.i("image...", mTwitter.mTwitter.showUser(id).getProfileImageURL());


	            }
	        } while (ids.hasNext());
	       Thread.sleep(10000); 
	       Calendar cal = Calendar.getInstance();
	    	cal.getTime();
	    	SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy", Locale.ENGLISH);
	    	startdate=new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy", Locale.ENGLISH).parse(sdf.format(cal.getTime()));   
	       
    }

            
	     //   status = twitter.showStatus(id="112652479837110273")
	         
	       
	        
	            }

	           
	  
}
