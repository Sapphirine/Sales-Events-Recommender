var map, pointarray, heatmap;
var markers = [];
var markerClusterer;
var mcOptions = {gridSize: 50, maxZoom: 30};
var taxiData = [];
//variables for storing google.maps points
function initialize() { 
	

      var mapOptions = {
        zoom: 2,
        //NYC
        center: new google.maps.LatLng(40.7143528, -74.0059731),
        mapTypeId: google.maps.MapTypeId.ROADMAP
      };
      map = new google.maps.Map(document.getElementById('map-canvas'),
          mapOptions);
      
      
    
	  taxiData = ParseLocation(); 
	  
      var size=taxiData.length;
      console.log("taxiData size " + size);
      //Heatmap
      heatmap = new google.maps.visualization.HeatmapLayer({
      		data: taxiData
      });
      heatmap.setMap(map);
      //Markers
      var markers = [];
      for (var i = 0; i < size; i++) {
      		var latLng = taxiData[i];
      		var marker = new google.maps.Marker({position: latLng, map: map});
      		markers.push(marker);
      }
      console.log("Markers size "+ markers.length);
     markerClusterer = new MarkerClusterer(map, markers, mcOptions);
}
    
function hide(){
	  heatmap.set('opacity', heatmap.get('opacity') ? null : 0.001);
}
    
function removeM(){

	if (markers.length!=0) {
		for (var i = 0; i < markers.length; i++)
		{
				console.log("before"+markers[i].String)
			    markers[i].setMap(null);
			    console.log("after"+markers[i].String);
		}  	    
	    markers=[];
	    console.log(markers);
	}
	else
	{
	      for (var i = 0; i < taxiData.length; i++)
	      {
	        var latLng = taxiData[i];
	        var marker = new google.maps.Marker({position: latLng,map: map});
	        console.log(marker);
	        markers.push(marker);
	      }
	}
}	

//main entry for google maps
google.maps.event.addDomListener(window, 'load', initialize);
  		
		
		
