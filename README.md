# Think Cricket

> Cricket app for nerds.

A REST API backend for managing cricket matches end-to-end — from registering players and teams, through toss and squad declaration, all the way to ball-by-ball scoring, wickets, and match results.

---

## Live App

**[https://think-cricket-frontend.vercel.app](https://think-cricket-frontend.vercel.app)**

Register an account, create teams, add players, host a match, and score it ball by ball.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Framework | Spring Boot 4.0.2 |
| Language | Java 17 |
| Database | PostgreSQL (Supabase in production) |
| ORM | Spring Data JPA / Hibernate |
| Migrations | Flyway |
| Cache | Caffeine (adapter pattern — Redis-swappable) |
| Build | Maven |
| Utilities | Lombok |

---

## Getting Started

### Prerequisites

- Java 17+
- PostgreSQL 14+ (or Docker)
- Maven 3.8+

### Database Setup

**Docker (quickest):**
```bash
docker run --name cricket-db -e POSTGRES_PASSWORD=password -e POSTGRES_DB=cricket -p 5432:5432 -d postgres:16
```

**Or local PostgreSQL install** — create a database named `cricket`.

### Configuration

Set these environment variables before running (or export them in your shell):

```bash
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/cricket
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=password
```

If no env vars are set, the app falls back to:
- URL: `jdbc:postgresql://localhost:5432/cricket`
- Username: `postgres`
- Password: *(empty)*

### Run

```bash
mvn spring-boot:run
```

Flyway runs all 17 migrations automatically on first boot — no manual schema setup required.

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
| `PlayerTypes` | `Batsman` `Bowler` `AllRounder` `WicketKeeper` |
| `Gender` | `Male` `Female` |
| `PlayerRole` | `PLAYING` `SUBSTITUTE` |

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

### Squad — `/squad`

Squads must be declared for both teams before an innings can start.

| Method | Endpoint | Description |
|---|---|---|
| POST | `/squad/declare` | Declare the squad for a team |
| POST | `/squad/substitute` | Record a player substitution |
| GET | `/squad/get` | Get the declared squad for a team |
| GET | `/squad/current-xi` | Get the current playing XI (after substitutions) |

**Declare squad:**
```json
{
  "sessionToken": "",
  "matchId": "",
  "teamId": "",
  "players": [
    { "playerId": "", "role": "PLAYING", "captain": false, "viceCaptain": false }
  ]
}
```

**Validation rules:**
- Exactly **11** players must have `role: PLAYING`
- Total squad size cannot exceed **15** players (configurable)
- Exactly **1** captain must be declared
- Captain and vice-captain must be different players
- All players must belong to the team

**Get squad / current XI response:**
```json
{
  "matchId": "",
  "teamId": "",
  "players": [
    { "playerId": "", "name": "", "role": "PLAYING", "captain": true, "viceCaptain": false }
  ]
}
```

`/squad/current-xi` reflects any substitutions applied on top of the declared squad.

---

### Innings — `/innings`

| Method | Endpoint | Description |
|---|---|---|
| POST | `/innings/start` | Start a new innings |
| POST | `/innings/end` | End the current innings |
| GET | `/innings/list` | Get all innings for a match (lightweight, cached) |
| GET | `/innings/scorecard` | Get full scorecard per innings (cached for completed matches) |
| POST | `/innings/set-batsmen` | Declare the current striker and non-striker |
| GET | `/innings/current-batsmen` | Get who is currently at the crease |
| GET | `/innings/eligible-batsmen` | Get players eligible to come in next |

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

When an innings ends (manually or automatically), any playing XI batsmen who did not face a single ball are automatically added to the scorecard with `0 (0b) not out`.

**Set batsmen** — call this after a wicket falls, before the next ball:
```json
{
  "sessionToken": "",
  "inningsId": "",
  "strikerId": "",
  "nonStrikerId": ""
}
```

**Current batsmen response:**
```json
{
  "inningsId": "",
  "strikerId": "",
  "strikerName": "",
  "nonStrikerId": "",
  "nonStrikerName": ""
}
```

**Eligible batsmen response** — playing XI minus dismissed minus currently at crease:
```json
[
  { "playerId": "", "playerName": "" }
]
```

**Innings list response:**
```json
[
  {
    "inningsId": "",
    "inningsNumber": 1,
    "battingTeamId": "",
    "battingTeamName": "",
    "bowlingTeamId": "",
    "bowlingTeamName": "",
    "status": "COMPLETED",
    "totalRuns": 145,
    "wickets": 6,
    "oversCompleted": 20,
    "extras": 8,
    "target": null
  }
]
```

**Scorecard response:**
```json
{
  "matchId": "",
  "matchStatus": "COMPLETED",
  "resultText": "Australia won by 4 wickets",
  "innings": [
    {
      "inningsId": "",
      "inningsNumber": 1,
      "battingTeamId": "",
      "bowlingTeamId": "",
      "batting": [...],
      "bowling": [...],
      "fallOfWickets": [...],
      "totalRuns": 145,
      "wickets": 6,
      "oversCompleted": 20
    }
  ]
}
```

**Result text formats:**
- `"<Team> won by X wickets"` — chasing team wins
- `"<Team> won by X runs"` — defending team wins
- `"<Team> won by an innings and X runs"` — Test match
- `"Match tied"`
- `"No result"` — match completed with fewer than 2 innings

**Caching:**
- `/innings/list` — cached per match with a **15-second TTL** (configurable); evicted immediately on every ball recorded
- `/innings/scorecard` — cached indefinitely per match once status is `COMPLETED`; bypassed for live matches

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

**Validation:** A bowler cannot bowl consecutive overs.

**Auto-end:** An over completes automatically after 6 legal deliveries.

**Balls response:**
```json
{
  "overId": "",
  "overNumber": 3,
  "inningsId": "",
  "bowlerId": "",
  "bowlerName": "",
  "status": "ACTIVE",
  "legalBallCount": 4,
  "totalRuns": 9,
  "wickets": 1,
  "balls": [...]
}
```

---

### Balls — `/balls`

| Method | Endpoint | Description |
|---|---|---|
| POST | `/balls/record` | Record a single delivery |
| POST | `/balls/undo` | Undo the last recorded ball |

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

**Record ball response:**
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

Check `overCompleted` and `inningsCompleted` — no need to call `/overs/end` or `/innings/end` when these are `true`.

**Undo last ball** — atomically reverses the last delivery in the innings:
```json
{ "sessionToken": "", "inningsId": "" }
```

**Undo response:**
```json
{
  "undoneBallId": "",
  "inningsId": "",
  "overReopened": false,
  "inningsReopened": false,
  "wicketReversed": false
}
```

- `overReopened: true` — the ball was the last ball of a completed over; the over is back to `ACTIVE`
- `inningsReopened: true` — the ball had auto-completed the innings; innings is back to `ACTIVE`
- `wicketReversed: true` — the wicket details (`/wickets/record`) were also rolled back; the dismissed batsman is restored

Undo also reverses maiden credit, batting/bowling aggregates, fall of wicket, and current batting state — all atomically.

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

Fall of wickets are stored as `completedOvers.legalBallInOver` notation (e.g. `0.6` = last ball of over 1).

---

### Match Result — `/match-result`

| Method | Endpoint | Description |
|---|---|---|
| POST | `/match-result/complete` | Calculate and persist the match result |
| GET | `/match-result` | Get the persisted result for a match |

Results are calculated once and stored. Subsequent reads return the stored result. A match can be completed at any point — including before any innings are played (abandoned after toss), which produces `"No result"`.

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
Declare Squads (both teams, before first innings)
     │
     ▼
Start Innings
     │
     ▼
Set Batsmen (declare opener striker + non-striker)
     │
     ▼
Start Over (assign bowler — cannot bowl consecutive overs)
     │
     ▼
Record Ball  ──►  [if wicket: true]  ──►  Record Wicket
     │                                          │
     │                                          ▼
     │                                   Set Batsmen (new batsman in)
     │
     │  (repeat per ball; undo available at any point)
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
Start 2nd Innings  (repeat from Set Batsmen step)
     │
     ▼
Complete Match  →  Result persisted
     │
     ▼
End Match  →  COMPLETED
```

---

## Current Batting State

The API maintains a persistent record of who is at the crease, stored in the `current_batting_state` table. This solves the problem of frontend state being lost on refresh — the UI can always recover the current pair from the server.

| Endpoint | When to call |
|---|---|
| `POST /innings/set-batsmen` | After innings start (openers), after every wicket (new batsman in) |
| `GET /innings/current-batsmen` | On page load / refresh to restore scorer state |
| `GET /innings/eligible-batsmen` | To populate the "select next batsman" dropdown |

`POST /balls/record` also updates the batting state on every delivery, so if the app never crashes between balls the state stays current automatically.

---

## Undo Last Ball

`POST /balls/undo` reverses the last recorded delivery in an innings atomically in a single transaction. Everything rolled back:

- Ball deleted
- Innings aggregates (runs, extras, wickets) decremented
- Over aggregates (runs, wickets, legal ball count) decremented
- Batting score (runs, balls, fours, sixes, strike rate) decremented
- Bowling score (runs conceded, balls bowled, overs, economy) decremented
- Maiden credit reversed if the over was a maiden when completed
- Wicket details (`Wicket`, `FallOfWicket` rows) deleted if `/wickets/record` was called
- Dismissed batsman restored (`BattingScore.isOut` set back to `false`)
- Current batting state restored to the ball's batsmanId/nonStrikerId
- Innings and over statuses restored if they were auto-completed by that ball
- Caches evicted

---

## Caching

The two most-read endpoints are cached using Caffeine (in-memory). The adapter pattern (`InningsListCache` / `ScorecardCache` interfaces) allows the backing store to be swapped to Redis for multi-instance deployments without changing any service code.

| Endpoint | Cache | Strategy |
|---|---|---|
| `GET /innings/list` | `InningsListCache` | Short TTL (15s) + evict on every ball recorded or undo |
| `GET /innings/scorecard` | `ScorecardCache` | Permanent (no TTL) for `COMPLETED` matches; bypassed for live matches; evicted on undo if innings was reopened |

To migrate to Redis: implement `ScorecardCache` and `InningsListCache` as Redis-backed beans and swap the `@Component` annotation.

---

## Configuration Reference

All cricket-specific rules are externalised to `application.yaml` — no code changes needed to adjust them.

```yaml
session:
  duration:
    ms: 86400000        # Session TTL (24 hours)

cricket:
  match:
    id-length: 12       # Generated match ID length
  team:
    id-length: 8        # Generated team ID length
  squad:
    playing-xi-size: 11 # Required number of PLAYING role players per squad
    max-size: 15        # Maximum total squad size (playing + substitutes)
  cache:
    scorecard:
      max-size: 500     # Max completed scorecards held in memory
      ttl-days: 7       # Eviction TTL for scorecard cache entries
    innings-list:
      max-size: 100     # Max innings list entries held in memory
      ttl-seconds: 15   # Eviction TTL for live innings list cache
```

---

## Database Migrations

Migrations live in `src/main/resources/db/migration/` and run automatically on startup via Flyway.

| Version | Description |
|---|---|
| V1 | Initial schema |
| V2 | Table renames + schema alignment |
| V3 | Drop status check constraint on matches |
| V4 | Add toss table |
| V5 | Add team/status indexes on matches |
| V6 | Add total_overs to matches |
| V7 | Add innings status and overs_completed |
| V8 | Add follow_on_enforced to matches |
| V9 | Cleanup + add parent_match_id |
| V10 | Create match_result table |
| V11 | Add over status and bowler |
| V12 | Add ball extra_runs, boundary_type, bowler_id; over legal_ball_count |
| V13 | Add created_by_user_id to player and team |
| V14 | Add match_squad and match_substitution tables |
| V15 | Add captain and vice-captain to match_squad |
| V16 | Fix squad foreign key to matches table |
| V17 | Add current_batting_state table |

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
├── cache/         # Cache interfaces and Caffeine implementations
├── config/        # Configuration classes
└── util/          # Utilities
```
