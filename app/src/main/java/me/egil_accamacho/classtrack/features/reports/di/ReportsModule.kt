package me.egil_accamacho.classtrack.features.reports.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.egil_accamacho.classtrack.features.reports.data.remote.ReportsApi
import me.egil_accamacho.classtrack.features.reports.data.repository.ReportsRepositoryImpl
import me.egil_accamacho.classtrack.features.reports.domain.repository.ReportsRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ReportsModule {

    @Binds
    @Singleton
    abstract fun bindReportsRepository(impl: ReportsRepositoryImpl): ReportsRepository

    companion object {

        @Provides
        @Singleton
        fun provideReportsApi(retrofit: Retrofit): ReportsApi =
            retrofit.create(ReportsApi::class.java)
    }
}
