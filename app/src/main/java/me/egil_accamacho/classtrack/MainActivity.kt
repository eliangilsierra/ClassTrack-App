package me.egil_accamacho.classtrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import me.egil_accamacho.classtrack.navigation.ClassTrackNavGraph
import me.egil_accamacho.classtrack.navigation.RootViewModel
import me.egil_accamacho.classtrack.ui.theme.ClasstrackTheme

/**
 * Single Activity host — owns [RootViewModel] and passes session state down
 * to [ClassTrackNavGraph] for role-aware bottom navigation.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val rootViewModel: RootViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ClasstrackTheme {
                val sessionState by rootViewModel.sessionState.collectAsStateWithLifecycle()
                ClassTrackNavGraph(
                    sessionState = sessionState,
                    navigationEvent = rootViewModel.navigationEvent,
                )
            }
        }
    }
}
