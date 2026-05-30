# 04-api-contracts.md

# ClassTrack API Contracts

Versión: 1.0

Base URL:

```text
https://api.classtrack.app/api/v1
```

---

# Convenciones Generales

## Content-Type

```http
application/json
```

---

## Autenticación

Todos los endpoints protegidos requieren:

```http
Authorization: Bearer <JWT>
```

---

# Formato Respuesta Exitosa

```json
{
  "success": true,
  "message": "Operation successful",
  "data": {}
}
```

---

# Formato Error

```json
{
  "success": false,
  "message": "Validation error",
  "errors": []
}
```

---

# AUTH

---

## Registro

### POST

```http
/auth/register
```

### Request

```json
{
  "fullName": "Juan Perez",
  "email": "juan@correo.com",
  "password": "Password123",
  "role": "STUDENT"
}
```

### Response

```json
{
  "success": true,
  "message": "User registered",
  "data": {
    "userId": 1,
    "token": "jwt"
  }
}
```

---

## Login

### POST

```http
/auth/login
```

### Request

```json
{
  "email": "teacher@test.com",
  "password": "Password123"
}
```

### Response

```json
{
  "success": true,
  "data": {
    "token": "jwt",
    "userId": 1,
    "role": "TEACHER"
  }
}
```

---

# PROFILE

---

## Obtener Perfil

### GET

```http
/profile
```

### Response

```json
{
  "id": 1,
  "fullName": "Juan Perez",
  "email": "juan@test.com",
  "role": "STUDENT"
}
```

---

# DIGITAL ID

---

## Obtener QR Digital

### GET

```http
/profile/digital-id
```

### Response

```json
{
  "userId": 10,
  "fullName": "Juan Perez",
  "studentCode": "2025001",
  "qrContent": "CT_USER_10"
}
```

---

# COURSES

---

## Crear Curso

### POST

```http
/courses
```

### Request

```json
{
  "name": "Desarrollo Movil",
  "description": "Curso Android"
}
```

### Response

```json
{
  "id": 1,
  "name": "Desarrollo Movil",
  "description": "Curso Android"
}
```

---

## Listar Cursos

### GET

```http
/courses
```

### Response

```json
[
  {
    "id": 1,
    "name": "Desarrollo Movil",
    "studentCount": 25
  }
]
```

---

## Obtener Curso

### GET

```http
/courses/{courseId}
```

### Response

```json
{
  "id": 1,
  "name": "Desarrollo Movil",
  "description": "Curso Android",
  "studentCount": 25
}
```

---

## Eliminar Curso

### DELETE

```http
/courses/{courseId}
```

---

# COURSE STUDENTS

---

## Vincular Estudiante

### POST

```http
/courses/{courseId}/students
```

### Request

```json
{
  "studentId": 15
}
```

### Response

```json
{
  "success": true,
  "message": "Student linked"
}
```

---

## Listar Estudiantes Curso

### GET

```http
/courses/{courseId}/students
```

### Response

```json
[
  {
    "id": 15,
    "fullName": "Juan Perez",
    "studentCode": "2025001"
  }
]
```

---

## Remover Estudiante

### DELETE

```http
/courses/{courseId}/students/{studentId}
```

---

# ATTENDANCE SESSION

---

## Crear Sesión

### POST

```http
/attendance/sessions
```

### Request

```json
{
  "courseId": 1
}
```

### Response

```json
{
  "sessionId": 100,
  "courseId": 1,
  "qrToken": "ATT_ABC123",
  "expiresAt": "2026-05-30T18:00:00"
}
```

---

## Obtener Sesión

### GET

```http
/attendance/sessions/{sessionId}
```

### Response

```json
{
  "sessionId": 100,
  "courseId": 1,
  "status": "ACTIVE"
}
```

---

## Cerrar Sesión

### POST

```http
/attendance/sessions/{sessionId}/close
```

---

# ATTENDANCE

---

## Registrar Asistencia

### POST

```http
/attendance/checkin
```

### Request

```json
{
  "sessionId": 100,
  "latitude": 7.1205,
  "longitude": -73.1221
}
```

### Response

```json
{
  "attendanceId": 500,
  "registeredAt": "2026-05-30T18:05:00"
}
```

---

## Obtener Asistentes

### GET

```http
/attendance/sessions/{sessionId}/records
```

### Response

```json
[
  {
    "studentId": 15,
    "studentName": "Juan Perez",
    "registeredAt": "2026-05-30T18:05:00"
  }
]
```

---

# REPORTS

---

## Reporte de Sesión

### GET

```http
/reports/sessions/{sessionId}
```

### Response

```json
{
  "sessionId": 100,
  "totalStudents": 25,
  "attendees": 22,
  "absent": 3
}
```

---

## Reporte de Curso

### GET

```http
/reports/courses/{courseId}
```

### Response

```json
{
  "courseId": 1,
  "courseName": "Desarrollo Movil",
  "totalStudents": 25,
  "attendanceRate": 88
}
```

---

## Reporte Estudiante

### GET

```http
/reports/students/{studentId}
```

### Response

```json
{
  "studentId": 15,
  "studentName": "Juan Perez",
  "attendancePercentage": 92,
  "sessionsAttended": 23
}
```

---

# Payloads QR

---

## QR Identificación Digital

Contenido QR:

```json
{
  "type": "USER",
  "userId": 15
}
```

Uso:

* Vinculación de estudiantes

---

## QR Asistencia

Contenido QR:

```json
{
  "type": "ATTENDANCE",
  "sessionId": 100,
  "token": "ATT_ABC123"
}
```

Uso:

* Registro asistencia

---

# HTTP Status Codes

## 200

Operación exitosa

---

## 201

Recurso creado

---

## 400

Validación

---

## 401

No autenticado

---

## 403

Sin permisos

---

## 404

No encontrado

---

## 409

Conflicto

---

## 500

Error interno

---

# Definition of Done

La API estará terminada cuando:

* Todos los endpoints respondan correctamente.
* Swagger documente todos los contratos.
* Android pueda consumir todos los endpoints.
* JWT funcione.
* Registro funcione.
* Login funcione.
* Vinculación funcione.
* Asistencia funcione.
* Reportes funcionen.
