package me.egil_accamacho.classtrack.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.egil_accamacho.classtrack.core.common.Constants
import me.egil_accamacho.classtrack.core.session.DataStoreSessionManager
import me.egil_accamacho.classtrack.core.session.SessionManager
import me.egil_accamacho.classtrack.data.local.AppDatabase
import javax.inject.Singleton

// ── DataStore extension property ──────────────────────────────────────────────
private val Context.sessionDataStore: DataStore<Preferences> by preferencesDataStore(
    name = Constants.SESSION_DATASTORE
)

// ── Module ────────────────────────────────────────────────────────────────────

@Module
@InstallIn(SingletonComponent::class)
abstract class StorageModule {

    /** Bind the concrete [DataStoreSessionManager] to its interface. */
    @Binds
    @Singleton
    abstract fun bindSessionManager(impl: DataStoreSessionManager): SessionManager

    companion object {

        @Provides
        @Singleton
        fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
            context.sessionDataStore

        @Provides
        @Singleton
        fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
            Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "classtrack.db"
            )
                .fallbackToDestructiveMigration()  // acceptable for academic MVP
                .build()
    }
}
