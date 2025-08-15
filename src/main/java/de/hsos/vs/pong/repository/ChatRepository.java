package de.hsos.vs.pong.repository;

import de.hsos.vs.pong.model.ChatMessage;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatRepository extends JpaRepository<ChatMessage, Long> {

    // Lobby Chat Nachrichten (neueste zuerst, limitiert)
    @Query("SELECT c FROM ChatMessage c WHERE c.type = 'LOBBY' ORDER BY c.timestamp DESC")
    List<ChatMessage> findLobbyMessagesOrderByTimestampDesc(Pageable pageable);

    // Lobby Chat seit bestimmtem Zeitpunkt (für Polling)
    @Query("SELECT c FROM ChatMessage c WHERE c.type = 'LOBBY' AND c.timestamp > :since ORDER BY c.timestamp ASC")
    List<ChatMessage> findLobbyMessagesSince(@Param("since") LocalDateTime since);

    // Anzahl der Lobby-Nachrichten
    @Query("SELECT COUNT(c) FROM ChatMessage c WHERE c.type = 'LOBBY'")
    long countLobbyMessages();

    // Alte Nachrichten löschen (für Cleanup)
    @Modifying
    @Transactional
    @Query("DELETE FROM ChatMessage c WHERE c.timestamp < :before")
    void deleteMessagesOlderThan(@Param("before") LocalDateTime before);
}
