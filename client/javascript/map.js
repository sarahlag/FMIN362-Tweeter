var map;

function getTweets()
{
	var url = "http://localhost:9000/FMIN362-Tweeter/resources/tweets/get";
	var xmlHttpRequest = (window.XMLHttpRequest) ? new XMLHttpRequest() : new ActiveXObject("Msxml2.XMLHTTP");
        if (xmlHttpRequest === null)
		return;
	xmlHttpRequest.open("GET", url, true);
	xmlHttpRequest.onreadystatechange = function() {
            if (xmlHttpRequest.readyState === xmlHttpRequest.DONE && xmlHttpRequest.status === 200){      // completed && OK
            	ajaxGetLonLat(JSON.parse(xmlHttpRequest.responseText));
            }
        };
	xmlHttpRequest.send("");
}

function getLocationList(tweets)
{
	var locationList = new Array();
	for (var i=0; i<tweets.length; i++)
	{
		var loc = tweets[i].photo_place;
		if (loc.contains(',') || loc.contains(' '))
			loc = "Dublin";
		locationList[i] = loc;
	}
	return locationList;
}

function ajaxGetLonLat(tweets)
{
	var locationList = getLocationList(tweets);
	var url = "http://overpass.osm.rambler.ru/cgi/interpreter?data=[out:json];";
	for (var i=0; i<locationList.length; i++)
		url += "node[name="+locationList[i]+"];out 1;";
	
	var xmlHttpRequest = (window.XMLHttpRequest) ? new XMLHttpRequest() : new ActiveXObject("Msxml2.XMLHTTP");
        if (xmlHttpRequest === null)
		return;
	xmlHttpRequest.open("GET", url, true);
	xmlHttpRequest.onreadystatechange = function() {
            if (xmlHttpRequest.readyState === xmlHttpRequest.DONE && xmlHttpRequest.status === 200){      // completed && OK
                showMap(JSON.parse(xmlHttpRequest.responseText), tweets);
            }
        };
	xmlHttpRequest.send("");
}

function getOpenLayersCoord(json)
{
	var lon, lat;
	var result = new Array();
	var j=0;
	
	for (var i=0; i<json.elements.length; i++)
	{
		lon = json.elements[i].lon;
		lat = json.elements[i].lat;
		var lonLat = new OpenLayers.LonLat(lon, lat)
	    .transform(
	      new OpenLayers.Projection("EPSG:4326"), // transform from WGS 1984
	      map.getProjectionObject() // to Spherical Mercator Projection
	    );
		result[j++] = lonLat;
	}
	
	return result;
}

function showPopupMarker(map, marker, contentHTML)
{
	marker.events.register("click", marker, function(){
    popup = new OpenLayers.Popup.FramedCloud(null,
    		this.lonlat,
            new OpenLayers.Size(200, 200),
            contentHTML,
            null,
            true);
	map.addPopup(popup);
	}); 
}

function showMap(json, tweets) 
{    
    var zoom=5;
    
    map = new OpenLayers.Map("mapdiv");
    map.addLayer(new OpenLayers.Layer.OSM());
        
    var lonLat = getOpenLayersCoord(json);
    var markers = new OpenLayers.Layer.Markers("Markers");
    for (var i=0; i<json.elements.length; i++)
    {    	
    	var marker = new OpenLayers.Marker(lonLat[i]);
    	var contentHTML = json.generator+" "+json.version+"</br>"+json.elements[i].tags.name;
    	showPopupMarker(map, marker, contentHTML);
    	 
        markers.addMarker(marker);
    }   
    map.addLayer(markers);
    
    map.setCenter (lonLat[0], zoom);
}

/*function queryMap()
{
	var locationList = new Array();
    locationList[0] = "Gielgen";
    locationList[1] = "sfqsdfsqf";
    locationList[2] = "Paris";
    
    ajaxGetLonLat(locationList);
}*/


