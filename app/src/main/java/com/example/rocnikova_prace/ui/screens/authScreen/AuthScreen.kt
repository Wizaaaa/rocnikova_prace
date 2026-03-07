package com.example.rocnikova_prace.ui.screens.authScreen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.rocnikova_prace.MainScreen
import com.example.rocnikova_prace.ui.components.AuthButton
import com.example.rocnikova_prace.ui.components.InformationCard
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.OTP
import io.github.jan.supabase.compose.auth.composable.NativeSignInResult
import io.github.jan.supabase.compose.auth.composable.rememberSignInWithGoogle
import io.github.jan.supabase.compose.auth.composeAuth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc
import kotlinx.coroutines.launch

enum class AuthStep {
    ENTER_EMAIL,
    REGISTER_NAME,
    CHECK_EMAIL
}


@Composable
fun AuthScreen(
    viewModel: AuthViewModel,
    navController: NavController,
    supabase: io.github.jan.supabase.SupabaseClient
) {
    val authState by viewModel.authState.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var currentStep by remember { mutableStateOf(AuthStep.ENTER_EMAIL) }

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
                is NativeSignInResult.ClosedByUser -> { }
                is NativeSignInResult.NetworkError -> {
                    Toast.makeText(context, "Chyba sítě", Toast.LENGTH_SHORT).show()
                }
            }
        }
    )

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (currentStep) {
            // -----------------------------------------
            // KROK 1: ZADÁNÍ E-MAILU
            // -----------------------------------------
            AuthStep.ENTER_EMAIL -> {
                InformationCard(
                    value = viewModel.email,
                    onValueChange = { viewModel.updateEmail(it) },
                    label = "Zadejte email"
                )

                Spacer(modifier = Modifier.height(16.dp))

                AuthButton(
                    onClick = {
                        scope.launch {
                            try {
                                val exists = supabase.postgrest.rpc(
                                    function = "check_email_exists",
                                    parameters = mapOf("lookup_email" to viewModel.email)
                                ).decodeAs<Boolean>()

                                if (exists) {
                                    supabase.auth.signInWith(OTP) {
                                        email = viewModel.email
                                    }
                                    currentStep = AuthStep.CHECK_EMAIL
                                } else {
                                    currentStep = AuthStep.REGISTER_NAME
                                }
                            } catch (_: Exception) {
                                Toast.makeText(context, "Chyba připojení", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    enable = authState !is AuthState.Loading && viewModel.email.isNotBlank(),
                    text = "Pokračovat",
                    filled = true
                )

                // Divider
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(top = 18.dp, bottom = 18.dp)
                ) {
                    HorizontalDivider(Modifier.weight(1f))

                    Text(text = "nebo", modifier = Modifier.padding(horizontal = 16.dp), color = Color.Gray)

                    HorizontalDivider(Modifier.weight(1f))
                }

                // Google auth
                AuthButton(
                    onClick = { googleSignIn.startFlow() },
                    icon = true,
                    text = "Continue with Google"
                )
            }

            // -----------------------------------------
            // KROK 2: REGISTRACE JMÉNA (Pro nové uživatele)
            // -----------------------------------------
            AuthStep.REGISTER_NAME -> {
                Text("Vypadá to, že tu jste noví! Jak vám máme říkat?")
                Spacer(modifier = Modifier.height(16.dp))

                AuthName(
                    viewModel = viewModel,
                    authState = authState,
                    onClick = {
                        viewModel.sendMagicLink()
                        currentStep = AuthStep.CHECK_EMAIL
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))
                TextButton(onClick = { currentStep = AuthStep.ENTER_EMAIL }) {
                    Text("Zpět na e-mail")
                }
            }

            // -----------------------------------------
            // KROK 3: ČEKÁNÍ NA KLIKNUTÍ V E-MAILU
            // -----------------------------------------
            AuthStep.CHECK_EMAIL -> {
                Text(
                    text = "Zkontrolujte si e-mail!",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Poslali jsme vám přihlašovací odkaz na adresu\n${viewModel.email}\n\nStačí na něj kliknout a aplikace se sama přihlásí.",
                    textAlign = TextAlign.Center,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(24.dp))

                TextButton(onClick = { currentStep = AuthStep.ENTER_EMAIL }) {
                    Text("Zadal jsem špatný e-mail")
                }
            }
        }
    }
}