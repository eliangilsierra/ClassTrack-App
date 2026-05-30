# 03-backend-architecture.md

# ClassTrack Backend Architecture

Versión: 1.0

---

# Objetivo

Definir la arquitectura oficial del backend de ClassTrack.

Este backend proporcionará:

* Autenticación
* Gestión de usuarios
* Gestión de cursos
* Vinculación de estudiantes
* Gestión de sesiones de asistencia
* Registro de asistencias
* Reportes

---

# Stack Tecnológico

## Framework

Spring Boot 3.5+

---

## Lenguaje

Java 21

---

## Build Tool

Gradle Kotlin DSL

---

## Base de Datos

PostgreSQL

---

## Seguridad

Spring Security

JWT Authentication

BCrypt

---

## Documentación

OpenAPI

Swagger

---

## Contenedores

Docker

Docker Compose

---

## Despliegue

Railway

---

# Arquitectura

Clean Architecture simplificada

```text
api
│
service
│
repository
│
entity
│
database
```

---

# Estructura de Carpetas

```text
src/main/java/com/classtrack

├── auth
├── users
├── courses
├── students
├── attendance
├── reports
│
├── common
├── security
├── config
├── exception
└── infrastructure
```

---

# Auth Module

Responsabilidad:

Autenticación y autorización.

---

## Componentes

```text
AuthController

AuthService

JwtService

UserDetailsServiceImpl
```

---

## Funcionalidades

* Registro
* Login
* Validación JWT
* Refresh Token (opcional)

---

# Users Module

Responsabilidad:

Gestión de usuarios.

---

## Roles

```java
TEACHER

STUDENT
```

---

## Entidad

User

---

## Funciones

* Consultar perfil
* Actualizar perfil
* Obtener QR digital

---

# Courses Module

Responsabilidad:

Gestión de cursos.

---

## Funciones

* Crear curso
* Consultar cursos
* Obtener detalle
* Asociar estudiantes

---

## Entidades

Course

CourseStudent

---

# Students Module

Responsabilidad:

Administrar relación estudiante-curso.

---

## Funciones

* Asociar estudiante
* Remover estudiante
* Consultar estudiantes

---

# Attendance Module

Responsabilidad:

Control de asistencia.

---

## Funciones

* Crear sesión
* Generar QR
* Registrar asistencia
* Consultar asistentes

---

# Reports Module

Responsabilidad:

Consultas académicas.

---

## Funciones

* Reporte por curso
* Reporte por sesión
* Reporte por estudiante

---

# Seguridad

## Autenticación

JWT

---

## Contraseña

BCrypt

---

## Header

```http
Authorization: Bearer <token>
```

---

# Configuración JWT

## Access Token

Duración:

24 horas

---

## Claims

```json
{
  "userId": 1,
  "email": "teacher@test.com",
  "role": "TEACHER"
}
```

---

# Base de Datos

PostgreSQL

---

# Entidades Principales

## User

Representa usuarios del sistema.

---

## Course

Representa cursos académicos.

---

## CourseStudent

Relación muchos a muchos.

---

## AttendanceSession

Sesiones de asistencia.

---

## AttendanceRecord

Asistencias registradas.

---

# DTO Strategy

Nunca exponer entidades directamente.

---

## Request DTO

```text
CreateCourseRequest

LoginRequest

RegisterRequest
```

---

## Response DTO

```text
CourseResponse

UserResponse

AttendanceResponse
```

---

# Validaciones

Usar:

```java
jakarta.validation
```

---

## Ejemplos

```java
@NotBlank

@Email

@Size
```

---

# Manejo de Excepciones

GlobalExceptionHandler

---

## Excepciones

```text
NotFoundException

BadRequestException

UnauthorizedException

ConflictException
```

---

# Respuestas API

Formato estándar.

```json
{
  "success": true,
  "message": "Course created",
  "data": {}
}
```

---

# Auditoría

Campos comunes.

```java
createdAt

updatedAt
```

---

# Configuración Swagger

Ruta:

```text
/swagger-ui.html
```

---

# Docker

## Dockerfile

Imagen única.

---

## Puerto

```text
8080
```

---

# Variables de Entorno

```text
DB_HOST

DB_PORT

DB_NAME

DB_USER

DB_PASSWORD

JWT_SECRET

JWT_EXPIRATION
```

---

# Railway

## Servicios

Backend

PostgreSQL

---

# Observabilidad

MVP:

Logs estándar Spring.

---

# Logging

Usar:

```java
@Slf4j
```

---

# Convenciones

## Controllers

```java
CourseController

AttendanceController
```

---

## Services

```java
CourseService

AttendanceService
```

---

## Repositories

```java
CourseRepository

AttendanceRepository
```

---

# Transacciones

Usar:

```java
@Transactional
```

en operaciones críticas.

---

# Flujo Registro

Usuario

↓

Register

↓

Hash Password

↓

Guardar Usuario

↓

Generar JWT

↓

Retornar Token

---

# Flujo Login

Usuario

↓

Login

↓

Validar Credenciales

↓

Generar JWT

↓

Retornar Token

---

# Flujo Vinculación

Profesor

↓

Escanea QR Estudiante

↓

Obtiene UserId

↓

Asocia a Curso

↓

Persistir CourseStudent

---

# Flujo Asistencia

Profesor

↓

Crear Sesión

↓

Generar QR

↓

Estudiante Escanea

↓

Capturar GPS

↓

Registrar Asistencia

↓

Persistir AttendanceRecord

---

# Criterios de Calidad

Todo endpoint deberá:

* Validar entrada
* Retornar DTO
* Manejar excepciones
* Tener pruebas unitarias básicas
* Documentarse en Swagger

---

# Definition of Done

El backend estará completo cuando:

* Login funcione
* Registro funcione
* CRUD Cursos funcione
* Vinculación funcione
* Generación de sesiones funcione
* Registro de asistencia funcione
* PostgreSQL funcione
* Docker funcione
* Railway funcione
* Swagger funcione

---

# Objetivo Final

Proporcionar una API REST segura, escalable y fácil de consumir por la aplicación Android, permitiendo gestionar usuarios, cursos y asistencia académica mediante QR y geolocalización.
