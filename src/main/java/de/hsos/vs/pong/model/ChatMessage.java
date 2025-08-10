package de.hsos.vs.pong.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_messages")
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, length = 500)
    private String message;

    @Column(nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private ChatType type = ChatType.LOBBY;

    @Column
    private Long gameSessionId; // null für Lobby-Chat, ID für Game-Chat

    public enum ChatType {
        LOBBY, GAME
    }

    public ChatMessage() {}

    public ChatMessage(String username, String message, ChatType type) {
        this.username = username;
        this.message = message;
        this.type = type;
    }

    public ChatMessage(String username, String message, ChatType type, Long gameSessionId) {
        this.username = username;
        this.message = message;
        this.type = type;
        this.gameSessionId = gameSessionId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public ChatType getType() {
        return type;
    }

    public void setType(ChatType type) {
        this.type = type;
    }

    public Long getGameSessionId() {
        return gameSessionId;
    }

    public void setGameSessionId(Long gameSessionId) {
        this.gameSessionId = gameSessionId;
    }
}
