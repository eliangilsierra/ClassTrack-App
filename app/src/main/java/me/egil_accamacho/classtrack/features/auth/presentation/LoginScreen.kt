package me.egil_accamacho.classtrack.features.auth.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.School
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import me.egil_accamacho.classtrack.navigation.Destination
import me.egil_accamacho.classtrack.ui.components.CtTextField
import me.egil_accamacho.classtrack.ui.components.PrimaryButton
import me.egil_accamacho.classtrack.ui.theme.ClasstrackTheme
import me.egil_accamacho.classtrack.ui.theme.CtPrimary
import me.egil_accamacho.classtrack.ui.theme.CtSpacing

/**
 * Login Screen — Figma node 1:901.
 * Email + password fields → POST /auth/login → navigate to role-based Home.
 */
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // Collect one-shot navigation actions
    LaunchedEffect(Unit) {
        viewModel.actions.collect { action ->
            when (action) {
                AuthAction.NavigateToHomeTeacher -> navController.navigate(Destination.HomeTeacher) {
                    popUpTo<Destination.Login> { inclusive = true }
                }
                AuthAction.NavigateToHomeStudent -> navController.navigate(Destination.HomeStudent) {
                    popUpTo<Destination.Login> { inclusive = true }
                }
                AuthAction.NavigateToRegister -> navController.navigate(Destination.Register)
                else -> Unit
            }
        }
    }

    // Show error in snackbar
    LaunchedEffect(state.generalError) {
        state.generalError?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onEvent(AuthEvent.DismissError)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LoginContent(
            state = state,
            onEvent = viewModel::onEvent,
        )
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}

@Composable
private fun LoginContent(
    state: AuthUiState,
    onEvent: (AuthEvent) -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .imePadding()
            .padding(horizontal = CtSpacing.xl, vertical = CtSpacing.xxxl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(CtSpacing.base),
    ) {
        // Logo
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(CtPrimary),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Rounded.School,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(40.dp),
            )
        }

        Spacer(Modifier.height(CtSpacing.sm))

        Text(
            text = "Bienvenido",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = "Inicia sesión para continuar",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(CtSpacing.lg))

        CtTextField(
            value = state.email,
            onValueChange = { onEvent(AuthEvent.EmailChanged(it)) },
            label = "Correo electrónico",
            isError = state.fieldErrors.containsKey(AuthField.EMAIL),
            errorMessage = state.fieldErrors[AuthField.EMAIL],
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) },
            ),
        )

        CtTextField(
            value = state.password,
            onValueChange = { onEvent(AuthEvent.PasswordChanged(it)) },
            label = "Contraseña",
            isPassword = true,
            isError = state.fieldErrors.containsKey(AuthField.PASSWORD),
            errorMessage = state.fieldErrors[AuthField.PASSWORD],
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    onEvent(AuthEvent.SubmitLogin)
                },
            ),
        )

        Spacer(Modifier.height(CtSpacing.sm))

        PrimaryButton(
            text = "Iniciar Sesión",
            onClick = { onEvent(AuthEvent.SubmitLogin) },
            loading = state.loading,
            modifier = Modifier.fillMaxWidth(),
        )

        TextButton(onClick = { onEvent(AuthEvent.NavigateToRegister) }) {
            Text(
                text = "¿No tienes cuenta? Regístrate",
                style = MaterialTheme.typography.bodyMedium,
                color = CtPrimary,
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF8FAFC)
@Composable
private fun LoginPreview() {
    ClasstrackTheme {
        LoginContent(
            state = AuthUiState(),
            onEvent = {},
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF8FAFC, name = "Login — with errors")
@Composable
private fun LoginErrorPreview() {
    ClasstrackTheme {
        LoginContent(
            state = AuthUiState(
                fieldErrors = mapOf(
                    AuthField.EMAIL to "Correo inválido",
                    AuthField.PASSWORD to "La contraseña es requerida",
                ),
            ),
            onEvent = {},
        )
    }
}
