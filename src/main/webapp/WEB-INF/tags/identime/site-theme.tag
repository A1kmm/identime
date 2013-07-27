<%@tag
%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"
%><%@attribute name="title" type="java.lang.String"
%><%-- Note that customBase is used for pages that must work even if the configured baseURL is wrong
--%><%@attribute name="customBase" type="java.lang.String"
%><%@variable name-given="useBase"
%><!DOCTYPE html>
<c:choose
><c:when test="${not empty customBase}"><c:set var="useBase" value="${customBase}"/></c:when
 ><c:otherwise><c:set var="useBase" value="${baseURL}"/></c:otherwise
 ></c:choose>
<html lang="en">
  <head>
    <title>${title} - ${siteName}</title>
    <link rel="openid2.provider" href="${baseURL}/provider"/>
    <link rel="openid.server" href="${baseURL}/provider"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    
    <link rel="stylesheet" type="text/css" href="${useBase}/resources/css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="${useBase}/resources/css/bootstrap-responsive.min.css"/>
    <script type="text/javascript" src="${useBase}/resources/js/jquery-1.10.2.min.js"></script>
    <script type="text/javascript" src="${useBase}/resources/js/bootstrap.min.js"></script>
  </head>
  <body>
    <div class="container">
       <c:choose>
         <c:when test="${not empty headerLogo}"><img src="${headerLogo}"/></c:when>
         <c:otherwise><h1>${siteName}</h1></c:otherwise>
       </c:choose>
       <div class="navbar"><div class="navbar-inner">
         <ul class="nav">
           <sec:authorize url="/"><li><a href="${baseURL}/">Home</a></li>
           </sec:authorize
           ><sec:authorize access="isAnonymous()">
           <li><a href="${baseURL}/login">Login</a></li>
           <li><a href="${baseURL}/forgot">Forgot Password</a></li>
           <li><a href="${baseURL}/register">Register</a></li>
           </sec:authorize
           ><sec:authorize url="/logout">
           <li><a href="${baseURL}/logout">Logout</a></li>
           </sec:authorize
           ><sec:authorize access="isAuthenticated()">
           <li><a href="${baseURL}/accountsettings">Account Settings</a></li>
           </sec:authorize
           ><sec:authorize url="/admin/settings">
           <li><a href="${baseURL}/admin/settings">Site Settings</a></li>
           </sec:authorize>
         </ul>
       </div></div>
       <h2>${title}</h2
       ><c:if test="${not empty siteNotice}"><div class="alert alert-info">${siteNotice}</div></c:if
       ><jsp:doBody/>
    </div>
  </body>
</html>