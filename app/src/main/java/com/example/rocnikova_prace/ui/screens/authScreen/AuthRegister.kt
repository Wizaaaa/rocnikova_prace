package com.example.rocnikova_prace.ui.screens.authScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.rocnikova_prace.ui.components.AuthButton
import com.example.rocnikova_prace.ui.components.InformationCard

@Composable
fun AuthRegister(
    viewModel: AuthViewModel,
    authState: AuthState,
    onClick: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .safeDrawingPadding()
            .padding(5.dp)
    ) {
        InformationCard(
            value = viewModel.password,
            onValueChange = {
                viewModel.updatePassword(it)
            },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            label = "Zadejte heslo"
        )

        InformationCard(
            value = viewModel.confirmPassword,
            onValueChange = {
                viewModel.updateConfirmPassword(it)
            },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            label = "Zopakujte heslo"
        )

        Spacer(modifier = Modifier.height(16.dp))

        AuthButton(
            onClick = { onClick() },
            enable = authState !is AuthState.Loading,
            filled = true,
            text = "Registrovat se"
        )

        if (authState is AuthState.Error) {
            val errorMsg = authState.message
            Text(text = errorMsg, color = Color.Red)
        }
    }
}