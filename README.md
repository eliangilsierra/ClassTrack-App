<div align="center">

# ClassTrack

**Sistema de gestión de asistencia académica para Android**

[![Android](https://img.shields.io/badge/Platform-Android-3DDC84?logo=android&logoColor=white)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-7F52FF?logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4?logo=jetpackcompose&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Min SDK](https://img.shields.io/badge/Min%20SDK-26%20(Android%208.0)-brightgreen)](https://developer.android.com/about/versions/oreo)
[![Version](https://img.shields.io/badge/Version-1.0-purple)](https://github.com)
[![License](https://img.shields.io/badge/License-MIT-blue)](LICENSE)

*Registro de asistencia sin fricciones mediante códigos QR y GPS.*

</div>

---

## Tabla de Contenidos

- [Descripción](#descripción)
- [Características](#características)
- [Tecnologías](#tecnologías)
- [Arquitectura](#arquitectura)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Primeros Pasos](#primeros-pasos)
- [Configuración del Backend](#configuración-del-backend)
- [Pantallas de la App](#pantallas-de-la-app)
- [Flujos Principales](#flujos-principales)
- [API REST](#api-rest)
- [Sistema de Diseño](#sistema-de-diseño)

---

## Descripción

**ClassTrack** es una aplicación Android nativa para gestión de asistencia académica universitaria. Elimina el pase de lista manual mediante un sistema de doble verificación: el estudiante escanea el QR de sesión del profesor, y la app captura su ubicación GPS para confirmar presencia física.

### Roles de usuario

| Rol | Acciones principales |
|---|---|
| 👨‍🏫 **Docente** | Crear cursos · Vincular estudiantes · Abrir sesiones QR · Ver reportes |
| 👨‍🎓 **Estudiante** | Mostrar QR de identificación · Escanear QR de sesión · Registrar asistencia con GPS |

---

## Características

### Para el Docente
- 📊 **Dashboard** con métricas en tiempo real (cursos activos, total de estudiantes)
- 📚 **Gestión de cursos** — crear, ver detalle y eliminar cursos
- 🔗 **Vinculación de estudiantes** — escanea el QR de identificación del estudiante para añadirlo al curso
- 📡 **Sesiones de asistencia** — genera un QR temporal con cuenta regresiva configurable (10–60 min) y monitorea asistentes en tiempo real
- 📈 **Reportes** — resumen por sesión (asistentes vs. ausentes) y resumen global por curso (tasa de asistencia)

### Para el Estudiante
- 🪪 **Identificación Digital** — QR permanente con datos del usuario, listo para vinculación
- ✅ **Check-in de asistencia** — escanea el QR de sesión del profesor; la app registra automáticamente la ubicación GPS
- 📱 **Dashboard** con acceso rápido al escáner de asistencia

### General
- 🔐 **Autenticación JWT** — registro e inicio de sesión con selección de rol (Docente / Estudiante)
- 🎨 **Modo claro y oscuro** — tokens de color completos para ambos temas
- 💾 **Caché offline** — los cursos se almacenan localmente en Room para consulta sin conexión

---

## Tecnologías

### Android (Mobile)

| Categoría | Librería / Herramienta | Versión |
|---|---|---|
| Lenguaje | Kotlin | 2.0.21 |
| UI | Jetpack Compose + Material 3 | BOM 2024.09.00 |
| Navegación | Navigation Compose (type-safe) | 2.8.5 |
| Inyección de dependencias | Hilt | 2.56.2 |
| Networking | Retrofit + OkHttp + kotlinx.serialization | 2.11.0 / 4.12.0 |
| Base de datos local | Room | 2.7.1 |
| Almacenamiento de sesión | DataStore Preferences | 1.1.1 |
| Cámara | CameraX | 1.4.1 |
| Escaneo QR | ML Kit Barcode Scanning | 17.3.0 |
| Generación QR | ZXing Core | 3.5.3 |
| GPS | Google Play Services Location | 21.3.0 |
| Permisos | Accompanist Permissions | 0.36.0 |
| Coroutines | Kotlinx Coroutines | 1.9.0 |
| Procesador anotaciones | KSP | 2.0.21-1.0.28 |

### Backend

| Elemento | Detalle |
|---|---|
| Framework | Spring Boot 3.5+ |
| Lenguaje | Java 21 |
| Base de datos | PostgreSQL |
| Autenticación | JWT (tokens de 24 horas) |
| Despliegue | Docker en Railway (puerto 8080) |
| Documentación API | Swagger UI en `/swagger-ui.html` |

---

## Arquitectura

ClassTrack sigue **Clean Architecture** con separación estricta en cuatro capas:

```
┌─────────────────────────────────────────┐
│           UI Layer                       │
│   Screens (Compose) + Components        │
└─────────────────┬───────────────────────┘
                  │  StateFlow / UiState
┌─────────────────▼───────────────────────┐
│         Presentation Layer               │
│   ViewModels (@HiltViewModel)           │
└─────────────────┬───────────────────────┘
                  │  UseCases
┌─────────────────▼───────────────────────┐
│           Domain Layer                   │
│   Use Cases + Domain Models             │
└─────────────────┬───────────────────────┘
                  │  Repository interfaces
┌─────────────────▼───────────────────────┐
│            Data Layer                    │
│   Repositories → Remote API / Room DB   │
└─────────────────────────────────────────┘
```

### Principios clave
- **Single Activity** — `MainActivity` aloja el `NavGraph` completo de Jetpack Compose
- **Type-safe Navigation** — `Destination` sealed class serializable, sin strings de ruta
- **Unidirectional Data Flow** — `UiState` inmutable expuesto vía `StateFlow`, eventos vía `SharedFlow`
- **Feature-first packaging** — cada feature es autónomo con sus propias capas
- **Offline-first en cursos** — Room como caché; la app funciona con datos locales si hay fallo de red

### Patrón de estado en ViewModels

```kotlin
@HiltViewModel
class FooViewModel @Inject constructor(...) : ViewModel() {
    private val _state = MutableStateFlow(FooUiState())
    val state: StateFlow<FooUiState> = _state.asStateFlow()

    private val _actions = MutableSharedFlow<FooAction>()
    val actions: SharedFlow<FooAction> = _actions.asSharedFlow()
}
```

---

## Estructura del Proyecto

```
app/src/main/java/me/egil_accamacho/classtrack/
│
├── core/
│   ├── common/          # Resource<T>, safeCall, ErrorMapper
│   ├── network/         # Retrofit, OkHttp, JWT Interceptor, ApiEnvelope
│   ├── location/        # FusedLocationProvider
│   ├── qr/              # BarcodeAnalyzer (ML Kit), QrEncoder (ZXing), QrParser
│   ├── permissions/     # CameraPermissionRequester
│   └── session/         # DataStoreSessionManager
│
├── di/                  # Hilt modules (NetworkModule, DatabaseModule, etc.)
│
├── navigation/
│   ├── Destinations.kt  # Sealed class con los 19 destinos de la app
│   └── NavGraph.kt      # Grafo de navegación completo
│
├── ui/
│   ├── components/      # Componentes reutilizables (CourseCard, QrDisplay, DigitalIdCard…)
│   └── theme/           # Color, Typography, Shape, CtSpacing tokens
│
└── features/
    ├── auth/            # Splash, Login, Register + JWT session
    ├── home/            # Dashboard Docente y Dashboard Estudiante
    ├── courses/         # Lista, crear, detalle y eliminar cursos
    ├── students/        # Vincular estudiante, escanear QR, éxito de vinculación
    ├── attendance/      # Crear sesión, QR con countdown, escáner, éxito, reportes
    ├── profile/         # Perfil, Identificación Digital
    └── reports/         # Reporte por sesión, reporte por curso
```

---

## Primeros Pasos

### Prerrequisitos

- **Android Studio** Hedgehog o superior
- **JDK 17**
- **Android SDK** con API 26–36
- Dispositivo físico o emulador con Google Play Services (necesario para GPS y ML Kit)

### Clonar e instalar

```bash
git clone https://github.com/tuusuario/classtrack.git
cd classtrack
```

### Configurar la URL del backend

Crea o edita `local.properties` en la raíz del proyecto:

```properties
BASE_URL=https://api.classtrack.app/api/v1/
```

> Si no se define, el build de `debug` apunta a la URL de producción por defecto.
> El build `staging` apunta a `http://54.209.230.174:8080/api/v1/` sin configuración adicional.

### Compilar y ejecutar

```powershell
# Build debug APK
.\gradlew assembleDebug

# Instalar en dispositivo conectado
.\gradlew installDebug

# Build staging (staging server)
.\gradlew assembleStagig

# Ejecutar tests unitarios
.\gradlew test

# Lint
.\gradlew lint
```

---

## Configuración del Backend

El backend es un monolito **Spring Boot 3.5+ / Java 21** desplegado en Railway.

### Variables de entorno requeridas

```env
DB_HOST=...
DB_PORT=5432
DB_NAME=classtrack
DB_USER=...
DB_PASSWORD=...

JWT_SECRET=...
JWT_EXPIRATION=86400000   # 24 horas en ms
```

### Despliegue con Docker

```dockerfile
# El servidor expone el puerto 8080
# Documentación Swagger disponible en:
# http://localhost:8080/swagger-ui.html
```

### Esquema de base de datos

5 tablas únicamente:

```
users               → id, full_name, email, password, role, student_code
courses             → id, name, description, teacher_id
course_students     → course_id, student_id
attendance_sessions → id, course_id, qr_token, expires_at, status
attendance_records  → id, session_id, student_id, latitude, longitude, registered_at
```

---

## Pantallas de la App

### Flujo de Autenticación
| Splash | Login | Registro |
|---|---|---|
| Verificación de sesión activa y redirección automática por rol | Email + contraseña + selección de rol | Nombre completo + email + contraseña + rol |

### Dashboard del Docente
- Métricas: cursos activos y total de estudiantes
- Lista de últimos 5 cursos con acceso rápido
- FAB para crear nuevo curso
- Acceso a lista completa de cursos

### Gestión de Cursos
- Lista de todos los cursos con tarjetas (nombre, estudiantes, tasa de asistencia)
- Detalle de curso: información, lista de estudiantes y acciones
- Crear curso: nombre y descripción
- Eliminar curso con diálogo de confirmación

### Vinculación de Estudiantes
1. Pantalla de instrucciones con contador de estudiantes vinculados
2. Scanner de cámara con overlay de encuadre
3. Pantalla de éxito con datos del estudiante vinculado
4. Opciones: "Escanear otro" o "Volver al curso"

### Sesión de Asistencia (Docente)
1. **Iniciar sesión** — seleccionar duración del QR (10 / 20 / 30 / 45 / 60 min)
2. **QR de asistencia** — código grande + cuenta regresiva con indicador de validez (verde → ámbar → rojo) + contador de asistentes en tiempo real
3. **Cerrar sesión** — con diálogo de confirmación → navega al reporte de sesión

### Check-in de Asistencia (Estudiante)
1. Scanner de cámara con solicitud de permisos (cámara + GPS)
2. Escaneo del QR de la sesión
3. Captura automática de coordenadas GPS
4. Pantalla de éxito con hora de registro

### Identificación Digital
- Tarjeta con avatar de iniciales, nombre completo, código/rol
- QR permanente `{"type":"USER","userId":X}` para vinculación por el docente

### Reportes
- **Por sesión**: total de inscritos, asistentes, ausentes, porcentaje
- **Por curso**: nombre, total de estudiantes, tasa de asistencia global

---

## Flujos Principales

### Flujo de Vinculación de Estudiante

```
Docente: Detalle de Curso → Vincular Estudiantes → Escáner QR
                                                        ↓
                              Estudiante muestra su Identificación Digital
                                                        ↓
                              QR parseado → {"type":"USER","userId":X}
                                                        ↓
                              POST /courses/{id}/students → Éxito
                                                        ↓
                              Pantalla de confirmación → Volver al Curso
                              (lista de estudiantes se actualiza automáticamente)
```

### Flujo de Registro de Asistencia

```
Docente: Detalle de Curso → Iniciar Sesión → Seleccionar duración
                                                  ↓
                              POST /attendance/sessions → QR generado
                                                  ↓
                              QR de sesión visible con countdown

Estudiante: Home → Registrar Asistencia → Scanner
                                              ↓
                          Escanea QR del profesor
                          QR parseado → {"type":"ATTENDANCE","sessionId":X,"token":"Y"}
                                              ↓
                          GPS capturado automáticamente
                                              ↓
                          POST /attendance/checkin → Éxito con hora de registro
```

---

## API REST

**Base URL:** `https://api.classtrack.app/api/v1`

Todas las respuestas usan el envelope estándar:
```json
{ "success": true, "message": null, "data": { ... } }
```

Los errores retornan:
```json
{ "success": false, "message": "Descripción del error", "errors": [] }
```

### Endpoints principales

| Método | Endpoint | Descripción | Rol |
|---|---|---|---|
| `POST` | `/auth/login` | Iniciar sesión | Público |
| `POST` | `/auth/register` | Registrar usuario | Público |
| `GET` | `/profile` | Perfil del usuario autenticado | Todos |
| `GET` | `/profile/digital-id` | ID digital con contenido QR | Todos |
| `GET` | `/courses` | Listar cursos del docente | Docente |
| `POST` | `/courses` | Crear curso | Docente |
| `GET` | `/courses/{id}` | Detalle de curso | Docente |
| `DELETE` | `/courses/{id}` | Eliminar curso | Docente |
| `GET` | `/courses/{id}/students` | Estudiantes del curso | Docente |
| `POST` | `/courses/{id}/students` | Vincular estudiante | Docente |
| `POST` | `/attendance/sessions` | Crear sesión de asistencia | Docente |
| `POST` | `/attendance/sessions/{id}/close` | Cerrar sesión | Docente |
| `POST` | `/attendance/checkin` | Registrar asistencia | Estudiante |
| `GET` | `/attendance/sessions/{id}/records` | Registros de sesión | Docente |
| `GET` | `/reports/sessions/{id}` | Reporte de sesión | Docente |
| `GET` | `/reports/courses/{id}` | Reporte de curso | Docente |

### Autenticación

JWT de 24 horas inyectado automáticamente por el interceptor OkHttp:
```
Authorization: Bearer <token>
```

Claims del token: `{ "userId": 1, "email": "...", "role": "TEACHER" }`

### Formatos de QR

```json
// Identificación de usuario (permanente — vinculación de estudiantes)
{"type": "USER", "userId": 15}

// Sesión de asistencia (efímero — check-in)
{"type": "ATTENDANCE", "sessionId": 100, "token": "ATT_ABC123"}
```

---

## Sistema de Diseño

### Paleta de Colores

| Token | Hex | Uso |
|---|---|---|
| Primary | `#7C3AED` | Botones principales, FAB, acciones clave |
| Primary Dark | `#6D28D9` | Estado presionado |
| Primary Light | `#A78BFA` | Indicadores secundarios |
| Success | `#10B981` | Asistencia confirmada, estados exitosos |
| Warning | `#F59E0B` | Alertas, QR próximo a expirar |
| Error | `#EF4444` | Errores de validación, QR expirado |

### Tipografía

Familia: **Roboto** (fallback: Inter)

| Estilo | Tamaño | Peso |
|---|---|---|
| Display Large | 32sp | Bold |
| Headline | 24sp | SemiBold |
| Title | 20sp | SemiBold |
| Body | 16sp | Regular |
| Label | 14sp | Medium |

### Espaciado

Grid de 8dp — múltiplos de 4: `4, 8, 12, 16, 24, 32, 48, 64dp`

### Radios de esquinas

- Primario: **16dp** (botones, cards, campos de texto)
- Secundario: **12dp** (cards de lista)
- FAB: circular

---

<div align="center">

Desarrollado con ❤️ por Elian Gil Accamacho

</div>
