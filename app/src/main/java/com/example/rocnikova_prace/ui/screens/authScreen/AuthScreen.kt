package com.example.rocnikova_prace.ui.screens.authScreen

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.rocnikova_prace.MainScreen
import io.github.jan.supabase.compose.auth.composable.NativeSignInResult
import io.github.jan.supabase.compose.auth.composable.rememberSignInWithGoogle
import io.github.jan.supabase.compose.auth.composeAuth

enum class LoginOrRegister{
    LOGIN,
    REGISTER
}


@Composable
fun AuthScreen(
    viewModel: AuthViewModel,
    navController: NavController,
    supabase: io.github.jan.supabase.SupabaseClient
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

    val googleSignIn = supabase.composeAuth.rememberSignInWithGoogle(
        onResult = { result ->
            when (result) {
                is NativeSignInResult.Success -> {
                    navController.navigate("home_wrapper") {
                        popUpTo("auth_screen") { inclusive = true }
                    }
                }
                is NativeSignInResult.Error -> {
                    Toast.makeText(context, "Chyba: ${result.message}", Toast.LENGTH_LONG).show()
                }
                is NativeSignInResult.ClosedByUser -> {
                }
                is NativeSignInResult.NetworkError -> {
                    Toast.makeText(context, "Chyba sítě", Toast.LENGTH_SHORT).show()
                }
            }
        }
    )

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