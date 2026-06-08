# Pending Security Implementation Tasks

**Date:** May 18, 2026  
**Project:** EventMesh AI  
**Version:** 0.0.1-SNAPSHOT  

This document outlines the remaining security-related tasks identified in the project status files (`PROJECT_STATUS.md`, `PROJECT_STATUS_ANALYSIS.md`) and the security changes document (`SECURITY_CHANGES.md`). These tasks are required to bring the project to full production readiness.

---

## 🔐 High-Priority Security Tasks (Estimated 30-60 minutes)

These tasks correspond to the "Option A" security hardening described in `SECURITY_CHANGES.md` and are marked as pending in the status files.

### 1. Hashed API Key Storage
**Goal:** Never store plaintext API keys; store only a secure hash (bcrypt/argon2) and return plaintext only once on creation.

**Files to Update:**
- `ingestion-service/src/main/java/com/eventmesh/ingestion/auth/entity/ApiKeyEntity.java`
  - Replace `api_key` column with `api_key_hash` (String)
  - Change `createdAt` and `lastUsedAt` to `Instant` (UTC)
  - Add `key_id` column (UUID) for public identification
- `ingestion-service/src/main/java/com/eventmesh/ingestion/auth/repository/ApiKeyRepository.java`
  - Remove any methods that lookup by plaintext key
  - Add methods for lookup by `key_id`
- `ingestion-service/src/main/java/com/eventmesh/ingestion/auth/service/ApiKeyService.java`
  - Integrate `PasswordEncoder` (BCryptPasswordEncoder)
  - Generate cryptographically random secret on creation
  - Hash the secret before storage; return plaintext secret only once in the creation response
  - Validate API key by comparing provided secret with stored hash using `passwordEncoder.matches()`
- `ingestion-service/src/main/java/com/eventmesh/ingestion/auth/controller/ApiKeyController.java`
  - Change API endpoints to use `key_id` in paths instead of raw keys
  - On creation, return the plaintext secret only once (in a secure response)
  - For other operations (get, deactivate), return masked key or only metadata (never plaintext)
  - Update DTOs/API responses to avoid exposing plaintext keys

**Database Migration:**
- Apply migration script (see `SECURITY_CHANGES.md` for Postgres example) to:
  - Rename `api_key` → `api_key_hash`
  - Add `key_id` column with UUID default
  - Convert timestamps to timestamptz UTC
  - Add unique constraint on `key_id`
- **Note:** Existing plaintext keys cannot be recovered; clients must be issued new keys.

### 2. Improved Exception Handling & Error Responses
**Goal:** Avoid leaking internal exception messages and include `errorId` + `correlationId` in all error responses.

**Files to Update:**
- `common-lib` (global error/response classes)
- `GlobalExceptionHandler` (likely in each service or common-lib)
  - Catch exceptions, log them internally with full stack trace
  - Generate a unique `errorId` (UUID) for each error occurrence
  - Ensure `correlationId` (from MDC/MDC) is included in response
  - Return a sanitized error message to the client (e.g., "Invalid API key" instead of detailed exception)
  - Maintain consistent error response format (timestamp, status, errorId, correlationId, message, etc.)

### 3. API Key Header Validation (Preferred Method)
**Goal:** Validate API keys via a secure header (e.g., `Authorization: ApiKey <keyId>:<secret>` or separate headers) instead of allowing keys in URL paths or query parameters.

**Files to Update:**
- Authentication filter (currently a basic filter; may need enhancement)
  - Extract `keyId` and `secret` from header
  - Lookup `ApiKeyEntity` by `keyId`
  - Use `PasswordEncoder.matches()` to validate secret against stored hash
  - Set authentication in `SecurityContext` if valid

> *Note:* This may already be partially implemented; ensure it uses the hashed lookup.

---

## 🛡️ Recommended Security Tasks (Optional but Recommended for Production)

These tasks are labeled as "additional production recommendations" in `SECURITY_CHANGES.md` and are optional but strongly recommended for a production-hardened system.

### 4. Full Spring Security Integration
**Goal:** Replace custom filter-based authentication with Spring Security's `AuthenticationProvider` and `SecurityFilterChain` for better integration, method-level security, and standard practices.

**Files to Update:**
- Create an `ApiKeyAuthenticationProvider` implementing `AuthenticationProvider`
- Configure `SecurityFilterChain` to use the provider for relevant API endpoints
- Remove custom filter if redundant
- Enable method-level security (`@PreAuthorize`, `@PostAuthorize`) where appropriate
- Configure session statelessness, CSRF disabled (for stateless API), etc.

### 5. Kafka Producer Hardening
**Goal:** Ensure reliable event publishing with proper acknowledgments, idempotence, retries, and dead-letter queue handling.

**Files to Update:**
- `application.yaml` (or `application-*.yml`) for each service
  - Configure Kafka producer properties:
    - `acks=all`
    - `enable.idempotence=true`
    - `retries` > 0 (with backoff)
    - `compression.type` (e.g., snappy, lz4)
    - `max.in.flight.requests.per.connection` (≤ 5 when idempotence enabled)
- Consider implementing an outbox pattern for exactly-once semantics if required.
- Configure DLQ (dead-letter topic) for failed produces via producer exception handling or using Spring Kafka's `DefaultErrorHandler`.

### 6. Secrets Management
**Goal:** Ensure secrets (database passwords, Kafka credentials, etc.) are not stored in plaintext files or image layers.

**Actions:**
- Use Kubernetes Secrets (or cloud secret manager) for environment-specific secrets
- Reference secrets in deployment manifests (Kubernetes) or Docker Compose (via `secrets:` or env files not committed)
- Avoid committing any `.env`, `application-prod.yml` with real secrets to version control.

### 7. Monitoring & Tracing
**Goal:** Enhance observability with distributed tracing and structured logging for security events.

**Files to Update:**
- Add Micrometer tracing (or OpenTelemetry) integration
- Log security-relevant events (API key validation failures, key creation/deactivation) with correlationId
- Export metrics to monitoring system (Prometheus, etc.)
- Consider adding audit logging for key management operations.

---

## 📋 Implementation Checklist

### Immediate Actions (High Priority)
- [ ] Update `ApiKeyEntity` to store hashed key, `Instant` timestamps, and add `key_id`
- [ ] Modify `ApiKeyRepository` to remove plaintext lookups and add `key_id`-based methods
- [ ] Refactor `ApiKeyService` to use `PasswordEncoder`, generate secrets, hash, and return plaintext once
- [ ] Restructure `ApiKeyController` to use `key_id` in paths and avoid returning plaintext keys after creation
- [ ] Apply database migration script to update schema (or recreate dev DB)
- [ ] Enhance `GlobalExceptionHandler` to include `errorId` and `correlationId` and sanitize messages
- [ ] Verify authentication header parsing uses hashed validation

### Recommended Actions (Optional but Advised)
- [ ] Implement Spring Security `AuthenticationProvider` and `SecurityFilterChain`
- [ ] Tune Kafka producer configs in `application.yaml` for reliability
- [ ] Set up secrets management for deployment (K8s Secrets, etc.)
- [ ] Add distributed tracing and audit logging for security events

### Verification Steps
- [ ] Run unit and integration tests to ensure authentication still works
- [ ] Test end-to-end flow: create API key (receive plaintext once), use key to publish events, validate that key cannot be retrieved again
- [ ] Attempt to exploit old endpoints (raw key in URL) – should fail
- [ ] Check logs for proper error IDs and correlation IDs
- [ ] Verify no plaintext secrets appear in logs, responses, or database

---

## 📚 References
- `SECURITY_CHANGES.md` – Detailed description of Option A changes and migration notes
- `PROJECT_STATUS.md` & `PROJECT_STATUS_ANALYSIS.md` – Current project status and remaining items
- Spring Security Documentation: https://docs.spring.io/spring-security/reference/
- BCrypt Password Encoding: https://docs.spring.io/spring-security/reference/authentication/password-encoding.html
- Kafka Producer Configs: https://kafka.apache.org/documentation/#producerconfigs

---
**Note:** Once these tasks are completed, the project’s security posture will align with production best practices, and the remaining pending items will be limited to configuration, testing, and optional enhancements.