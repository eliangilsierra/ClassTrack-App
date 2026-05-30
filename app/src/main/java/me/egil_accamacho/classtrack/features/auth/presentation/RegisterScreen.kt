package me.egil_accamacho.classtrack.features.auth.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import me.egil_accamacho.classtrack.features.auth.domain.model.UserRole
import me.egil_accamacho.classtrack.navigation.Destination
import me.egil_accamacho.classtrack.ui.components.CtTextField
import me.egil_accamacho.classtrack.ui.components.PrimaryButton
import me.egil_accamacho.classtrack.ui.theme.ClasstrackTheme
import me.egil_accamacho.classtrack.ui.theme.CtPrimary
import me.egil_accamacho.classtrack.ui.theme.CtSpacing

/**
 * Register Screen — Figma node 1:1038.
 * Full registration form → POST /auth/register → navigate to role-based Home.
 */
@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.actions.collect { action ->
            when (action) {
                AuthAction.NavigateToHomeTeacher -> navController.navigate(Destination.HomeTeacher) {
                    popUpTo<Destination.Login> { inclusive = true }
                }
                AuthAction.NavigateToHomeStudent -> navController.navigate(Destination.HomeStudent) {
                    popUpTo<Destination.Login> { inclusive = true }
                }
                AuthAction.NavigateToLogin -> navController.popBackStack()
                else -> Unit
            }
        }
    }

    LaunchedEffect(state.generalError) {
        state.generalError?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onEvent(AuthEvent.DismissError)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        RegisterContent(
            state = state,
            onEvent = viewModel::onEvent,
            onBack = { navController.popBackStack() },
        )
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}

@Composable
private fun RegisterContent(
    state: AuthUiState,
    onEvent: (AuthEvent) -> Unit,
    onBack: () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .imePadding(),
    ) {
        // Top bar with back button
        IconButton(
            onClick = onBack,
            modifier = Modifier.padding(start = CtSpacing.sm, top = CtSpacing.lg),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = "Volver",
                tint = MaterialTheme.colorScheme.onBackground,
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = CtSpacing.xl),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(CtSpacing.base),
        ) {
            Text(
                text = "Crear Cuenta",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                text = "Completa los datos para registrarte",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(CtSpacing.sm))

            CtTextField(
                value = state.fullName,
                onValueChange = { onEvent(AuthEvent.FullNameChanged(it)) },
                label = "Nombre completo",
                isError = state.fieldErrors.containsKey(AuthField.FULL_NAME),
                errorMessage = state.fieldErrors[AuthField.FULL_NAME],
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next,
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) },
                ),
            )

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
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) },
                ),
            )

            CtTextField(
                value = state.confirmPassword,
                onValueChange = { onEvent(AuthEvent.ConfirmPasswordChanged(it)) },
                label = "Confirmar contraseña",
                isPassword = true,
                isError = state.fieldErrors.containsKey(AuthField.CONFIRM_PASSWORD),
                errorMessage = state.fieldErrors[AuthField.CONFIRM_PASSWORD],
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        onEvent(AuthEvent.SubmitRegister)
                    },
                ),
            )

            // Role selector
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(CtSpacing.xs),
            ) {
                Text(
                    text = "Rol",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(CtSpacing.sm)) {
                    UserRole.entries.forEach { role ->
                        FilterChip(
                            selected = state.role == role,
                            onClick = { onEvent(AuthEvent.RoleChanged(role)) },
                            label = { Text(role.label) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = CtPrimary,
                                selectedLabelColor = androidx.compose.ui.graphics.Color.White,
                            ),
                        )
                    }
                }
            }

            Spacer(Modifier.height(CtSpacing.sm))

            PrimaryButton(
                text = "Crear Cuenta",
                onClick = { onEvent(AuthEvent.SubmitRegister) },
                loading = state.loading,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(CtSpacing.lg))
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF8FAFC)
@Composable
private fun RegisterPreview() {
    ClasstrackTheme {
        RegisterContent(
            state = AuthUiState(),
            onEvent = {},
            onBack = {},
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF8FAFC, name = "Register — with errors")
@Composable
private fun RegisterErrorPreview() {
    ClasstrackTheme {
        RegisterContent(
            state = AuthUiState(
                fieldErrors = mapOf(
                    AuthField.FULL_NAME to "El nombre es requerido",
                    AuthField.EMAIL to "Correo inválido",
                    AuthField.PASSWORD to "Mínimo 8 caracteres",
                    AuthField.CONFIRM_PASSWORD to "Las contraseñas no coinciden",
                ),
            ),
            onEvent = {},
            onBack = {},
        )
    }
}
