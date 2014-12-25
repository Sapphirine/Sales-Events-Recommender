package servlet;

import javax.ws.rs.*;

import org.json.*;

import dbAPI.*;

@Path("/retailer")
public class retailer {
	@Path("/postProduct")
	@POST
	public javax.ws.rs.core.Response createProduct(String product_info) throws JSONException{
		System.out.println(product_info);
		rds api=rds.getInstance();
		api.insert("product", product_info);
		
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
	@Path("/retrievePost")
	@GET
	public javax.ws.rs.core.Response retrievePost(@QueryParam("retailer") String retailer_name) throws JSONException{
		System.out.println(retailer_name);
		// search products posted by this retailer
		rds api=rds.getInstance();
		JSONObject retailer=new JSONObject();
		retailer.put("name", retailer_name);
		JSONObject[] result = api.retrieve("product_posted", retailer.toString());
		JSONObject data=new JSONObject();
		data.put("result", result);
		return javax.ws.rs.core.Response.ok().entity(data).build();
	}
}
