package me.egil_accamacho.classtrack.core.network

import android.util.Log
import kotlinx.serialization.json.Json
import me.egil_accamacho.classtrack.core.common.AppError
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Converts raw exceptions (from Retrofit / OkHttp) into typed [AppError] values.
 *
 * Inject this in every Repository implementation and use [safeCall] from Resource.kt.
 */
@Singleton
class ErrorMapper @Inject constructor(private val json: Json) {

    fun map(throwable: Throwable): AppError {
        Log.e("ErrorMapper", "Mapping throwable: ${throwable.message}", throwable)
        return when (throwable) {
            is IOException -> AppError.Network()

            is HttpException -> mapHttpException(throwable)

            else -> AppError.Unknown(throwable.message ?: "Error inesperado")
        }
    }

    private fun mapHttpException(e: HttpException): AppError {
        val code = e.code()
        val body = runCatching {
            val errorJson = e.response()?.errorBody()?.string()
            Log.d("ErrorMapper", "HTTP Error Body: $errorJson")
            errorJson?.let { json.decodeFromString<ApiErrorBody>(it) }
        }.onFailure {
            Log.e("ErrorMapper", "Failed to parse error body", it)
        }.getOrNull()

        val serverMessage = body?.message
        val serverErrors = body?.errors ?: emptyList()

        return when (code) {
            400  -> AppError.Validation(errors = serverErrors, message = serverMessage ?: "Datos inválidos")
            401  -> AppError.Unauthorized(serverMessage ?: "Sesión expirada")
            403  -> AppError.Forbidden(serverMessage ?: "Sin permisos")
            404  -> AppError.NotFound(serverMessage ?: "No encontrado")
            409  -> AppError.Conflict(serverMessage ?: "Conflicto de datos")
            in 500..599 -> AppError.Server(serverMessage ?: "Error en el servidor")
            else -> AppError.Unknown(serverMessage ?: "Error HTTP $code")
        }
    }
}
