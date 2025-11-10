package com.example.rocnikova_prace.ui.components

import androidx.annotation.StringRes
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rocnikova_prace.R
import com.woowla.compose.icon.collections.heroicons.Heroicons
import com.woowla.compose.icon.collections.heroicons.heroicons.Outline
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.Plus


@Composable
fun Cards(
    icon: ImageVector,
    @StringRes text: Int,
    modifier: Modifier = Modifier
) {
    var isClicked by remember { mutableStateOf(false) }

    val elevation by animateDpAsState(
        targetValue = if (isClicked) 2.dp else 8.dp,
        animationSpec = tween(durationMillis = 150)
    )

    val scale by animateFloatAsState(
        targetValue = if (isClicked) 0.97f else 1f,
        animationSpec = tween(durationMillis = 150)
    )

    ElevatedCard(
        onClick = { isClicked = !isClicked },
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isClicked = true
                        tryAwaitRelease()  // čeká, dokud uživatel nepustí prst
                        isClicked = false
                    }
                )
            }
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale
            ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = elevation),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(10.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = stringResource(text),
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}


@Preview
@Composable
fun PreviewCard() {
    Cards(
        text = R.string.new_questions,
        icon = Heroicons.Outline.Plus
    )
}