# 08-claude-code-context.md

# ClassTrack - Master Context Document

Versión: 1.0

---

# PROPÓSITO DEL DOCUMENTO

Este documento es la fuente única de verdad para cualquier agente de IA utilizado durante el desarrollo del proyecto.

Aplica para:

* Claude Code
* Cursor
* GitHub Copilot
* ChatGPT
* Gemini Code Assist
* Windsurf

Toda generación de código deberá respetar las reglas y restricciones definidas aquí.

---

# RESUMEN DEL PROYECTO

ClassTrack es una plataforma móvil Android para gestión académica de asistencia mediante:

* Identificación digital QR
* Vinculación de estudiantes mediante QR
* Registro de asistencia mediante QR
* Geolocalización
* Backend Spring Boot
* PostgreSQL
* JWT Authentication

El objetivo es digitalizar el proceso de control de asistencia académica.

---

# STACK OFICIAL

## Mobile

Kotlin

Jetpack Compose

Material Design 3

Navigation Compose

MVVM

Hilt

Retrofit

Room

DataStore

CameraX

Google Location Services

---

## Backend

Spring Boot 3

Java 21

Spring Security

JWT

PostgreSQL

OpenAPI

Swagger

---

## Infraestructura

Docker

Railway

GitHub

---

# RESTRICCIONES DEL PROYECTO

NO implementar:

* NFC
* IA
* Notificaciones Push
* LMS
* Calificaciones
* Chat
* Videollamadas
* Reconocimiento Facial
* WebSockets
* Microservicios múltiples

El proyecto utilizará un único backend monolítico Spring Boot.

---

# ROLES

## Teacher

Puede:

* Crear cursos
* Gestionar cursos
* Vincular estudiantes
* Crear sesiones
* Consultar reportes

No puede:

* Registrar asistencia

---

## Student

Puede:

* Consultar cursos
* Ver QR digital
* Registrar asistencia

No puede:

* Crear cursos
* Crear sesiones

---

# NAVEGACIÓN FINAL

## Teacher

Bottom Navigation:

```text id="w4z6k3"
Home

Courses

Profile
```

---

## Student

Bottom Navigation:

```text id="9j9w0u"
Home

Profile
```

---

# FLUJO PRINCIPAL DOCENTE

```text id="lbj8n0"
Login

↓

Home

↓

Courses

↓

Course Detail

↓

Link Students

↓

Scan Student QR

↓

Success

↓

Create Session

↓

Attendance QR

↓

Report
```

---

# FLUJO PRINCIPAL ESTUDIANTE

```text id="n7lgxw"
Login

↓

Home

↓

Profile

↓

Digital ID

↓

Attendance Scanner

↓

Attendance Success
```

---

# ARQUITECTURA ANDROID

Debe utilizar:

```text id="6n7hwl"
MVVM
```

---

Cada feature debe contener:

```text id="e1nt2j"
presentation

domain

data
```

---

Cada pantalla debe tener:

```text id="4ubjfe"
Screen

ViewModel

UiState
```

---

NO utilizar:

```text id="d3l4y7"
MVP

MVC

LiveData
```

---

Utilizar:

```text id="bx1twl"
StateFlow
```

---

# ESTRUCTURA ANDROID

```text id="6nll0j"
features

auth

home

courses

students

attendance

profile

reports
```

---

# ESTRUCTURA BACKEND

```text id="v5h8yr"
auth

users

courses

students

attendance

reports
```

---

# BASE DE DATOS

Tablas oficiales:

```text id="5j6h72"
users

courses

course_students

attendance_sessions

attendance_records
```

---

NO crear tablas adicionales salvo necesidad técnica justificada.

---

# QR DE IDENTIFICACIÓN

Cada usuario tendrá un QR permanente.

Contenido:

```json id="uy1l7i"
{
  "type": "USER",
  "userId": 15
}
```

---

Uso:

* Vinculación de estudiantes

---

# QR DE ASISTENCIA

Generado por sesión.

Contenido:

```json id="rftx9k"
{
  "type": "ATTENDANCE",
  "sessionId": 100,
  "token": "ATT_ABC123"
}
```

---

Uso:

* Registro de asistencia

---

# REGLAS DE NEGOCIO

## REGISTRO

Todo usuario debe:

* Nombre
* Email
* Contraseña
* Rol

---

## LOGIN

Debe devolver:

* JWT
* UserId
* Role

---

## CURSOS

Solo docentes pueden crear cursos.

---

## VINCULACIÓN

Solo docentes pueden vincular estudiantes.

---

Un estudiante puede pertenecer a múltiples cursos.

---

## ASISTENCIA

Un estudiante solo puede registrar asistencia una vez por sesión.

---

La asistencia debe almacenar:

* Fecha
* Hora
* Latitud
* Longitud

---

# API OFICIAL

Todos los contratos definidos en:

```text id="ikfr1f"
04-api-contracts.md
```

deben respetarse estrictamente.

---

# PERSISTENCIA LOCAL

Android debe almacenar:

```text id="r9n1v8"
JWT

UserId

Role
```

utilizando:

```text id="f6p5ne"
DataStore
```

---

# CACHÉ LOCAL

Room puede almacenar:

```text id="o9wyul"
Courses

Profile

Attendance History
```

---

# GPS

El GPS solo se utiliza para:

```text id="vw6b1f"
Attendance Check-In
```

---

NO utilizar GPS para:

* Tracking
* Monitoreo continuo

---

# CÁMARA

La cámara solo se utiliza para:

```text id="zsj5mr"
Scan Student QR

Scan Attendance QR
```

---

# AUTENTICACIÓN

JWT obligatorio.

---

Spring Security obligatorio.

---

Password Hash:

```text id="sdysdn"
BCrypt
```

---

# DEPLOYMENT

Backend:

```text id="omvafn"
Railway
```

---

Database:

```text id="gdn86k"
Railway PostgreSQL
```

---

Container:

```text id="f4vx6i"
Docker
```

---

# SWAGGER

Debe estar habilitado.

Ruta esperada:

```text id="4ymk8d"
/swagger-ui.html
```

---

# CONVENCIONES DE CÓDIGO

## Kotlin

Seguir:

* Clean Code
* SOLID
* Kotlin Style Guide

---

## Java

Seguir:

* Spring Best Practices

---

# NOMENCLATURA

## ViewModels

```text id="l2m3ki"
AuthViewModel

CoursesViewModel

AttendanceViewModel
```

---

## Services

```text id="f7clyq"
AuthService

CourseService

AttendanceService
```

---

## Controllers

```text id="z6c58l"
AuthController

CourseController

AttendanceController
```

---

# MANEJO DE ERRORES

Todas las operaciones deben contemplar:

## Loading

---

## Success

---

## Error

---

NO ignorar errores.

---

# ESTADOS DE UI

Toda pantalla debe tener:

```kotlin id="m7x4yn"
loading

error

content
```

---

# TESTING

Prioridad:

## Backend

Service Layer

---

## Android

ViewModels

---

No es obligatorio alcanzar cobertura alta.

---

# OBJETIVO DE LA DEMOSTRACIÓN

Durante la sustentación debe ser posible ejecutar el siguiente escenario:

---

Profesor:

```text id="jv7a3r"
Login
```

↓

```text id="czmbt2"
Crear Curso
```

↓

```text id="v0gukg"
Abrir Detalle Curso
```

↓

```text id="nt9dwa"
Vincular Estudiante
```

↓

```text id="ah8dx5"
Escanear QR Estudiante
```

↓

```text id="g3n7zm"
Estudiante Asociado
```

↓

```text id="n6o3eo"
Crear Sesión
```

↓

```text id="xg5sqp"
Generar QR Asistencia
```

---

Estudiante:

```text id="3j2ksz"
Login
```

↓

```text id="uy6j7w"
Abrir Mi QR
```

↓

```text id="j9x8ow"
Mostrar QR
```

↓

```text id="o2h6zm"
Escanear QR Asistencia
```

↓

```text id="l0mr8r"
GPS
```

↓

```text id="m7hvnr"
Asistencia Registrada
```

---

Profesor:

```text id="g4d4ym"
Consultar Reporte
```

↓

```text id="zujjz4"
Ver Asistentes
```

---

# CRITERIOS DE ÉXITO

La aplicación se considerará terminada cuando:

✅ Login funcione

✅ Registro funcione

✅ JWT funcione

✅ Cursos funcionen

✅ Vinculación funcione

✅ QR Identificación funcione

✅ QR Asistencia funcione

✅ GPS funcione

✅ Reportes funcionen

✅ PostgreSQL funcione

✅ Docker funcione

✅ Railway funcione

✅ APK generada

---

# INSTRUCCIONES PARA CLAUDE CODE

Al generar código:

1. Mantener estrictamente MVVM.
2. No introducir funcionalidades fuera del alcance.
3. No modificar contratos API.
4. No modificar modelo de datos.
5. No introducir dependencias innecesarias.
6. Priorizar simplicidad sobre sobreingeniería.
7. Generar código listo para producción académica.
8. Mantener coherencia con todos los documentos de la carpeta `/docs`.
9. Utilizar buenas prácticas modernas de Android y Spring Boot.
10. Asumir siempre que este documento es la fuente oficial de verdad del proyecto.

---

# RESULTADO ESPERADO

ClassTrack debe convertirse en una solución móvil moderna para la gestión académica de asistencia mediante QR y geolocalización, cumpliendo completamente los requisitos de la asignatura y obteniendo la máxima cantidad posible de puntos de la rúbrica mediante backend propio, despliegue cloud, Docker y distribución de la aplicación.
