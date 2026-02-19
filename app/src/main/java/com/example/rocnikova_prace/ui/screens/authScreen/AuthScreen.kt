package com.example.rocnikova_prace.ui.screens.authScreen

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.rocnikova_prace.MainScreen
import com.example.rocnikova_prace.ui.components.InformationCard

@Composable
fun AuthScreen(
    viewModel: AuthViewModel,
    navController: NavController
) {
    val authState by  viewModel.authState.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(authState) {
        when (val state = authState) {
            is AuthState.Success -> {
                navController.navigate("home_wrapper") {
                    popUpTo(MainScreen.AuthScreen.name) { inclusive = true }
                }
                viewModel.resetState()
            }
            is AuthState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                viewModel.resetState()
            }
            else -> {  }
        }
    }

    Column() {
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
            label = "Zadejte heslo"
        )

        Button(
            onClick = {
                viewModel.signIn(email = viewModel.email, password = viewModel.password)
            },
            enabled = authState !is AuthState.Loading
        ) {
            if (authState is AuthState.Loading) {
                CircularProgressIndicator(color = Color.White)
            } else {
                Text("Přihlásit se")
            }
        }

        if (authState is AuthState.Error) {
            val errorMsg = (authState as AuthState.Error).message
            Text(text = errorMsg, color = Color.Red)
        }
    }
}