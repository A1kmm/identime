<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="local" tagdir="/WEB-INF/tags/identime" %>
<local:site-theme title="Edit Account Settings">
  <form:form class="form-horizontal" method="POST" action="${baseURL}/accountsettings"
    modelAttribute="accountSettingModel">
    <local:csrfProtect/>
    <c:choose>
      <c:when test="${not empty accountSettingModel.token}">
        <form:hidden path="token"/>
      </c:when>
      <c:otherwise>
        <div class="control-group">
          <label class="control-label" for="currentPassword">Current password</label>
          <div class="controls">
            <form:password placeholder="Current Password" showPassword="true" path="currentPassword"/><form:errors class="alert alert-error" path="currentPassword"/>
          </div>
        </div>
      </c:otherwise>
    </c:choose>
    <div class="control-group">
      <label class="control-label">New password</label>
      <div class="controls">
        <form:password showPassword="true" placeholder="New password" path="newPassword"/><form:errors class="alert alert-error" path="newPassword"/>
      </div>
    </div>
    <div class="control-group">
      <label class="control-label">New password (repeat)</label>
      <div class="controls">
        <form:password path="newPassword2" placeholder="New password (repeat)"/><form:errors class="alert alert-error" path="newPassword2"/>
      </div>
    </div>
    <div class="control-group">
      <label class="control-label">E-mail</label>
      <div class="controls">
        <form:input path="email" placeholder="E-mail"/><form:errors class="alert alert-error" path="email"/>
      </div>
    </div>
    <div class="control-group">
      <div class="controls"><button class="btn btn-primary" type="submit">Update Account</button></div>
    </div>
  </form:form>
</local:site-theme>