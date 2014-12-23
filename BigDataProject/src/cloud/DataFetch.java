package cloud;

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
	public String myDomain = "car1";
	public String itemName = "100000";
	public JSONObject obj;

	public JSONArray TweetsArray;
	
	public static void main(String[] args) throws Exception {
		String filePath = "/Users/qianyizhong/Documents/workspace/SSS/10-30-2014-0.json";
		//String filePath = "/Users/Jialu/Desktop/10-30-2014-0.json";
		DataFetch jfr = new DataFetch(filePath, "UTF-8");
        List<Item> items2 = jfr.readLatLngKeyword("Computer");
		System.out.println("=============select begin, keyword is Baseball============");
		System.out.println(items2.size());
        for(Item item: items2){
        	List<Attribute> listAttribute = item.getAttributes();
        	System.out.println("====!!!!========="+item.getAttributes().get(0).getValue());
        	System.out.println("====!!!!========="+item.getAttributes().get(1).getValue());
        }
        
        AWSCredentials credentials = new PropertiesCredentials(DataFetch.class.getResourceAsStream("AwsCredentials.properties"));
        AmazonSimpleDBClient newDb = new AmazonSimpleDBClient(credentials);
        
        SelectResult selectResult = null;
        String query = null;     
        query = "select Latitude, Longitude, keyword from " + "car1" + " where keyword = '" + "University" + "'";
        SelectRequest selectRequest = new SelectRequest(query);
        selectResult = newDb.select(selectRequest);
        List<Item> l = selectResult.getItems();
        System.out.println("??????????" + l.get(0));
        
	}

	/*
	 * Constructor of Parsing json file to aws simpleDB
	 * @param: FilePath, Type 
	 */
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

    		str = clearStr(str);

			obj = new JSONObject(str);
			AWSCredentials credentials = new PropertiesCredentials(DataFetch.class.getResourceAsStream("AwsCredentials.properties"));
			sdb = new AmazonSimpleDBClient(credentials);
	
			Region usEast1 = Region.getRegion(Regions.US_EAST_1);
			sdb.setRegion(usEast1);
			
		    System.out.println("Creating domain called " + myDomain + ".\n");
		   sdb.createDomain(new CreateDomainRequest(myDomain));
            System.out.println("flag1");
		    // ...
            
            Thread.sleep(10000);
			
			for(int i = 0; i <= 9; i++) {
				 List<ReplaceableAttribute> list = new ArrayList<ReplaceableAttribute>();
				PutAttributesRequest putAttributesRequest = new PutAttributesRequest();
		        putAttributesRequest.setDomainName(myDomain);
		        putAttributesRequest.setItemName(itemName+FilePath);
		//	String s0 = obj.getJSONArray("tweets").getJSONObject(i).getString("user");
//				String s1 = obj.getJSONArray("tweets").getJSONObject(i).getString("text");
				String s2 = obj.getJSONArray("tweets").getJSONObject(i).getString("Date");
				Double s3 = obj.getJSONArray("tweets").getJSONObject(i).getDouble("Latitude");
				Double s4 = obj.getJSONArray("tweets").getJSONObject(i).getDouble("Longitude");
				String s5 = obj.getJSONArray("tweets").getJSONObject(i).getString("keyword");
//				
//				System.out.println(s0);
//				System.out.println(s1);
//				System.out.println(s2);
//				System.out.println(s3);
				System.out.println("json keyword is");
				System.out.println(s5);		
				
		//		list.add(new ReplaceableAttribute("user", s0, true));
	           // list.add(new ReplaceableAttribute("text", s1, true));
	            list.add(new ReplaceableAttribute("Date", s2, true));
	            list.add(new ReplaceableAttribute("Latitude",s3.toString() , true));
	            list.add(new ReplaceableAttribute("Longitude", s4.toString(), true));
	            list.add(new ReplaceableAttribute("keyword", s5.toString(),true));
				putAttributesRequest.setAttributes(list);
	            sdb.putAttributes(putAttributesRequest);
	            Integer k= Integer.parseInt(itemName)+1;
	            itemName=k.toString();
	            System.out.println(itemName);
	            if (k%50==0){
	            	Thread.sleep(1000*3600);
	            }
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
