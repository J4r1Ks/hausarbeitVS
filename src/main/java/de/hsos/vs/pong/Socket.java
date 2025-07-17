package de.hsos.vs.pong;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.websocket.server.ServerEndpoint;

// War nur zum testen ob sockets und rest in diesem Projekt drin ist.
@ServerEndpoint(value = "/PongServer")
public class Socket extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

    }
}
