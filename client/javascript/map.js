var map;

function ajaxGetLonLat(location)
{
	var url = "http://overpass.osm.rambler.ru/cgi/interpreter?data=[out:json];";
	for (var i=0; i<location.length; i++)
		url += "node[name="+location[i]+"];out skel 1;";
	
	var xmlHttpRequest = (window.XMLHttpRequest) ? new XMLHttpRequest() : new ActiveXObject("Msxml2.XMLHTTP");
        if (xmlHttpRequest === null)
		return;
	xmlHttpRequest.open("GET", url, true);
	xmlHttpRequest.onreadystatechange = function() {
            if (xmlHttpRequest.readyState === xmlHttpRequest.DONE && xmlHttpRequest.status === 200){      // completed && OK
                showMap(JSON.parse(xmlHttpRequest.responseText));
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

function showMap(json) 
{    
    var zoom=5;
    
    map = new OpenLayers.Map("mapdiv");
    map.addLayer(new OpenLayers.Layer.OSM());
        
    var lonLat = getOpenLayersCoord(json);
    var markers = new OpenLayers.Layer.Markers("Markers");
    for (var i=0; i<json.elements.length; i++)
    	markers.addMarker(new OpenLayers.Marker(lonLat[i]));
    map.addLayer(markers);
    
    map.setCenter (lonLat[0], zoom);
}

function queryMap()
{
	var locationList = new Array();
    locationList[0] = "Gielgen";
    locationList[1] = "Paris";
    
    ajaxGetLonLat(locationList);
}


