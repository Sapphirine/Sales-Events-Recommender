package servlet;

import javax.ws.rs.*;

import org.json.*;
import dbAPI.*;

@Path("/retailer")
public class retailer {
	@Path("/createProduct")
	@GET
	public javax.ws.rs.core.Response createProduct(@QueryParam("test") String testStr) throws JSONException{
		System.out.println(testStr);
		JSONObject data=new JSONObject();
		data.put("success", true);
		System.out.println("test");
		return javax.ws.rs.core.Response.ok().entity(data).build();
	}
	
	@Path("/updateProduct")
	@POST
	public javax.ws.rs.core.Response updateProduct(String testStr) throws JSONException{
		System.out.println("testStr");
		JSONObject product=new JSONObject(testStr);
		String productID="123";
		return javax.ws.rs.core.Response.ok().entity(productID).build();	
	}
}
