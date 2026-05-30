package me.egil_accamacho.classtrack.core.common

import me.egil_accamacho.classtrack.BuildConfig

/**
 * App-wide constants.
 * BASE_URL is injected at build time via BuildConfig (from local.properties or env var BASE_URL).
 */
object Constants {
    val BASE_URL: String = BuildConfig.BASE_URL

    // DataStore preference file name
    const val SESSION_DATASTORE = "classtrack_session"

    // QR payload type discriminators (see docs/04-api-contracts.md §QR Payloads)
    const val QR_TYPE_USER       = "USER"
    const val QR_TYPE_ATTENDANCE = "ATTENDANCE"

    // Session status values returned by the API
    const val SESSION_STATUS_ACTIVE = "ACTIVE"
    const val SESSION_STATUS_CLOSED = "CLOSED"

    // User roles as returned by the API
    const val ROLE_TEACHER = "TEACHER"
    const val ROLE_STUDENT = "STUDENT"
}
