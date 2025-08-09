<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body>
<h2>Login</h2>
<form action="${pageContext.request.contextPath}/login" method="post">
  <input type="text" name="username" placeholder="Benutzer" required />
  <input type="password" name="password" placeholder="Passwort" required />
  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
  <button type="submit">Login</button>
</form>
<c:if test="${param.error != null}">
  <p style="color:red">Login fehlgeschlagen</p>
</c:if>
</body>
</html>
