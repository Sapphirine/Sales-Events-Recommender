package twitter;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Example application that uses OAuth method to acquire access to your account.<br>
 * This application illustrates how to use OAuth method with Twitter4J.<br>
 *
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public final class UpdateStatus {
	public static int Key_Size;
	public static String[] ConsumeKey;
	public static String[] ConsumerSecret;
	public static String[] AccessToken;
	public static String[] AccessTokenSecret;
	
	public static String[] keywords;
	public static int Query_Size;
	public static ConfigurationBuilder cb = new ConfigurationBuilder();
    /**
     * Usage: java twitter4j.examples.tweets.UpdateStatus [text]
     *
     * @param args message
     */
    public static void main(String[] args) {
    	Properties prop = new Properties();
        String filename = "config.properties";
		try {
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
		cb.setDebugEnabled(true)
		.setOAuthConsumerKey(ConsumeKey[0])  
		.setOAuthConsumerSecret(ConsumerSecret[0])
		.setOAuthAccessToken(AccessToken[0])
		.setOAuthAccessTokenSecret(AccessTokenSecret[0]);
        if (args.length < 1) {
            System.out.println("Usage: java twitter4j.examples.tweets.UpdateStatus [text]");
         //   System.exit(-1);
        }
        try {
        	 System.out.println("xxx");
            Twitter twitter = new TwitterFactory().getInstance();
            System.out.println("xxx");
            try {
            	twitter.setOAuthConsumer("IPARRPuQ5DBarVoVeVZehgNVz", "43MIOppURdou2M2qJyqeRW3j3jSrdTEdWK7RuJdhqFfMvLPqxD");
                // get request token.
                // this will throw IllegalStateException if access token is already available
            	System.out.println("xxx");
            	
             //   RequestToken requestToken = twitter.getOAuthRequestToken();
//               System.out.println(requestToken.getAuthenticationURL());
//                System.out.println("xxx");
//                System.out.println("Got request token.");
//                System.out.println("Request token: " + requestToken.getToken());
//                System.out.println("Request token secret: " + requestToken.getTokenSecret());
                AccessToken accessToken = new AccessToken("2844197428-whw0SKsjpslz5ta7PJjMLcIQKnuNw5iZzZfSJl9","fX40K9iymU8CuGLmgmZzbviNNqDwXBWmtVQxyLb5KltUP");
               // AccessToken accessToken = null;
               
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                while (null == accessToken) {
                    System.out.println("Open the following URL and grant access to your account:");
                  //  System.out.println(requestToken.getAuthorizationURL());
                    System.out.print("Enter the PIN(if available) and hit enter after you granted access.[PIN]:");
                    String pin = br.readLine();
//                    try {
//                        if (pin.length() > 0) {
//                        	
//                            accessToken = twitter.getOAuthAccessToken(requestToken, pin);
//                        } else {
//                            accessToken = twitter.getOAuthAccessToken(requestToken);
//                        }
//                    } catch (TwitterException te) {
//                        if (401 == te.getStatusCode()) {
//                            System.out.println("Unable to get the access token.");
//                        } else {
//                            te.printStackTrace();
//                        }
//                    }
                }
                System.out.println("Got access token.");
                System.out.println("Access token: " + accessToken.getToken());
                System.out.println("Access token secret: " + accessToken.getTokenSecret());
            } catch (IllegalStateException ie) {
                // access token is already available, or consumer key/secret is not set.
                if (!twitter.getAuthorization().isEnabled()) {
                    System.out.println("OAuth consumer key/secret is not set.");
                    System.exit(-1);
                }
            }
            Status status = twitter.updateStatus("Cool");
            System.out.println("Successfully updated the status to [" + status.getText() + "].");
            System.exit(0);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get timeline: " + te.getMessage());
            System.exit(-1);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("Failed to read the system input.");
            System.exit(-1);
        }
    }
}