package me.egil_accamacho.classtrack.features.courses.domain.repository

import me.egil_accamacho.classtrack.core.common.Resource
import me.egil_accamacho.classtrack.features.courses.domain.model.Course
import me.egil_accamacho.classtrack.features.courses.domain.model.StudentSummary

interface CourseRepository {
    suspend fun getCourses(): Resource<List<Course>>
    suspend fun getCourseDetail(courseId: Long): Resource<Course>
    suspend fun createCourse(name: String, description: String): Resource<Course>
    suspend fun deleteCourse(courseId: Long): Resource<Unit>
    suspend fun getCourseStudents(courseId: Long): Resource<List<StudentSummary>>
    suspend fun linkStudent(courseId: Long, studentId: Long): Resource<Unit>
}
