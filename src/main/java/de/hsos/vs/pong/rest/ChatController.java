package de.hsos.vs.pong.rest;

import de.hsos.vs.pong.chat.ChatService;
import de.hsos.vs.pong.model.ChatMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    /**
     * Sendet eine Lobby-Nachricht
     */
    @PostMapping("/lobby/send")
    public ResponseEntity<Map<String, Object>> sendLobbyMessage(@RequestBody Map<String, String> request, Authentication auth) {

        Map<String, Object> response = new HashMap<>();

        try {
            String message = request.get("message");
            String username = auth.getName();

            ChatMessage chatMessage = chatService.sendLobbyMessage(username, message);

            response.put("success", true);
            response.put("message", "Nachricht gesendet");
            response.put("chatMessage", Map.of(
                    "id", chatMessage.getId(),
                    "username", chatMessage.getUsername(),
                    "message", chatMessage.getMessage(),
                    "timestamp", chatMessage.getTimestamp().toString()
            ));
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "Fehler beim Senden der Nachricht");
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Holt die letzten Lobby-Nachrichten
     */
    @GetMapping("/lobby/messages")
    public ResponseEntity<Map<String, Object>> getLobbyMessages(@RequestParam(defaultValue = "50") int limit) {

        Map<String, Object> response = new HashMap<>();
        try {
            List<ChatMessage> messages = chatService.getLobbyMessages(limit);

            response.put("success", true);
            response.put("messages", messages.stream().map(msg -> Map.of(
                    "id", msg.getId(),
                    "username", msg.getUsername(),
                    "message", msg.getMessage(),
                    "timestamp", msg.getTimestamp().toString()
            )).toList());
            response.put("count", messages.size());
            response.put("totalMessages", chatService.countLobbyMessages());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "Fehler beim Laden der Nachrichten");
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Holt neue Lobby-Nachrichten seit einem bestimmten Zeitpunkt (für Polling)
     */
    @GetMapping("/lobby/messages/since")
    public ResponseEntity<Map<String, Object>> getLobbyMessagesSince(@RequestParam String since) {

        Map<String, Object> response = new HashMap<>();

        try {
            LocalDateTime sinceTime = LocalDateTime.parse(since);
            List<ChatMessage> messages = chatService.getLobbyMessagesSince(sinceTime);

            response.put("success", true);
            response.put("messages", messages.stream().map(msg -> Map.of(
                    "id", msg.getId(),
                    "username", msg.getUsername(),
                    "message", msg.getMessage(),
                    "timestamp", msg.getTimestamp().toString()
            )).toList());
            response.put("count", messages.size());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "Fehler beim Laden neuer Nachrichten");
            return ResponseEntity.badRequest().body(response);
        }
    }
}
