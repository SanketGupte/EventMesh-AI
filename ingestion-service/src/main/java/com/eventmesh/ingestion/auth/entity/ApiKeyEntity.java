package com.eventmesh.ingestion.auth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name="api_keys", uniqueConstraints = @UniqueConstraint(columnNames = "api_key"))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiKeyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /*Unique API Key used for authentication*/
    @Column(name = "api_key", nullable=false, unique = true, length=255)
    private String apiKey;
    /*Client name (who owns the key)*/
    @Column(name = "client_name", nullable = false)
    private String clientName;
    /*Role assigned to this API Key
    * Example: ADMIN, CLIENT, VIEWER
    */
    @Column(name="role", nullable = false)
    private String role;
    /*Whether Key is active or not*/
    @Column(name="active", nullable = false)
    private boolean active=true;
    /*When was the key created*/
    @Column(name="created_at", nullable = false)
    private LocalDateTime createdAt;
    /*When key was last used*/
    @Column(name="last_used_at")
    private LocalDateTime lastUsedAt;

    /*AutoSet Creation timestamp*/
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
