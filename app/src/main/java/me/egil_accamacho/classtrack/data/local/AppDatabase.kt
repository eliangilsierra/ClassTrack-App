package me.egil_accamacho.classtrack.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import me.egil_accamacho.classtrack.data.local.entity.CourseEntity
import me.egil_accamacho.classtrack.features.courses.data.local.CourseDao

/**
 * Room database for ClassTrack local cache.
 *
 * Version history:
 *   1 — Initial setup (stub CourseEntity, no DAO)
 *   2 — Phase 6: CourseEntity schema finalized, CourseDao added
 */
@Database(
    entities = [CourseEntity::class],
    version = 2,
    exportSchema = false,
)
@TypeConverters(AppTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun courseDao(): CourseDao
}
