<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>관리자 메인화면</title>

<link rel="stylesheet" type="text/css" href="./css/admin.css" />
<link rel="stylesheet" type="text/css" href="./css/board.css" />
<link rel="stylesheet" type="text/css" href="./css/admin_board.css" />
<link rel="stylesheet" type="text/css" href="./css/admin_bbs.css" />
<link rel="stylesheet" type="text/css" href="./css/admin_member.css" />

<script src="./js/jquery.js"></script>
<script src="./js/bbs.js"></script>
<script src="./js/gongji.js"></script>
<script src="./js/board.js"></script>
<script src="./js/member.js"></script>

</head>

<body>
<div id="aMain_wrap">

<%--관리자 메인 상단 --%>
<div id="aMain_header">

<%--관리자 로고 --%>
<div id="aMain_logo">
	<a href="admin_index">
		<img src="./images/admin/admin_logo.png" />
	</a>
</div>

<%--관리자 상단메뉴 --%>
<div id="aMain_menu">
	<ul>
		<li><a href="admin_gongji_list">공지사항</a></li>
		<li><a href="admin_board_list">게시판</a></li>
		<li><a href="admin_bbs_list">자료실</a></li>
		<li><a href="admin_member_list">회원관리</a></li>
	</ul>
</div>

<%--관리자 메인 우측메뉴 --%>
<div id="aMain_right">
	<form method="post" action="admin_logout">
		<h3 class="aRight_title">
			${admin_name}님 로그인을 환영합니다.
			<input type="submit" value="로그아웃" />
		</h3>
	</form>
</div>
</div>

<div class="clear"></div>