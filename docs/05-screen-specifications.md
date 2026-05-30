# 05-screen-specifications.md

# ClassTrack Screen Specifications

Versión: 1.0

---

# Objetivo

Definir detalladamente todas las pantallas del sistema ClassTrack para garantizar consistencia entre:

* Diseño UX/UI
* Desarrollo Android
* Backend
* Casos de uso
* Pruebas

Este documento debe utilizarse como referencia oficial para el desarrollo de interfaces.

---

# Roles

## Teacher

Permisos:

* Crear cursos
* Gestionar estudiantes
* Crear sesiones
* Generar QR
* Consultar reportes

Bottom Navigation:

```text
Home
Courses
Profile
```

---

## Student

Permisos:

* Consultar cursos
* Ver QR de identificación
* Registrar asistencia

Bottom Navigation:

```text
Home
Profile
```

---

# SCREEN 01

# Splash Screen

## Objetivo

Mostrar carga inicial.

---

## Componentes

Logo ClassTrack

Nombre aplicación

Loading Indicator

---

## Navegación

Si existe sesión:

```text
Home
```

Si no existe:

```text
Login
```

---

# SCREEN 02

# Login Screen

## Objetivo

Autenticar usuarios.

---

## Componentes

Email

Password

Botón Login

Link Registro

---

## Validaciones

Email requerido

Password requerida

---

## Acciones

Login

Ir Registro

---

## Navegación

Login exitoso:

Teacher → Home Teacher

Student → Home Student

---

# SCREEN 03

# Register Screen

## Objetivo

Registrar nuevos usuarios.

---

## Campos

Nombre completo

Correo electrónico

Contraseña

Confirmar contraseña

Rol

* Teacher
* Student

---

## Validaciones

Campos obligatorios

Correo válido

Contraseñas coinciden

---

## Acción

Crear Cuenta

---

## Resultado

Usuario registrado

JWT almacenado

---

# SCREEN 04

# Home Teacher

## Objetivo

Pantalla principal docente.

---

## Información

Saludo

Nombre usuario

---

## Acciones rápidas

Crear Curso

Iniciar Asistencia

---

## Sección

Mis Cursos

---

## Curso Card

Nombre curso

Cantidad estudiantes

Última sesión

Botón Ver Curso

---

## Navegación

Courses

Profile

Detalle Curso

---

# SCREEN 05

# Home Student

## Objetivo

Pantalla principal estudiante.

---

## Información

Saludo

Nombre usuario

---

## Acciones rápidas

Mi QR

Registrar Asistencia

---

## Sección

Mis Cursos

---

## Curso Card

Nombre curso

Docente

---

## Navegación

Profile

Digital ID

Attendance Scanner

---

# SCREEN 06

# Courses Screen

## Objetivo

Listar cursos del docente.

---

## Componentes

Lista dinámica

Floating Action Button

---

## Curso Card

Nombre

Cantidad estudiantes

---

## Acciones

Ver detalle

Crear curso

---

# SCREEN 07

# Create Course Screen

## Objetivo

Crear curso.

---

## Campos

Nombre curso

Descripción

---

## Validaciones

Nombre obligatorio

---

## Acción

Guardar Curso

---

## Resultado

Curso creado

---

# SCREEN 08

# Course Detail Screen

## Objetivo

Administrar curso.

---

## Información

Nombre curso

Descripción

Cantidad estudiantes

---

## Acciones principales

Vincular Estudiantes

Iniciar Asistencia

---

## Secciones

Lista estudiantes

Sesiones recientes

---

## Navegación

Link Students

Attendance Session

Reports

---

# SCREEN 09

# Link Students Screen

## Objetivo

Asociar estudiantes al curso.

---

## Información

Nombre curso

Cantidad estudiantes

---

## Acción principal

Escanear QR

---

## Acción secundaria

Buscar estudiante

(Opcional MVP)

---

## Navegación

Scan Student QR

---

# SCREEN 10

# Scan Student QR Screen

## Objetivo

Escanear QR de identificación.

---

## Componentes

Camera Preview

Área escaneo

Indicador visual

---

## QR esperado

```json
{
  "type": "USER",
  "userId": 15
}
```

---

## Flujo

Escanear

↓

Consultar usuario

↓

Mostrar datos

---

# SCREEN 11

# Student Linked Success

## Objetivo

Confirmar vinculación.

---

## Información

Nombre estudiante

Código estudiante

Curso

---

## Acciones

Agregar otro estudiante

Volver al curso

---

# SCREEN 12

# Create Attendance Session

## Objetivo

Crear sesión de asistencia.

---

## Información

Curso

Fecha

Hora

---

## Campos

Duración

Observaciones (Opcional)

---

## Acción

Generar Sesión

---

## Resultado

Session creada

QR generado

---

# SCREEN 13

# Attendance QR Screen

## Objetivo

Mostrar QR de asistencia.

---

## Información

Curso

Fecha

Hora

Estado sesión

---

## Componentes

QR grande

Temporizador

Cantidad asistentes

---

## Acciones

Cerrar Sesión

Actualizar Lista

---

## QR generado

```json
{
  "type": "ATTENDANCE",
  "sessionId": 100,
  "token": "ATT_ABC123"
}
```

---

# SCREEN 14

# Attendance Scanner Screen

## Objetivo

Escanear QR de asistencia.

---

## Usuario

Student

---

## Componentes

Camera Preview

Área escaneo

Indicador visual

---

## Flujo

Escanear QR

↓

Obtener SessionId

↓

Capturar GPS

↓

Registrar asistencia

---

# SCREEN 15

# Attendance Success Screen

## Objetivo

Confirmar asistencia.

---

## Información

Curso

Fecha

Hora

Ubicación registrada

---

## Componentes

Check éxito

Mensaje confirmación

---

## Acción

Volver Home

---

# SCREEN 16

# Attendance Session Report

## Objetivo

Consultar asistentes.

---

## Información

Curso

Sesión

Fecha

---

## Métricas

Total estudiantes

Asistentes

Ausentes

---

## Lista

Nombre estudiante

Hora registro

Estado

---

## Acciones

Exportar (No MVP)

Actualizar

---

# SCREEN 17

# Profile Screen

## Objetivo

Administrar perfil.

---

## Información

Nombre

Correo

Rol

---

## Opciones

Mi Identificación Digital

Cerrar Sesión

---

## Navegación

Digital ID

Logout

---

# SCREEN 18

# Digital ID Screen

## Objetivo

Mostrar identificación digital.

---

## Información

Nombre completo

Correo

Código estudiante

Rol

---

## Componentes

QR grande

Tarjeta identificación

---

## Acciones

Compartir QR

Actualizar

---

## QR

```json
{
  "type": "USER",
  "userId": 15
}
```

---

# SCREEN 19

# Logout Confirmation Dialog

## Objetivo

Confirmar cierre de sesión.

---

## Mensaje

¿Desea cerrar sesión?

---

## Acciones

Cancelar

Cerrar Sesión

---

# Componentes Reutilizables

## CourseCard

Utilizado en:

* Home Teacher
* Courses

---

## StudentCard

Utilizado en:

* Course Detail
* Reports

---

## AttendanceCard

Utilizado en:

* Reports
* Session Detail

---

## PrimaryButton

Utilizado globalmente.

---

## SecondaryButton

Utilizado globalmente.

---

## EmptyState

Utilizado en:

* Cursos
* Estudiantes
* Reportes

---

# Estados Globales

## Loading

CircularProgressIndicator

---

## Empty

Mensaje amigable

---

## Error

Snackbar

---

## Success

Confirmation Screen

---

# Navegación Final

## Teacher

```text
Splash
 ↓
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
Attendance Session
 ↓
Attendance QR
 ↓
Report
```

---

## Student

```text
Splash
 ↓
Login
 ↓
Home
 ↓
Profile
 ↓
Digital ID
 ↓
Scan Attendance QR
 ↓
Attendance Success
```

---

# Definition of Done

Cada pantalla deberá:

* Tener ViewModel propio o compartido.
* Tener UiState.
* Tener navegación funcional.
* Manejar Loading.
* Manejar Error.
* Manejar Empty State.
* Consumir API cuando aplique.
* Cumplir arquitectura MVVM.
* Tener Preview Compose.
* Ser responsive para teléfonos Android.

---

# Resultado Esperado

El sistema deberá ofrecer una experiencia completa para gestión de asistencia académica basada en QR y geolocalización, alineada con los requisitos del proyecto de maestría y con los diseños aprobados en Stitch.
