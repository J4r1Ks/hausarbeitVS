<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    // sichere Ermittlung des Benutzernamens (falls vorhanden)
    String username = request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : "";
%>
<!DOCTYPE html>
<html>
<head>
    <title>Lobby</title>
    <style>
        /* very small, unobtrusive layout */
        #chat {
            border: 1px solid #ccc;
            padding: 8px;
            width: 600px;
            max-width: 95%;
        }
        #messages {
            height: 300px;
            overflow-y: auto;
            border: 1px solid #eee;
            padding: 6px;
            background: #fafafa;
            white-space: pre-wrap;
            font-family: monospace;
            margin-bottom: 8px;
        }
        .msg {
            margin-bottom: 6px;
            padding-bottom: 4px;
            border-bottom: 1px dashed #ddd;
        }
        .meta {
            font-size: 0.85em;
            color: #666;
        }
        #sendRow { display:flex; gap:8px; }
        #sendRow input[type="text"] { flex:1; padding:6px; }
    </style>
</head>
<body>
<h2>Willkommen in der Lobby!</h2>
<p>Nur eingeloggte Nutzer sehen das.</p>

<!-- Spielauswahl -->
<div>
    <span>Number of players</span>
    <fieldset onchange="setPlayerSelection()">
        <input type="radio" id="numberOfPlayers" name="numberOfPlayers" value="1"><span>1</span>
        <input type="radio" id="numberOfPlayers" name="numberOfPlayers" value="2"><span>2</span>
        <input type="radio" id="numberOfPlayers" name="numberOfPlayers" value="3"><span>3</span>
        <input type="radio" id="numberOfPlayers" name="numberOfPlayers" value="4"><span>4</span>
    </fieldset>
    <!-- <span>playerID</span>
    <fieldset>
        <input type="radio" id="playerID" name="playerID" value="0"><span>0</span>
        <input type="radio" id="playerID" name="playerID" value="1"><span>1</span>
        <input type="radio" id="playerID" name="playerID" value="2"><span>2</span>
        <input type="radio" id="playerID" name="playerID" value="3"><span>3</span>
    </fieldset> -->
    <button onclick="createWebsocket('quong')">Create Game</button>
</div>

<hr/>

<!-- Chatbereich -->
<div id="chat">
    <h3>Lobby-Chat</h3>
    <div id="messages" aria-live="polite" aria-atomic="false">
        <!-- Nachrichten werden hier von JS eingefügt -->
    </div>

    <div id="sendRow">
        <input id="chatInput" type="text" placeholder="Schreibe eine Nachricht..." maxlength="500" />
        <button id="sendBtn">Senden</button>
    </div>

    <div id="chatStatus" style="margin-top:6px;color:#666;font-size:0.9em;"></div>
</div>

<hr/>

<!-- Logout -->
<form action="${pageContext.request.contextPath}/perform_logout" method="post">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    <button type="submit">Logout</button>
</form>


<script>
    /**
     * game related
     */
    let websocket;
    let numberOfPlayers;
    let playerID = 0;

    function setPlayerSelection(){
        let playerSelections = document.getElementsByName("numberOfPlayers");

        playerSelections.forEach((playerSelection) => {
            if(playerSelection.checked){
                numberOfPlayers = playerSelection.value;
            }
        });
    }

    function createWebsocket(url){
        if(websocket != null) {
            websocket.close();
            playerID = 0;
        }

        websocket = new WebSocket(url);

        websocket.onopen = function (event) {
            console.log("Websocket conntected.");
        };

        websocket.onmessage = function (event) {
            //console.log(event.data);
            try {
                let data = JSON.parse(event.data);
                if(data.type === "giveValues" && playerID < 4){
                    websocket.send('{"type":"startGame", "numberOfPlayers":'+numberOfPlayers+', "playerID":'+playerID+'}');
                    ++playerID;
                }
            }catch (e) {
                console.error("Error occured while parsing the message: ", e, event.data);
            }

        };

        websocket.onmessage = function (event) {
            try {
                //const message = JSON.parse(event.data);
                //console.log(JSON.parse(event.data));
                websocket.send(event.data);
            }catch (e) {
                console.error("Error occured while parsing the message: ", e, event.data);
            }

        };

        websocket.onclose = function (event) {
            console.log("Websocket disconntected.");
        };

    }

    /**
     * Chat related
     */
    // server-injected Werte (JSP)
    const contextPath = '${pageContext.request.contextPath}';
    const csrfToken = '${_csrf.token}';
    const username = '<%= username %>';

    // state
    let lastTimestamp = null;
    let longPollRunning = false;

    // DOM refs
    const messagesDiv = document.getElementById('messages');
    const chatInput = document.getElementById('chatInput');
    const sendBtn = document.getElementById('sendBtn');
    const statusDiv = document.getElementById('chatStatus');

    const seenMessageIds = new Set();

    // Initial load
    async function loadInitialMessages() {
        try {
            const resp = await fetch(contextPath + '/api/chat/lobby/messages?limit=50', { method: 'GET', credentials: 'same-origin' });
            if (!resp.ok) throw new Error('HTTP ' + resp.status);
            const data = await resp.json();
            if (!data.success) throw new Error(data.error || 'error loading messages');

            let msgs = data.messages || [];
            msgs = msgs.slice().reverse(); // oldest-first
            renderMessages(msgs);

            if (msgs.length > 0) {
                lastTimestamp = msgs[msgs.length - 1].timestamp;
            } else {
                // fallback: ISO ohne Millisekunden
                lastTimestamp = new Date().toISOString().split('.')[0];
            }
            setStatus('Letzte Aktualisierung: ' + new Date().toLocaleTimeString());
        } catch (e) {
            console.error('Fehler beim Laden initialer Nachrichten:', e);
            setStatus('Fehler beim Laden der Nachrichten');
            // trotzdem initialisieren, damit longPoll laufen kann
            lastTimestamp = new Date().toISOString().split('.')[0];
        }
    }

    // Long-poll loop
    async function longPollLoop() {
        if (longPollRunning) return;
        longPollRunning = true;

        if (!lastTimestamp) {
            await loadInitialMessages();
        }

        async function doLongPoll() {
            try {
                const url = contextPath + '/api/chat/lobby/longpoll?since=' + encodeURIComponent(lastTimestamp);
                const resp = await fetch(url, { method: 'GET', credentials: 'same-origin' });
                if (!resp.ok) {
                    console.warn('Long-poll HTTP', resp.status);
                    setTimeout(doLongPoll, 2000);
                    return;
                }
                const data = await resp.json();
                if (data && data.success && Array.isArray(data.messages) && data.messages.length > 0) {
                    appendMessages(data.messages);
                    // KORREKT: letztes Element durch length-1
                    lastTimestamp = data.messages[data.messages.length - 1].timestamp;
                }
                // sofort neuen Long-Poll starten (kleiner gap)
                setTimeout(doLongPoll, 50);
            } catch (e) {
                console.error('Long-poll failed', e);
                setTimeout(doLongPoll, 2000);
            }
        }

        doLongPoll();
    }

    // Send message (POST)
    async function sendMessage() {
        if (!chatInput) return;
        const text = chatInput.value.trim();
        if (!text) return;
        sendBtn.disabled = true;
        try {
            const resp = await fetch(contextPath + '/api/chat/lobby/send', {
                method: 'POST',
                credentials: 'same-origin',
                headers: {
                    'Content-Type': 'application/json',
                    'X-CSRF-TOKEN': csrfToken
                },
                body: JSON.stringify({ message: text })
            });
            const data = await resp.json();
            if (resp.ok && data.success) {
                const msg = data.chatMessage;
                appendMessages([msg]);
                if (msg.timestamp) {
                    lastTimestamp = msg.timestamp;
                }
                chatInput.value = '';
                setStatus('Nachricht gesendet');
            } else {
                console.warn('Send failed', data);
                setStatus('Fehler beim Senden: ' + (data.error || resp.status));
            }
        } catch (e) {
            console.error('Send exception', e);
            setStatus('Fehler beim Senden');
        } finally {
            sendBtn.disabled = false;
        }
    }

    // DOM helpers
    function renderMessages(msgList) {
        if (!messagesDiv) return;
        messagesDiv.innerHTML = '';
        seenMessageIds.clear(); // beim kompletten Neuladen alte IDs entfernen
        msgList.forEach(m => {
            const el = createMessageElement(m);
            messagesDiv.appendChild(el);
            if (m.id != null) seenMessageIds.add(String(m.id));
        });
        //Update lastTimestamp sicher auf das neueste angezeigte
        if (msgList.length > 0) {
            lastTimestamp = msgList[msgList.length - 1].timestamp;
        }
        scrollMessagesToBottom();
    }

    function appendMessages(msgList) {
        if (!messagesDiv) return;
        msgList.forEach(m => {
            const id = m.id != null ? String(m.id) : null;
            if (id && seenMessageIds.has(id)) {
                return;
            }
            const el = createMessageElement(m);
            messagesDiv.appendChild(el);
            if (id) seenMessageIds.add(id);
        });
        // Update lastTimestamp falls neue Nachrichten angefügt wurden
        if (msgList.length > 0) {
            const newest = msgList[msgList.length - 1];
            if (newest && newest.timestamp) lastTimestamp = newest.timestamp;
        }
        scrollMessagesToBottom();
    }

    function createMessageElement(m) {
        const container = document.createElement('div');
        container.className = 'msg';
        // set data-id wenn vorhanden, hilft Debugging and CSS
        if (m.id != null) {
            container.setAttribute('data-id', String(m.id));
        }
        const meta = document.createElement('div');
        meta.className = 'meta';
        meta.textContent = (m.username || 'unknown') + ' — ' + formatTimestamp(m.timestamp || '');
        const body = document.createElement('div');
        body.textContent = m.message || '';
        container.appendChild(meta);
        container.appendChild(body);
        return container;
    }

    function formatTimestamp(ts) {
        try {
            if (!ts) return '';
            const s = ts.length > 19 ? ts.slice(0,19) : ts;
            const dt = new Date(s);
            if (isNaN(dt)) return ts;
            return dt.toLocaleString();
        } catch (e) {
            return ts;
        }
    }

    function scrollMessagesToBottom() {
        if (!messagesDiv) return;
        messagesDiv.scrollTop = messagesDiv.scrollHeight;
    }

    function setStatus(text) {
        if (!statusDiv) return;
        statusDiv.textContent = text;
    }

    // Enter to send + button
    if (chatInput) {
        chatInput.addEventListener('keydown', function (e) {
            if (e.key === 'Enter' && !e.shiftKey) {
                e.preventDefault();
                sendMessage();
            }
        });
    }
    if (sendBtn) sendBtn.addEventListener('click', sendMessage);

    // Start everything
    (async function init() {
        await loadInitialMessages();
        longPollLoop(); // <-- wichtig: Long-Poll starten
    })();
</script>
</body>
</html>