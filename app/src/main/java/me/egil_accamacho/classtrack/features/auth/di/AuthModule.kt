package me.egil_accamacho.classtrack.features.auth.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.egil_accamacho.classtrack.features.auth.data.remote.AuthApi
import me.egil_accamacho.classtrack.features.auth.data.repository.AuthRepositoryImpl
import me.egil_accamacho.classtrack.features.auth.domain.repository.AuthRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    companion object {

        @Provides
        @Singleton
        fun provideAuthApi(retrofit: Retrofit): AuthApi =
            retrofit.create(AuthApi::class.java)
    }
}
