# 01-project-charter.md

# ClassTrack

## Smart Academic Attendance Management Platform

Versión: 2.0

---

# 1. Información General

## Nombre del Proyecto

ClassTrack

## Tipo de Proyecto

Aplicación móvil Android para gestión académica.

## Categoría

EdTech

Mobile Application

Academic Management

---

# 2. Descripción General

ClassTrack es una aplicación móvil diseñada para modernizar el proceso de control de asistencia académica mediante el uso de códigos QR, geolocalización y autenticación digital.

La plataforma permite a docentes administrar cursos, asociar estudiantes mediante códigos QR de identidad digital, generar sesiones de asistencia y consultar registros históricos.

Los estudiantes pueden registrarse en la plataforma, generar su identificación digital y registrar asistencia mediante el escaneo de códigos QR temporales generados por los docentes.

---

# 3. Problemática

En muchos entornos educativos el control de asistencia continúa realizándose mediante listas físicas o procesos manuales.

Esto genera:

* Pérdida de información.
* Errores humanos.
* Procesos lentos.
* Dificultad para consultar históricos.
* Escasa trazabilidad.
* Baja digitalización de procesos académicos.

Se requiere una solución móvil que permita registrar asistencia de forma rápida, organizada y segura.

---

# 4. Objetivo General

Desarrollar una aplicación móvil Android que permita gestionar cursos académicos y registrar asistencia mediante códigos QR y geolocalización.

---

# 5. Objetivos Específicos

* Implementar autenticación de usuarios.
* Diferenciar perfiles Docente y Estudiante.
* Permitir la gestión de cursos.
* Asociar estudiantes a cursos.
* Implementar identificación digital mediante QR.
* Generar sesiones de asistencia.
* Registrar asistencia mediante QR.
* Capturar ubicación GPS.
* Consultar históricos y reportes.
* Aplicar arquitectura MVVM.
* Implementar backend REST desplegado en la nube.

---

# 6. Alcance

## Incluido

### Autenticación

* Registro
* Inicio de sesión
* Cierre de sesión

### Gestión Académica

* Crear curso
* Consultar cursos
* Ver detalle de curso
* Vincular estudiantes

### Identificación Digital

* QR permanente para cada usuario

### Asistencia

* Crear sesión
* Generar QR temporal
* Escanear QR
* Registrar asistencia
* Capturar GPS

### Reportes

* Consultar asistentes
* Consultar ausentes
* Consultar histórico

---

## No Incluido

* Reconocimiento facial
* NFC
* Calificaciones
* LMS
* Integraciones externas
* Videoconferencias
* Notificaciones push

---

# 7. Roles

## Docente

Puede:

* Crear cursos
* Consultar cursos
* Ver detalle de cursos
* Vincular estudiantes
* Crear sesiones
* Generar QR
* Consultar reportes

No puede:

* Registrar asistencia propia

---

## Estudiante

Puede:

* Registrarse
* Iniciar sesión
* Ver cursos asociados
* Consultar identificación digital
* Registrar asistencia
* Consultar historial propio

No puede:

* Crear cursos
* Crear sesiones
* Consultar reportes globales

---

# 8. Navegación Final

## Docente

Bottom Navigation

* Home
* Courses
* Profile

---

## Estudiante

Bottom Navigation

* Home
* Profile

---

# 9. Flujo Principal Docente

Login

↓

Home

↓

Courses

↓

Detalle Curso

↓

Vincular Estudiantes

↓

Escanear QR Estudiante

↓

Estudiante Asociado

↓

Crear Sesión

↓

Generar QR

↓

Consultar Asistencia

---

# 10. Flujo Principal Estudiante

Registro

↓

Login

↓

Home

↓

Mi Identificación Digital

↓

Docente Escanea QR

↓

Curso Asociado

↓

Escanear QR Asistencia

↓

GPS

↓

Asistencia Registrada

---

# 11. Pantallas Finales

## 1 Splash

Carga inicial de la aplicación.

---

## 2 Login

Inicio de sesión.

---

## 3 Registro

Creación de cuentas.

Roles:

* Docente
* Estudiante

---

## 4 Home Docente

Acceso principal del docente.

---

## 5 Home Estudiante

Acceso principal del estudiante.

---

## 6 Lista de Cursos

Cursos disponibles.

---

## 7 Crear Curso

Formulario de creación.

---

## 8 Detalle Curso

Información del curso.

Acciones:

* Crear sesión
* Vincular estudiantes

---

## 9 Vincular Estudiantes

Asociar estudiantes mediante QR.

---

## 9.1 Escanear QR Estudiante

Lectura de identificación digital.

---

## 9.2 Confirmación Vinculación

Confirmación de asociación.

---

## 10 Crear Sesión

Configuración de sesión de asistencia.

---

## 11 Generar QR

QR temporal de asistencia.

---

## 12 Escanear QR Asistencia

Escaneo por estudiante.

---

## 13 Confirmación Asistencia

Registro exitoso.

---

## 17 Mi Identificación Digital

QR permanente del usuario.

---

# 12. Tecnologías

## Frontend

* Kotlin
* Jetpack Compose
* Navigation Compose
* Material Design 3

---

## Arquitectura

* MVVM
* Repository Pattern

---

## Backend

* Spring Boot 3
* Java 21

---

## Seguridad

* JWT

---

## Persistencia

* PostgreSQL

---

## Infraestructura

* Docker
* Railway

---

# 13. Factores Diferenciadores

* Identificación digital mediante QR.
* Vinculación de estudiantes mediante QR.
* Registro de asistencia mediante QR.
* Captura de geolocalización.
* Arquitectura moderna.
* Backend cloud desplegado.

---

# 14. Relación con la Rúbrica

## UI

* Scaffold
* TopAppBar
* Navigation
* Formularios

## Estado

* remember
* mutableStateOf
* ViewModel

## Arquitectura

* MVVM

## Persistencia

* API REST
* PostgreSQL

## Servicios

* GPS
* Cámara
* QR

## Bonus

* Backend propio
* Docker
* Railway
* APK Release

---

# 15. Resultado Esperado

ClassTrack debe funcionar como una plataforma móvil académica capaz de gestionar cursos, asociar estudiantes mediante identificación digital y registrar asistencia mediante códigos QR y geolocalización utilizando una arquitectura moderna y desplegada en la nube.
