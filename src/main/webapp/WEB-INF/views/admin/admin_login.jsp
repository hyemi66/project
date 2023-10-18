<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>관리자 로그인</title>
<link rel="stylesheet" type="text/css" href="./css/admin.css" />
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<script>
	function admin_check(){
		if($.trim($("#admin_id").val()) == ""){
			alert("관리자 아이디를 입력하세요!");
			$("#admin_id").val("").focus();
			return false;
		}
		
		if($.trim($("#admin_pwd").val()) == ""){
			alert("관리자 비번을 입력하세요!");
			$("#admin_pwd").val("").focus();
			return false;
		}
	}
</script>
</head>
<body>
<div id="aLogin_wrap">
	<h2 class="aLogin_title">관리자 로그인</h2>
	
	<form method="post" action="admin_login_ok" onsubmit="return admin_check();">
		<table id="aLogin_t">
			<tr>
				<th>관리자 아이디</th>
				<td>
					<input name="admin_id" id="admin_id" size="14" tabindex="1" />
					<%-- tabindex="1"로 설정하면 탭키를 눌렀을 때 첫번째로 포커스를 가짐. --%>
				</td>
				<th rowspan="2"><input type="submit" value="로그인" /></th>
			</tr>
			<tr>
				<th>관리자 비밀번호</th>
				<td>
					<input type="password" name="admin_pwd" id="admin_pwd" size="14" tabindex="2" />
					<%-- tabindex="2"로 설정하면 탭키를 눌렀을때 2번째로 포커스를 가짐. --%>
				</td>
			</tr>
		</table>
	</form>
</div>
</body>
</html>