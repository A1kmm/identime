<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="local" tagdir="/WEB-INF/tags/identime" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Approve a new site</title>
</head>
<body>
  <div>The following site is requesting that your identity be verified:</div>
  <pre>${site}</pre>
  <form method="POST" action="./approveSite">
    <local:csrfProtect/>
    <input type="hidden" name="siteEndpoint" value="<c:out value="${site}" escapeXml="true"/>"/>
    <input type="submit" value="Approve Request"/>
  </form>
</body>
</html>