<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
%><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"
%><%@ taglib tagdir="/WEB-INF/tags/identime" prefix="local"
%><local:site-theme title="Login" customBase=".">
<c:if test="${not empty param.login_error}"
 ><div class="alert alert-error">
    <c:out escapeXml="true" value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/>
  </div
></c:if>
<form class="form-horizontal" action='./checklogin' method='POST'>
  <local:csrfProtect/>
  <div class="control-group">
    <label class="control-label" for="j_username">User</label>
    <div class="controls">
      <c:choose
       ><c:when test="${not empty needUser}"
         ><input type='text' name='j_username' readonly value='${needUser}'
        ></c:when
        ><c:otherwise
         ><input type='text' name='j_username' placeholder="Username"
        ></c:otherwise>
      </c:choose>
    </div>
  </div>
  <div class="control-group">
    <label class="control-label" for="j_password">Password</label>
    <div class="controls">
      <input type='password' name='j_password' placeholder="Password">
    </div>
  </div>
  <div class="control-group">
    <div class="controls">
      <label class="checkbox">
        <input type="checkbox" name="_spring_security_remember_me">Remember me
      </label>
      <button type="submit" class="btn btn-primary">Login</button>
    </div>
  </div>  
</form>
</local:site-theme>