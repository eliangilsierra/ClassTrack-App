package me.egil_accamacho.classtrack.features.courses.domain.usecase

import me.egil_accamacho.classtrack.core.common.Resource
import me.egil_accamacho.classtrack.features.courses.domain.repository.CourseRepository
import javax.inject.Inject

class LinkStudentToCourseUseCase @Inject constructor(private val repository: CourseRepository) {
    suspend operator fun invoke(courseId: Long, studentId: Long): Resource<Unit> =
        repository.linkStudent(courseId, studentId)
}
