package cmu.arktweetnlp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import cmu.arktweetnlp.RunTagger.Decoder;
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
import twitter4j.conf.Configuration;
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
	public static int Query_Size;
	public static ConfigurationBuilder cb = new ConfigurationBuilder();
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
	
    public static void main(String[] args) throws TwitterException, ParseException, InterruptedException, IOException {
		Initial();
   
            
            		

	        ConfigurationBuilder cb = new ConfigurationBuilder();
	        cb.setDebugEnabled(true)
	            .setOAuthConsumerKey(ConsumeKey[0])
	            .setOAuthConsumerSecret(ConsumerSecret[0])
	            .setOAuthAccessToken(AccessToken[0])
	            .setOAuthAccessTokenSecret(AccessTokenSecret[0]);

	        TwitterFactory tf = new TwitterFactory(cb.build());


	       Twitter twitter = tf.getInstance();
			Query query = new Query("iphone6");   
			query.setResultType(Query.RECENT);
		    query.setCount(10);   
		    QueryResult result = twitter.search(query);  
		    for (Status status : result.getTweets()) {              
		      System.out.println("@" + status.getUser().getScreenName() + ":" + status.getText());
		    }
	        long cursor = -1;
            twitter.getHomeTimeline();
            for (Status status:  twitter.getHomeTimeline()){
            	System.out.println("status is :" + status.getId());
            	System.out.println("status is :" + status.getInReplyToStatusId());
            }
	        IDs ids = twitter.getFollowersIDs(cursor);
	     //   System.out.println(ids);
	        do {
	        
	            for (long id : ids.getIDs()) {               


	                String ID = "followers ID #" + id;
	                String[] firstname = ID.split("#");
	                String first_Name = firstname[0];
	                String Id = firstname[1];

	              //  Log.i("split...........", first_Name + Id);

	                String Name = twitter.showUser(id).getName();
	                String screenName = twitter.showUser(id).getScreenName();
                    System.out.println(Name);
                    final List<Status> statuses = twitter.getUserTimeline(screenName);
                    
                    System.out.println("&&&&&&&&&&&&&&&&&&&&&&"+Name);
                    String tmp="10";
                    for (Status status : statuses) {
                    	System.out.println("status is: !!!!!!!"+status.getId());
                    	System.out.println("response status is :!!!!!!!!!"+status.getInReplyToStatusId());
                        if (status.getInReplyToStatusId()==Long.parseLong("542254825703219200")) {
                        	System.out.println("the response is:" + status.getText());
                        	
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
                    	File f = new File("/Users/qianyizhong/Desktop/example.txt");
                    	boolean success = f.delete();
                    	String originalText = status.getText();//get original twitter text
                    	/***********process tweet text********************/
                    	/***********write to example.txt******************/

                	    try {
                	      File file = new File("/Users/qianyizhong/Desktop/example.txt");
                	      BufferedWriter output = new BufferedWriter(new FileWriter(file));
                	      output.write(originalText);
                	      output.close();
                	    } catch ( IOException e ) {
                	       e.printStackTrace();
                	    }
                	    
                	    
                	    
                	    /************Run NLP ******************/
                	    

                		System.err.println("1.here~~~~~~~");
                		RunTagger tagger;
						try {
							tagger = new RunTagger();
							tagger.inputFilename="/Users/qianyizhong/Desktop/example.txt";
							try {
								tagger.runTagger();
							} catch (ClassNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}		
						} catch (UnsupportedEncodingException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						   AnalyzeNLP a = new AnalyzeNLP();
						   ArrayList<String> str = a.getNoun();
						   System.out.println(str);
						   String product = "";
						   if (product.length() != 0) {
                         for (int i = 0; i < str.size()-1; i++) {
                        	 product = product + str.get(i) + " ";
                         }
                             product = product + str.get(str.size()-1);
						   }
					//	   String product = str.toString();
						   System.out.println(product);
                	    
                	    if (product != null && product.length() != 0)  {
                    	
                        ArrayList<String> list = ItemSearch.getProductInfo(product, "Toys");
                        System.out.println(list.get(0));
                        System.out.println(list.get(1));
                    //    tmp=list.get(1);
                        tmp="test";
                        StatusUpdate st = new StatusUpdate("@"+status.getUser().getScreenName()+" "+tmp);
                        System.out.println("statusid!!!!!!!!!"+status.getId());
                     //   st.inReplyToStatusId(status.getId()).getStatus();
                      //  System.out.println(st.inReplyToStatusId(status.getId()).inReplyToStatusId(status.getId()));
                        st.setInReplyToStatusId(status.getId());
                     
 
                   //     st.notify();
                        GeoLocation location= new GeoLocation(-35.9281897,26.0819792);
                        st.setLocation(location);
                        try {
                          twitter.updateStatus(st);
                          Thread.sleep(10000);
                        } catch (TwitterException e) {
                          // TODO Auto-generated catch block
                          e.printStackTrace();
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

            
	     //   status = twitter.showStatus(id="112652479837110273")
	         
	        
	        
	            }

	           
	  
}
