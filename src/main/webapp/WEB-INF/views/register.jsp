<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head><title>Register</title></head>
<body>
<h2>Registrieren</h2>

<c:if test="${not empty error}">
  <div style="color:red">${error}</div>
</c:if>

<form action="${pageContext.request.contextPath}/register" method="post">
  <label>Benutzer:
    <input type="text" name="username" required/>
  </label><br/>
  <label>Passwort:
    <input type="password" name="password" required/>
  </label><br/>
  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
  <button type="submit">Registrieren</button>
</form>

<p><a href="${pageContext.request.contextPath}/login">Zum Login</a></p>
</body>
</html>