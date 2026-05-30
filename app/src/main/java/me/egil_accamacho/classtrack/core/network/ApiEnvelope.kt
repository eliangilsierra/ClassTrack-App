package me.egil_accamacho.classtrack.core.network

import kotlinx.serialization.Serializable

/**
 * Standard success response envelope used by auth endpoints.
 * See docs/04-api-contracts.md §"Formato Respuesta Exitosa".
 *
 * ```json
 * { "success": true, "message": "...", "data": { ... } }
 * ```
 *
 * Note: not all endpoints wrap their response in this envelope.
 * Resource endpoints (courses, profile, attendance) return the DTO directly.
 */
@Serializable
data class ApiEnvelope<T>(
    val success: Boolean = true,
    val message: String? = null,
    val data: T? = null
)

/**
 * Standard error response body.
 * See docs/04-api-contracts.md §"Formato Error".
 *
 * ```json
 * { "success": false, "message": "...", "errors": [] }
 * ```
 */
@Serializable
data class ApiErrorBody(
    val success: Boolean = false,
    val message: String = "Error desconocido",
    val errors: List<String>? = null
)
