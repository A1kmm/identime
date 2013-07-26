<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="local" tagdir="/WEB-INF/tags/identime" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Change account settings</title>
</head>
<body>
  <form:form method="POST" action="${baseURL}/accountsettings" modelAttribute="accountSettingModel">
    <table>
    <local:csrfProtect/>
    <c:choose>
      <c:when test="${not empty accountSettingModel.token}">
        <form:hidden path="token"/>
      </c:when>
      <c:otherwise>
      <tr><td><label>Current password</label></td>
          <td><form:password showPassword="true" path="currentPassword"/></td><form:errors class="error" path="currentPassword"/></tr>
      </c:otherwise>
    </c:choose>
    <tr><td><label>New password</label></td>
        <td><form:password showPassword="true" path="newPassword"/><form:errors class="error" path="newPassword"/></td></tr>
    <tr><td><label>New password (repeat)</label></td>
        <td><form:password path="newPassword2"/><form:errors class="error" path="newPassword2"/></td></tr>
    <tr><td><label>E-mail</label></td>
      <td><form:input path="email"/><form:errors class="error" path="email"/></td></tr>
    </table>
    <input type="submit" value="Update Account"/>
  </form:form>
</body>
</html>