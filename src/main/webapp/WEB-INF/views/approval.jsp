<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
%><%@ taglib prefix="local" tagdir="/WEB-INF/tags/identime"
%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><local:site-theme title="Approve a new site">
  <div>The following site is requesting that your identity be verified:</div>
  <blockquote>${site}</blockquote>
  <form method="POST" action="./approveSite">
    <local:csrfProtect/>
    <input type="hidden" name="siteEndpoint" value="<c:out value="${site}" escapeXml="true"/>"/>
    <button type="submit" class="btn btn-primary">Approve Request</button>
  </form>
</local:site-theme>