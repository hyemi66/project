<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>자료실 수정폼</title>
<link rel="stylesheet" type="text/css" href="./css/bbs.css" />
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="./js/bbs.js"></script>
</head>
<body>
<div id="bsW_wrap">
	<h2 class="bsW_title">자료실 수정</h2>
	<form method="post" action="bbs_edit_ok" onsubmit="return write_check();" enctype="multipart/form-data">
	<%-- 자료실 기능을 만들려면 첫번째,method=post만 가능하다.
	두번째 폼태그내에 enctype속성을 꼭 지정해야 한다. 파일첨부해서
	서버로 전송되는 첨부된 파일을 포함한 일반문자를 바이너리 모드
	(binary mode) 즉 이진파일이라 부른다. 그렇지 않은 일반게시판에
	서 서버로 전송되는 데이터를 ascii mode(아스키 모드) 파일
	이라 한다. --%>
	
	<%-- 자료실 번호 히든값--%>
	<input type="hidden" name="bbs_no" value="${b.bbs_no}" >
	<%--책갈피 페이지 번호 히든값 --%>
	<input type="hidden" name="page" value="${page}" >
	 
	<table id="bsW_t">
		<tr>
			<th>글쓴이</th>
			<td>
				<input name="bbs_name" id="bbs_name" size="14" value="${b.bbs_name}"/>
			</td>
		</tr>
		<tr>
			<th>글제목</th>
			<td>
				<input name="bbs_title" id="bbs_title" size="33" value="${b.bbs_title}" />
			</td>
		</tr>
		<tr>
			<th>비밀번호</th>
			<td>
				<input type="password" name="bbs_pwd" id="bbs_pwd"   size="14" />
			</td>
		</tr>
		<tr>
			<th>글내용</th>
			<td>
				<textarea name="bbs_cont" id="bbs_cont" rows="8" cols="34">${b.bbs_cont}</textarea>
			</td>
		</tr>
		<tr>
			<th>파일첨부</th>
			<td>
				<input type="file" name="bbs_file" /><br>${b.bbs_file}
			</td>
		</tr>
	</table>
	
	<div id="bsW_menu">
		<input type="submit" value="수정" />
		<input type="reset" value="취소" onclick="$('#bbs_name').focus();">
		<input type="button" value="목록" onclick="location='bbs_list?page=${page}';">
	</div>
	
	</form>
</div>
</body>
</html>