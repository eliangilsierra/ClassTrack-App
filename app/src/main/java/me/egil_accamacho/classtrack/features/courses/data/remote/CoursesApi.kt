package me.egil_accamacho.classtrack.features.courses.data.remote

import me.egil_accamacho.classtrack.features.courses.data.remote.dto.CourseDetailResponse
import me.egil_accamacho.classtrack.features.courses.data.remote.dto.CourseListItemResponse
import me.egil_accamacho.classtrack.features.courses.data.remote.dto.CreateCourseRequest
import me.egil_accamacho.classtrack.features.courses.data.remote.dto.LinkStudentRequest
import me.egil_accamacho.classtrack.features.courses.data.remote.dto.StudentSummaryResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CoursesApi {
    @GET("courses")
    suspend fun getCourses(): List<CourseListItemResponse>

    @GET("courses/{courseId}")
    suspend fun getCourseDetail(@Path("courseId") courseId: Long): CourseDetailResponse

    @POST("courses")
    suspend fun createCourse(@Body request: CreateCourseRequest): CourseDetailResponse

    @DELETE("courses/{courseId}")
    suspend fun deleteCourse(@Path("courseId") courseId: Long)

    @GET("courses/{courseId}/students")
    suspend fun getCourseStudents(@Path("courseId") courseId: Long): List<StudentSummaryResponse>

    @POST("courses/{courseId}/students")
    suspend fun linkStudent(
        @Path("courseId") courseId: Long,
        @Body request: LinkStudentRequest,
    )
}
