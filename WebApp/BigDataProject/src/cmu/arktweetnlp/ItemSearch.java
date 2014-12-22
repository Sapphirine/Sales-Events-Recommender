package cmu.arktweetnlp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

/*
 * This class shows how to make a simple authenticated ItemLookup call to the
 * Amazon Product Advertising API.
 * 
 * See the README.html that came with this sample for instructions on
 * configuring and running the sample.
 */
public class ItemSearch {

    private static final String AWS_ACCESS_KEY_ID = "AKIAJO7HUEJZDB6ZFIEA";
    private static final String AWS_SECRET_KEY = "x+6GUE/Bqpg66EqLv5j7QkW8g6tW0tI6j9lwZaTE";
    private static final String ASSOCIATE_TAG = "bigdataprojec-20";
    private static final String ENDPOINT = "webservices.amazon.com";
    

    /*
     * Utility function to fetch the response from the service and extract the XML.
     */
    private static ArrayList<String> fetchContent(String requestUrl) {
        String title = null;
        String url = null;
        //Map<String, String> content = new HashMap<String, String>();
        ArrayList<String> list = new ArrayList<String>();
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(requestUrl);

            //can fetch multiple items
            for (int i = 0; i< 1; i++) {
              //item title
                Node titleNode = doc.getElementsByTagName("Title").item(i);
                title = titleNode.getTextContent();
                //item url
                Node urlNode = doc.getElementsByTagName("DetailPageURL").item(i);
                url = urlNode.getTextContent();
                //content.put("name", title);
                //content.put("url", url);
                list.add(title);
                list.add(url);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }
    
    public static ArrayList<String> getProductInfo(String KeyWord, String Dept) {
        /*
         * Set up the signed requests helper 
         */
        SignedRequestsHelper helper;
        try {
            helper = SignedRequestsHelper.getInstance(ENDPOINT, AWS_ACCESS_KEY_ID, AWS_SECRET_KEY);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
        String requestUrl = null;

        /* The helper can sign requests in two forms - map form and string form */
        
        /*
         * Here is an example in map form, where the request parameters are stored in a map.
         */
        
        Map<String, String> params = new HashMap<String, String>();
        params.put("Service", "AWSECommerceService");
        params.put("Version", "2009-03-31");
        params.put("Operation", "ItemSearch");
        params.put("SignatureMethod", "HmacSHA256");
        params.put("SignatureVersion", "2");
        params.put("AssociateTag", ASSOCIATE_TAG);
        
        //search info
        params.put("Timestamp", "2014-12-05T18:40:30");
        params.put("Keywords", KeyWord);
        params.put("SearchIndex", Dept);
        
        //Map<String, String> result = new HashMap<String, String>();
        ArrayList<String> result = new ArrayList<String>();

        requestUrl = helper.sign(params);
        //System.out.println("Signed Request is \"" + requestUrl + "\"");
        result = fetchContent(requestUrl);
        //System.out.println("Signed Title is \"" + title + "\"");
        //System.out.println();
        return result;
        
    }

}
