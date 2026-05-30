# 07-database-model.md

# ClassTrack Database Model

Versión: 1.0

---

# Objetivo

Definir el modelo relacional oficial de PostgreSQL para ClassTrack.

Este documento servirá como referencia para:

* Backend Spring Boot
* JPA Entities
* Flyway/Liquibase
* API Contracts
* Android Models

---

# Motor de Base de Datos

PostgreSQL 16+

---

# Convenciones

## Naming

Tablas:

```text id="v2okp3"
snake_case
```

Ejemplo:

```text id="ud7h4f"
attendance_sessions
course_students
```

---

## Primary Keys

Todas las tablas utilizarán:

```sql id="0v41ad"
BIGSERIAL
```

---

## Auditoría

Todas las entidades tendrán:

```sql id="g6x6eq"
created_at
updated_at
```

---

# Modelo Conceptual

```text id="5swc6q"
User
 │
 ├── Course (Teacher)
 │
 ├── CourseStudent
 │
 └── AttendanceRecord

Course
 │
 ├── CourseStudent
 │
 └── AttendanceSession

AttendanceSession
 │
 └── AttendanceRecord
```

---

# Tabla Users

## Descripción

Usuarios del sistema.

Incluye:

* Docentes
* Estudiantes

---

## Tabla

```sql id="x1o4yb"
users
```

---

## Columnas

| Campo         | Tipo         |
| ------------- | ------------ |
| id            | BIGSERIAL    |
| full_name     | VARCHAR(150) |
| email         | VARCHAR(255) |
| password_hash | VARCHAR(255) |
| role          | VARCHAR(20)  |
| student_code  | VARCHAR(50)  |
| active        | BOOLEAN      |
| created_at    | TIMESTAMP    |
| updated_at    | TIMESTAMP    |

---

## Restricciones

```sql id="74o1jp"
UNIQUE(email)
```

---

## Role

Valores:

```text id="sj3s7z"
TEACHER
STUDENT
```

---

## Observaciones

student_code:

* Obligatorio para estudiantes
* Nulo para docentes

---

# Tabla Courses

## Descripción

Cursos creados por docentes.

---

## Tabla

```sql id="nynm6o"
courses
```

---

## Columnas

| Campo       | Tipo         |
| ----------- | ------------ |
| id          | BIGSERIAL    |
| teacher_id  | BIGINT       |
| name        | VARCHAR(120) |
| description | TEXT         |
| active      | BOOLEAN      |
| created_at  | TIMESTAMP    |
| updated_at  | TIMESTAMP    |

---

## FK

```sql id="hnw04g"
teacher_id -> users.id
```

---

# Tabla Course Students

## Descripción

Relación muchos a muchos.

---

## Tabla

```sql id="n76tbj"
course_students
```

---

## Columnas

| Campo      | Tipo      |
| ---------- | --------- |
| id         | BIGSERIAL |
| course_id  | BIGINT    |
| student_id | BIGINT    |
| created_at | TIMESTAMP |

---

## FK

```sql id="4h64oc"
course_id -> courses.id

student_id -> users.id
```

---

## Restricción

Evitar duplicados.

```sql id="j9v4wc"
UNIQUE(course_id, student_id)
```

---

# Tabla Attendance Sessions

## Descripción

Sesiones de asistencia creadas por docentes.

---

## Tabla

```sql id="c7zot6"
attendance_sessions
```

---

## Columnas

| Campo      | Tipo         |
| ---------- | ------------ |
| id         | BIGSERIAL    |
| course_id  | BIGINT       |
| qr_token   | VARCHAR(255) |
| status     | VARCHAR(20)  |
| started_at | TIMESTAMP    |
| expires_at | TIMESTAMP    |
| closed_at  | TIMESTAMP    |
| created_at | TIMESTAMP    |

---

## FK

```sql id="nd5n9m"
course_id -> courses.id
```

---

## Status

```text id="jlwmha"
ACTIVE

CLOSED
```

---

## Observaciones

Cada sesión genera un QR temporal.

---

# Tabla Attendance Records

## Descripción

Registro de asistencia individual.

---

## Tabla

```sql id="9oxdxm"
attendance_records
```

---

## Columnas

| Campo         | Tipo          |
| ------------- | ------------- |
| id            | BIGSERIAL     |
| session_id    | BIGINT        |
| student_id    | BIGINT        |
| latitude      | NUMERIC(10,7) |
| longitude     | NUMERIC(10,7) |
| registered_at | TIMESTAMP     |
| created_at    | TIMESTAMP     |

---

## FK

```sql id="6owrv6"
session_id -> attendance_sessions.id

student_id -> users.id
```

---

## Restricción

Un estudiante solo puede registrar una asistencia por sesión.

```sql id="v0o6f5"
UNIQUE(session_id, student_id)
```

---

# Índices

## Users

```sql id="jsv7d9"
CREATE INDEX idx_users_email
ON users(email);
```

---

## Courses

```sql id="dwmq6h"
CREATE INDEX idx_courses_teacher
ON courses(teacher_id);
```

---

## Course Students

```sql id="6z2nnv"
CREATE INDEX idx_course_students_course
ON course_students(course_id);
```

---

```sql id="g4plxj"
CREATE INDEX idx_course_students_student
ON course_students(student_id);
```

---

## Attendance Sessions

```sql id="v4wsg8"
CREATE INDEX idx_attendance_sessions_course
ON attendance_sessions(course_id);
```

---

## Attendance Records

```sql id="b7w0gz"
CREATE INDEX idx_attendance_records_session
ON attendance_records(session_id);
```

---

```sql id="7kv6ah"
CREATE INDEX idx_attendance_records_student
ON attendance_records(student_id);
```

---

# Relaciones

## Teacher → Courses

```text id="d0u7zc"
1:N
```

Un docente puede tener múltiples cursos.

---

## Course → Students

```text id="5qvfx6"
N:M
```

Implementado mediante:

```text id="6zbv7x"
course_students
```

---

## Course → Sessions

```text id="vy3v8h"
1:N
```

---

## Session → Attendance Records

```text id="mzvxt9"
1:N
```

---

## Student → Attendance Records

```text id="zw6ps5"
1:N
```

---

# Modelo ER Simplificado

```text id="7aw4px"
users
│
├──< courses
│
├──< course_students >── courses
│
└──< attendance_records
            │
            v
attendance_sessions
            │
            v
courses
```

---

# Entidades JPA

## UserEntity

```java id="xf99vb"
@Entity
@Table(name = "users")
```

---

## CourseEntity

```java id="c0w4w9"
@Entity
@Table(name = "courses")
```

---

## CourseStudentEntity

```java id="m7wgr6"
@Entity
@Table(name = "course_students")
```

---

## AttendanceSessionEntity

```java id="b9yzcl"
@Entity
@Table(name = "attendance_sessions")
```

---

## AttendanceRecordEntity

```java id="dd3z57"
@Entity
@Table(name = "attendance_records")
```

---

# Datos Iniciales

## Roles

No se requiere tabla roles.

Se utilizará enum.

---

# Casos de Uso Cubiertos

## Registro

users

---

## Login

users

---

## Crear Curso

courses

---

## Vincular Estudiante

course_students

---

## Generar QR

attendance_sessions

---

## Registrar Asistencia

attendance_records

---

## Reportes

courses

attendance_sessions

attendance_records

---

# Queries Frecuentes

## Cursos del docente

```sql id="79r31v"
SELECT *
FROM courses
WHERE teacher_id = ?
```

---

## Estudiantes de un curso

```sql id="l7vdtw"
SELECT u.*
FROM users u
JOIN course_students cs
ON cs.student_id = u.id
WHERE cs.course_id = ?
```

---

## Asistentes de una sesión

```sql id="5k54la"
SELECT u.full_name
FROM attendance_records ar
JOIN users u
ON ar.student_id = u.id
WHERE ar.session_id = ?
```

---

## Historial estudiante

```sql id="b0evtt"
SELECT *
FROM attendance_records
WHERE student_id = ?
```

---

# Definition of Done

La base de datos estará terminada cuando:

* PostgreSQL despliegue correctamente.
* Todas las tablas existan.
* Todas las FK funcionen.
* Restricciones estén aplicadas.
* Índices creados.
* Spring Boot pueda mapear todas las entidades.
* Los endpoints definidos en API Contracts funcionen correctamente.

---

# Resultado Esperado

El modelo de datos deberá soportar completamente la gestión de usuarios, cursos, vinculación de estudiantes, sesiones de asistencia y registros georreferenciados, manteniendo integridad referencial y simplicidad suficiente para el alcance académico del proyecto.
