<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>




<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">



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



</head>
<body>
	<div class="container">
		<form action="userlist.do">
			<table class="table table-hober" border="0">
				<tbody>

					<c:if test="${!empty UserVO }">


						<c:forEach var="user" items="${UserVO}">

							<tr>
								<th>회원번호</th>
								<th>회원아이디</th>
								<th>휴대전화</th>
							</tr>


							<tr>
								<td><a href="/bbs/mobile/m.userlistdata.do?mno=${user.mno}" target="_blank">${user.mno}</a></td>
								<td>${user.userId}&nbsp&nbsp</td>
								<td>${user.phone}</td>

							</tr>

							<tr>
								<th>기기번호</th>
								<th>회원이름</th>
								<th>E-mail</th>

							</tr>

							<tr>
								<td><a
									href="/bbs/location/m.userlistlocation.do?dev_no=${user.dev_no}" target="_blank">${user.dev_no}&nbsp&nbsp</a></td>
								<td>${user.name}</td>
								<td>${user.email}</td>
							</tr>

						</c:forEach>
					</c:if>


				</tbody>

			</table>


		</form>
	</div>






</body>
</html>