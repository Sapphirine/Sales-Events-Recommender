package twitter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;



import java.util.List;
import java.util.Properties;

import javax.swing.text.html.parser.Entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.omg.CORBA.portable.InputStream;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.Attribute;
import com.amazonaws.services.simpledb.model.CreateDomainRequest;
import com.amazonaws.services.simpledb.model.Item;
import com.amazonaws.services.simpledb.model.PutAttributesRequest;
import com.amazonaws.services.simpledb.model.ReplaceableAttribute;
import com.amazonaws.services.simpledb.model.SelectRequest;
import com.amazonaws.services.simpledb.model.SelectResult;


public class DataFetch	 {
	public static AmazonSimpleDBClient sdb;
	public String myDomain = "car";
	public static String itemName = "51";
	public JSONObject obj;

	public JSONArray TweetsArray;
	
	
	//TODO: delete. This is for test purpose.
	public static void main(String[] args) throws Exception {
		String filePath = "/Users/Jialu/Desktop/10-30-2014-5.json";
	//	String filePath = "/Users/Jialu/Documents/EC2workspace/cloudOnePartOne/10-28-2014-0.json";
		DataFetch jfr = new DataFetch(filePath, "UTF-8");
        List<Item> items2 = jfr.readLatLngKeyword("Columbia");
		System.out.println("=============select begin, keyword is Baseball============");
		System.out.println(items2.size());
        for(Item item: items2){
        	List<Attribute> listAttribute = item.getAttributes();
        	//int size = listAttribute.size()/2;
        //	for (int i = 0; i < size; i++){
        	System.out.println("====!!!!========="+item.getAttributes().get(0).getValue());
        	System.out.println("====!!!!========="+item.getAttributes().get(1).getValue());
        //	}
//        	item.getName(); // itemName
//        //	List<Attribute> listAttribute = item.getAttributes();
//        	System.out.println("att===" + listAttribute.size());
        //	for(Attribute attribute:listAttribute){
        		//attribute.getName(); // Attribute Name
        //		System.out.println(attribute.getName());
        //		//attribute.getValue(); // Attribute value;
        //		System.out.println(attribute.getValue());
        //	}
        }
        
	}

	//TODO: add config.properties file.
//	private static String remoteUrl;
//	public static String remoteFileLocation;
//	public static String startDay="04-25-2014";
//	public static String Today="";
//	static {
//		Properties prop = new Properties();
//		String filename = "config.properties";
//		try{
//			prop.load(JsonFileReader.class.getResourceAsStream(filename));
//			remoteUrl = prop.getProperty("url");
//		} catch (Exception e) {
//			System.out.println("ERROR: Get API Key");
//		} finally {
//		}
//	}
 

//	/**
//	 * test url exits or not
//	 * @param URLName
//	 * @return
//	 */
//	public static boolean Urlexists(String URLName){
//	    try {
//	      HttpURLConnection con =
//	         (HttpURLConnection) new URL(URLName).openConnection();
//	      con.setInstanceFollowRedirects(false);
//	      con.setRequestMethod("HEAD");
//	      return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
//	    }
//	    catch (Exception e) {
//	       e.printStackTrace();
//	       return false;
//	    }
//	} 
	

	// TODO: add return value (boolean)
	public DataFetch(String FilePath, String Type) throws Exception {
		try {

    		String str="";

    		String encoding = Type;

    		InputStreamReader read;

			read = new InputStreamReader(new FileInputStream(FilePath), encoding);

    		BufferedReader bufread = new BufferedReader(read);

    		String tmpLineVal;

    		while((tmpLineVal = bufread.readLine())!=null)

			{

				str=str + tmpLineVal;		

			}

    		bufread.close();

    		read.close();

    		

            //JSON:

    		str = clearStr(str);

    		//System.out.println(str.substring(225800, 225920));

    		//System.out.println(str.substring(225800, 225920));

			obj = new JSONObject(str);
			AWSCredentials credentials = new PropertiesCredentials(DataFetch.class.getResourceAsStream("AwsCredentials.properties"));
			sdb = new AmazonSimpleDBClient(credentials);
			Thread.sleep(10000);
	        //SimpleDB sdb = new SimpleDB(credentials);
	
			Region usEast1 = Region.getRegion(Regions.US_EAST_1);
			sdb.setRegion(usEast1);
			
			// Create a domain
		    System.out.println("Creating domain called " + myDomain + ".\n");
		//    sdb.createDomain(new CreateDomainRequest(myDomain));
            System.out.println("flag1");
		    // ...
            
            Thread.sleep(30000);
          
          

			//TweetsArray = obj.getJSONObject("entityinfo").getJSONArray("tweets");
			
			for(int i = 0; i <= 4; i++) {
				 List<ReplaceableAttribute> list = new ArrayList<ReplaceableAttribute>();
				PutAttributesRequest putAttributesRequest = new PutAttributesRequest();
		        putAttributesRequest.setDomainName(myDomain);
		        putAttributesRequest.setItemName(itemName);
				String s0 = obj.getJSONArray("tweets").getJSONObject(i).getString("user");
				String s1 = obj.getJSONArray("tweets").getJSONObject(i).getString("text");
				String s2 = obj.getJSONArray("tweets").getJSONObject(i).getString("Date");
				Double s3 = obj.getJSONArray("tweets").getJSONObject(i).getDouble("Latitude");
				Double s4 = obj.getJSONArray("tweets").getJSONObject(i).getDouble("Longitude");
				String s5 = obj.getJSONArray("tweets").getJSONObject(i).getString("keyword");
				
//				System.out.println(s0);
//				System.out.println(s1);
//				System.out.println(s2);
//				System.out.println(s3);
				System.out.println("json keyword is");
				System.out.println(s5);		
				
				list.add(new ReplaceableAttribute("user", s0, true));
	            list.add(new ReplaceableAttribute("text", s1, true));
	            list.add(new ReplaceableAttribute("Date", s2, true));
	            list.add(new ReplaceableAttribute("Latitude",s3.toString() , true));
	            list.add(new ReplaceableAttribute("Longitude", s4.toString(), true));
	            list.add(new ReplaceableAttribute("keyword", s5.toString(),true));
				putAttributesRequest.setAttributes(list);
	            sdb.putAttributes(putAttributesRequest);
	            Integer k= Integer.parseInt(itemName)+1;
	            itemName=k.toString();
	            System.out.println(itemName);
			}
			

		    
		
			} catch (UnsupportedEncodingException e) {

				e.printStackTrace();

			} catch (FileNotFoundException e) {

				e.printStackTrace();

			} catch (IOException e) {

				e.printStackTrace();

			} catch (JSONException e) {

				e.printStackTrace();

			}
	}
//		remoteFileLocation = remoteUrl + time + "-" + index + ".json";
//		if(!Urlexists(remoteFileLocation))
//		{
//			return false;
//		}
//		else
//		{
//			try{
//				URL remote = new URL(remoteFileLocation);
//				System.out.println(remoteFileLocation);
//				
//				HttpURLConnection conn = (HttpURLConnection)remote.openConnection();
//				if(conn.getResponseCode() == 200)
//				{
//					//Successfully
//					JSONObject obj;
//					JSONArray TweetsArray;
//
//
//					//DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
//					
//					//List<Entity> batchEntities = new ArrayList<Entity>(500);
//					//Retrive 500 tweets(or 1 file) at a time
//					
//					String str="";
//
//		    		String encoding = Type;
//
//		    		InputStreamReader read;
//
//					read = new InputStreamReader(new FileInputStream(FilePath), encoding);
//
//		    		BufferedReader bufread = new BufferedReader(read);
//
//		    		String tmpLineVal;
//		    		
//					InputStream urlInputStream = (InputStream)remote.openConnection().getInputStream();
//							
//					BufferedReader bufread = new BufferedReader(new InputStreamReader(urlInputStream, "UTF-8"));
//
//					String tmpLineVal;
//
//    				while((tmpLineVal = bufread.readLine())!=null)
//
//					{
//
//						str=str + tmpLineVal;		
//
//					}
//
//    				bufread.close();
//
//    				read.close();
//
//    		
//
//            		//JSON:
//
//    				str = clearStr(str);
//
//    				obj = new JSONObject(str);
//
//    				AWSCredentials credentials = new PropertiesCredentials(JsonFileReader.class.getResourceAsStream("AwsCredentials.properties"));
//					sdb = new AmazonSimpleDBClient(credentials);
//			        //SimpleDB sdb = new SimpleDB(credentials);
//			
//					Region usEast1 = Region.getRegion(Regions.US_EAST_1);
//					sdb.setRegion(usEast1);
//					
//					// Create a domain
//					String myDomain = "car";
//				    String itemName = "Toyota";
//				    System.out.println("Creating domain called " + myDomain + ".\n");
//				    sdb.createDomain(new CreateDomainRequest(myDomain));
//		            System.out.println("flag1");
//				    // ...
//		            
//		            Thread.sleep(10000);
//		            PutAttributesRequest putAttributesRequest = new PutAttributesRequest();
//		            putAttributesRequest.setDomainName(myDomain);
//		            putAttributesRequest.setItemName(itemName);
//		            List<ReplaceableAttribute> list = new ArrayList<ReplaceableAttribute>();
//
//					TweetsArray = obj.getJSONObject("entityinfo").getJSONArray("tweets");
//					
//					if(tweets!=null)
//					{
//						for(int i = 0; i <= 9; i++) 
//						{
//							String s0 = obj.getJSONArray("tweets").getJSONObject(i).getString("user");
//							String s1 = obj.getJSONArray("tweets").getJSONObject(i).getString("text");
//							String s2 = obj.getJSONArray("tweets").getJSONObject(i).getString("Date");
//							Double s3 = obj.getJSONArray("tweets").getJSONObject(i).getDouble("Latitude");
//							Double s4 = obj.getJSONArray("tweets").getJSONObject(i).getDouble("Longitude");
//								
//							
//							list.add(new ReplaceableAttribute("user", s0, true));
//				            list.add(new ReplaceableAttribute("text", s1, true));
//				            list.add(new ReplaceableAttribute("Date", s2, true));
//				            list.add(new ReplaceableAttribute("Latitude",s3.toString() , true));
//				            list.add(new ReplaceableAttribute("Longitude", s4.toString(), true));				
//						}
//			
//						putAttributesRequest.setAttributes(list);
//            			sdb.putAttributes(putAttributesRequest);
//					}
//					else
//					{
//						return false;
//					}
//				}
//			}
//			catch(IOException | ParseException  e)
//			{
//					e.printStackTrace();
//			}
//		}
//		
//		return true;
	
	/*
	 * Getting Entity from simpleDB by Date
	 * @param: Date 
	 * @return: a list of entities
	 */
	public List<Item> readLatLng(String date)
	{
    	SelectResult selectResult = null;
        String query = null;
        
        query = "select Latitude, Longitude from " + myDomain + " where Date = '" + date + "'";
        SelectRequest selectRequest = new SelectRequest(query);
        selectResult = sdb.select(selectRequest);
        List<Item> l = selectResult.getItems();
        return l;
    }
	
	/*
	 * Getting Entity from simpleDB by Date and keyword
	 * @param: Date, keyword
	 * @return: a list of entities
	 */
	public List<Item> readLatLngDateKeyword(String date, String keyword)
	{
    	SelectResult selectResult = null;
        String query = null;
        
        query = "select Latitude, Longitude from " + myDomain + " where Date = '" + date + "' and keyword = '" + keyword + "'";
        SelectRequest selectRequest = new SelectRequest(query);
        selectResult = sdb.select(selectRequest);
        List<Item> l = selectResult.getItems();

        return l;
    }
	
	/*
	 * Getting Entity from simpleDB by keyword
	 * @param: keyword
	 * @return: a list of entities
	 */
	public List<Item> readLatLngKeyword(String word)
	{
    	SelectResult selectResult = null;
        String query = null;
        
        query = "select Latitude, Longitude, keyword from " + myDomain 
        		+ " where keyword = '" + word + "'";
        SelectRequest selectRequest = new SelectRequest(query);
        selectResult = sdb.select(selectRequest);
        List<Item> l = selectResult.getItems();

        return l;
    }
	

	// To Delete:

    String getName () {

    	String str = null;

    	try {

			str = obj.getJSONObject("entityinfo").getString("entity");

		} catch (JSONException e) {

			// TODO Auto-generated catch block

			e.printStackTrace();

		}

    	return str;

    }

	String get(String Name, int Num) throws JSONException {

		if (Num < TweetsArray.length()) {

			String str = TweetsArray.getJSONObject(Num).getString(Name);

            return str;

		}

		else {

			System.out.println("The query is out of range!");

			return "0";

		}

	}

	

	int length() {

		return TweetsArray.length();

	}

	

	Date date() {

		String str="2010-04-19 20:00";

		try {

			

			str = obj.getJSONObject("entityinfo").getString("harvest_time");

		} catch (JSONException e) {

			// TODO Auto-generated catch block

			e.printStackTrace();

		}

		Date ordate = new Date(0);

		SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		String dateStringToParse = str;

		try {

			ordate = bartDateFormat.parse(dateStringToParse);

		} catch (ParseException e1) {

			// TODO Auto-generated catch block

			e1.printStackTrace();

		}

		return ordate;

    }



String clearStr(String str) {

	int i = 0;

	int n = str.length();

	char[] c = str.toCharArray();

	while (i<n) {

		if (c[i] == '\\') {

			if (i<n-1) {

				if (c[i+1] == '\\') {

					i++;

				}

				else {

					c[i] = '!';

				}

			}

			else {}

		}

		else {}

		i++;

	}

	return new String(c);

}

String entityname() {

	String str = "";

	try {

		str = obj.getJSONObject("entityinfo").getString("entity");

	} catch (JSONException e) {

		// TODO Auto-generated catch block

		e.printStackTrace();

	}

	return str;

}
}
