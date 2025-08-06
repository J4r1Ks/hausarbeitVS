<%--
  Created by IntelliJ IDEA.
  User: jstrelow
  Date: 17.07.2025
  Time: 11:21
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login & Registrierung</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 2rem; }
        input, button { padding: 0.5rem; margin: 0.5rem; width: 200px; }
        .message { margin-top: 1rem; color: green; }
        .error { color: red; }
    </style>
</head>
<body>
    <h2>Login oder Registrieren</h2>
<form id="authForm">
    <input type="text" id="username" placeholder="Benutzername" required /><br>
    <input type="password" id="password" placeholder="Passwort" required /><br>
    <button type="button" onclick="login()">Login</button>
    <button type="button" onclick="register()">Registrieren</button>
</form>
<div class="message" id="message"></div>
    <script>
        const api = "/api/auth";

        async function login() {
            const data = getFormData();
            const res = await fetch(api + "/login", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(data)
            });

            const msg = document.getElementById("message");
            const result = await res.json();

            if (res.ok) {
                msg.textContent = `Willkommen, ${result.username}!`;
                msg.className = "message";
            } else {
                msg.textContent = result.message || "Login fehlgeschlagen.";
                msg.className = "message error";
            }
        }

        async function register() {
            const data = getFormData();
            const res = await fetch(api + "/register", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(data)
            });

            const msg = document.getElementById("message");
            const result = await res.json();

            if (res.ok) {
                msg.textContent = `Registrierung erfolgreich. Willkommen, ${result.username}!`;
                msg.className = "message";
            } else {
                msg.textContent = result.message || "Registrierung fehlgeschlagen.";
                msg.className = "message error";
            }
        }

        function getFormData() {
            return {
                username: document.getElementById("username").value.trim(),
                password: document.getElementById("password").value.trim()
            };
        }
    </script>
</body>
</html>
