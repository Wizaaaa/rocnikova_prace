package com.example.rocnikova_prace.ui.screens.authScreen

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.rocnikova_prace.MainScreen

enum class LoginOrRegister{
    LOGIN,
    REGISTER
}


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

    if (viewModel.isRegistered) {
        AuthComponent(
            viewModel = viewModel,
            type = LoginOrRegister.LOGIN,
            authState = authState,
            onClick = { viewModel.signIn(viewModel.email, viewModel.password) }
        )
    } else {
        AuthComponent(
            viewModel = viewModel,
            type = LoginOrRegister.REGISTER,
            authState = authState,
            onClick = { viewModel.signUp(viewModel.email, viewModel.password) }
        )
    }
}