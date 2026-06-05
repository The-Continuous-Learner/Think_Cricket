# Think Cricket

> Cricket app for nerds.

A REST API backend for managing cricket matches end-to-end — from registering players and teams, through toss and innings, all the way to match results. Built with Spring Boot 4, MySQL, and Flyway.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Framework | Spring Boot 4.0.2 |
| Language | Java 17 |
| Database | MySQL 8+ |
| ORM | Spring Data JPA / Hibernate |
| Migrations | Flyway |
| Build | Maven |
| Utilities | Lombok |

---

## Getting Started

### Prerequisites

- Java 17+
- MySQL 8+
- Maven 3.8+

### Database Setup

```sql
CREATE DATABASE cricket;
CREATE USER 'myapp_user'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON cricket.* TO 'myapp_user'@'localhost';
```

### Configuration

Update `src/main/resources/application.yaml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/cricket?useSSL=false&serverTimezone=UTC
    username: myapp_user
    password: your_password
```

### Run

```bash
mvn spring-boot:run
```

Flyway will automatically create all tables on first boot.

---

## API Overview

### Auth — `/users`

| Method | Endpoint | Description |
|---|---|---|
| POST | `/users/register` | Register a new user |
| POST | `/users/login` | Login and receive a session token |
| POST | `/users/logout` | Invalidate session |

All protected endpoints require a `sessionToken` in the request body.
Sessions expire after **24 hours**.

---

### Players — `/players`

| Method | Endpoint | Description |
|---|---|---|
| POST | `/players/save` | Create or update a player |
| GET | `/players/{id}` | Get player by ID |
| GET | `/players` | List all players |

---

### Teams — `/teams`

| Method | Endpoint | Description |
|---|---|---|
| POST | `/teams/create` | Create a team |
| POST | `/teams/modify` | Update team name or description |
| POST | `/teams/delete` | Delete a team |
| POST | `/teams/add-player` | Add a player to a team |
| POST | `/teams/remove-player` | Remove a player from a team |

---

### Matches — `/matches`

| Method | Endpoint | Description |
|---|---|---|
| POST | `/matches/host` | Create a new match |
| POST | `/matches/start` | Start a hosted match |
| POST | `/matches/end` | End or cancel a match |
| GET | `/matches/details` | Get match details |

**Hosting a match:**
```json
{
  "sessionToken": "...",
  "teamAId": "...",
  "teamBId": "...",
  "format": "T20",
  "totalOvers": 20,
  "plannedStartTime": 1748908800000,
  "parentMatchId": null
}
```
Set `totalOvers: 0` for a Test match (unlimited overs).
Set `parentMatchId` to link a super over back to its parent match.

---

### Toss — `/toss`

| Method | Endpoint | Description |
|---|---|---|
| POST | `/toss/conduct` | Record toss result and decision |
| GET | `/toss` | Get toss details for a match |
| GET | `/toss/flip` | Flip a coin (returns HEAD or TAIL) |

Must be conducted after match starts. `decision` is `BAT_FIRST` or `BOWL_FIRST`.

---

### Innings — `/innings`

| Method | Endpoint | Description |
|---|---|---|
| POST | `/innings/start` | Start a new innings |
| POST | `/innings/end` | End the current innings |

**Starting an innings:**
```json
{
  "sessionToken": "...",
  "matchId": "...",
  "battingTeamId": "...",
  "bowlingTeamId": "..."
}
```

Batting and bowling teams are provided explicitly — the server handles all formats (T20, ODI, Test, custom, super over) without special-casing. Follow-on is handled naturally by providing the same team again for innings 3.

**Innings ID format:** `{matchId}-INN-{inningsNumber}`

---

### Overs — `/overs`

| Method | Endpoint | Description |
|---|---|---|
| POST | `/overs/start` | Start a new over |
| POST | `/overs/end` | End the current over |

The bowler must belong to the bowling team. Over limit is enforced automatically for non-Test matches.

**Over ID format:** `{inningsId}-OVR-{overNumber}`

---

### Match Result — `/match-result`

| Method | Endpoint | Description |
|---|---|---|
| POST | `/match-result/complete` | Finalise match and calculate result |
| GET | `/match-result` | Get persisted result for a match |

Results are calculated from innings aggregates and persisted once — no recalculation on subsequent reads.

**Result types handled automatically:**
- Won by X wickets (chasing team wins in last innings)
- Won by X runs (defending team wins)
- Won by an innings and X runs (Test — winner only needed one innings)
- Match tied

---

## Match Lifecycle

```
Host Match (NOT_STARTED)
    │
    ▼
Start Match (IN_PROGRESS)
    │
    ▼
Conduct Toss
    │
    ▼
Start Innings ──► Start Over ──► End Over ──► (repeat)
    │
    ▼
End Innings
    │
    ▼
(repeat for remaining innings)
    │
    ▼
Complete Match ──► Result persisted (COMPLETED)
```

For **Test matches**, up to 4 innings are supported.
For **super overs**, create a new 1-over match with `parentMatchId` pointing to the tied match.

---

## Configuration Reference

```yaml
cricket:
  match:
    id-length: 12     # Length of generated match IDs
  team:
    id-length: 8      # Length of generated team IDs

session:
  duration:
    ms: 86400000      # Session TTL in milliseconds (default: 24h)
```

---

## Database Migrations

Migrations live in `src/main/resources/db/migration/` and run automatically on startup via Flyway.

| Version | Description |
|---|---|
| V1 | Initial schema |
| V2 | Lowercase table names + schema alignment |
| V3 | Drop status check constraint on matches |
| V4 | Add toss table |
| V5 | Add team/status indexes on matches |
| V6 | Add total_overs to matches |
| V7 | Add innings status and overs_completed |
| V8 | Add follow_on_enforced to matches |
| V9 | Cleanup + add parent_match_id |
| V10 | Create match_result table |
| V11 | Add over status and bowler |

> **Note:** Before first production deploy, V1–V11 will be consolidated into a single clean migration.

---

## Project Structure

```
src/main/java/com/gmh/cricket_app/
├── rest/          # Controllers (HTTP layer)
├── service/       # Business logic
├── repositories/  # Spring Data JPA repositories
├── models/        # JPA entities
├── dto/           # Request / response objects
├── enums/         # Enumerations
├── exceptions/    # Custom exceptions
├── config/        # Configuration classes
└── util/          # Utilities
```
