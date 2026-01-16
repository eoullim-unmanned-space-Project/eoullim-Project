package org.example.eoullimback.sse;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SseChatService extends JpaRepository<SseChat, Long> {
}
