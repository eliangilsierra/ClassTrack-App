package me.egil_accamacho.classtrack.features.profile.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.egil_accamacho.classtrack.features.profile.data.remote.ProfileApi
import me.egil_accamacho.classtrack.features.profile.data.repository.ProfileRepositoryImpl
import me.egil_accamacho.classtrack.features.profile.domain.repository.ProfileRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ProfileModule {

    @Binds
    @Singleton
    abstract fun bindProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository

    companion object {

        @Provides
        @Singleton
        fun provideProfileApi(retrofit: Retrofit): ProfileApi =
            retrofit.create(ProfileApi::class.java)
    }
}
