<%@ tag %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<input id='csrf' type="hidden" name="csrfToken"
       value='<c:out value="${csrfToken}" escapeXml="true" />' >