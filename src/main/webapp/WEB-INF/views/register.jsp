<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="local" tagdir="/WEB-INF/tags/identime" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Register an OpenID Account</title>
</head>
<body>
  <form:form id="form" modelAttribute="registerModel" method="POST">
    <local:csrfProtect/>
    <table>
    <tr><td><label>New username:</label></td>
         <td><form:input path="username" id="username"/></td>
         <td><form:errors class="error" path="username"/></td></tr>
    <tr><td><label>New password:</label></td>
         <td><form:password showPassword="true" path="password" id="password"/></td>
         <td><form:errors class="error" path="password"/></td></tr>
    <tr><td><label>Confirm password:</label></td>
         <td><form:password showPassword="true" path="password2" id="password2"/></td>
         <td><form:errors class="error" path="password2"/></td>
    </tr>
    <tr><td><label>E-mail (optional:)</label></td>
        <td><form:input path="email" id="email"/></td>
        <td><form:errors class="error" path="email"/></td>
    </tr>
    </table>   
    <input type="submit" value="Register" />
  </form:form>
</body>
</html>