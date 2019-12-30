<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>페이지를 찾을 수 없습니다.</title>
<link rel="icon" type="imga/x-icon" href="img/hanul.ico">
<style>
#error{
	width: 600px; margin: 0 auto;
	position: absolute;
	left: 50%; top: 50%; transform: translate(-50%, -50%)
}
#error a { cursor: pointer;}
</style>
</head>
<body>
<div id="error">
	<div class="left">
		<a onclick="location='<c:url value="/"/>'"><img src="img/hanul.logo.png"/></a> 
	</div><hr>
	<tiles:insertAttribute name="main"/>
</div>
</body>
</html>