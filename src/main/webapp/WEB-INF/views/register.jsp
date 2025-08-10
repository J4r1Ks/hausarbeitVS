<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head><title>Login</title></head>
<body>
<h2>Login</h2>

<c:if test="${not empty error}">
  <div style="color:red">${error}</div>
</c:if>
<c:if test="${not empty msg}">
  <div style="color:green">${msg}</div>
</c:if>

<form action="${pageContext.request.contextPath}/perform_login" method="post">
  <label>Benutzer:
    <input type="text" name="username" required/>
  </label><br/>
  <label>Passwort:
    <input type="password" name="password" required/>
  </label><br/>
  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
  <button type="submit">Login</button>
</form>

<p><a href="${pageContext.request.contextPath}/register">Registrieren</a></p>
</body>
</html>