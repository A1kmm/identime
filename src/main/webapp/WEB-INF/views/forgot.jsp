<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@ taglib tagdir="/WEB-INF/tags/identime" prefix="local"
%><local:site-theme title="Reset forgotten password">
<c:if test="${not empty error}"
  ><div class="alert alert-error">${error}</div
></c:if>
<p>To reset your password, please enter your <b>username</b> below and press
the reset button. A link to reset your password will be e-mailed to you.</p>
<form class="form-inline" action="${baseURL}/forgot" method="POST">
  <input type="text" name="accountName" placeholder="Username"/>
  <button type="submit" class="btn btn-primary">E-mail Reset Link</button>
</form>
</local:site-theme>