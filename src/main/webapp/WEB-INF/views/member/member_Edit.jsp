<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원정보 수정폼</title>
<link rel="stylesheet" type="text/css" href="./css/member.css" />
<script src="https://code.jquery.com/jquery-latest.min.js"></script>
<script src="./js/member.js"></script>
</head>
<body>
<div id="mJoin_wrap">
	<h2 class="mJoin_title">회원수정</h2>
	
	<form name="m" method="post" action="member_update_ok" onsubmit="return edit_check();">
		<table id="mJoin_t">
			<tr>
				<th>회원아이디</th>
				<td>${id}</td>
			</tr>
			<tr>
				<th>비밀번호</th>
				<td>
					<input type="password" name="mem_pwd" id="mem_pwd" size="14" class="input_box" />
				</td>
			</tr>
			<tr>
				<th>비밀번호확인</th>
				<td>
					<input type="password" name="mem_pwd2" id="mem_pwd2" size="14" class="input_box" />
				</td>
			</tr>
			<tr>
				<th>회원이름</th>
				<td>
					<input name="mem_name" id="mem_name" size="14" class="input_box" value="${em.mem_name}"/>
				</td>
			</tr>
			<tr>
				<th>우편번호</th>
				<td>
					<input name="mem_zip" id="mem_zip" size="3" class="input_box" readonly value="${em.mem_zip}"/>
					-
					<input name="mem_zip2" id="mem_zip2" size="3" class="input_box" readonly value="${em.mem_zip2}"/>
					<%-- readonly 속성은 읽기만 가능. --%>
					<input type="button" value="우편검색" class="input_b" onclick="post_check();" />
				</td>
			</tr>
			<tr>
				<th>주소</th>
				<td>
					<input name="mem_addr" id="mem_addr" size="40" class="input_box" readonly value="${em.mem_addr}" />
				</td>
			</tr>
			<tr>
				<th>나머지 주소</th>
				<td>
					<input name="mem_addr2" id="mem_addr2" size="36" class="input_box" value="${em.mem_addr2}" />
				</td>
			</tr>
			<tr>
				<th>폰번호</th>
				<td>
					<select name="mem_phone01" id="mem_phone01">
						<c:forEach var="p" items="${phone}">
							<option value="${p}"
								<c:if test="${em.mem_phone01 == p}">
									${'selected'}
								</c:if>>
								${p}
							</option>
						</c:forEach>
					</select>
					-
					<input name="mem_phone02" id="mem_phone02" size="4" maxlength="4" class="input_box" value="${em.mem_phone02}" />
					-
					<input name="mem_phone03" id="mem_phone03" size="4" maxlength="4" class="input_box" value="${em.mem_phone03}" />
					<%-- maxlength="4"로 지정하면 최대 4자까지만 입력가능 --%>
				</td>
			</tr>
			<tr>
				<th>전자우편</th>
				<td>
					<input name="mail_id" id="mail_id" size="14" class="input_box" value="${em.mail_id}" />
					@
					<input name="mail_domain" id="mail_domain" size="18" class="input_box" readonly value="${em.mail_domain}"/>
					<select name="mail_list" onchange="domain_list();">
						<c:forEach var="mail" items="${email}">
							<option value="${mail}" 
								<c:if test="${em.mail_domain == mail}">
									${'selected'}
								</c:if>>
								${mail}
							</option>
						</c:forEach>
					</select>
				</td>
			</tr>
		</table>
		
		<div id="mJoin_menu">
			<input type="submit" value="수정" class="input_b" />
			<input type="reset" value="취소" class="input_b" onclick="$('#mem_pwd').focus();" />
		</div>
	</form>
</div>
</body>
</html>