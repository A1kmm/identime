<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="local" uri="/WEB-INF/tags/identime" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Register an OpenID Account</title>
</head>
<body>
  <form:form id="form" modelAttribute="registerModel" method="POST">
    <local:csrfProtect/>
    <div><label>New username:</label>
         <form:input path="username" id="username"/>
         <form:errors class="error" path="username"/></div>
    <div><label>New password:</label>
         <form:password showPassword="true" path="password" id="password"/>
         <form:errors class="error" path="password"/></div>
    <div><label>Confirm password:</label>
         <form:password showPassword="true" path="password2" id="password2"/>
         <form:errors class="error" path="password2"/>
    </div>    
    <input type="submit" value="Register" />
  </form:form>
</body>
</html>