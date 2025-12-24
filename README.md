# eBank Backend (Spring Boot 3)

Backend REST API for the mini-project eBank.

## Tech
- Spring Boot 3 (Java 17)
- Spring Security + JWT (JJWT)
- Spring Data JPA + MySQL 8
- Bean Validation (DTO)
- AOP (logging aspect)
- Optional email sending (Spring Mail)

## Roles (table)
Roles are stored in a `roles` table, and users can have **multiple roles** via `user_roles`.
The core roles expected by the cahier des charges are `CLIENT` and `AGENT_GUICHET`.

## Quick start
1) Create a MySQL database:
```sql
CREATE DATABASE ebank CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2) Configure env vars (recommended):
- `DB_URL` (e.g. `jdbc:mysql://localhost:3306/ebank?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC`)
- `DB_USER`
- `DB_PASS`
- `JWT_SECRET_B64` (base64-encoded secret, at least 32 bytes before base64)
- Optional mail:
  - `MAIL_ENABLED` (true/false)
  - `MAIL_HOST`, `MAIL_PORT`, `MAIL_USER`, `MAIL_PASS`, `MAIL_FROM`

3) Run (with Maven installed):
```bash
mvn spring-boot:run
```

## Default seed users
When `app.seed=true` (default), the app creates:
- AGENT_GUICHET: login `agent` / password `agent1234`
- CLIENT sample: login `client1` / password `client1234`

## API
Auth:
- `POST /api/auth/login`
- `POST /api/auth/change-password`

AGENT_GUICHET:
- `POST /api/agent/clients`
- `POST /api/agent/accounts`

CLIENT:
- `GET /api/client/accounts`
- `GET /api/client/dashboard`
- `POST /api/client/transfers`

Role management (AGENT_GUICHET):
- `GET /api/admin/roles`
- `POST /api/admin/roles`
- `DELETE /api/admin/roles/{roleId}`
- `GET /api/admin/users`
- `GET /api/admin/users/{userId}`
- `PUT /api/admin/users/{userId}/roles`
