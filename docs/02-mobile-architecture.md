# 02-mobile-architecture.md

# ClassTrack Android Architecture

Versión: 1.0

---

# Objetivo

Definir la arquitectura completa de la aplicación Android ClassTrack utilizando Kotlin, Jetpack Compose y MVVM.

Este documento servirá como referencia oficial para todo el desarrollo móvil.

---

# Stack Tecnológico

## Lenguaje

Kotlin

---

## UI

Jetpack Compose

Material Design 3

---

## Arquitectura

MVVM

Repository Pattern

Single Activity Architecture

---

## Navegación

Navigation Compose

---

## Inyección de Dependencias

Hilt

---

## Networking

Retrofit

OkHttp

Kotlin Serialization

---

## Persistencia Local

Room Database

DataStore Preferences

---

## Cámara

CameraX

---

## QR

ZXing

ML Kit Barcode Scanner

---

## Ubicación

FusedLocationProviderClient

Google Play Services Location

---

# Arquitectura General

```text
UI Layer
│
├── Screens
├── Components
├── Navigation
│
ViewModel Layer
│
├── State
├── Events
├── Actions
│
Domain Layer
│
├── UseCases
│
Data Layer
│
├── Repository
├── Remote Data Source
├── Local Data Source
│
Infrastructure
│
├── Retrofit
├── Room
├── DataStore
```

---

# Estructura de Carpetas

```text
app
│
├── core
│
├── navigation
│
├── ui
│
├── data
│
├── domain
│
├── features
│
└── di
```

---

# Core

Contiene elementos reutilizables.

```text
core
│
├── constants
├── extensions
├── utils
├── network
├── location
├── qr
├── designsystem
└── common
```

---

# Navigation

```text
navigation
│
├── NavGraph.kt
├── Destinations.kt
└── NavigationRoutes.kt
```

---

# UI

```text
ui
│
├── theme
│
├── components
│
├── states
│
└── previews
```

---

# Features

Cada módulo funcional será independiente.

```text
features
│
├── auth
├── home
├── courses
├── attendance
├── students
├── profile
└── reports
```

---

# Auth Module

```text
auth
│
├── presentation
├── domain
├── data
└── di
```

---

## Pantallas

* SplashScreen
* LoginScreen
* RegisterScreen

---

## ViewModels

```text
AuthViewModel
```

---

## Casos de Uso

```text
LoginUseCase
RegisterUseCase
LogoutUseCase
```

---

# Home Module

## Docente

HomeTeacherScreen

---

## Estudiante

HomeStudentScreen

---

## ViewModel

```text
HomeViewModel
```

---

# Courses Module

## Pantallas

```text
CoursesScreen

CreateCourseScreen

CourseDetailScreen
```

---

## ViewModels

```text
CoursesViewModel

CourseDetailViewModel
```

---

# Students Module

## Pantallas

```text
LinkStudentScreen

ScanStudentQrScreen

StudentLinkedScreen
```

---

## ViewModels

```text
StudentLinkViewModel
```

---

# Attendance Module

## Pantallas

```text
CreateAttendanceSessionScreen

GenerateAttendanceQrScreen

ScanAttendanceQrScreen

AttendanceSuccessScreen
```

---

## ViewModels

```text
AttendanceViewModel
```

---

# Profile Module

## Pantallas

```text
ProfileScreen

DigitalIdScreen
```

---

## ViewModels

```text
ProfileViewModel
```

---

# Reports Module

## Pantallas

```text
AttendanceReportScreen
```

---

## ViewModels

```text
ReportsViewModel
```

---

# Arquitectura MVVM

Cada pantalla debe implementar:

```text
Screen

ViewModel

UiState

UiEvent

UiAction
```

---

# Ejemplo

```kotlin
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val loading: Boolean = false,
    val error: String? = null
)
```

---

# Gestión de Estado

Usar:

```kotlin
StateFlow
```

Evitar:

```kotlin
LiveData
```

---

# Navegación

## Flujo Inicial

```text
Splash

↓

Login

↓

Home
```

---

## Flujo Docente

```text
Home

↓

Courses

↓

Course Detail

↓

Link Student

↓

Create Attendance

↓

Generate QR

↓

Report
```

---

## Flujo Estudiante

```text
Home

↓

Profile

↓

Digital ID

↓

Scan Attendance

↓

Success
```

---

# Manejo de Sesión

Guardar:

```text
JWT Token

User Id

Role
```

Utilizando:

```text
DataStore
```

---

# Roles

```kotlin
enum class UserRole {
    TEACHER,
    STUDENT
}
```

---

# Bottom Navigation

## Teacher

```text
Home

Courses

Profile
```

---

## Student

```text
Home

Profile
```

---

# Networking

Retrofit

---

## Base URL

```text
https://api.classtrack.app
```

---

# Interceptor JWT

Agregar automáticamente:

```http
Authorization: Bearer token
```

---

# QR Scanner

Responsabilidades:

* Leer QR estudiante
* Leer QR asistencia

---

# GPS

Capturar:

```text
latitude

longitude
```

al momento de registrar asistencia.

---

# Room Cache

Persistir:

* Usuario
* Cursos
* Historial

---

# Offline First

No requerido para MVP.

---

# Testing

## Unit Testing

ViewModels

Use Cases

---

## UI Testing

Flujos críticos

---

# Definition of Done

Cada pantalla deberá:

* Tener ViewModel
* Tener UiState
* Tener navegación funcional
* Manejar loading
* Manejar errores
* Consumir repositorio
* Ser compatible con Material 3
* Cumplir arquitectura MVVM

---

# Objetivo Final

Mantener una arquitectura limpia, modular y escalable que permita completar el proyecto académico rápidamente sin sacrificar buenas prácticas de desarrollo Android moderno.
