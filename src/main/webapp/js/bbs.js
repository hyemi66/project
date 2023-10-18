/*
	bbs.js
*/
 
 function write_check(){
	if($.trim($("#bbs_name").val())==""){
		alert("글쓴이를 입력하세요!");
		$("#bbs_name").val("").focus();
		return false;
	}
	if($.trim($("#bbs_title").val())==""){
		alert("글제목을 입력하세요!");
		$("#bbs_title").val("").focus();
		return false;
	}
	if($.trim($("#bbs_pwd").val())==""){
		alert("비밀번호를 입력하세요!");
		$("#bbs_pwd").val("").focus();
		return false;
	}
	if($.trim($("#bbs_cont").val())==""){
		alert("글내용을 입력하세요!");
		$("#bbs_cont").val("").focus();
		return false;
	}
}