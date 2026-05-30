package me.egil_accamacho.classtrack.features.courses.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import me.egil_accamacho.classtrack.navigation.Destination
import me.egil_accamacho.classtrack.ui.components.CtTextField
import me.egil_accamacho.classtrack.ui.components.CtTopAppBar
import me.egil_accamacho.classtrack.ui.components.PrimaryButton
import me.egil_accamacho.classtrack.ui.theme.CtSpacing

@Composable
fun CreateCourseScreen(
    navController: NavController,
    viewModel: CreateCourseViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.actions.collect { action ->
            when (action) {
                is CreateCourseAction.NavigateToDetail ->
                    navController.navigate(Destination.CourseDetail(action.courseId)) {
                        popUpTo(Destination.CreateCourse) { inclusive = true }
                    }
            }
        }
    }

    LaunchedEffect(state.error) {
        state.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onEvent(CreateCourseEvent.DismissError)
        }
    }

    Scaffold(
        topBar = {
            CtTopAppBar(
                title = "Crear Curso",
                onBack = { navController.popBackStack() },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(CtSpacing.base),
        ) {
            Spacer(modifier = Modifier.height(CtSpacing.sm))

            CtTextField(
                value = state.name,
                onValueChange = { viewModel.onEvent(CreateCourseEvent.NameChanged(it)) },
                label = "Nombre del curso",
                modifier = Modifier.fillMaxWidth(),
                isError = state.nameError != null,
                errorMessage = state.nameError,
            )

            Spacer(modifier = Modifier.height(CtSpacing.base))

            CtTextField(
                value = state.description,
                onValueChange = { viewModel.onEvent(CreateCourseEvent.DescriptionChanged(it)) },
                label = "Descripción (opcional)",
                modifier = Modifier.fillMaxWidth(),
                singleLine = false,
            )

            Spacer(modifier = Modifier.height(CtSpacing.xl))

            PrimaryButton(
                text = "Crear Curso",
                onClick = { viewModel.onEvent(CreateCourseEvent.Submit) },
                loading = state.loading,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
