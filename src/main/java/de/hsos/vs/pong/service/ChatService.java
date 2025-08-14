package de.hsos.vs.pong.service;

import de.hsos.vs.pong.model.ChatMessage;
import de.hsos.vs.pong.repository.ChatRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.time.LocalDateTime;
import java.util.List;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class ChatService {
    private final ChatRepository chatRepository;

    // Warteliste für Long-Polling-Anfragen
    private final List<WaitingClient> waitingClients = new CopyOnWriteArrayList<>();

    private static final long LONG_POLL_TIMEOUT_MS = 30_000L;

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
        ChatMessage saved = chatRepository.save(chatMessage);

        // Benachichtige wartende Long-Poll-Clients
        notifyWaitingClients();

        return saved;
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
     * Registriert eine Long-Poll-Request. Wenn sofort Ergebnisse vorhanden sind, werden diese zurückgegeben.
     * Ansonsten wird die DeferredResult in die Warteliste gelegt und bei neuer Nachricht erfüllt oder nach Timeout leer geantwortet.
     */
    public DeferredResult<ResponseEntity<Map<String, Object>>> registerLongPoll(LocalDateTime since) {
        DeferredResult<ResponseEntity<Map<String, Object>>> deferred = new DeferredResult<>(LONG_POLL_TIMEOUT_MS);

        // Prüft sofort, ob neue Nachrichten seit 'since' vorhanden sind
        List<ChatMessage> msgs = getLobbyMessagesSince(since);
        if(msgs != null && !msgs.isEmpty()) {
            Map<String, Object> map = makeMessagesResponse(msgs);
            deferred.setResult(ResponseEntity.ok(map));
            return deferred;
        }

        // Wenn nicht, in Warteliste aufnehmen
        WaitingClient wc = new WaitingClient(since, deferred);
        waitingClients.add(wc);

        // Bei Timeout:, antworte mit leerer Liste (kein Fehler)
        deferred.onTimeout(() -> {
            Map<String, Object> res = new HashMap<>();
            res.put("success", true);
            res.put("messages", Collections.emptyList());
            res.put("count", 0);
            deferred.setResult(ResponseEntity.ok(res));
            waitingClients.remove(wc);
        });

        // Bei Completion aus Warteliste entfernen
        deferred.onCompletion(() -> waitingClients.remove(wc));
        return deferred;
    }

    /**
     * Benachrichtigt alle wartenden Clients - prüft für jeden, ob es neue Nachrichten seit dessen 'since' gibt.
     */
    private void notifyWaitingClients() {
        if(waitingClients.isEmpty()) { return; }

        for (WaitingClient wc : new ArrayList<>(waitingClients)) {
            try {
                List<ChatMessage> msgs = getLobbyMessagesSince(wc.since);
                if (msgs != null && !msgs.isEmpty()) {
                    Map<String, Object> map = makeMessagesResponse(msgs);
                    wc.deferred.setResult(ResponseEntity.ok(map));
                    waitingClients.remove(wc);
                }
            } catch (Exception e) {
                // Falls beim Beantworten etwas schiefgeht: entferme und ignoriere
                waitingClients.remove(wc);
            }
        }
    }

    private Map<String, Object> makeMessagesResponse(List<ChatMessage> msgs) {
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("messages", msgs.stream().map(m -> Map.of(
                "id", m.getId(),
                "username", m.getUsername(),
                "message", m.getMessage(),
                "timestamp", m.getTimestamp().toString()
        )).toList());
        resp.put("count", msgs.size());
        return resp;
    }

    // Klasse für wartende Clients
    private static class WaitingClient {
        final LocalDateTime since;
        final DeferredResult<ResponseEntity<Map<String, Object>>> deferred;

        WaitingClient(LocalDateTime since, DeferredResult<ResponseEntity<Map<String, Object>>> deferred) {
            this.since = since;
            this.deferred = deferred;
        }
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


