<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" 
	prefix="c"%>      
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style type="text/css">
	table tr td:nth-child(2) {
	text-align: left;
}
table{
	margin-top: 100px;
	border: 2px solid #000000;
	border-radius: 1px;
	
	
}
table td, table th{
	padding: 20px;
	color: #000000;
	border-bottom: 1px solid #cad3de;
	
}

body{
background-color: #d3ddde;
font-size: 15px;
}
#my {
	width: 1000px;
	height: 50px;
	margin-top: 100px;
	margin-left: 450px;
	border-radius: 10px 10px 10px 10px;
}
</style>
</head>
<body>
	<img src="img/my2.png"/><br>
	<hr>
<br>



<form id="list" method="post">
<input type="hidden" name="curPage" value="1" />
<input type="hidden" name="id" />
<input type="hidden" name="read" value="false"/>
</form>

<table>
<tr>
	<th>아이디</th>
	<th>제목</th>
	<th>날짜</th>
	<th>조회수</th>
	<th>댓글</th>
</tr>

<c:forEach items="${mypage.list}" var="vo">
<tr>
	<td>${vo.userid}</td>
	<td><a onclick="go_detail(${vo.id}, '${vo.menu}')"> ${vo.title} </a></td>
	<td>${vo.writedate}</td>
	<td>${vo.readcnt}</td>
	<td>${vo.step}</td>

</tr>
</c:forEach>

</table><br>

<script type="text/javascript">
function go_detail(id, menu){
	$('[name=id]').val(id);	
	$('[name=read]').val(true);
	$('#list').attr( 'action', 'detail.'+ menu );
	$('#list').submit();

}
</script>

<jsp:include page="/WEB-INF/views/include/mypage.jsp" />

</body>
</html>