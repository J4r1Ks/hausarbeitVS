package de.hsos.vs.pong.chat;

import de.hsos.vs.pong.model.ChatMessage;
import de.hsos.vs.pong.repository.ChatRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatService {
    private final ChatRepository chatRepository;

    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    public ChatMessage sendLobbyMessage(String username, String message) {
        if(message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException("Message can't be null or empty");
        }
        if(message.length() > 500) {
            throw new IllegalArgumentException("Message can't be longer than 500 characters");
        }
        if(username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username can't be null or empty");
        }
        ChatMessage chatMessage = new ChatMessage(username, message, ChatMessage.ChatType.LOBBY);
        return chatRepository.save(chatMessage);
    }

    /**
     * Holt die letzen Nachrichten aus der Lobby
     */
    public List<ChatMessage> getLobbyMessages(int limit) {
        Pageable pageable = PageRequest.of(0, Math.min(limit, 100)); // Max 100 nachrichten
        return chatRepository.findLobbyMessagesOrderByTimestampDesc(pageable);
    }

    /**
     * Holt Lobby-Nachrichten seit einem bestimmten Zeitpunkt (für Polling)
     */
    public List<ChatMessage> getLobbyMessagesSince(LocalDateTime since) {
        return chatRepository.findLobbyMessagesSince(since);
    }

    /**
     * Löscht alte Nachrichten (für Cleanup)
     */
    public void deleteOldMessages(LocalDateTime before) {
        chatRepository.deleteMessagesOlderThan(before);
    }

    /**
     * Zählt die Anzahl aller Lobby-Nachrichten
     */
    public long countLobbyMessages() {
        return chatRepository.countLobbyMessages();
    }

}


