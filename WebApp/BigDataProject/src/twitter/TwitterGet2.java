package twitter;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.CreateDomainRequest;
import com.amazonaws.services.simpledb.model.PutAttributesRequest;
import com.amazonaws.services.simpledb.model.ReplaceableAttribute;

import twitter4j.FilterQuery;
import twitter4j.JSONObject;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterGet2 {
	public static int Key_Size;
	public static String[] ConsumeKey;
	public static String[] ConsumerSecret;
	public static String[] AccessToken;
	public static String[] AccessTokenSecret;
	
	public static String[] keywords;
	public static int Query_Size;
	public static ConfigurationBuilder cb = new ConfigurationBuilder();
	public static  AmazonSimpleDBClient sdb;
	public String myDomain = "car1";
	public static String itemName ="301";
    static TweetServer tweetServer;
  //  static int port = 11083;
	
	/*
	 * Load configuration from config
	 */
	public static void Initial() 
	
	{
		Date date = new Date(); 
		System.out.println("==============API_KEY & Queries fetch process starts=============\n");
		Properties prop = new Properties();
        String filename = "config.properties";

		try {
			
			 AWSCredentials credentials = new PropertiesCredentials(DataFetch.class.getResourceAsStream("AwsCredentials.properties"));
				sdb = new AmazonSimpleDBClient(credentials);
				
		        //SimpleDB sdb = new SimpleDB(credentials);
		
				Region usEast1 = Region.getRegion(Regions.US_EAST_1);
				sdb.setRegion(usEast1);
				
				// Create a domain
			    System.out.println("Creating domain called " + "car1" + ".\n");
			  sdb.createDomain(new CreateDomainRequest("car1"));
	         System.out.println("flag1");
			//Load
			prop.load(TwitterGet.class.getResourceAsStream(filename));
			
			ConsumeKey = prop.getProperty("ConsumerKey").split(";");
			ConsumerSecret = prop.getProperty("ConsumerSecret").split(";");
			AccessToken = prop.getProperty("AccessToken").split(";");
			AccessTokenSecret = prop.getProperty("AccessTokenSecret").split(";");
			keywords = prop.getProperty("Keywords").split(";");
		} catch (Exception e) {
			System.out.println("Get twitter API Key error");
			// TODO: handle exception
		}finally {
			
		}
		//Record the key size and keyword size
		Key_Size = ConsumeKey.length;
		Query_Size = keywords.length;
		//Verbose informations
		System.out.println("Getting "+Key_Size+" API_KEY:"+ConsumeKey[0]);
		System.out.println(Query_Size +" Queries are:" + Arrays.toString(keywords));
		
		System.out.println("==============API_KEY & Queries fetch process Ends=============\n");
	}	
	
	public static void main(String[] args) throws IOException, InterruptedException {
		
//        try {
//            tweetServer = new TweetServer(port);
//            tweetServer.start();
//            System.out.println( "TweetServer started on port: " + tweetServer.getPort());
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
		Initial();
		
		cb.setDebugEnabled(true)
		.setOAuthConsumerKey(ConsumeKey[0])  
		.setOAuthConsumerSecret(ConsumerSecret[0])
		.setOAuthAccessToken(AccessToken[0])
		.setOAuthAccessTokenSecret(AccessTokenSecret[0]);
		
       

			//TweetsArray = obj.getJSONObject("entityinfo").getJSONArray("tweets");
			
			

			
			
		 
		 
		 
		 
		StatusListener listener = new StatusListener(){
			private int fileNo = 0;
			private String filePath = "";
	        private int count = 0;
	        private PrintWriter pw;
	        private FileWriter Log;
	        
			public  void onStatus(Status tweet) {
				DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
				Date date = new Date(); 
			
				try{

					
					
					
					    // ...
			         
			      
			       
					if(count == 0) {
						String newfileName = dateFormat.format(date);
						if(!newfileName.equals(filePath))
						{
							filePath = newfileName;
							fileNo = 0;
						}
						pw = new PrintWriter(new File(filePath+"-"+fileNo+".json"));
						pw.write("{\"tweets\":[");
					}
					
					if (tweet.getGeoLocation() != null
	            			|| (tweet.getUser().getLocation() != null
	            			&& !tweet.getUser().getLocation().isEmpty())) {
	            		
						//Avoid in case Lat Long cannot be parsed 
						Double latitude = null ;
						Double longtitude = null;
						
		            	if (tweet.getGeoLocation() != null) {
			            	System.out.println("From GeoLocation:");
			            	latitude = tweet.getGeoLocation().getLatitude();
			            	longtitude = tweet.getGeoLocation().getLongitude();
		            	}
//		            	else{
//		            		//Get LatLng by user location
//	                    	String address = tweet.getUser().getLocation();
//			            	System.out.println("From Address: "+ address);
//	                    	Double[] LatLng =  TwitterLatLng.getLongitudeLatitude(address);
//	                    	if(LatLng !=null )
//	                    	{
//		                    	latitude = LatLng[0];
//		                    	longtitude = LatLng[1];
//	                    	}
//		            	}
		            	
		            	ArrayList<String> list = new ArrayList<String>();
		            	list.add("Columbia");
		            	list.add("Snow");
		            	list.add("University");
		            	list.add("Baseball");
		            	list.add("Computer");
//		            	list.add("A");
//		            	list.add("Is");
//		            	list.add("My");
//		            	list.add("Weather");
		            	String temp="";
		   
		            	
		            	if(!(latitude == null || longtitude == null || latitude.isNaN() || longtitude.isNaN()))
		            	{
			            	JSONObject jo = new JSONObject();
			            	
			            	String textString = tweet.getText();
			            	String[] singleWord = textString.split("[\\p{Punct}, \\s]+");
			            	for (String word : singleWord) {
			            		if (word == null || word.length() == 0 ) {
			            			continue;
			            		}
			            		for (int i = 0; i<=4; i++){
			            			if (word.toLowerCase().equals(list.get(i).toLowerCase())){
			            				temp = list.get(i);
			            				break;
			            			}
			            				
			            		}
			            	}
//			            	if(temp.equals("A")){
//			            		temp = "University";
//			            	} else if (temp.equals("Is")){
//			            		temp = "Snow";
//			            		
//			            	} else if (temp.equals("My")){
//			            		temp = "Computer";
//			            	} else if (temp.equals("Weather")){
//			            		temp = "Baseball";
//			            	}
			            	 filePath = "11-19-2014";
							 List<ReplaceableAttribute> list1 = new ArrayList<ReplaceableAttribute>();
								PutAttributesRequest putAttributesRequest = new PutAttributesRequest();
						        putAttributesRequest.setDomainName("car1");
						        putAttributesRequest.setItemName(filePath+"-"+itemName);
						        System.out.println(itemName);
						//		String s0 = obj.getJSONArray("tweets").getJSONObject(i).getString("user");
							//	String s1 = obj.getJSONArray("tweets").getJSONObject(i).getString("text");
								//String s2 = obj.getJSONArray("tweets").getJSONObject(i).getString("Date");
								//Double s3 = obj.getJSONArray("tweets").getJSONObject(i).getDouble("Latitude");
								//Double s4 = obj.getJSONArray("tweets").getJSONObject(i).getDouble("Longitude");
								//String s5 = obj.getJSONArray("tweets").getJSONObject(i).getString("keyword");
								
//								System.out.println(s0);
//								System.out.println(s1);
//								System.out.println(s2);
//								System.out.println(s3);
							//	System.out.println("json keyword is");
							//	System.out.println(s5);		
						      
								list1.add(new ReplaceableAttribute("user", tweet.getUser().getScreenName(), true));
					            list1.add(new ReplaceableAttribute("text", tweet.getText(), true));
					            list1.add(new ReplaceableAttribute("Date", filePath, true));
					            list1.add(new ReplaceableAttribute("Latitude",latitude.toString() , true));
					            list1.add(new ReplaceableAttribute("Longitude", longtitude.toString(), true));
					            list1.add(new ReplaceableAttribute("keyword", temp.toString(),true));
					            
								putAttributesRequest.setAttributes(list1);
					            sdb.putAttributes(putAttributesRequest);
					            Integer k= Integer.parseInt(itemName)+1;
					            itemName=k.toString();
					            System.out.println("??????????????????????");
					            System.out.println(itemName);
			            	    if (k%50 == 0) {
			            	    	Thread.sleep(1000*3600);
			            	    }
			            	//jo.put("Date", tweet.getCreatedAt());
			            	jo.put("Date",filePath);
			            	jo.put("Latitude", latitude);
			            	jo.put("Longitude", longtitude);
			            	jo.put("address", tweet.getUser().getLocation());
			            	jo.put("user", tweet.getUser().getScreenName());
			            	jo.put("text", tweet.getText());
			            	jo.put("keyword", temp);
			            	
			            	System.out.println("================="+temp);
			            	
			            	System.out.println("The "+ count + " tweets: " + dateFormat.format(date)+ ", File No: "+fileNo);
			            	System.out.println("latitude "+latitude+",longitude "+longtitude);
			            	System.out.println(jo+"\n");
			            	
			            	count++;
			            	if (count == 5){
			            		pw.write(jo.toString() + "]}");
			            		pw.close();
			            		count = 0;	//initialize counter
			            		//Modify the log file, record the current file number
			            		//Once finishes writing a file, record the current fileNo and date
								Log = new FileWriter("Log",false);
			            		Log.write(dateFormat.format(date) + "\n" + fileNo);
			            		Log.close();
			            		System.out.println("written to file: " + filePath + ("-"+ fileNo++)+".json");
			            		
			            	}
			            	else
			            		pw.write(jo.toString() + ",");
		            	}
            	}
				}catch(Exception e){
					pw.close();
					e.printStackTrace();
				}
            }
			@Override
			public void onException(Exception ex) {
				pw.close();
				ex.printStackTrace();
			}

			@Override
			public void onDeletionNotice(StatusDeletionNotice arg0) {}

			@Override
			public void onScrubGeo(long arg0, long arg1) {}

			@Override
			public void onStallWarning(StallWarning arg0) {}

			@Override
			public void onTrackLimitationNotice(int arg0) {}
	    };
	    
	    TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
	    twitterStream.addListener(listener);
	    FilterQuery fil = new FilterQuery();
	    String[] keywords = {"Columbia", "Snow", "University", "Baseball", "Computer"};
	    fil.track(keywords);
	    twitterStream.filter(fil);

	    //twitterStream.sample();
	}

}
