package com.example.rocnikova_prace.ui.screens.authScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.rocnikova_prace.R
import com.example.rocnikova_prace.ui.components.AuthButton
import com.example.rocnikova_prace.ui.components.InformationCard

@Composable
fun AuthName(
    viewModel: AuthViewModel,
    authState: AuthState,
    onClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .safeDrawingPadding()
            .padding(5.dp)
    ) {
        Text(stringResource(R.string.AS_what_is_your_name))

        InformationCard(
            value = viewModel.name,
            onValueChange = {
                viewModel.updateName(it)
            },
            label = stringResource(R.string.AS_enter_username)
        )

        Spacer(modifier = Modifier.height(16.dp))

        AuthButton(
            onClick = { onClick() },
            enable = authState !is AuthState.Loading && viewModel.name.isNotBlank(),
            filled = true,
            text = stringResource(R.string.AS_continue)
        )
    }
}