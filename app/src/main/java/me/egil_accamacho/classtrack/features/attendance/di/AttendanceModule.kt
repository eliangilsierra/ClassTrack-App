package me.egil_accamacho.classtrack.features.attendance.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.egil_accamacho.classtrack.features.attendance.data.remote.AttendanceApi
import me.egil_accamacho.classtrack.features.attendance.data.repository.AttendanceRepositoryImpl
import me.egil_accamacho.classtrack.features.attendance.domain.repository.AttendanceRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AttendanceModule {

    @Binds
    @Singleton
    abstract fun bindAttendanceRepository(impl: AttendanceRepositoryImpl): AttendanceRepository

    companion object {

        @Provides
        @Singleton
        fun provideAttendanceApi(retrofit: Retrofit): AttendanceApi =
            retrofit.create(AttendanceApi::class.java)
    }
}
