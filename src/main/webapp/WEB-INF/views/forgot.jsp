<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Reset forgotten password</title>
</head>
<body>
<c:if test="${not empty error}">
  <div class="error">${error}</div>
</c:if>
<p>To reset your password, please enter your <b>username</b> below and press
the reset button. A link to reset your password will be e-mailed to you.</p>
  <form method="POST">
    Username: <input type="text" name="accountName"/><input type="submit" value="E-mail Reset Link"/>
  </form>
</body>
</html>