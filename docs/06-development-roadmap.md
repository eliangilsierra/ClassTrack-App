# 06-development-roadmap.md

# ClassTrack Development Roadmap

Versión: 1.0

---

# Objetivo

Definir el plan completo de implementación de ClassTrack para garantizar que el proyecto pueda desarrollarse de forma incremental, demostrable y alineada con los criterios de evaluación de la asignatura.

Este roadmap está diseñado para:

* Minimizar riesgos.
* Maximizar puntos de la rúbrica.
* Permitir entregas parciales funcionales.
* Facilitar el trabajo con IA (Claude Code, Cursor, ChatGPT).

---

# Estrategia General

## Prioridad 1

Cumplir todos los requisitos obligatorios.

---

## Prioridad 2

Obtener puntos bonus.

---

## Prioridad 3

Mantener el alcance controlado.

---

# MVP Objetivo

Al finalizar el MVP el sistema debe permitir:

* Registro de usuarios.
* Login.
* Crear cursos.
* Vincular estudiantes.
* Generar QR de identificación.
* Crear sesión de asistencia.
* Escanear QR.
* Registrar asistencia.
* Consultar reportes básicos.

---

# Cronograma de Implementación

## Fase 0

# Preparación Proyecto

Duración estimada:

2 horas

---

## Actividades

### Android

Crear proyecto Android.

Configurar:

* Kotlin
* Compose
* Navigation
* Hilt
* Retrofit

---

### Backend

Crear proyecto Spring Boot.

Configurar:

* Security
* PostgreSQL
* JWT
* Swagger

---

### Infraestructura

Crear:

* GitHub Repository
* Railway Project
* PostgreSQL Instance

---

## Entregable

Proyecto base funcionando.

---

# Fase 1

# Autenticación

Duración estimada:

4 horas

---

## Objetivo

Permitir registro e inicio de sesión.

---

## Backend

Implementar:

```text id="y22a4v"
POST /auth/register

POST /auth/login
```

---

## Android

Pantallas:

* Splash
* Login
* Register

---

## Casos de Uso

### Registro

Usuario crea cuenta.

---

### Login

Usuario inicia sesión.

---

### Logout

Usuario cierra sesión.

---

## Definition of Done

Usuario autenticado.

JWT persistido.

---

# Fase 2

# Perfil e Identificación Digital

Duración estimada:

3 horas

---

## Objetivo

Mostrar información usuario.

---

## Backend

Implementar:

```text id="mmyhmy"
GET /profile

GET /profile/digital-id
```

---

## Android

Pantallas:

* Profile
* Digital ID

---

## Funcionalidades

Visualizar:

* Nombre
* Correo
* Rol
* QR Digital

---

## Definition of Done

QR visible correctamente.

---

# Fase 3

# Gestión de Cursos

Duración estimada:

5 horas

---

## Objetivo

Permitir gestión básica de cursos.

---

## Backend

Implementar:

```text id="q1f54j"
POST /courses

GET /courses

GET /courses/{id}

DELETE /courses/{id}
```

---

## Android

Pantallas:

* Courses
* Create Course
* Course Detail

---

## Casos de Uso

Crear curso.

Consultar cursos.

Eliminar curso.

---

## Definition of Done

CRUD básico funcionando.

---

# Fase 4

# Vinculación de Estudiantes

Duración estimada:

4 horas

---

## Objetivo

Asociar estudiantes mediante QR.

---

## Backend

Implementar:

```text id="lnvc2s"
POST /courses/{id}/students

GET /courses/{id}/students

DELETE /courses/{id}/students/{studentId}
```

---

## Android

Pantallas:

* Link Students
* Scan Student QR
* Student Linked Success

---

## Flujo

Profesor

↓

Escanea QR

↓

Obtiene UserId

↓

Asocia estudiante

---

## Definition of Done

Estudiante asociado exitosamente.

---

# Fase 5

# Sesiones de Asistencia

Duración estimada:

6 horas

---

## Objetivo

Permitir crear sesiones.

---

## Backend

Implementar:

```text id="l87tlf"
POST /attendance/sessions

GET /attendance/sessions/{id}

POST /attendance/sessions/{id}/close
```

---

## Android

Pantallas:

* Create Attendance Session
* Attendance QR Screen

---

## Funcionalidades

Crear sesión.

Generar QR.

Cerrar sesión.

---

## Definition of Done

QR generado correctamente.

---

# Fase 6

# Registro de Asistencia

Duración estimada:

5 horas

---

## Objetivo

Permitir registrar asistencia.

---

## Backend

Implementar:

```text id="7n6gkq"
POST /attendance/checkin
```

---

## Android

Pantallas:

* Attendance Scanner
* Attendance Success

---

## Flujo

Estudiante

↓

Escanea QR

↓

GPS

↓

Registrar asistencia

---

## Datos Capturados

SessionId

Latitude

Longitude

Fecha

Hora

---

## Definition of Done

Asistencia almacenada.

---

# Fase 7

# Reportes

Duración estimada:

4 horas

---

## Objetivo

Consultar registros.

---

## Backend

Implementar:

```text id="bx4qph"
GET /reports/sessions/{id}

GET /reports/courses/{id}

GET /reports/students/{id}
```

---

## Android

Pantallas:

* Attendance Report

---

## Métricas

Asistentes

Ausentes

Porcentaje

---

## Definition of Done

Reportes visibles.

---

# Fase 8

# Calidad y Hardening

Duración estimada:

3 horas

---

## Android

Validaciones.

Errores.

Loading states.

---

## Backend

Manejo excepciones.

Logs.

Validaciones DTO.

---

## Definition of Done

Flujos estables.

---

# Fase 9

# Docker

Duración estimada:

1 hora

---

## Objetivo

Obtener puntos bonus.

---

## Entregables

Dockerfile

docker-compose.yml

---

## Verificación

```bash id="9v4hl7"
docker build .
```

---

```bash id="6dx2j4"
docker run
```

---

# Fase 10

# Railway

Duración estimada:

1 hora

---

## Objetivo

Backend público.

---

## Entregables

URL pública.

---

## Verificación

Swagger accesible.

API accesible.

---

# Fase 11

# Distribución APK

Duración estimada:

1 hora

---

## Objetivo

Obtener puntos bonus.

---

## Opciones

GitHub Releases

Firebase App Distribution

---

## Entregables

APK firmada.

---

# Plan de Desarrollo Recomendado

## Día 1

Backend:

* Auth
* Profile

Android:

* Login
* Register
* Profile

---

## Día 2

Backend:

* Courses

Android:

* Courses
* Create Course

---

## Día 3

Backend:

* Student Linking

Android:

* QR Student

---

## Día 4

Backend:

* Attendance

Android:

* QR Attendance

---

## Día 5

Backend:

* Reports

Android:

* Report Screens

---

# Priorización para la Sustentación

## Crítico

Debe funcionar obligatoriamente:

✅ Login

✅ Registro

✅ Crear curso

✅ Vincular estudiante

✅ QR identificación

✅ QR asistencia

✅ Registro asistencia

---

## Importante

✅ Reportes

✅ Históricos

---

## Bonus

✅ Docker

✅ Railway

✅ APK Release

---

# Riesgos

## Riesgo

QR Scanner no funciona.

### Mitigación

Mock QR local.

---

## Riesgo

GPS falla.

### Mitigación

Permitir ubicación aproximada.

---

## Riesgo

Backend no despliega.

### Mitigación

Railway + Docker previamente probado.

---

# Checklist Final Sustentación

## Android

* Splash
* Login
* Register
* Home Teacher
* Home Student
* Courses
* Course Detail
* Link Students
* QR Student
* Attendance QR
* Attendance Scanner
* Profile
* Digital ID

---

## Backend

* JWT
* PostgreSQL
* Swagger
* Railway

---

## Infraestructura

* Docker
* GitHub

---

## Entregables

* APK
* Código Fuente
* PDF
* URL Backend

---

# Resultado Esperado

Al finalizar todas las fases, ClassTrack deberá ser una solución móvil completamente funcional para la gestión académica de asistencia mediante QR y geolocalización, cumpliendo el 100% de los requisitos obligatorios de la rúbrica y obteniendo la mayor cantidad posible de puntos bonus.
