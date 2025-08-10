package de.hsos.vs.pong.repository;

import de.hsos.vs.pong.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatRepository extends JpaRepository<ChatMessage, Long> {

    // Lobby Chat Nachrichten (neuste zuerst, limitiert)
    @Query("SELECT c FROM ChatMessage c WHERE c.type = 'LOBBY' ORDER BY c.timestamp DESC")
    List<ChatMessage> findLobbyMessagesOrderByTimestampDesc();

    // Game Chat Nachrichten für bestimmte Session
    @Query("SELECT c FROM ChatMessage c WHERE c.type = 'GAME' AND c.gameSessionId = :sessionId ORDER BY c.timestamp ASC")
    List<ChatMessage> findGameMessagesBySessionId(@Param("sessionId") Long sessionId);

    // Lobby Chat seit bestimmtem Zeitpunkt (für Polling)
    @Query("SELECT c FROM ChatMessage c WHERE c.type = 'LOBBY' AND c.timestamp > :since ORDER BY c.timestamp ASC")
    List<ChatMessage> findLobbyMessagesSince(@Param("since") LocalDateTime since);

    // Game Chat seit bestimmtem Zeitpunkt
    @Query("SELECT c FROM ChatMessage c WHERE c.type = 'GAME' AND c.gameSessionId = :sessionId AND c.timestamp > :since ORDER BY c.timestamp ASC")
    List<ChatMessage> findGameMessagesSince(@Param("sessionId") Long sessionId, @Param("since") LocalDateTime since);

    // Alte Nachrichten löschen (für Cleanup)
    @Query("DELETE FROM ChatMessage c WHERE c.timestamp < :before")
    void deleteMessagesOlderThan(@Param("before") LocalDateTime before);
}
