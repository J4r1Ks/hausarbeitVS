<!DOCTYPE html>
<html>
<head><title>Lobby</title></head>
<body>
<h2>Willkommen in der Lobby!</h2>
<p>Nur eingeloggte Nutzer sehen das.</p>
<form action="${pageContext.request.contextPath}/perform_logout" method="post">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    <button type="submit">Logout</button>
</form>
</body>
</html>
