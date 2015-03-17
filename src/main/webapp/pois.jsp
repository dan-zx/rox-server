<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <style>
        	html, body, #map {
        		width: 100%;
        		height: 100%;
       		}
    	</style>
        <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery-2.1.3.min.js"></script>
        <script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBUYU5rjbjXnhaZGkCG9-sYo1CDSspomDs"></script>
        <script type="text/javascript">
        	var map;
        	function initializeMap() {
        		var mapOptions = {
       				center: { lat: 19.43, lng: -99.13 },
       				zoom: 5
   		        };
        		map = new google.maps.Map(document.getElementById('map'), mapOptions);
       		}
        	google.maps.event.addDomListener(window, 'load', initializeMap);
        	$(document).ready(function(){
        		$.getJSON('${pageContext.request.contextPath}/api/pois/all', function(json) {
        			$.each(json.response, function(i, obj) {
        				var marker = new google.maps.Marker({
        					position: new google.maps.LatLng(obj.location.latitude, obj.location.longitude),
        					title: obj.name
       					});
        				marker.setMap(map);
        			});
        		});
       		});
        </script>
        <title>POIs</title>
    </head>
    <body>
        <div id="map" />
    </body>
</html>