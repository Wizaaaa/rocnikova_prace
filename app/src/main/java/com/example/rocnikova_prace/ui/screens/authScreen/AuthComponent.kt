package com.example.rocnikova_prace.ui.screens.authScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.example.rocnikova_prace.ui.components.InformationCard

@Composable
fun AuthComponent(
    viewModel: AuthViewModel,
    type: LoginOrRegister,
    authState: AuthState,
    onClick: () -> Unit
) {
    Column {
        InformationCard(
            value = viewModel.email,
            onValueChange = {
                viewModel.updateEmail(it)
            },
            label = "Zadejte email"
        )

        InformationCard(
            value = viewModel.password,
            onValueChange = {
                viewModel.updatePassword(it)
            },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            label = "Zadejte heslo"
        )

        if (type == LoginOrRegister.REGISTER) {
            InformationCard(
                value = viewModel.confirmPassword,
                onValueChange = {
                    viewModel.updateConfirmPassword(it)
                },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                label = "Zopakujte heslo"
            )
        } else {
            TextButton(
                onClick = { viewModel.setupRegister(false) }
            ) {
                Text("Zaregistrovat se")
            }
        }


        Button(
            onClick = { onClick() },
            enabled = authState !is AuthState.Loading
        ) {
            if (authState is AuthState.Loading) {
                CircularProgressIndicator(color = Color.White)
            } else {
                Text("Přihlásit se")
            }
        }

        if (authState is AuthState.Error) {
            val errorMsg = authState.message
            Text(text = errorMsg, color = Color.Red)
        }
    }
}