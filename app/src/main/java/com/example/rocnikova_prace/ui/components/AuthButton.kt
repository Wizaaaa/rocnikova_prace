package com.example.rocnikova_prace.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rocnikova_prace.R
import com.example.rocnikova_prace.ui.theme.Dimens

@Composable
fun AuthButton(
    onClick: () -> Unit,
    icon: Boolean = false,
    text: String,
    enable: Boolean = true,
    filled: Boolean = false
) {
    OutlinedButton(
        onClick = { onClick() },
        shape = RoundedCornerShape(10.dp),
        enabled = enable,
        colors = if (filled) {
            ButtonDefaults.buttonColors()
        } else {
            ButtonDefaults.outlinedButtonColors()
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icon) {
                Image(
                    painter = painterResource(R.drawable.ic_google),
                    contentDescription = "Google icon",
                    modifier = Modifier.size(Dimens.extraLarge)
                )

                Spacer(Modifier.width(12.dp))
            }

            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}