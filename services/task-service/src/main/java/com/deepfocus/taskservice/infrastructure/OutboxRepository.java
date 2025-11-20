package com.deepfocus.taskservice.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import jakarta.persistence.LockModeType;
import java.util.List;

@Repository
public interface OutboxRepository extends JpaRepository<OutboxMessage, Long> {
    List<OutboxMessage> findByStatus(String status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select o from OutboxMessage o where o.status = 'PENDING'")
    List<OutboxMessage> findAndLockPending();
}