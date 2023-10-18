<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>자료실 답변폼</title>
<%-- CDN(Content Delivery Network의 약어)방식으로 
인터넷이 연결된 상태에서 온라인으로 jQuery라이브러리를 연결해서 사용하는 방식이다
로컬pc로 jQuery라이브러리를 다운 받지 않고 사용가능하다 --%>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="./js/bbs.js"></script>
<link rel="stylesheet" type="text/css" href="./css/bbs.css" >
</head>
<body>
<div id="bsW_wrap">
	<h2 class="bsW_title">자료실 답변</h2>
	<form method="post" action="bbs_reply_ok" onsubmit="return write_check();">
		<%-- 답변글 히든값 --%>
		<%-- 원본글과 답변글을 묶어주는 글 그룹번호 역할 -> bbs_ref --%>
		<input type="hidden" name="bbs_ref" value="${b.bbs_ref}" >
		<%-- 원본글이면 0, 첫번째 답변글이면 1, 두번째 답변글이면 2, ...
		즉 원본글과 답변글을 구분하는 번호이면서 몇번째 답변글인가를 알려줌 --%>
		<input type="hidden" name="bbs_step" value="${b.bbs_step}" >
		<%-- 답변글 정렬순서 --%>
		<input type="hidden" name="bbs_level" value="${b.bbs_level}" >
		<%-- 페이징 즉 쪽나누기에서 내가 본 쪽번호로 바로 이동하기 위한 책갈피 기능때문에 히든으로 쪽번호를 전달 --%>
		<input type="hidden" name="page" value="${page}" >
		
		<table id="bsW_t">
			<tr>
				<th>글쓴이</th>
				<td>
					<input name="bbs_name" id="bbs_name" size="14" >
				</td>
			</tr>
			<tr>
				<th>글제목</th>
				<td>
					<input name="bbs_title" id="bbs_title" size="33" value="Re:${b.bbs_title}" >
				</td>
			</tr>
			<tr>
				<th>비밀번호</th>
				<td>
					<input type="password" name="bbs_pwd" id="bbs_pwd" size="14" >
				</td>
			</tr>
			<tr>
				<th>글내용</th>
				<td>
					<textarea name="bbs_cont" id="bbs_cont" rows="8" cols="34"></textarea>
				</td>
			</tr>
		</table>
		
		<div id="bsW_menu">
			<input type="submit" value="답변">
			<input type="button" value="취소" onclick="location='bbs_cont?bbs_no=${b.bbs_no}&page=${page}&state=cont';">
			<input type="button" value="목록" onclick="location='bbs_list?page=${page}';">
		</div>
	</form>
</div>
</body>
</html>