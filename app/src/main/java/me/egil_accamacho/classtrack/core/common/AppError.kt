package me.egil_accamacho.classtrack.core.common

/**
 * Unified error domain for the entire app.
 * Produced by [me.egil_accamacho.classtrack.core.network.ErrorMapper]
 * and consumed by every [Resource.Error] in ViewModels.
 */
sealed class AppError(open val message: String) {

    /** No internet connection or host unreachable. */
    data class Network(override val message: String = "Sin conexión a internet") : AppError(message)

    /** HTTP 401 — JWT absent or expired. */
    data class Unauthorized(override val message: String = "Sesión expirada. Inicia sesión nuevamente") : AppError(message)

    /** HTTP 403 — authenticated but lacks permission. */
    data class Forbidden(override val message: String = "No tienes permiso para realizar esta acción") : AppError(message)

    /** HTTP 404 — resource not found. */
    data class NotFound(override val message: String = "Recurso no encontrado") : AppError(message)

    /** HTTP 409 — state conflict (e.g., student already linked, already checked in). */
    data class Conflict(override val message: String = "Ya existe un registro con estos datos") : AppError(message)

    /** HTTP 400 — validation failure from the backend. [errors] mirrors the "errors" array. */
    data class Validation(
        val errors: List<String> = emptyList(),
        override val message: String = errors.firstOrNull() ?: "Datos inválidos"
    ) : AppError(message)

    /** HTTP 5xx — server-side failure. */
    data class Server(override val message: String = "Error en el servidor. Intenta más tarde") : AppError(message)

    /** Catch-all for anything not covered above. */
    data class Unknown(override val message: String = "Ocurrió un error inesperado") : AppError(message)
}
