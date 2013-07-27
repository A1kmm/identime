<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
%><%@ taglib prefix="local" tagdir="/WEB-INF/tags/identime"
%><local:site-theme title="Reset Code Sent">
<div class="alert alert-success">
  We just sent you a password reset code by e-mail. If you don't receve it,
  check your spam folder.
</div>
<div class="alert alert-info">
  Got the code? Click on the link in your e-mail or type it in the box below.
</div>
<form class="form-inline" method="POST" action="${baseURL}/forgot/token">
  <label>Code</label><input type="text" name="token"/>
  <button type="submit" class="btn btn-primary">Use Code</button>
</form>
</body>
</html>
</local:site-theme>