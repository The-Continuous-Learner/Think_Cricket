# Think Cricket

> Cricket app for nerds.

A REST API backend for managing cricket matches end-to-end — from registering players and teams, through toss and innings, all the way to ball-by-ball scoring and match results. Built with Spring Boot 4, MySQL, and Flyway.

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

## API Design

All requests use `Content-Type: application/json`.

Every protected endpoint requires a `sessionToken` field in the **request body**. Sessions expire after **24 hours**.

> Note: GET endpoints in this API also use a JSON request body. This is intentional and consistent across the entire codebase.

---

## Enums Reference

| Enum | Values |
|---|---|
| `MatchStatus` | `NOT_STARTED` `IN_PROGRESS` `COMPLETED` `ABANDONED` `CANCELLED` |
| `InningsStatus` | `ACTIVE` `COMPLETED` |
| `OverStatus` | `ACTIVE` `COMPLETED` |
| `TossResult` | `HEAD` `TAIL` |
| `TossDecision` | `BAT_FIRST` `BOWL_FIRST` |
| `ExtraType` | `WIDE` `NO_BALL` `BYE` `LEG_BYE` |
| `BoundaryType` | `FOUR` `SIX` |
| `WicketType` | `BOWLED` `CAUGHT` `RUN_OUT` `LBW` `STUMPED` `HIT_WICKET` `RETIRED_HURT` `OBSTRUCTING_FIELD` `TIMED_OUT` |
| `PlayerTypes` | `Batsman` `Baller` `AllRounder` `WicketKeeper` |
| `Gender` | `Male` `Female` |

---

## API Reference

### Auth — `/users`

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| POST | `/users/register` | — | Register a new user |
| POST | `/users/login` | — | Login and receive a session token |
| POST | `/users/logout` | Header | Invalidate session |
| POST | `/users/validateSession` | Header | Validate an active session |

> `/logout` and `/validateSession` use `Session-Token` request header, not request body.

**Login response:**
```json
{ "sessionToken": "", "userId": "", "username": "" }
```

---

### Players — `/players`

| Method | Endpoint | Description |
|---|---|---|
| POST | `/players/save` | Create a new player |
| GET | `/players/get` | Get a player by ID |
| GET | `/players/get-all` | List all players |
| GET | `/players/get-by-name` | Search players by name |

**Save player:**
```json
{
  "sessionToken": "",
  "name": "",
  "age": 25,
  "gender": "Male",
  "type": "Batsman"
}
```

---

### Teams — `/teams`

| Method | Endpoint | Description |
|---|---|---|
| POST | `/teams/create` | Create a team |
| POST | `/teams/modify` | Update team name or description |
| POST | `/teams/delete` | Delete a team |
| POST | `/teams/add-player` | Add a player to a team |
| POST | `/teams/remove-player` | Remove a player from a team |
| GET | `/teams/players` | List all players in a team |

**Get team players response:**
```json
{
  "teamId": "",
  "players": [{ "playerId": "", "name": "", "type": "Batsman" }]
}
```

---

### Matches — `/matches`

| Method | Endpoint | Description |
|---|---|---|
| POST | `/matches/host` | Create a new match |
| POST | `/matches/start` | Start a hosted match |
| POST | `/matches/end` | End or cancel a match |
| GET | `/matches/getDetails` | Get full match details |
| GET | `/matches/list` | List all matches hosted by the session user |
| GET | `/matches/live-state` | Get real-time match state (innings, over, last batsmen) |
| GET | `/matches/score` | Get full scorecard for a match |

**Host a match:**
```json
{
  "sessionToken": "",
  "teamAId": "",
  "teamBId": "",
  "format": "T20",
  "totalOvers": 20,
  "plannedStartTime": 1748908800000,
  "parentMatchId": null
}
```

Set `totalOvers: 0` for a Test match (unlimited overs).  
Set `parentMatchId` to link a super over back to its parent match.

**Live state response:**
```json
{
  "matchId": "",
  "format": "T20",
  "status": "IN_PROGRESS",
  "totalOvers": 20,
  "teamAId": "",
  "teamBId": "",
  "activeInnings": {
    "inningsId": "",
    "inningsNumber": 1,
    "battingTeamId": "",
    "bowlingTeamId": "",
    "status": "ACTIVE",
    "totalRuns": 45,
    "wickets": 2,
    "oversCompleted": 5,
    "extras": 3,
    "target": null
  },
  "activeOver": {
    "overId": "",
    "overNumber": 6,
    "bowlerId": "",
    "status": "ACTIVE",
    "legalBallCount": 3,
    "totalRuns": 8,
    "wickets": 0
  },
  "lastBatsmanId": "",
  "lastNonStrikerId": ""
}
```

`target` is `null` for innings 1; for innings 2 in limited-overs it is `firstInningsRuns + 1`.

---

### Toss — `/toss`

| Method | Endpoint | Description |
|---|---|---|
| POST | `/toss/conduct` | Record toss result and decision |
| GET | `/toss` | Get toss details for a match |
| GET | `/toss/flip` | Flip a coin (returns `HEAD` or `TAIL`) |

**Conduct toss:**
```json
{
  "sessionToken": "",
  "matchId": "",
  "tossResult": "HEAD",
  "winnerTeamId": "",
  "decision": "BAT_FIRST"
}
```

---

### Innings — `/innings`

| Method | Endpoint | Description |
|---|---|---|
| POST | `/innings/start` | Start a new innings |
| POST | `/innings/end` | End the current innings |

**Start innings:**
```json
{
  "sessionToken": "",
  "matchId": "",
  "battingTeamId": "",
  "bowlingTeamId": ""
}
```

Batting and bowling teams are always provided explicitly. All formats (T20, ODI, Test, super over, follow-on) are handled without special-casing.

**Auto-end:** An innings ends automatically when:
- 10 wickets fall
- The over limit is reached (limited-overs only)
- The target is chased (innings 2, limited-overs only)

---

### Overs — `/overs`

| Method | Endpoint | Description |
|---|---|---|
| POST | `/overs/start` | Start a new over, assign a bowler |
| POST | `/overs/end` | End the current over |
| GET | `/overs/balls` | Get ball-by-ball breakdown of an over |

**Start over:**
```json
{ "sessionToken": "", "inningsId": "", "bowlerId": "" }
```

**Auto-end:** An over completes automatically after 6 legal deliveries.

---

### Balls — `/balls`

| Method | Endpoint | Description |
|---|---|---|
| POST | `/balls/record` | Record a single delivery |

**Record a ball:**
```json
{
  "sessionToken": "",
  "overId": "",
  "batsmanId": "",
  "nonStrikerId": "",
  "runs": 0,
  "extraRuns": 0,
  "extraType": null,
  "boundaryType": null,
  "bowlerId": null,
  "wicket": false
}
```

- `runs` — runs off the bat (0–6)
- `extraRuns` — wides, no-ball penalties, byes, or leg-byes
- `extraType` — `WIDE` / `NO_BALL` / `BYE` / `LEG_BYE` / `null` for normal delivery
- `boundaryType` — `FOUR` / `SIX` / `null`
- `bowlerId` — optional override; defaults to the over's assigned bowler
- `wicket` — set `true` if a wicket fell; follow up immediately with `POST /wickets/record`

**Response:**
```json
{
  "ballId": "",
  "overId": "",
  "inningsId": "",
  "ballNumber": 3,
  "legalDelivery": true,
  "runs": 4,
  "extraRuns": 0,
  "extraType": null,
  "boundaryType": "FOUR",
  "wicket": false,
  "legalBallsInOver": 3,
  "overCompleted": false,
  "inningsCompleted": false
}
```

Check `overCompleted` and `inningsCompleted` in the response — no need to call `/overs/end` or `/innings/end` when these are `true`.

---

### Wickets — `/wickets`

| Method | Endpoint | Description |
|---|---|---|
| POST | `/wickets/record` | Record wicket details for a ball |

**This is a two-step flow.** First call `POST /balls/record` with `wicket: true`, then immediately call this endpoint with the returned `ballId`.

**Record wicket:**
```json
{
  "sessionToken": "",
  "ballId": "",
  "playerOutId": "",
  "type": "CAUGHT",
  "bowlerId": "",
  "fielderId": ""
}
```

- `bowlerId` — set `null` for run-outs, obstructing field, etc.
- `fielderId` — set `null` if no fielder involved

**Response:**
```json
{
  "wicketId": "",
  "ballId": "",
  "inningsId": "",
  "playerOutId": "",
  "type": "CAUGHT",
  "wicketNumber": 3,
  "teamScoreAtFall": 87
}
```

---

### Match Result — `/match-result`

| Method | Endpoint | Description |
|---|---|---|
| POST | `/match-result/complete` | Calculate and persist the match result |
| GET | `/match-result` | Get the persisted result for a match |

Results are calculated once and stored. Subsequent reads return the stored result.

**Result types handled automatically:**
- Won by X wickets (chasing team wins)
- Won by X runs (defending team wins)
- Won by an innings and X runs (Test match)
- Match tied

---

## Match Lifecycle

```
Host Match  →  NOT_STARTED
     │
     ▼
Start Match  →  IN_PROGRESS
     │
     ▼
Conduct Toss
     │
     ▼
Start Innings
     │
     ▼
Start Over (assign bowler)
     │
     ▼
Record Ball  ──►  [if wicket: true]  ──►  Record Wicket
     │
     │  (repeat per ball)
     │
     ▼
Over auto-completes after 6 legal balls
     │
     ▼
Start next Over  (repeat)
     │
     ▼
Innings auto-ends on: 10 wickets / over limit / target chased
     │
     ▼
Start 2nd Innings  (repeat from Over step)
     │
     ▼
Complete Match  →  Result persisted
     │
     ▼
End Match  →  COMPLETED
```

---

## Configuration Reference

```yaml
cricket:
  match:
    id-length: 12
  team:
    id-length: 8

session:
  duration:
    ms: 86400000
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
| V12 | Add ball extra_runs, boundary_type, bowler_id |

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
