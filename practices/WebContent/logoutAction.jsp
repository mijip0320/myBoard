<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
	session.invalidate();//지금 접속한 회원의 세션이 빼앗김
	

%>
<script>
	location.href = 'main.jsp';
</script>
</body>
</html>