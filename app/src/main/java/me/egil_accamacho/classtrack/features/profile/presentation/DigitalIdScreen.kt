package me.egil_accamacho.classtrack.features.profile.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import me.egil_accamacho.classtrack.ui.components.CtTopAppBar
import me.egil_accamacho.classtrack.ui.components.DigitalIdCard
import me.egil_accamacho.classtrack.ui.components.LoadingState
import me.egil_accamacho.classtrack.ui.theme.CtSpacing

@Composable
fun DigitalIdScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.onEvent(ProfileEvent.LoadDigitalId)
    }

    LaunchedEffect(state.error) {
        state.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onEvent(ProfileEvent.DismissError)
        }
    }

    Scaffold(
        topBar = {
            CtTopAppBar(
                title = "Identificación Digital",
                onBack = { navController.popBackStack() },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        if (state.loading && state.digitalId == null) {
            LoadingState(modifier = Modifier.padding(padding))
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState()),
            ) {
                Spacer(modifier = Modifier.height(CtSpacing.base))

                val digitalId = state.digitalId
                if (digitalId != null) {
                    DigitalIdCard(
                        fullName = digitalId.fullName,
                        email = state.profile?.email ?: "",
                        studentCode = digitalId.studentCode,
                        qrBitmap = state.qrBitmap,
                        modifier = Modifier.padding(horizontal = CtSpacing.base),
                    )
                }

                Spacer(modifier = Modifier.height(CtSpacing.xl))
            }
        }
    }
}
