package Util;

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

    private static final String AWS_ACCESS_KEY_ID = "";
    private static final String AWS_SECRET_KEY = "";
    private static final String ASSOCIATE_TAG = "";
    private static final String ENDPOINT = "webservices.amazon.com";
    

    /*
     * Utility function to fetch the response from the service and extract the XML.
     */
    private static ArrayList<String> fetchContent(String requestUrl) {
        String title = null;
        String url = null;
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
                Node itemID = doc.getElementsByTagName("ASIN").item(i);
                url = "http://amzn.com/" + itemID.getTextContent();
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
         * The request parameters are stored in a map.
         */
        
        Map<String, String> params = new HashMap<String, String>();
        params.put("Service", "AWSECommerceService");
        params.put("Operation", "ItemSearch");
        params.put("SignatureMethod", "HmacSHA256");
        params.put("SignatureVersion", "2");
        params.put("AssociateTag", ASSOCIATE_TAG);
        
        //search info
        params.put("Keywords", KeyWord);
        params.put("SearchIndex", Dept);
        
        ArrayList<String> result = new ArrayList<String>();

        requestUrl = helper.sign(params);
        System.out.println("Signed Request is \"" + requestUrl + "\"");
        result = fetchContent(requestUrl);
        //System.out.println("Signed Title is \"" + title + "\"");
        //System.out.println();
        return result;
        
    }

}
