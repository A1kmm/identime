<%@page import="org.springframework.security.core.context.SecurityContextHolder, java.net.URLEncoder" session="false"
%><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"
%><%@ taglib uri="http://www.springframework.org/tags" prefix="s"
%><%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"
%><%@ taglib tagdir="/WEB-INF/tags/identime" prefix="local"
%><local:site-theme title="Home"
><c:if test="${not empty message}"
  ><div class="alert alert-info"
    ><c:out escapeXml="true" value="${message}"
 /></div
></c:if>
  <p>To log in to a site that supports OpenID, use the following
    Claimed ID URL:</p>
  <blockquote>${baseURL}/u/<s:eval expression="T(java.net.URLEncoder).encode(T(org.springframework.security.core.context.SecurityContextHolder).getContext()
        .getAuthentication().getName(), \"UTF-8\")"/></blockquote>
  <sec:authorize url="/admin/settings">
    <h1>Administration</h1>
    <div>You have administrative access:</div>
    <ul>
      <li><a href="admin/settings">Configure OpenID settings</a>.</li>
    </ul>
  </sec:authorize>
</local:site-theme>
 