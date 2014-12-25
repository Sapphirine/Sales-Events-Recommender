package servlet;
import javax.ws.rs.*;
import org.json.*;
import dbAPI.*;

@Path("/account")
public class account {

	@Path("/updateRetailer")
	@POST
	public javax.ws.rs.core.Response updateRetailer(String retailer_information) throws JSONException{
		System.out.println(retailer_information);
		rds api=rds.getInstance();
		JSONObject data=new JSONObject();
		data.put("success", api.update("retailer", retailer_information));
		return javax.ws.rs.core.Response.ok().entity(data).build();
	}
	
	@Path("/createRetailer")
	@POST
	public javax.ws.rs.core.Response createRetailer(String retailer_information) throws JSONException{
		System.out.println(retailer_information);
		rds api=rds.getInstance();
		//api.init();
		JSONObject data=new JSONObject();
		data.put("success", api.insert("retailer", retailer_information));
		return javax.ws.rs.core.Response.ok().entity(data).build();
	}
	
	@Path("/createUser")
	@POST
	public javax.ws.rs.core.Response createUser(String user_information) throws JSONException{
		System.out.println(user_information);
		rds api=rds.getInstance();
		//api.init();
		JSONObject data=new JSONObject();
		data.put("success", api.insert("twitter_user", user_information));
		return javax.ws.rs.core.Response.ok().entity(data).build();
	}
	
	@Path("/verifyUser")
	@POST
	public javax.ws.rs.core.Response verifyUser(String user_information) throws JSONException{
		System.out.println(user_information);
		rds api=rds.getInstance();
		JSONObject[] result_set=api.retrieve("twitter_user", user_information);
		System.out.println("returned result_set length: " + result_set.length);
		for(int i=0;i<result_set.length;i++){
			System.out.println(result_set[i].getString("password"));
		}
		String name=null;
		Double id=null;
		// verify password
		boolean success_value=false;
		if(result_set.length!=0){
			String password_db=result_set[0].getString("password");
			JSONObject obj=new JSONObject(user_information);
			String password_fr=obj.getString("password");
			success_value=(password_db.equals(password_fr));
			name=obj.getString("name");
			// get id of user
			System.out.println(result_set[0]);
			id=(double)result_set[0].get("user_id");
		}
		// encode
		JSONObject data=new JSONObject();
		data.put("success", success_value);
		data.put("name", name);
		data.put("user_id", id);
		return javax.ws.rs.core.Response.ok().entity(data).build();
	}
	
	@Path("/verifyRetailer")
	@POST
	public javax.ws.rs.core.Response verifyRetailer(String retailer_information) throws JSONException{
		System.out.println(retailer_information);
		rds api=rds.getInstance();
		JSONObject[] result_set=api.retrieve("retailer", retailer_information);
		System.out.println("returned result_set length: " + result_set.length);
		for(int i=0;i<result_set.length;i++){
			System.out.println(result_set[i].getString("password"));
		}
		// verify password
		boolean success_value=false;
		if(result_set.length!=0){
			String password_db=result_set[0].getString("password");
			JSONObject obj=new JSONObject(retailer_information);
			String password_fr=obj.getString("password");
			success_value=(password_db.equals(password_fr));
		}
		// encode
		JSONObject data=new JSONObject();
		data.put("success", success_value);
		return javax.ws.rs.core.Response.ok().entity(data).build();
	}
	
}
