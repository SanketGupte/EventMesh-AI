package com.eventmesh.routing.repository;

import com.eventmesh.routing.entity.EventLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface  EventLogRepository extends JpaRepository<EventLog, Long> {
    Optional<EventLog> findByEventId(String eventId);
    boolean existsByEventId(String eventId);
    List<EventLog> findByEventType(String eventType);
    @Query("SELECT e.destinationTopic, COUNT(e) FROM EventLog e WHERE e.eventType = :eventType GROUP BY e.destinationTopic")
    List<Object[]> findRoutingStats(@Param("eventType") String eventType);
}
