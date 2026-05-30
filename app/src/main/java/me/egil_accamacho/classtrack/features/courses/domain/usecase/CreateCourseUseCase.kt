package me.egil_accamacho.classtrack.features.courses.domain.usecase

import me.egil_accamacho.classtrack.core.common.Resource
import me.egil_accamacho.classtrack.features.courses.domain.model.Course
import me.egil_accamacho.classtrack.features.courses.domain.repository.CourseRepository
import javax.inject.Inject

class CreateCourseUseCase @Inject constructor(private val repository: CourseRepository) {
    suspend operator fun invoke(name: String, description: String): Resource<Course> =
        repository.createCourse(name, description)
}
