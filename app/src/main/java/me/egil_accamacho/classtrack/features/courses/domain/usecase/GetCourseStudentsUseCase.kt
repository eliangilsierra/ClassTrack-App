package me.egil_accamacho.classtrack.features.courses.domain.usecase

import me.egil_accamacho.classtrack.core.common.Resource
import me.egil_accamacho.classtrack.features.courses.domain.model.StudentSummary
import me.egil_accamacho.classtrack.features.courses.domain.repository.CourseRepository
import javax.inject.Inject

class GetCourseStudentsUseCase @Inject constructor(private val repository: CourseRepository) {
    suspend operator fun invoke(courseId: Long): Resource<List<StudentSummary>> =
        repository.getCourseStudents(courseId)
}
