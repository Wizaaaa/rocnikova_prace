package com.example.rocnikova_prace.ui.screens.datailProfile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.woowla.compose.icon.collections.heroicons.Heroicons
import com.woowla.compose.icon.collections.heroicons.heroicons.Outline
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.Bell
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.ChevronRight
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.ShieldCheck
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.Trash
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.User

@Composable
fun DetailProfile(
    viewModel: DetailProfileViewModel,
    modifier: Modifier = Modifier
) {
    val totalProgress = viewModel.totalProgress

    val context = LocalContext.current

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                val inputStream = context.contentResolver.openInputStream(uri)
                val bytes = inputStream?.readBytes()

                if (bytes != null) {
                    viewModel.uploadNewAvatar(bytes)
                }
            }
        }
    )

    LazyColumn(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Surface(
                    shape = CircleShape,
                    modifier = Modifier.size(100.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    AsyncImage(
                        model = viewModel.userAvatar,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable {
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = viewModel.mail, color = Color.Gray)
            }
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp)
            ) {
                Row(
                    modifier = Modifier.padding(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.size(80.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            progress = { totalProgress / 100f },
                            modifier = Modifier.fillMaxSize(),
                            strokeWidth = 8.dp,
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                        Text(
                            text = "${totalProgress.toInt()}%",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(24.dp))
                    Column {
                        Text(text = "Celková úroveň", fontWeight = FontWeight.Bold)
                        Text(
                            text = "Průměrná úspěšnost napříč všemi tématy.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Nastavení aplikace",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier.clip(RoundedCornerShape(16.dp))
            ) {
                SettingsItem(icon = Heroicons.Outline.User, title = "Změnit jméno")
                SettingsItem(
                    icon = Heroicons.Outline.Bell,
                    title = "Notifikace",
                    showSwitch = true,
                    switchState = viewModel.notificationsEnabled,
                    onClick = {
                        viewModel.changeNotificationsSettings()
                    }
                )
                SettingsItem(icon = Heroicons.Outline.ShieldCheck, title = "Soukromí")
                SettingsItem(icon = Heroicons.Outline.Trash, title = "Smazat data", isDanger = true)
            }
        }
    }
}

@Composable
private fun SettingsItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit = {},
    isDanger: Boolean = false,
    showSwitch: Boolean = false,
    switchState: Boolean = false
) {
    val color = if (isDanger) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface

    Surface(
        onClick = onClick,
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color.copy(alpha = 0.7f),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = color,
                modifier = Modifier.weight(1f)
            )

            when {
                isDanger -> {  }
                showSwitch -> {
                    androidx.compose.material3.Switch(
                        checked = switchState,
                        onCheckedChange = { onClick() }
                    )
                }
                else -> {
                    Icon(
                        imageVector = Heroicons.Outline.ChevronRight,
                        contentDescription = null,
                        tint = Color.LightGray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}