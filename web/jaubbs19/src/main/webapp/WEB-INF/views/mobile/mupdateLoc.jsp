﻿<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt"%>
<HTML>
<head>
<!-- Theme Made By www.w3schools.com - No Copyright -->
<title>기기등록</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
<link href="https://fonts.googleapis.com/css?family=Montserrat"
	rel="stylesheet">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.0/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>

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

<body data-spy="scroll" data-target=".navbar" data-offset="50"
	style="background: #FFFFFF;">

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
					src="${pageContext.request.contextPath}/resources/img/marker.png"
					class="d-inline-block align-baseline"
					style="width: 28px; height: 28px;">
				</a> <a class="navbar-brand" href="/bbs/mobile/m.updateLoc.do">기기등록</a>
			</div>
		</div>
	</nav>


	<div class="container-fluid" style="background: #FFFFFF">
		<div style="background: #FFFFFF" class="col-sm-12 col-md-12">
			<div class="container" style="margin-top: 150px;">
				<div class="thumbnail"
					style="background: #EAEAEA; padding-bottom: 8px; padding-top: 8px; padding-left: 8px; padding-right: 8px;">
					<div class="caption" style="background: #FFFFFF; height: 300px;">

						<spring:hasBindErrors name="user">
							<c:forEach items="${errors.globalErrors}" var="error">
								<spring:message code="${error.code}" />
							</c:forEach>
							</font>
						</spring:hasBindErrors>
						<br>
						<form:form modelAttribute="userVO" Name='mupdateLoc' Method='post'
							Action='/bbs/mobile/m.updateLoc.do' style="margin-top: 20px;">
							<input type="hidden" name="mno" value="${userVO.mno}" />
							<TABLE border='2' width='600' cellSpacing=0 cellPadding=5
								align=center>


								<div class="form-inline">
									<form:input type="hidden" class="form-control" id="userId"
										path="userId" name="userId" maxlength="10" />
								</div>

								<div class="form-group">
									<label for="passwd">비밀번호</label>
									<div class="form-inline">
										<span class="font-weight-bold text-danger"><small>&nbsp;</small></span>
										<form:password class="form-control text-truncate"
											style="width:300px;" id="passwd" path="passwd" name="passwd"
											placeholder="비밀번호를 써주세요" minlength="6" maxlength="16" />
									</div>
								</div>

								<div class="form-group">
									<label for="name">컴백홈 기기등록</label>
									<div class="form-inline">
										<span class="font-weight-bold text-danger"><small>&nbsp;</small></span>
										<form:input type="text" class="form-control"
											style="width:300px;" id="dev_no" path="dev_no" name="dev_no"
											maxlength="30" placeholder="기기코드를 써주세요" />

									</div>
								</div>

								<div class="form-group"
									style="margin-top: 45px; margin-left: 190px;">
									<div class="form-inline">

										<labal style="text-align: right; background-color:#FFFFFF;">
										<a href="m.mypage.do"> <input class="btn btn-secondary"
											type="button" float: right;"
											value="뒤로가기"></a></labal>

										<labal style="text-align: right; background-color:#FFFFFF;">
										<input class="btn btn-secondary" type="submit" float:
											right;"
											value="등록"></labal>

									</div>
								</div>


							</TABLE>
						</form:form>
					</div>
				</div>
			</div>
		</div>
	</div>


	<footer class="footer bg-light mt-5">
		<div class="container p-auto">
			<div class="row sticky-bottom justify-content-center">
				<div>
					<p class="text-muted"></p>
				</div>
			</div>
		</div>
	</footer>



</BODY>
</HTML>