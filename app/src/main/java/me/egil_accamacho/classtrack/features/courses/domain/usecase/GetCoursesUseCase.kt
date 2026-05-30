package me.egil_accamacho.classtrack.features.courses.domain.usecase

import me.egil_accamacho.classtrack.core.common.Resource
import me.egil_accamacho.classtrack.features.courses.domain.model.Course
import me.egil_accamacho.classtrack.features.courses.domain.repository.CourseRepository
import javax.inject.Inject

class GetCoursesUseCase @Inject constructor(private val repository: CourseRepository) {
    suspend operator fun invoke(): Resource<List<Course>> = repository.getCourses()
}
