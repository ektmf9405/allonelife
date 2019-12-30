<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"
	prefix="c" %>
<style>
.page-on, .page-off, .page-next, .page-last, .page-first, .page-prev {
	display: inline-block;
	width: 30px;
	line-height: 28px;
}
.page-on{
	border: 1px solid #207ea9;
	background-color: #ffffff;
	font-weight: bold;
	color: #207ea9;
}
.page-next, .page-last, .page-first, .page-prev{
	border: 1px solid #ffffff;
	text-indent: -999999px;
}
.page-prev {
	background: url("img/page_prev.jpg") center no-repeat;
}
.page-first {
	background: url("img/page_first.jpg") center no-repeat;
}
.page-next {
	background: url("img/page_next.jpg") center no-repeat;
}
.page-last {
	background: url("img/page_last.jpg") center no-repeat;
}
</style>

<p id="mypage-list">

<c:if test="${page.curBlock gt 1}">
	<a onclick="go_page(1)" class="page-first">처음</a>
	<a onclick="go_page(${mypage.beginPage-mypage.blockPage})" class="page-prev">이전</a>
</c:if>

<c:forEach var="no" begin="${mypage.beginPage}"
					end="${mypage.endPage }">
	<c:if test="${no eq mypage.curPage}">
		<span class="page-on">${no}</span>
	</c:if>
	<c:if test="${no ne mypage.curPage}">
		<a onclick="go_page(${no})" class="page-off">${no}</a>
	</c:if>	
</c:forEach>

<c:if test="${mypage.curBlock lt mypage.totalBlock}">
	<a onclick="go_page(${mypage.endPage+1})" class="page-next">다음</a>
	<a onclick="go_page(${mypage.totalPage})" class="page-last">마지막</a>
</c:if>


</p>	

<script>
function go_page(no){
	$('[name=curPage]').val(no);
	$('#list').submit();
}
</script>    
    
    