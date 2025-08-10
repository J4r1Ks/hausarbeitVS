<!DOCTYPE html>
<html>
<head><title>Lobby</title></head>
<body>
<h2>Willkommen in der Lobby!</h2>
<p>Nur eingeloggte Nutzer sehen das.</p>

<script>
    let websocket;

    function createWebsocket(url){
        websocket = new WebSocket(url);

        websocket.onopen = function (event) {
            console.log("Websocket conntected.");
        };

    }

    createWebsocket("quong/3");
</script>

<form action="${pageContext.request.contextPath}/perform_logout" method="post">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    <button type="submit">Logout</button>
</form>
</body>
</html>