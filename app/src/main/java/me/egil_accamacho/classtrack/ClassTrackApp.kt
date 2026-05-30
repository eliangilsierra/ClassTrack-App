package me.egil_accamacho.classtrack

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application entry point required by Hilt.
 * Registered in AndroidManifest.xml via android:name=".ClassTrackApp".
 */
@HiltAndroidApp
class ClassTrackApp : Application()
