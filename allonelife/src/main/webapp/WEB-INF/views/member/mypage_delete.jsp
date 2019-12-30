<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<style>
.div_left{
	width: 700px;
	margin: 0 auto;
	text-align: left;
}
<<<<<<< .mine



||||||| .r78
=======

h2 {
	text-align: center;
	font-size: 30px;
	font-family: 'Gothic A1', sans-serif;
}

h3 {
	text-align: center;
	font-size: 20px;
	font-family: 'Gothic A1', sans-serif;
}

p {
	font-weight: blod;
	
}

.div_left {
    padding: 42px 50px 29px;
    overflow: hidden;
    border-top: 1px solid #e1e1e1;
    border-bottom: 1px solid #e1e1e1;
    background-color: #fbfbfb;
    
}

.msg {
	font-weight: bold;
}

.msg2 {
	font-weight: bold;
}  


>>>>>>> .r85
</style>
<head>
<link href="https://fonts.googleapis.com/css?family=Black+Han+Sans|Gothic+A1:500&display=swap" rel="stylesheet">


<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<br>
<br>
		<div class="div_left">				
			
			<h2 style="text-align: center;">얼라이프 회원탈퇴 안내</h2>
			<br>

			<h3>회원탈퇴를 신청하기 전에 아래의 안내 사항을 꼭 확인해주세요.</h3>

			<br>
			<hr>
			
			
	<div class="div_mem_out">
		<p class="mem_out1">회원 탈퇴 시 유의사항</p>
		회원 약관 및 개인정보제공, 활용에 관한 약관 동의가 모두 철회되며<br> 
		가입하신 모든 얼라이프 어플리케이션 및 사이트로부터 탈퇴 됩니다.<br>
			
		탈퇴할 경우 가입할 때 입력하신 회원님의 <p class="msg">개인정보와 작성한 글, 댓글 그리고 현재 보유중인 얼과 상품이 전부 소멸됩니다.</p>
		
		<p class="msg2">
		삭제된 데이터는 복구되지 않습니다. <br />
		또한 얼라이프에서 제공하고 있는 플랫폼에서도 로그인할 수 없습니다. <br />
		신중하게 고려하신 후 선택하시기 바랍니다.
		</p>

	</div>
	<br>
	<br>
	<br>
	<br>
	     	 
		</div>
		<br>
		<br>
		<hr>

			<form method="post" action="user_info_delete">
			<input type="hidden" value="${login_info.userid}" name="userid">
					<input type="checkbox" id="dropoutAgree" name="dropoutAgree">
					<label for="dropoutAgree"><strong>안내 사항을 모두 확인하였으며, 이에 동의합니다.</strong></label><br><br><br>
						<a class="btn-fill" id="check" onclick="check();">확인</a>
				</form>			
<script>
function check() {
	if( $("#dropoutAgree").is(":checked")){
		$('form').submit();
	}else{
		alert("탈퇴 안내를 확인하고 동의해 주세요.");
		$("#dropoutAgree").focus();
	}
}

</script>
</body>
</html>