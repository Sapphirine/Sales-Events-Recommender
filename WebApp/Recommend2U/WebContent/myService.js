//app.service("myService", function(){
//	var user;
////	var user2="yuechen";
//	var setUser=function(user_name){
//		user=user_name;
//		alert("set user: " + user);
//	};
//	var getUser=function(){
//		alert("get user: " + user);
//		return user;
//	};
//	
//	return{
//		getUser:getUser,
//		setUser:setUser
//	};
//});

app.service("myService", function(){
	var user;
	return{
		getUser: function(){
			alert("get user: " + user);
			return user;
		},
		setUser: function(user_naem){
			user=user_name
			alert("set user: " + user);
		}
	};
});
