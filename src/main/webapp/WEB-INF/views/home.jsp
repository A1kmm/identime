<%@page import="org.springframework.security.core.context.SecurityContextHolder, java.net.URLEncoder" session="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<head>
  <title>Home</title>
</head>
<body>
<c:if test="${not empty message}">
  <p class="message">
    <c:out escapeXml="true" value="${message}"/>
  </p>
</c:if>

  <h1>This is the OpenID Home Page for logged in users</h1>

  <p>To log in to a site that supports OpenID, use the following
    Claimed ID URL:</p>
  <pre>
  ${baseURL}/u/<%=URLEncoder.encode(SecurityContextHolder.getContext()
          .getAuthentication().getName(), "UTF-8")%>
  </pre>
  <sec:authorize url="/admin/settings">
    <h1>Administration</h1>
    <div>You have administrative access:</div>
    <ul>
      <li><a href="admin/settings">Configure OpenID settings</a>.</li>
    </ul>
  </sec:authorize>
</body>
</html>
