# Security & Production Hardening Changes (Planned and Implemented)

Date: May 13, 2026

This document summarizes the security-focused changes proposed and the minimal, high-priority implementation ("Option A") that will be applied to bring the project up to production standards.

Summary of changes (Option A - urgent, security-first)
- Stop storing API keys in plaintext. Store only a secure hash (bcrypt/argon2) and never persist plaintext.
- Return plaintext API key only once on creation; subsequent responses return a masked key or ID only.
- Change API endpoints so operations do not accept raw API keys in URL paths. Use key id for management operations.
- Introduce a PasswordEncoder-based hashing utility (BCryptPasswordEncoder) and update ApiKeyService to generate secure secrets and validate using matches().
- Use Instant (UTC) for createdAt and lastUsedAt fields.
- Introduce domain-specific exceptions for auth failures and replace broad RuntimeException usage with explicit exceptions.
- Update GlobalExceptionHandler to avoid leaking internal exception messages and include errorId + correlationId in error responses.

Additional production recommendations (non-blocking, planned)
- Integrate Spring Security fully: AuthenticationProvider, SecurityFilterChain and method-level security integration.
- Replace blocking Kafka sends in controllers with async send+callback or an outbox pattern.
- Harden Kafka producer configs (acks=all, idempotence, compression, retries) and configure DLQ for failed produces.
- Ensure secrets are stored in a secrets manager (Kubernetes Secrets, Vault, or cloud secret manager) and not in files.
- Add monitoring/tracing (Micrometer + OpenTelemetry) and structured error reporting (errorId, correlationId).

Files that will be changed under Option A (planned edits)
- `ingestion-service/src/main/java/com/eventmesh/ingestion/auth/entity/ApiKeyEntity.java` (store hashed key, use Instant timestamps)
- `ingestion-service/src/main/java/com/eventmesh/ingestion/auth/service/ApiKeyService.java` (generate secure secret, hash, return plaintext once, use PasswordEncoder)
- `ingestion-service/src/main/java/com/eventmesh/ingestion/auth/repository/ApiKeyRepository.java` (no lookup by plaintext key; use id-based operations)
- `ingestion-service/src/main/java/com/eventmesh/ingestion/auth/controller/ApiKeyController.java` (use id for deactivate, return masked values)
- `common-lib` global error/response classes and `GlobalExceptionHandler` to include errorId and correlationId

Operational notes
- Creation flow: server returns plaintext key once (visible to admin) and stores only hash.
- Validation flow: header `Authorization: ApiKey <key>` is preferred. The service will validate using `passwordEncoder.matches(headerKey, storedHash)`.
- Rotations: implement key rotation and auditing; keep lastUsedAt as Instant and optionally offload updates asynchronously.

Database migration notes
- The change replaces the plaintext `api_key` column with `api_key_hash` and adds a new `key_id` column (public UUID). Existing databases must be migrated carefully.

If you are in development and can recreate the database, the easiest approach is to remove the docker volume and let the schema be recreated by JPA/Hibernate (or run migrations):

```powershell
docker compose down
docker volume rm eventmesh_postgres-dev-data
docker compose up -d postgres-dev
```

If you must preserve existing data, apply an SQL migration (example for Postgres):

```sql
-- enable uuid generation if not present
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

ALTER TABLE api_keys RENAME COLUMN api_key TO api_key_hash;
ALTER TABLE api_keys ADD COLUMN key_id uuid DEFAULT uuid_generate_v4();
ALTER TABLE api_keys ALTER COLUMN created_at TYPE timestamptz USING created_at AT TIME ZONE 'UTC';
-- ensure key_id is unique
CREATE UNIQUE INDEX ON api_keys(key_id);
```

Note: After migration you will need to generate plaintext secrets for existing rows (there is no way to recover original plaintext). For existing clients, rotate keys by creating new keys and distributing them to clients.

Next steps (if you approve Option A)
1. I will implement the code changes for Option A and run static checks. This includes adding a `PasswordEncoder` bean, updating entity/repository/service/controller, and updating exception handling.
2. I will run workspace static checks and report back with diffs and guidance to run integration tests locally.

If you prefer a more invasive approach (Option B: full Spring Security integration), I can prepare a separate plan and PR.

