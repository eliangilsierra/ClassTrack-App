package me.egil_accamacho.classtrack.features.courses.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.egil_accamacho.classtrack.data.local.AppDatabase
import me.egil_accamacho.classtrack.features.courses.data.local.CourseDao
import me.egil_accamacho.classtrack.features.courses.data.remote.CoursesApi
import me.egil_accamacho.classtrack.features.courses.data.repository.CourseRepositoryImpl
import me.egil_accamacho.classtrack.features.courses.domain.repository.CourseRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CoursesModule {

    @Binds
    @Singleton
    abstract fun bindCourseRepository(impl: CourseRepositoryImpl): CourseRepository

    companion object {

        @Provides
        @Singleton
        fun provideCoursesApi(retrofit: Retrofit): CoursesApi =
            retrofit.create(CoursesApi::class.java)

        @Provides
        @Singleton
        fun provideCourseDao(db: AppDatabase): CourseDao = db.courseDao()
    }
}
