package me.egil_accamacho.classtrack.core.location

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

/**
 * Wrapper around FusedLocationProviderClient.
 * Returns (latitude, longitude) or null if location is unavailable.
 * Caller must hold ACCESS_FINE_LOCATION or ACCESS_COARSE_LOCATION permission.
 */
@Singleton
class LocationProvider @Inject constructor(
    @param:ApplicationContext private val context: Context,
) {
    private val client = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    suspend fun getLocation(): Pair<Double, Double>? {
        // Try last known location first (instant)
        val last = getLastLocation()
        if (last != null) return last

        // Fall back to a fresh current location request
        return getCurrentLocation()
    }

    @SuppressLint("MissingPermission")
    private suspend fun getLastLocation(): Pair<Double, Double>? =
        suspendCancellableCoroutine { cont ->
            client.lastLocation
                .addOnSuccessListener { location ->
                    cont.resume(location?.let { it.latitude to it.longitude })
                }
                .addOnFailureListener { cont.resume(null) }
        }

    @SuppressLint("MissingPermission")
    private suspend fun getCurrentLocation(): Pair<Double, Double>? =
        suspendCancellableCoroutine { cont ->
            val request = CurrentLocationRequest.Builder()
                .setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
                .setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
                .setMaxUpdateAgeMillis(30_000L)
                .build()
            client.getCurrentLocation(request, null)
                .addOnSuccessListener { location ->
                    cont.resume(location?.let { it.latitude to it.longitude })
                }
                .addOnFailureListener { cont.resume(null) }
        }
}
