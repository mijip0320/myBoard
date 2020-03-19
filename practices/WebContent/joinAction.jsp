<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "user.UserDAO" %>
<%@ page import = "java.io.PrintWriter" %>
<% request.setCharacterEncoding("UTF-8"); %>

<jsp:useBean id="user" class="user.User" scope="page" />
<jsp:setProperty name = "user" property="userID" /> 
<jsp:setProperty name = "user" property="userPassword" />
<jsp:setProperty name = "user" property="userName" />
<jsp:setProperty name = "user" property="userGender" />
<jsp:setProperty name = "user" property="userEmail" />
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
	String userID = null;
	if(session.getAttribute("userID")!=null){ //로그인이 되었을 때
		userID = (String) session.getAttribute("userID");
	}
	
	
	if(userID != null){
		PrintWriter script = response.getWriter();
		script.println("<script>");
		script.println("alert('이미 로그인이 되었습니다.')");
		script.println("location.href = 'main.jsp'");
		script.println("</script>");
	}



	if(user.getUserID() == null || user.getUserPassword() == null || user.getUserName() == null || user.getUserGender() == null || user.getUserEmail() == null){
		PrintWriter script = response.getWriter();
		script.println("<script>");
		script.println("alert('입력이 안 된 사항이 있습니다.')");
		script.println("history.back()");
		script.println("</script>");
		
	}else{
		UserDAO userDAO = new UserDAO();
		int result = userDAO.join(user);
		
		if(result == -1){ //이미 아이디가 존재할 때
			PrintWriter script = response.getWriter();
			
			script.println("<script>");
			script.println("alert('존재하는 아이디')");
			script.println("history.back()");
			script.println("</script>");
		}else { //회원가입 완료되면 메인페이지로 이동
		PrintWriter script = response.getWriter();
		
		script.println("<script>");
		script.println("location.href = 'main.jsp'");
		script.println("</script>");
	}
}
%>
</body>
</html>