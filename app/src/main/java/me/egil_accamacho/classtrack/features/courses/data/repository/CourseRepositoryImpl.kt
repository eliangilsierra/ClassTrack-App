package me.egil_accamacho.classtrack.features.courses.data.repository

import android.util.Log
import me.egil_accamacho.classtrack.core.common.Resource
import me.egil_accamacho.classtrack.core.common.safeCall
import me.egil_accamacho.classtrack.core.network.ErrorMapper
import me.egil_accamacho.classtrack.data.local.entity.CourseEntity
import me.egil_accamacho.classtrack.features.courses.data.local.CourseDao
import me.egil_accamacho.classtrack.features.courses.data.remote.CoursesApi
import me.egil_accamacho.classtrack.features.courses.data.remote.dto.CreateCourseRequest
import me.egil_accamacho.classtrack.features.courses.data.remote.dto.LinkStudentRequest
import me.egil_accamacho.classtrack.features.courses.domain.model.Course
import me.egil_accamacho.classtrack.features.courses.domain.model.StudentSummary
import me.egil_accamacho.classtrack.features.courses.domain.repository.CourseRepository
import javax.inject.Inject

class CourseRepositoryImpl @Inject constructor(
    private val api: CoursesApi,
    private val dao: CourseDao,
    private val errorMapper: ErrorMapper,
) : CourseRepository {

    override suspend fun getCourses(): Resource<List<Course>> = safeCall(errorMapper) {
        val remote = api.getCourses().data ?: emptyList()
        val entities = remote.map { dto ->
            CourseEntity(id = dto.id, name = dto.name, studentCount = dto.studentCount)
        }
        dao.upsertAll(entities)
        remote.map { dto -> Course(id = dto.id, name = dto.name, studentCount = dto.studentCount) }
    }.recoverFromCache { dao.getAll().map { it.toDomain() } }

    override suspend fun getCourseDetail(courseId: Long): Resource<Course> =
        safeCall(errorMapper) {
            val dto = api.getCourseDetail(courseId).data
                ?: throw Exception("Detalle de curso vacío")
            dao.upsert(CourseEntity(id = dto.id, name = dto.name, description = dto.description, studentCount = dto.studentCount))
            Course(id = dto.id, name = dto.name, description = dto.description, studentCount = dto.studentCount)
        }

    override suspend fun createCourse(name: String, description: String): Resource<Course> =
        safeCall(errorMapper) {
            val dto = api.createCourse(CreateCourseRequest(name = name, description = description)).data
                ?: throw Exception("Respuesta de creación de curso vacía")
            val entity = CourseEntity(id = dto.id, name = dto.name, description = dto.description, studentCount = dto.studentCount)
            dao.upsert(entity)
            Course(id = dto.id, name = dto.name, description = dto.description, studentCount = dto.studentCount)
        }

    override suspend fun deleteCourse(courseId: Long): Resource<Unit> =
        safeCall(errorMapper) {
            api.deleteCourse(courseId)
            dao.deleteById(courseId)
        }

    override suspend fun getCourseStudents(courseId: Long): Resource<List<StudentSummary>> =
        safeCall(errorMapper) {
            (api.getCourseStudents(courseId).data ?: emptyList()).map { dto ->
                StudentSummary(id = dto.id, fullName = dto.fullName, studentCode = dto.studentCode)
            }
        }

    override suspend fun linkStudent(courseId: Long, studentId: Long): Resource<Unit> =
        safeCall(errorMapper) {
            api.linkStudent(courseId, LinkStudentRequest(studentId))
        }

    // ── Private helpers ───────────────────────────────────────────────────────

    private fun CourseEntity.toDomain() = Course(id = id, name = name, description = description, studentCount = studentCount)

    private suspend fun <T> Resource<T>.recoverFromCache(cacheBlock: suspend () -> T): Resource<T> {
        if (this is Resource.Error) {
            return try {
                Resource.Success(cacheBlock())
            } catch (e: Exception) {
                Log.e("CourseRepository", "Cache recovery failed", e)
                this
            }
        }
        return this
    }
}
