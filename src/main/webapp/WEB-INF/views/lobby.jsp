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

    /*function disableAllPlayerIDs(){
        let playerIDs = document.getElementsByName("playerID");

        playerIDs.forEach((playerID) => {
            playerID.disabled = true;
            playerID.checked = false;
        });
        playerIDs[0].checked = true;
    }*/

    function setPlayerSelection(){
        let playerSelections = document.getElementsByName("numberOfPlayers");
        //let playerIDs = document.getElementsByName("playerID");

        //disableAllPlayerIDs();

        playerSelections.forEach((playerSelection) => {
            if(playerSelection.checked){
                numberOfPlayers = playerSelection.value;
            }
        });
        /*for(let i = 0; i < numberOfPlayers; i++){
            playerIDs[i].disabled = false;
        }*/
    }

    function createWebsocket(url){
        /*let playerIDs = document.getElementsByName("playerID");
        playerIDs.forEach((playerID) => {
            if(playerID.checked){
                //url = playerID.value;
                return 0;
            }
        });*/

        websocket = new WebSocket(url);

        websocket.onopen = function (event) {
            console.log("Websocket conntected.");
            websocket.send(numberOfPlayers);
            console.log("Game created.");
        };

        websocket.onmessage = function (event) {
            try {
                console.log(event.data);
                let data = JSON.parse(event.data);
                if(data.type === "giveValues" && playerID < 4){
                    websocket.send('{"type":"startGame", "numberOfPlayers":'+numberOfPlayers+', "playerID":'+playerID+'}');
                    ++playerID;
                }else if(data.type === "getData"){
                    //data.type = "setData";
                    websocket.send(JSON.stringify(data));
                }else if(data.type === "setData"){
                    //data.type = "setData";
                    websocket.send(JSON.stringify(data));
                }
            }catch (e) {
                console.error("Error occured while parsing the message: ", e, event.data);
            }

        };

        websocket.onclose = function (event) {
            console.log("Websocket disconntected.");
        };

    }

    //disableAllPlayerIDs();

    /**
     * Chat related
     */
        // server-injected Werte (JSP)
    const csrfToken = '${_csrf.token}';
    const contextPath = '${pageContext.request.contextPath}';
    const username = '<%= username %>';

    // Polling state
    let lastTimestamp = null; // z.B. "2025-08-09T15:13:31.385"
    const pollIntervalMs = 2500; // poll alle 2.5s
    let pollHandle = null;

    // DOM references
    const messagesDiv = document.getElementById('messages');
    const chatInput = document.getElementById('chatInput');
    const sendBtn = document.getElementById('sendBtn');
    const statusDiv = document.getElementById('chatStatus');

    // initial load: hole die letzten 50 Nachrichten
    async function loadInitialMessages() {
        try {
            const resp = await fetch(contextPath + '/api/chat/lobby/messages?limit=50');
            if (!resp.ok) throw new Error('HTTP ' + resp.status);
            const data = await resp.json();
            if (!data.success) throw new Error(data.error || 'error loading messages');

            // server liefert newest first (DESC). Wir wollen oldest-first anzeigen.
            let msgs = data.messages || [];
            msgs = msgs.slice().reverse();

            renderMessages(msgs);

            if (msgs.length > 0) {
                // neueste ist jetzt letztes Element
                lastTimestamp = msgs[msgs.length - 1].timestamp;
            } else {
                // fallback: aktuelle Zeit (ohne Z)
                lastTimestamp = (new Date()).toISOString().slice(0,19);
            }

            // Start polling
            if (!pollHandle) pollHandle = setInterval(pollNewMessages, pollIntervalMs);
            setStatus('Letzte Aktualisierung: ' + new Date().toLocaleString());
        } catch (e) {
            console.error('Fehler beim Laden initialer Nachrichten:', e);
            setStatus('Fehler beim Laden der Nachrichten');
        }
    }

    // Poll: lade neue Nachrichten seit lastTimestamp
    async function pollNewMessages() {
        if (!lastTimestamp) return;
        try {
            const url = contextPath + '/api/chat/lobby/messages/since?since=' + encodeURIComponent(lastTimestamp);
            const resp = await fetch(url);
            if (!resp.ok) {
                console.warn('Polling HTTP', resp.status);
                return;
            }
            const data = await resp.json();
            if (!data.success) return;

            const newMsgs = data.messages || [];
            if (newMsgs.length > 0) {
                appendMessages(newMsgs);
                lastTimestamp = newMsgs[newMsgs.length - 1].timestamp;
                setStatus('Neue Nachrichten: ' + newMsgs.length + ' — ' + new Date().toLocaleTimeString());
            }
        } catch (e) {
            console.error('Polling error', e);
        }
    }

    // Sende Nachricht (AJAX POST JSON)
    async function sendMessage() {
        if (!chatInput) return;
        const text = chatInput.value.trim();
        if (!text) return;
        sendBtn.disabled = true;
        try {
            const body = JSON.stringify({ message: text });
            const resp = await fetch(contextPath + '/api/chat/lobby/send', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'X-CSRF-TOKEN': csrfToken
                },
                body: body
            });
            const data = await resp.json();
            if (resp.ok && data.success) {
                // server returns chatMessage; füge direkt an
                const msg = data.chatMessage;
                appendMessages([ msg ]);
                lastTimestamp = msg.timestamp; // newest
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

    // Render initial list (ersetzt gesamten Inhalt)
    function renderMessages(msgList) {
        if (!messagesDiv) return;
        messagesDiv.innerHTML = '';
        msgList.forEach(m => messagesDiv.appendChild(createMessageElement(m)));
        scrollMessagesToBottom();
    }

    // Append (one or several new messages) to the end
    function appendMessages(msgList) {
        if (!messagesDiv) return;
        msgList.forEach(m => messagesDiv.appendChild(createMessageElement(m)));
        scrollMessagesToBottom();
    }

    // Create DOM element for message (kein JSP-EL in JS Strings!)
    function createMessageElement(m) {
        const container = document.createElement('div');
        container.className = 'msg';

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
        } catch(e){ return ts; }
    }

    function scrollMessagesToBottom() {
        if (!messagesDiv) return;
        messagesDiv.scrollTop = messagesDiv.scrollHeight;
    }

    function setStatus(text) {
        if (!statusDiv) return;
        statusDiv.textContent = text;
    }

    // allow pressing Enter to send
    if (chatInput) {
        chatInput.addEventListener('keydown', function(e){
            if (e.key === 'Enter' && !e.shiftKey) {
                e.preventDefault();
                sendMessage();
            }
        });
    }
    if (sendBtn) sendBtn.addEventListener('click', sendMessage);

    // initialize
    loadInitialMessages();
</script>



</body>
</html>