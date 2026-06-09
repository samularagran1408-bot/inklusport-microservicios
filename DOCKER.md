# Docker — Inklusport microservices

Ready-to-run Docker setup. Requires **Docker Desktop** (or Docker Engine + Compose v2) and ~8 GB RAM for a full build.

## Quick start

**Core stack** (MySQL ×3, auth, users, sports, gateway):

```bash
docker compose up --build
```

**Full stack** (+ reports, admin, accessibility, AI, MongoDB):

```bash
docker compose --profile full up --build
```

| Entry point | URL |
|-------------|-----|
| API Gateway | http://localhost:8080 |
| Auth (direct) | http://localhost:3001 |
| Users (direct) | http://localhost:3002 |
| Sports (direct) | http://localhost:3003 |
| Reports (full profile) | http://localhost:3006 |
| Admin (full profile) | http://localhost:3005 |
| Accessibility (full profile) | http://localhost:3004 |
| AI assistant (full profile) | http://localhost:8087 |

Default DB password in Compose: `root123` (development only).

## How it works

- **`SPRING_PROFILES_ACTIVE=docker`** loads `application-docker.yml` (or `.properties`) with hostnames for Compose networking.
- **Monorepo services** (`ink-ms-auth`, `users`, `gateway`, `reports`, `accessibility`) build from the **repository root** (`context: .`).
- **Standalone services** (`ink-ms-sports`, `ink-ms-admin`, `ai-assistant-ms`) build from their own folder.
- **`search-ms`** and **`eureka-server`** are scaffolds — Dockerfiles are commented until those modules are implemented.

## Suggested commits (manual, in order)

Use these messages in English when pushing in parts:

### Commit 1 — Docker Spring profiles (core monorepo)

**Files:**

- `ink-ms-auth/src/main/resources/application-docker.yml`
- `ink-ms-users/src/main/resources/application-docker.yml`
- `ink-ms-reports/src/main/resources/application-docker.yml`
- `ink-ms-gateway/src/main/resources/application-docker.yml`

**Message:**

```
feat(docker): add Spring docker profile for core monorepo services
```

### Commit 2 — Multi-stage Dockerfiles (monorepo) + dockerignore

**Files:**

- `.dockerignore`
- `ink-ms-auth/Dockerfile`
- `ink-ms-users/Dockerfile`
- `ink-ms-gateway/Dockerfile`
- `ink-ms-reports/Dockerfile`

**Message:**

```
feat(docker): add root-context multi-stage Dockerfiles for monorepo modules
```

### Commit 3 — Docker profiles + Dockerfiles (standalone MS)

**Files:**

- `ink-ms-sports/src/main/resources/application-docker.yml`
- `ink-ms-admin/src/main/resources/application-docker.yml`
- `ink-ms-accesibility/src/main/resources/application-docker.yml`
- `ai-assistant-ms/src/main/resources/application-docker.properties`
- `ink-ms-sports/Dockerfile`
- `ink-ms-admin/Dockerfile`
- `ink-ms-accesibility/Dockerfile`
- `ai-assistant-ms/Dockerfile`

**Message:**

```
feat(docker): add docker profile and Dockerfiles for standalone microservices
```

### Commit 4 — Compose orchestration + docs

**Files:**

- `docker-compose.yml`
- `DOCKER.md`
- `README.md` (Docker section, if updated)

**Message:**

```
feat(docker): add compose stack with core and full profiles
```

### Commit 5 (optional) — Scaffold placeholders

**Files:**

- `search-ms/Dockerfile`
- `eureka-server/Dockerfile`

**Message:**

```
chore(docker): document placeholder Dockerfiles for scaffold modules
```

## Troubleshooting

| Issue | Check |
|-------|--------|
| Build fails on auth/users/reports | Run from repo root; ensure `pom.xml` + `ink-ms-common` are copied (see Dockerfile). |
| Sports build: JAR not found | Artifact must be `ink-ms-sports-*.jar` (fixed in Dockerfile). |
| Gateway 502 | Backend MS not healthy; `docker compose ps` and logs `docker compose logs ink-ms-auth`. |
| MySQL connection refused | Wait for healthcheck; first start can take 1–2 minutes. |
| Reports vs sports port clash | Reports uses **3006** in Docker only (`application-docker.yml`). |

## Not included yet

- `search-ms` — empty module
- `eureka-server` — not wired; gateway uses static routes
- Production secrets (mail, JWT) — override via env in real deployments
