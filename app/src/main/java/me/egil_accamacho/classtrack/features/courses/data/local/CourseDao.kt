package me.egil_accamacho.classtrack.features.courses.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import me.egil_accamacho.classtrack.data.local.entity.CourseEntity

@Dao
interface CourseDao {
    @Query("SELECT * FROM courses ORDER BY name ASC")
    fun observeAll(): Flow<List<CourseEntity>>

    @Query("SELECT * FROM courses ORDER BY name ASC")
    suspend fun getAll(): List<CourseEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(courses: List<CourseEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(course: CourseEntity)

    @Query("DELETE FROM courses WHERE id = :courseId")
    suspend fun deleteById(courseId: Long)

    @Query("DELETE FROM courses")
    suspend fun deleteAll()
}
