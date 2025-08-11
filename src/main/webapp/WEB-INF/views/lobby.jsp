<!DOCTYPE html>
<html>
<head><title>Lobby</title></head>
<body>
<h2>Willkommen in der Lobby!</h2>
<p>Nur eingeloggte Nutzer sehen das.</p>


<span>Number of players</span>
<fieldset onchange="setPlayerSelection()">
    <input type="radio" id="numberOfPlayers" name="numberOfPlayers" value="1"><span>1</span>
    <input type="radio" id="numberOfPlayers" name="numberOfPlayers" value="2"><span>2</span>
    <input type="radio" id="numberOfPlayers" name="numberOfPlayers" value="3"><span>3</span>
    <input type="radio" id="numberOfPlayers" name="numberOfPlayers" value="4"><span>4</span>
</fieldset>
<span>playerID</span>
<fieldset>
    <input type="radio" id="playerID" name="playerID" value="0"><span>0</span>
    <input type="radio" id="playerID" name="playerID" value="1"><span>1</span>
    <input type="radio" id="playerID" name="playerID" value="2"><span>2</span>
    <input type="radio" id="playerID" name="playerID" value="3"><span>3</span>
</fieldset>
<button onclick="createWebsocket('quong/')">Create Game</button>


<script>
    let websocket;

    function  disableAllPlayerIDs(){
        let playerIDs = document.getElementsByName("playerID");

        playerIDs.forEach((playerID) => {
            playerID.disabled  = true;
        });
    }

    function setPlayerSelection(){
        let playerSelections = document.getElementsByName("numberOfPlayers");
        let playerIDs = document.getElementsByName("playerID");

        disableAllPlayerIDs();

        let number;
        playerSelections.forEach((playerSelection) => {
            if(playerSelection.checked){
                number = playerSelection.value;
            }
        });
        for(let i = 0; i < number; i++){
            playerIDs[i].disabled = false;
        }


    }

    function createWebsocket(url){
        let playerIDs = document.getElementsByName("playerID");
        playerIDs.forEach((playerID) => {
            if(playerID.checked){
                url += playerID.value;
                return 0;
            }
        });

        websocket = new WebSocket(url);

        websocket.onopen = function (event) {
            console.log("Websocket conntected.");
        };

    }

    disableAllPlayerIDs();
</script>

<form action="${pageContext.request.contextPath}/perform_logout" method="post">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    <button type="submit">Logout</button>
</form>
</body>
</html>