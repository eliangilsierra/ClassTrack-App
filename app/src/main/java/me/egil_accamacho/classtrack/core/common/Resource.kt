package me.egil_accamacho.classtrack.core.common

/**
 * Represents the lifecycle of any async data operation.
 *
 * Usage in a ViewModel:
 * ```kotlin
 * viewModelScope.launch {
 *     repository.getCourses().collect { resource ->
 *         when (resource) {
 *             is Resource.Loading -> _uiState.update { it.copy(loading = true) }
 *             is Resource.Success -> _uiState.update { it.copy(loading = false, courses = resource.data) }
 *             is Resource.Error   -> _uiState.update { it.copy(loading = false, error = resource.error.message) }
 *         }
 *     }
 * }
 * ```
 */
sealed interface Resource<out T> {

    /** Operation is in flight. */
    data object Loading : Resource<Nothing>

    /** Operation succeeded and [data] holds the result. */
    data class Success<T>(val data: T) : Resource<T>

    /** Operation failed. [error] describes what went wrong. */
    data class Error(val error: AppError) : Resource<Nothing>
}

// ── Extension helpers ─────────────────────────────────────────────────────────

/** Returns [Resource.Success] data or null if this is not a Success. */
fun <T> Resource<T>.dataOrNull(): T? = (this as? Resource.Success)?.data

/** Returns true if this is [Resource.Loading]. */
val Resource<*>.isLoading: Boolean get() = this is Resource.Loading

/** Returns true if this is [Resource.Error]. */
val Resource<*>.isError: Boolean get() = this is Resource.Error

/**
 * Wraps a suspending [block] that returns [T] in a [Resource],
 * catching exceptions and delegating to [ErrorMapper].
 */
suspend fun <T> safeCall(
    mapper: me.egil_accamacho.classtrack.core.network.ErrorMapper,
    block: suspend () -> T
): Resource<T> = try {
    Resource.Success(block())
} catch (e: Exception) {
    Resource.Error(mapper.map(e))
}
