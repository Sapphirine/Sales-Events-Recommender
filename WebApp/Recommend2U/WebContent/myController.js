/**
 * Created by dongxueliu on 12/8/14.
 */
app.controller("myController", function($scope, $http, $location, $window, myService){

	$scope.user="test user";
	$scope.retailer="test retailer";
	$scope.userName="test user name";
	$scope.createUserMessage="";
	$scope.createRetailerMessage="";
	$scope.recommend_product_info=[];

	/*************************************************user service******************************************************/
	$scope.searchProducts=function(key_words){
//		alert(key_words);
		var config={params:{keyWords:key_words}};
        var response=$http.get('/Recommend2U/CRM/user/searchProduct', config);
        response.success(function(data,statuc,headers,config){
        	$scope.searchResult=data.products;
        	//alert(data.products[0].product_id);
        	$scope.searchResultShow=true;
//        	alert($scope.searchResult.length);
        });
	};
	
	$scope.getHistory=function(user_name){
		//alert(user_name);
		var config={params:{user_name:user_name}};
        var response=$http.get('/Recommend2U/CRM/user/getHistory', config);
        response.success(function(data,statuc,headers,config){
        	$scope.history_product=data.products;
        	//alert(data.products.length);
        	$scope.historyShow=true;
        	// alert($scope.searchResult.length);
        });
	};
	
	$scope.addLike=function(product_id){};
	$scope.writeReview=function(){};
	
	$scope.getRecommend=function(user_name){
		//alert(user_name);
		var config={params:{user_name: user_name}};
        var response=$http.get('/Recommend2U/CRM/user/getRecommend', config);
        response.success(function(data,statuc,headers,config){
        	$scope.recommend_product_info=data.products;
//        	$scope.recommend_product_info=data.products;
        	//alert($scope.recommend_product_info.length);
        });
	};

	/******************************************************* retailer service *****************************************************/
	
	
	$scope.postProduct = function(product_info){
//		alert(product_info.retailer);
		var response=$http.post("/Recommend2U/CRM/retailer/postProduct", product_info);
		response.success(function(data,status,headers,config){
			
		});
	};
	
	$scope.updateRetailer=function(name, password){
		$scope.updateRetailer={};
		$scope.updateRetailer.name=name; 
		$scope.updateRetailer.password=password;
		var response=$http.post("/Recommend2U/CRM/account/updateRetailer", $scope.updateRetailer);
		response.success(function(data,status,headers,config){
			if(data.success){
				$scope.updateRetailerMessage="update succeeded! Thanks!";
			}else if(!data.success){
				$scope.updateRetailerMessage="update failed, please try again..";
			}else{
				$scope.updateRetailerMessage="SORRY, INTERNAL SERVER ERROR";
			}
		});
	};
	
	$scope.retrievePost=function(retailer_name){
		
		var config={params:{retailer: retailer_name}};
        var response=$http.get('/Recommend2U/CRM/retailer/retrievePost', config);
        response.success(function(data,statuc,headers,config){
//        	alert("received by controller: " + data.result.length);
        	$scope.product_info=data.result;
//    		$scope.event_info;
//        	$scope.searchResultShow=true;
//        	alert($scope.product_info.length);
        });
	};
	
	
	
	/**************************************************  account operation  ****************************************************/
	$scope.callToSetUser=function(user_name){
		myService.setUser(user_name);
	};
    $scope.createRetailer=function(retailer){
        var response=$http.post("/Recommend2U/CRM/account/createRetailer", retailer);
        response.success(function(data, status, headers, config){
            if(data.success){
            	$scope.createRetailerMessage="Create succeeded! Thanks!";
            }else if(!data.success){
            	$scope.createRetailerMessage="Create failed, please try again...";
            }else{
            	$scope.createRetailerMessage="SORRY, INTERNAL SERVER ERROR";
            }  
        });
    };
    $scope.createUser=function(user){
    	var response=$http.post("/Recommend2U/CRM/account/createUser", user);
    	response.success(function(data, status, headers, config){
    		if(data.success){
    			$scope.createUserMessage="Create succeeded! Thanks!";
    		}else if(!data.success){
    			$scope.createUserMessage="Create failed, please try again...";
    		}else{
    			$scope.createUserMessage="SORRY, INTERNAL SERVER ERROR";
    		}
    	});
    };
    
    $scope.setUserShow=function(){
    	$scope.userShow=true;
    	$scope.retailerShow=false;
    };
    $scope.setRetailerShow=function(){
    	$scope.userShow=false;
    	$scope.retailerShow=true;
    };
    
    $scope.setUserName=function(name){
    	$scope.userName=name;
    };
    
    $scope.setUserID=function(id){
    	$scope.user=id;
    };
    
    // user login 
    $scope.verifyUser=function(user, $location, $window){
    	$scope.user=user.name;
    	var response=$http.post("/Recommend2U/CRM/account/verifyUser", user);
    	response.success(function(data, status, headers, config){
//    		alert("data.success: " + data.success);
    		if(data.success){
    			$scope.setUserName(data.name);
    			$scope.setUserID(data.user_id);
    			$scope.userLoginMessage="Login succeeded! Thanks!";   
    			$scope.setUserShow();
    			//$scope.go("http://localhost:8080/Recommend2U/userMainPage.html");
    		}else if(!data.success){
    			$scope.userLoginMessage="Login failed, please try again...";
    		}else{
    			$scope.userLoginMessage="SORRY, INTERNAL SERVER ERROR";
    		}	
    	});
    };

    // retailer login
    $scope.verifyRetailer=function(retailer){
    	$scope.retailer=retailer.name;
    	$scope.retailerLoginMessage="";
    	var response=$http.post("/Recommend2U/CRM/account/verifyRetailer", retailer);
    	response.success(function(data, status, headers, config){
    		if(data.success){
    			$scope.retailerLoginMessage="Login succeeded! Thanks!"; 
    			$scope.setRetailerShow();
    			//scope.go("http://localhost:8080/Recommend2U/retailerMainPage.html");
    		}else if(!data.success){
    			$scope.retailerLoginMessage="Login failed, please try again...";
    		}else{
    			$scope.retailerLoginMessage="SORRY, INTERNAL SERVER ERROR";
    		}	
    	});
    };
//    
//    $scope.product_info=[
//        {product_id: "B0000630MB", name: "Sassy Baby Warming Dish, Colors May Vary",
//            description: "Sassy baby Warming Dish has a spout to add warm water under plate to keep food warm during meal time. Can be used as a suctioned dish, or separately as a large bowl. Comes in assorted colors and is dishwasher safe.The Warming Dish offers an easy way to keep baby's food warm without cords or plugs. Add warm water through spout to keep food warm during mealtime. Warming dish can be used as a sectioned dish or separately for a large bowl. Suction base keeps Warming Dish securely in place.",
//            category: 'Home', score: 5.0, price: 10.00, retailer: "host"},
//        {product_id: "B00064C0IU", name: "Oscar Eau de Toilette for Women by Oscar de La Renta",
//            description: "Introduced in 2009, this perfume has a distinct and excellent fragrance.Whenapplyingany fragrance please consider that there are several factors which can affect the natural smell of your skin and, in turn, the way a scent smells on you. For instance, your mood, stress level, age, body chemistry,diet, and current medications may all alter the scents you wear.Similarly, factor such as dry or oilyskin can even affect the amount of time a fragrance will last after being applied",
//            category: "Home", score: 4.0, price: 24.19, retailer: "host"},
//        {product_id: "B000FKGRT8", name: "Artec Textureline Smoothing Serum, 8.4-Ounce Pump (Pack of 2)",
//            description: "Artec Textureline Smooth smoothing serum. ",
//            category: "Home", score:5.0, price: 25.00, retailer: "host"},
//        {product_id: "B000G35MR2", name: "Liz Claiborne Curve Eau de Toilette Spray",
//            description: 'NA',
//            category: "Home", score: 4.5, price: 14.99, retailer: "host"},
//        {product_id: "B000H5WLXM", name: "ChildProTech #338 Door Knob Covers ",
//            description: "Door Knob Covers Easy To Install. 3 Each (Carded)",
//            category: 'Home', score: 4.5, price: 3.5, retailer: "host"},
//        {product_id: "B000K5JBZU", name: "Optimum Care Anti-Breakage Therapy Moisture Replenish Cream Hairdress",
//            description: "Optimum Care Anti-Breakage Therapy features Oleo Ceramide Technology with reparative cermaide,strengthening panthenol and protein.It's proven to penetrate chemically relaxed,natural or color treated hair instantly,rebuilding damaged internal structure for more than 100% stronger hair after just 1 use.Moisture Replenish Cream Hairdress infuses moisture into fragile hair,shielding it with a long lasting barrier against dryness for increased manageability and healthy looking style.",
//            category: 'Home', score: 4.0, price: 30.00, retailer: "host"},
//        {product_id: "B000MATPVI", name: "Swazi - Picture Frame",
//            description: "NA",
//            category: 'Home', score: 3.5, price: 6.71, retailer: "host"}
//    ];



    $scope.event_info=[
        {event_id:"0001", name:"Pono by Joan Goodman sample sale", category:'Clothing',
            description:"Stock up on horn and resin fashion jewelry at this blowout sale. Shop locally designed baubles marked up to 80 percent off retail. From Italian jewel-toned sculpted resin bangles ($20, were $50) to chunky acid-tone necklaces (were $300, now $75), these jewels will add a bright pop of color to your winter wardrobe.",
            location:"PONO sale 347 W 36th St , between Eighth and Ninth Aves, suite 405 , 10018", time: "Mon Dec 8 - Fri Dec 12", retailer:"host"},
        {event_id:"0002", name:"Autumn Cashmere sample sale", category:"Clothing",
            description:"It’s sweater season, and that’s reason enough to treat yourself to some very fine threads, like the ones at this sample sale where cashmere items are up to 75 percent off. Scoop up luxury crewnecks with zippers (originally $348, now $149), pullovers with leather sleeves ($175, were $440) for women and thermal zip mock neck sweaters ($159, formerly $339) for guys.",
            location:"Autumn Cashmere showroom 231 W 39th St, between Seventh and Eighth Aves, suite 1111 , 10018", time: "Mon Dec 8 - Fri Dec 12", retailer:"host"},
        {event_id:"0003", name:"Rivet sale", category:"Clothing", description:"This high-end Carroll Gardens boutique is hosting a long-running warehouse sale through the end of the year. Deals include half off all clothing, shoes and hats, plus 70 percent off sweaters and coats. Look for women’s Autumn Cashmere cardigans dipped from $295 to $89 and men’s JBrand raw denim for $99 (normally $198).",
            location:"Rivet Warehouse 109 Smith St , at Pacific St, 11201", time:"Mon Dec 8 - Wed Dec 31", retailer:"host"}
    ];
//    $scope.retailer={name:"Host", password:"123456"};
});



