<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
%><%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"
%><%@ taglib prefix="local" tagdir="/WEB-INF/tags/identime"
%><local:site-theme title="Register an OpenID Account">
  <form:form class="form-horizontal" id="form" modelAttribute="registerModel" method="POST">
    <local:csrfProtect/>
    <div class="control-group">
      <label class="control-label">New username</label>
      <div class="controls">
         <form:input placeholder="Username" path="username" id="username"/>
         <form:errors class="alert alert-error" path="username"/>
      </div>
    </div>
    <div class="control-group">
      <label class="control-label">New password</label>
      <div class="controls">
         <form:password placeholder="New password" showPassword="true" path="password" id="password"/>
         <form:errors class="alert alert-error" path="password"/>
      </div>
    </div>
    <div class="control-group">
      <label class="control-label">Confirm password</label>
      <div class="controls">
         <form:password showPassword="true" placeholder="Confirm Password" path="password2" id="password2"/>
         <form:errors class="alert alert-error" path="password2"/>
      </div>
    </div>
    <div class="control-group">
      <label class="control-label">E-mail (optional)</label></td>
      <div class="controls">
        <form:input path="email" placeholder="E-mail" id="email"/>
        <form:errors class="alert alert-error" path="email"/>
      </div>
    </div>
    <div class="control-group">
      <div class="controls">
        <button class="btn btn-primary" type="submit">Register"</button>
      </div>
    </div>
  </form:form>
</local:site-theme>