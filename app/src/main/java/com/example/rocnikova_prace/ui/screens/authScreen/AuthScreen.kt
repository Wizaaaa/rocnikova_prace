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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.rocnikova_prace.MainScreen
import com.example.rocnikova_prace.R
import com.example.rocnikova_prace.ui.components.AuthButton
import com.example.rocnikova_prace.ui.components.InformationCard
import com.example.rocnikova_prace.ui.theme.Dimens
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
            is AuthState.InvalidEmail -> {
                Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                currentStep = AuthStep.ENTER_EMAIL
                viewModel.resetState()
            }
            else -> {  }
        }
    }

    val networkErrorText = stringResource(R.string.AS_network_error)
    val errorTextTemplate = stringResource(R.string.AS_error)

    val googleSignIn = supabase.composeAuth.rememberSignInWithGoogle(
        onResult = { result ->
            when (result) {
                is NativeSignInResult.Success -> {
                    navController.navigate("home_wrapper") {
                        popUpTo("auth_screen") { inclusive = true }
                    }
                }
                is NativeSignInResult.Error -> {
                    Toast.makeText(context, errorTextTemplate.format(result.message), Toast.LENGTH_LONG).show()
                }
                is NativeSignInResult.ClosedByUser -> { }
                is NativeSignInResult.NetworkError -> {
                    Toast.makeText(context, networkErrorText, Toast.LENGTH_SHORT).show()
                }
            }
        }
    )

    val connectionErrorText = stringResource(R.string.AS_connection_error)
    val invalidEmailText = stringResource(R.string.AS_invalid_email_error)

    Column(
        modifier = Modifier.fillMaxSize().padding(Dimens.medium),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (currentStep) {
            // -----------------------------------------
            // STEP 1: ENTER EMAIL
            // -----------------------------------------
            AuthStep.ENTER_EMAIL -> {
                InformationCard(
                    value = viewModel.email,
                    onValueChange = { viewModel.updateEmail(it) },
                    label = stringResource(R.string.AS_enter_email)
                )

                Spacer(modifier = Modifier.height(Dimens.medium))

                AuthButton(
                    onClick = {
                        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(viewModel.email.trim()).matches()) {
                            Toast.makeText(context, invalidEmailText, Toast.LENGTH_SHORT).show()
                            return@AuthButton
                        }

                        scope.launch {
                            try {
                                val exists = supabase.postgrest.rpc(
                                    function = "check_email_exists",
                                    parameters = mapOf("lookup_email" to viewModel.email.trim())
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
                                Toast.makeText(context, connectionErrorText, Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    enable = authState !is AuthState.Loading && viewModel.email.isNotBlank(),
                    text = stringResource(R.string.AS_continue),
                    filled = true
                )

                // Divider
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(top = 18.dp, bottom = 18.dp)
                ) {
                    HorizontalDivider(Modifier.weight(1f))

                    Text(text = stringResource(R.string.AS_or), modifier = Modifier.padding(horizontal = Dimens.medium), color = MaterialTheme.colorScheme.onSurfaceVariant)

                    HorizontalDivider(Modifier.weight(1f))
                }

                // Google auth
                AuthButton(
                    onClick = { googleSignIn.startFlow() },
                    icon = true,
                    text = stringResource(R.string.AS_continue_with_google)
                )
            }

            // -----------------------------------------
            // STEP 2: REGISTER NAME (For new users)
            // -----------------------------------------
            AuthStep.REGISTER_NAME -> {
                Text(stringResource(R.string.AS_looks_like_new_user))
                Spacer(modifier = Modifier.height(Dimens.medium))

                AuthName(
                    viewModel = viewModel,
                    authState = authState,
                    onClick = {
                        viewModel.sendMagicLink(
                            onSuccess = {
                                currentStep = AuthStep.CHECK_EMAIL
                            }
                        )
                    }
                )

                Spacer(modifier = Modifier.height(Dimens.medium))
                TextButton(onClick = { currentStep = AuthStep.ENTER_EMAIL }) {
                    Text(stringResource(R.string.AS_back_to_email))
                }
            }

            // -----------------------------------------
            // STEP 3: WAIT FOR EMAIL VERIFICATION
            // -----------------------------------------
            AuthStep.CHECK_EMAIL -> {
                Text(
                    text = stringResource(R.string.AS_check_email_title),
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(Dimens.small))
                Text(
                    text = stringResource(R.string.AS_check_email_body, viewModel.email),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(Dimens.extraLarge))

                TextButton(onClick = { currentStep = AuthStep.ENTER_EMAIL }) {
                    Text(stringResource(R.string.AS_wrong_email))
                }
            }
        }
    }
}