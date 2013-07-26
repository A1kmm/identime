<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Reset Code Sent</title>
</head>
<body>
<p>
  We just sent you a password reset code by e-mail. If you don't receve it,
  check your spam folder.
</p>
<p>
  Got the code? Click on the link in your e-mail or type it in the box below.
</p>
<form method="POST" action="${baseURL}/forgot/token">
  <label>Code:</label><input type="text" name="token"/>
  <input type="submit" value="Use Code"/>
</form>
</body>
</html>