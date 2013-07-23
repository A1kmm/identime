<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags/identime" prefix="local" %>
<html>
  <head><title>Login Page</title></head><body onload='document.f.j_username.focus();'>
<h3>Login with Username and Password</h3>
<c:if test="${not empty param.login_error}">
  <p class="error">
    <c:out escapeXml="true" value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/>
  </p>
</c:if>
<form name='f' action='${baseURL}/checklogin' method='POST'>
  <local:csrfProtect/>
  <table>
    <tr><td>User:</td><td>
    <c:choose>
      <c:when test="${not empty needUser}">
        <input type='text' name='j_username' readonly value='${needUser}'>
      </c:when>
      <c:otherwise>
        <input type='text' name='j_username'>
      </c:otherwise>
    </c:choose></td></tr>
    <tr><td>Password:</td><td><input type='password' name='j_password'/></td></tr>
    <tr><td colspan='2'><input name="submit" type="submit" value="Login"/></td></tr>
  </table>
</form>
</body>
</html>