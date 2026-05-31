package com.eventmesh.ingestion.auth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "api_keys", uniqueConstraints = @UniqueConstraint(columnNames = "key_id"))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiKeyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*Public identifier for the key (returned to client)*/
    @Column(name = "key_id", nullable = false, unique = true, length = 36)
    private String keyId;

    /*Hashed API key (bcrypt/argon2) - DO NOT store plaintext*/
    @Column(name = "api_key_hash", nullable = false, length = 255)
    private String apiKeyHash;

    /*Client name (who owns the key)*/
    @Column(name = "client_name", nullable = false)
    private String clientName;

    /*Role assigned to this API Key
     * Example: ADMIN, CLIENT, VIEWER
     */
    @Column(name = "role", nullable = false)
    private String role;

    /*Whether Key is active or not*/
    @Column(name = "active", nullable = false)
    private boolean active = true;

    /*When was the key created*/
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    /*When key was last used*/
    @Column(name = "last_used_at")
    private Instant lastUsedAt;

    /*AutoSet Creation timestamp and keyId*/
    @PrePersist
    public void prePersist() {
        if (this.keyId == null) {
            this.keyId = UUID.randomUUID().toString();
        }
        this.createdAt = Instant.now();
    }
}
