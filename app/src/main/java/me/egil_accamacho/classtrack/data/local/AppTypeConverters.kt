package me.egil_accamacho.classtrack.data.local

import androidx.room.TypeConverter
import java.time.Instant

/**
 * Room TypeConverters for types not natively supported by SQLite.
 * Registered in [AppDatabase] via @TypeConverters.
 */
class AppTypeConverters {

    @TypeConverter
    fun fromInstant(value: Instant?): Long? = value?.toEpochMilli()

    @TypeConverter
    fun toInstant(value: Long?): Instant? = value?.let { Instant.ofEpochMilli(it) }
}
