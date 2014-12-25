package servlet;

import javax.ws.rs.*;

import org.json.*;

import dbAPI.*;

@Path("/user")
public class user {
	@Path("/searchProduct")
	@GET
	public javax.ws.rs.core.Response searchProduct(@QueryParam("keyWords") String key_words) throws JSONException{
		System.out.println("key words: " + key_words);
		// search product in database and get result set
		rds api=rds.getInstance();
		JSONObject keyWords=new JSONObject();
		keyWords.put("key_words", key_words);
		JSONObject[] result_set=api.retrieve("product", keyWords.toString());
		if(result_set.length!=0){
			for(int i=0;i<result_set.length;i++){
				System.out.println(result_set);
			}
		}
				JSONObject data=new JSONObject();
		data.put("products", result_set);
		data.put("success", true);
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
	
	@Path("/getHistory")
	@GET
	public javax.ws.rs.core.Response getHistory(@QueryParam("user_name") String user_name) throws JSONException{
		System.out.println(user_name);
		rds api=rds.getInstance();
		JSONObject user=new JSONObject();
		user.put("user_name", user_name);
		JSONObject[] result_set=api.retrieve("history", user.toString());
		if(result_set.length!=0){
			for(int i=0;i<result_set.length;i++){
				System.out.println(result_set);
			}
		}
		JSONObject data=new JSONObject();
		data.put("products", result_set);
		return javax.ws.rs.core.Response.ok().entity(data).build();
	}
	
	@Path("/getRecommend")
	@GET
	public javax.ws.rs.core.Response getRecommend(@QueryParam("user_name") String user_name) throws JSONException{
		System.out.println(user_name);
		rds api=rds.getInstance();
		JSONObject user=new JSONObject();
		user.put("user_name", user_name);
		JSONObject[] result_set=api.retrieve("recommendation", user.toString());
		if(result_set.length!=0){
			for(int i=0;i<result_set.length;i++){
				System.out.println(result_set);
			}
		}
		JSONObject data=new JSONObject();
		data.put("products", result_set);
		return javax.ws.rs.core.Response.ok().entity(data).build();
	}
	
}