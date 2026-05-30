package me.egil_accamacho.classtrack.core.qr

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long

/** Typed representation of the two QR payload variants. */
sealed class QrPayload {
    data class User(val userId: Long) : QrPayload()
    data class Attendance(val sessionId: Long, val token: String) : QrPayload()
    data object Unknown : QrPayload()
}

object QrParser {
    private val json = Json { ignoreUnknownKeys = true }

    fun parse(content: String): QrPayload = try {
        val obj = json.decodeFromString<JsonObject>(content)
        when (obj["type"]?.jsonPrimitive?.content) {
            "USER"       -> QrPayload.User(
                userId = obj["userId"]!!.jsonPrimitive.long,
            )
            "ATTENDANCE" -> QrPayload.Attendance(
                sessionId = obj["sessionId"]!!.jsonPrimitive.long,
                token     = obj["token"]!!.jsonPrimitive.content,
            )
            else -> QrPayload.Unknown
        }
    } catch (_: Exception) {
        QrPayload.Unknown
    }
}
