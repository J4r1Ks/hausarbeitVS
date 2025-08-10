package de.hsos.vs.pong.chat;

import de.hsos.vs.pong.model.ChatMessage;
import org.springframework.stereotype.Service;
/*
@Service
public class ChatService {
    private final ChatRepository chatRepository;

    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    public ChatMessage sendLobbyMessage(String username, String message) {
        if(message == null || message.trim().isEmpty() || message.length() > 500) {
            throw new IllegalArgumentException("Message is invalid");
        }

        ChatMessage chatMessage = new ChatMessage(username, message, ChatMessage.ChatType.LOBBY);
        return chatRepository.save(chatMessage);
    }


}

 */
