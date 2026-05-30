package me.egil_accamacho.classtrack.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "courses")
data class CourseEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val description: String = "",
    val studentCount: Int = 0,
)
