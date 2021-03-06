<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>




<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<title>list</title>


<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css"
	integrity="sha384-HSMxcRTRxnN+Bdg0JdbxYKrThecOKuH5zCYotlSAcp1+c8xmyTe9GYg1l9a69psu"
	crossorigin="anonymous">


<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.0/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>
<script type="text/javascript"
	src="//dapi.kakao.com/v2/maps/sdk.js?appkey=f3e52b22aa7a2f504ea39f5cb27cf97a"></script>

<script>
    on.click(); 
</script>
<style>
.navbar {
	padding-top: 15px;
	padding-bottom: 15px;
	border: 0;
	border-radius: 0;
	margin-bottom: 0;
	font-size: 12px;
	letter-spacing: 5px;
}

.navbar-nav  li a:hover {
	color: #1abc9c !important;
}
</style>
</head>
<body onload="javascript:markertTest(${loc.lat},${loc.lon})"
	style="background-color: #EAEAEA;">

	<nav class=" navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse"
					data-target="#myNavbar">
					<span class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href=""
					style="margin-left: 0px; padding-bottom: 0px; padding-left: 21px; padding-right: 0px; padding-top: 10px;">
					<img
					src="${pageContext.request.contextPath}/resources/img/map2.png"
					class="d-inline-block align-baseline"
					style="width: 28px; height: 28px;">
				</a> <a class="navbar-brand"
					href="/bbs/location/m.location_list.do?dev_no=${dev_no}">์์น์กฐํ</a>
			</div>
		</div>
	</nav>


	<div class="container-fluid"
		style="background: #EAEAEA; margin-top: 90px;">


		<div style="background: #EAEAEA" class="col-sm-12 col-md-12">
			<div class="container">
				<div class="thumbnail" style="background: #EAEAEA">
					<div class="caption">

						<div style="overflow: scroll; height: 150px; overflow-x: hidden;">

							<table class="table table-bordered table table-hover">
								<tbody>
									<c:if test="${!empty location }">


										<c:forEach var="loc" items="${Location}">

											<tr>
												<th>์์?์๊ฐ</th>
												<th>๊ธฐ๊ธฐ๋ฒํธ</th>
											</tr>

											<tr>
												<td>${loc.time}</td>
												<td>${loc.dev_no}</td>
											</tr>

											<tr>
												<th>์๋, ๊ฒฝ๋</th>
												<th>์์น์กฐํ</th>
											</tr>

											<tr>
												<td>${loc.lat},${loc.lon}</td>
												<td><a
													onclick="javascript:markertTest(${loc.lat},${loc.lon})">์กฐํ</a>
													<!--  <a onclick="javascript:markertTest(${loc.lat},${loc.lon})" onclick="polyline.setMap(map)">๋ง์ปค์์ฑ</a>-->
												</td>
											</tr>
										</c:forEach>
									</c:if>


								</tbody>
							</table>
						</div>
						<div class="container-fluid"
							style="text-align: center; margin-top: 15px;">
							<div style="float: left;">
							<label style="text-align: right; margin-bottom: 0px;"><iframe
										src="http://192.168.43.97/gpio/" width="50" height="27"></iframe></label>
							</div>
							<div style="float: right;">
								<a onclick="polyline.setMap(map)">์? ์์ฑ&nbsp</a> <a
									onclick="polyline.setMap(null)">์? ์จ๊ธฐ๊ธฐ&nbsp</a> <a
									onclick="hideMarkers()">๋ชจ๋? ๋ง์ปค ์ญ์?</a>
							</div>
							<!-- ์ง๋๋ฅผ ํ์ํ? div ์๋๋ค -->
							<div id="map" style="width: 100%; height: 450px;"></div>

							<p>
								<!-- <a onclick="polyline.setMap(map)"> ์? ์์ฑ</a>-->
								<!--<a onclick="polyline.setMap(null)"> ์? ์จ๊ธฐ๊ธฐ</a><a onclick="setMarkers(null)">๋ชจ๋? ๋ง์ปค ์จ๊ธฐ๊ธฐ</a>-->
							</p>

							<br>

							<script>
							<!-- ์๋์ ๊ฒฝ๋๋ฅผ ๋ฐ์์ฌ ๋ฐฐ์ด ์์ฑ-->
							var Newlat= new Array();
							var Newlon= new Array();
							
							<!-- ์๋์ ๊ฒฝ๋ ๋ฐฐ์ด์ ๊ธฐ์-->
							<c:forEach var="loc" items="${Location}">	
							Newlat.push(${loc.lat});
							Newlon.push(${loc.lon});
							</c:forEach>
							
							<!-- ์ง๋์์ฑ-->
								var mapContainer = document.getElementById('map'), // ์ง๋๋ฅผ ํ์ํ? div 
								mapOption = {
									center : new kakao.maps.LatLng(Newlat[Newlat.length - 1],Newlon[Newlon.length - 1]), // ์ง๋์ ์ค์ฌ์ขํ
									level : 3
								// ์ง๋์ ํ๋ ๋?๋ฒจ
								};		

								var map = new kakao.maps.Map(mapContainer,
					                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            					mapOption); // ์ง๋๋ฅผ ์์ฑํฉ๋๋ค
								
										kakao.maps.event.addListener(map, 'click', function(mouseEvent) {        
										    // ํด๋ฆญํ ์์น์ ๋ง์ปค๋ฅผ ํ์ํฉ๋๋ค 
										    addMarker(mouseEvent.LatLng(
													m1, m2));             
										});
										
										var markers = [];

										<!-- ๋ง์ปค ์์ฑ-->
										function markertTest(m1, m2) {
									// ๋ง์ปค๊ฐ ํ์๋? ์์น์๋๋ค 
									var markerPosition = new kakao.maps.LatLng(
											m1, m2);

									// ๋ง์ปค๋ฅผ ์์ฑํฉ๋๋ค
									var marker = new kakao.maps.Marker({
										position : markerPosition
									});

									// ๋ง์ปค๊ฐ ์ง๋ ์์ ํ์๋๋๋ก ์ค์?ํฉ๋๋ค
									marker.setMap(map);
									
									markers.push(marker);
								}
								
								<!-- ๋ง์ปค๋ฅผ ์ฌ๋ฌ๊ฐ ์์ฑํ๊ธฐ์ํ ๋ฉ์๋-->
								function setMarkers(map) {
								    for (var i = 0; i < markers.length; i++) {
								        markers[i].setMap(map);
								    }            
								}

								// "๋ง์ปค ๊ฐ์ถ๊ธฐ" ๋ฒํผ์ ํด๋ฆญํ๋ฉด ํธ์ถ๋์ด ๋ฐฐ์ด์ ์ถ๊ฐ๋ ๋ง์ปค๋ฅผ ์ง๋์์ ์ญ์?ํ๋ ํจ์์๋๋ค
								function hideMarkers() {
								    setMarkers(null);    
								}
								
								
							
								var iwContent = '<div style="padding:5px;">์ต์ข์์น</div>', // ์ธํฌ์๋์ฐ์ ํ์ถ๋? ๋ด์ฉ์ผ๋ก HTML ๋ฌธ์์ด์ด๋ document element๊ฐ ๊ฐ๋ฅํฉ๋๋ค
							    iwPosition = new kakao.maps.LatLng(Newlat[Newlat.length - 1],Newlon[Newlon.length - 1]), //์ธํฌ์๋์ฐ ํ์ ์์น์๋๋ค
							    iwRemoveable = true; // removeable ์์ฑ์ ture ๋ก ์ค์?ํ๋ฉด ์ธํฌ์๋์ฐ๋ฅผ ๋ซ์ ์ ์๋ x๋ฒํผ์ด ํ์๋ฉ๋๋ค

							// ์ธํฌ์๋์ฐ๋ฅผ ์์ฑํ๊ณ? ์ง๋์ ํ์ํฉ๋๋ค
							var infowindow = new kakao.maps.InfoWindow({
							    map: map, // ์ธํฌ์๋์ฐ๊ฐ ํ์๋? ์ง๋
							    position : iwPosition, 
							    content : iwContent,
							    removable : iwRemoveable
							});
								
								
								
								
								// ์?์ ๊ตฌ์ฑํ๋ ์ขํ ๋ฐฐ์ด์๋๋ค. ์ด ์ขํ๋ค์ ์ด์ด์ ์?์ ํ์ํฉ๋๋ค		
								var linePath = [
									<c:forEach var="loc" items="${Location}">	
								    new kakao.maps.LatLng(${loc.lat}, ${loc.lon}),
								    </c:forEach>			   
								];
								

								// ์ง๋์ ํ์ํ? ์?์ ์์ฑํฉ๋๋ค
								var polyline = new kakao.maps.Polyline({
								    path: linePath, // ์?์ ๊ตฌ์ฑํ๋ ์ขํ๋ฐฐ์ด ์๋๋ค
								    strokeWeight: 5, // ์?์ ๋๊ป ์๋๋ค
								    strokeColor: '#FFAE00', // ์?์ ์๊น์๋๋ค
								    strokeOpacity: 0.7, // ์?์ ๋ถํฌ๋ช๋ ์๋๋ค 1์์ 0 ์ฌ์ด์ ๊ฐ์ด๋ฉฐ 0์ ๊ฐ๊น์ธ์๋ก ํฌ๋ชํฉ๋๋ค
								    strokeStyle: 'solid' // ์?์ ์คํ์ผ์๋๋ค
								});
								
								var positions = [
								    {
								        title: '์นด์นด์ค', 
								        latlng: new kakao.maps.LatLng(Newlat[Newlat.length - 1],Newlon[Newlon.length - 1])
								    }
								];
								
							/*
								// ๋ง์ปค ์ด๋ฏธ์ง์ ์ด๋ฏธ์ง ์ฃผ์์๋๋ค
								var imageSrc = "http://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png"; 
								    
								for (var i = 0; i < positions.length; i ++) {
								    
								    // ๋ง์ปค ์ด๋ฏธ์ง์ ์ด๋ฏธ์ง ํฌ๊ธฐ ์๋๋ค
								    var imageSize = new kakao.maps.Size(24, 35); 
								    
								    // ๋ง์ปค ์ด๋ฏธ์ง๋ฅผ ์์ฑํฉ๋๋ค    
								    var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize); 
								    
								    // ๋ง์ปค๋ฅผ ์์ฑํฉ๋๋ค
								    var marker = new kakao.maps.Marker({
								        map: map, // ๋ง์ปค๋ฅผ ํ์ํ? ์ง๋
								        position: positions[i].latlng, // ๋ง์ปค๋ฅผ ํ์ํ? ์์น
								        title : positions[i].title, // ๋ง์ปค์ ํ์ดํ, ๋ง์ปค์ ๋ง์ฐ์ค๋ฅผ ์ฌ๋ฆฌ๋ฉด ํ์ดํ์ด ํ์๋ฉ๋๋ค
								        image : markerImage // ๋ง์ปค ์ด๋ฏธ์ง 
								    });
								}
								*/
															
							</script>




						</div>


					</div>
				</div>
			</div>
		</div>





	</div>

	<script>
		$(document).ready(function() {
			$('[data-toggle="tooltip"]').tooltip();
		})
	</script>

	<script src="https://code.jquery.com/jquery-1.12.4.min.js"
		integrity="sha384-nvAa0+6Qg9clwYCGGPpDQLVpLNn0fRaROjHqs13t4Ggj3Ez50XnGQqc/r8MhnRDZ"
		crossorigin="anonymous"></script>

	<script
		src="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"
		integrity="sha384-aJ21OjlMXNL5UyIl/XNwTMqvzeRMZH2w8c5cRVpzpU8Y5bApTppSuUkhZXN0VxHd"
		crossorigin="anonymous"></script>




</body>
</html>