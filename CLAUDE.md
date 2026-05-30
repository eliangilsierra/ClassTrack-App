# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

ClassTrack is an Android academic attendance management app. It uses QR scanning and GPS for check-in. The project has two actor roles: **Teacher** (creates courses, links students, opens sessions, views reports) and **Student** (shows their QR for linking, scans the session QR to check in).

The authoritative design documents live in `docs/`. The most important are:
- `docs/08-claude-code-context.md` — master context and hard constraints
- `docs/04-api-contracts.md` — locked API contracts; do not deviate
- `docs/02-mobile-architecture.md` — Android module and layer layout
- `docs/07-database-model.md` — DB schema (5 tables only)

## Build Commands

```powershell
# Build debug APK
.\gradlew assembleDebug

# Run unit tests
.\gradlew test

# Run instrumented tests (requires connected device/emulator)
.\gradlew connectedAndroidTest

# Run a single test class
.\gradlew test --tests "me.egil_accamacho.classtrack.ExampleUnitTest"

# Lint
.\gradlew lint

# Install on connected device
.\gradlew installDebug
```

## Android Architecture

Single-Activity app. `MainActivity` hosts the Compose `NavGraph`.

**Layer order (top → bottom):**
```
UI (Screens + Components)  →  ViewModel (StateFlow + UiState)  →  Domain (UseCases)  →  Data (Repository → RemoteDataSource / LocalDataSource)
```

**Feature modules** under `app/src/main/java/me/egil_accamacho/classtrack/features/`:
- `auth` — Splash, Login, Register + `AuthViewModel`
- `home` — role-split Home screens + `HomeViewModel`
- `courses` — list, create, detail + `CoursesViewModel`, `CourseDetailViewModel`
- `students` — link flow, scan student QR + `StudentLinkViewModel`
- `attendance` — create session, generate QR, scan QR, success + `AttendanceViewModel`
- `profile` — profile screen, Digital ID (permanent user QR) + `ProfileViewModel`
- `reports` — session/course/student reports + `ReportsViewModel`

**Cross-cutting packages:**
- `core/` — constants, extensions, network (Retrofit + JWT interceptor), location (FusedLocationProvider), qr (CameraX + ZXing/ML Kit), designsystem, common utilities
- `navigation/` — `NavGraph.kt`, `Destinations.kt`, `NavigationRoutes.kt`
- `di/` — Hilt modules

## Tech Stack (to be added to `app/build.gradle.kts`)

The `gradle/libs.versions.toml` currently only contains base Compose dependencies. These libraries must be added as the features are built:

| Purpose | Library |
|---|---|
| DI | Hilt |
| Navigation | Navigation Compose |
| Networking | Retrofit + OkHttp + Kotlin Serialization |
| Local DB | Room |
| Session storage | DataStore Preferences |
| Camera / QR scan | CameraX + ZXing or ML Kit Barcode |
| GPS | Google Play Services Location |
| QR generation | ZXing (`core`) |

## State Management Rules

Every screen must have a `UiState` data class with at minimum `loading: Boolean`, `error: String?`, and content fields. Expose it via `StateFlow`. Never use `LiveData`.

```kotlin
// Pattern every ViewModel must follow
@HiltViewModel
class FooViewModel @Inject constructor(...) : ViewModel() {
    private val _uiState = MutableStateFlow(FooUiState())
    val uiState: StateFlow<FooUiState> = _uiState.asStateFlow()
}
```

## API Contracts

Base URL: `https://api.classtrack.app/api/v1`

All responses wrap data in `{"success": true, "data": {...}}`. Do not change endpoint paths, request bodies, or response shapes — they are frozen in `docs/04-api-contracts.md`.

JWT is stored in DataStore and injected by an OkHttp interceptor into every protected request as `Authorization: Bearer <token>`.

## QR Payloads

Two types — parse and validate the `type` field before acting:

```json
// User identification (permanent, used for student linking)
{"type": "USER", "userId": 15}

// Attendance session (ephemeral, used for check-in)
{"type": "ATTENDANCE", "sessionId": 100, "token": "ATT_ABC123"}
```

## Hard Constraints

Do **not** implement: NFC, push notifications, WebSockets, multiple microservices, facial recognition, chat, grades, or any LMS feature.

GPS is used **only** at attendance check-in to capture `latitude` and `longitude`. Do not use it for continuous tracking.

The database has exactly 5 tables: `users`, `courses`, `course_students`, `attendance_sessions`, `attendance_records`. Do not add tables without a justified technical reason.

## Backend Architecture (Spring Boot)

The backend is a separate Spring Boot 3.5+ / Java 21 monolith. Base package: `com.classtrack`.

**Layer order:** `Controller → Service → Repository → Entity`

**Domain packages** (each contains Controller, Service, Repository, DTOs):
- `auth` — register/login, JWT issuance (`JwtService`, `UserDetailsServiceImpl`)
- `users` — profile, digital ID
- `courses` — CRUD, student enrollment
- `students` — course-student relationship management
- `attendance` — session creation, QR token generation, check-in
- `reports` — session/course/student attendance summaries

**Cross-cutting packages:** `common`, `security`, `config`, `exception`, `infrastructure`

**Key conventions:**
- Never expose JPA entities directly — always use Request/Response DTOs (`CreateCourseRequest`, `CourseResponse`, etc.)
- Use `@Transactional` on all write operations in service layer
- Use `GlobalExceptionHandler` with custom exceptions: `NotFoundException`, `BadRequestException`, `UnauthorizedException`, `ConflictException`
- All entities have `createdAt` / `updatedAt` audit fields
- Use `@Slf4j` for logging

**JWT:** 24-hour access tokens. Claims: `{"userId": 1, "email": "...", "role": "TEACHER"}`.

**Required environment variables:**
```
DB_HOST, DB_PORT, DB_NAME, DB_USER, DB_PASSWORD
JWT_SECRET, JWT_EXPIRATION
```

**Deployment:** Docker on Railway at port `8080`. Swagger at `/swagger-ui.html`.

## Design System & Brand Identity

Source of truth: `docs/Manual_Marca_ClassTrack.md`.

### Color Palette

| Role | Hex | Usage |
|---|---|---|
| Primary | `#7C3AED` | Primary buttons, FAB, key actions |
| Primary Dark | `#6D28D9` | Pressed / dark variant |
| Primary Light | `#A78BFA` | Secondary indicators |
| Secondary | `#8B5CF6` | Secondary elements |
| Success | `#10B981` | Attendance confirmed, success states |
| Warning | `#F59E0B` | Alerts, reminders |
| Error | `#EF4444` | Validation errors |

**Light theme tokens:**

| Token | Hex |
|---|---|
| Background | `#F8FAFC` |
| Surface | `#FFFFFF` |
| Surface Variant | `#F1F5F9` |
| Border | `#E2E8F0` |
| Primary Text | `#0F172A` |
| Secondary Text | `#475569` |

**Dark theme tokens:**

| Token | Hex |
|---|---|
| Background | `#0F172A` |
| Surface | `#1E293B` |
| Surface Variant | `#334155` |
| Primary Text | `#F8FAFC` |
| Secondary Text | `#CBD5E1` |

### Typography

Font family: **Roboto** (fallback: Inter).

| Style | Size | Weight |
|---|---|---|
| Display Large | 32sp | Bold |
| Headline | 24sp | SemiBold |
| Title | 20sp | SemiBold |
| Body | 16sp | Regular |
| Label | 14sp | Medium |

### Spacing

8dp grid — use multiples of 4: `4, 8, 12, 16, 24, 32, 48, 64dp`.

### Shape & Corner Radius

- Primary radius: **16dp** (buttons, cards, text fields)
- Secondary radius: **12dp**
- FAB: circular

### Component Specs

- **Primary button:** background `#7C3AED`, white text, radius 16dp, height 56dp
- **Secondary button:** outlined, border `#7C3AED`
- **TextFields:** Material 3 Outlined style, radius 16dp
- **Cards:** low elevation, radius 16dp, padding 16dp — use for courses, students, reports, sessions
- **Icons:** Material Symbols Rounded only — avoid complex iconography
- **Animations:** 200–300ms transitions; use Skeleton Loading for async content

### QR Display Rules

The session attendance QR must show: large centered code + visual countdown timer + validity/expiration indicator. It must convey security and reliability.

### Lists & Dashboards

- Use **Cards** instead of plain list rows. Each item: title + secondary info + primary action.
- Dashboards follow Stripe-style: metric cards, clear hierarchy, quick stats. Never use complex tables.

### UI Copy Tone

Professional, clear, direct. Examples:
- ✅ "Asistencia registrada correctamente" / "Curso creado exitosamente"
- ❌ "¡Genial! ¡Lo lograste!" / "Oops, algo salió mal"

## Testing Priority

- **ViewModel unit tests** (Android) — highest priority
- **Service-layer tests** (Spring Boot backend) — highest priority
- High coverage is not required; critical business flows must be covered
