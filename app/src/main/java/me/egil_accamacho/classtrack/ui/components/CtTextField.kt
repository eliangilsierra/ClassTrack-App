package me.egil_accamacho.classtrack.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.egil_accamacho.classtrack.ui.theme.ClasstrackTheme
import me.egil_accamacho.classtrack.ui.theme.CtBorderLight
import me.egil_accamacho.classtrack.ui.theme.CtPrimary
import me.egil_accamacho.classtrack.ui.theme.CtSpacing
import me.egil_accamacho.classtrack.ui.theme.ShapePrimary

/**
 * ClassTrack outlined text field — 56dp height, 16dp radius, brand label color.
 * Figma: "Email Input" and "Password Input" on Login/Register screens.
 *
 * @param value        Current field value
 * @param onValueChange Callback with new value
 * @param label        Floating label text
 * @param modifier     Optional modifier
 * @param placeholder  Optional placeholder shown when field is empty and focused
 * @param isPassword   When true, toggles password visibility; sets keyboard to Password
 * @param isError      Shows error state (red outline + error text)
 * @param errorMessage Error message displayed below the field
 * @param keyboardOptions Keyboard configuration
 * @param keyboardActions Keyboard action callbacks
 * @param singleLine   Whether the field is single-line (default: true)
 * @param enabled      Whether the field is interactive
 * @param readOnly     Whether the field is read-only
 * @param leadingIcon  Optional leading icon composable
 */
@Composable
fun CtTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    isPassword: Boolean = false,
    isError: Boolean = false,
    errorMessage: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    var passwordVisible by remember { mutableStateOf(false) }

    val resolvedKeyboardOptions = if (isPassword) {
        keyboardOptions.copy(
            keyboardType = KeyboardType.Password,
            imeAction = keyboardOptions.imeAction.takeIf { it != ImeAction.Default }
                ?: ImeAction.Done,
        )
    } else {
        keyboardOptions
    }

    val visualTransformation = if (isPassword && !passwordVisible) {
        PasswordVisualTransformation()
    } else {
        VisualTransformation.None
    }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyLarge,
                )
            },
            placeholder = placeholder?.let {
                {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            },
            isError = isError,
            enabled = enabled,
            readOnly = readOnly,
            singleLine = singleLine,
            visualTransformation = visualTransformation,
            keyboardOptions = resolvedKeyboardOptions,
            keyboardActions = keyboardActions,
            shape = ShapePrimary,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = CtPrimary,
                unfocusedBorderColor = CtBorderLight,
                errorBorderColor = MaterialTheme.colorScheme.error,
                focusedLabelColor = CtPrimary,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                cursorColor = CtPrimary,
            ),
            leadingIcon = leadingIcon,
            trailingIcon = if (isPassword) {
                {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) {
                                Icons.Rounded.Visibility
                            } else {
                                Icons.Rounded.VisibilityOff
                            },
                            contentDescription = if (passwordVisible) {
                                "Ocultar contraseña"
                            } else {
                                "Mostrar contraseña"
                            },
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            } else {
                null
            },
            modifier = Modifier
                .fillMaxWidth()
                // Min height ~56dp is achieved by minHeight on the field itself;
                // OutlinedTextField default min height already satisfies this.
                .then(Modifier),
        )

        if (isError && !errorMessage.isNullOrBlank()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(
                    start = CtSpacing.base,
                    top = 4.dp,
                ),
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF8FAFC)
@Composable
private fun CtTextFieldPreview() {
    ClasstrackTheme {
        Column(
            modifier = Modifier.padding(CtSpacing.base),
            verticalArrangement = Arrangement.spacedBy(CtSpacing.base),
        ) {
            var email by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }

            CtTextField(
                value = email,
                onValueChange = { email = it },
                label = "Correo electrónico",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            )
            CtTextField(
                value = password,
                onValueChange = { password = it },
                label = "Contraseña",
                isPassword = true,
            )
            CtTextField(
                value = "correo@invalido",
                onValueChange = {},
                label = "Correo electrónico",
                isError = true,
                errorMessage = "Formato de correo inválido",
            )
        }
    }
}
