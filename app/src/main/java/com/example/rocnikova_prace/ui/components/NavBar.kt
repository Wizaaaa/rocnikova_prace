package com.example.rocnikova_prace.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.rocnikova_prace.MainScreen
import com.woowla.compose.icon.collections.heroicons.Heroicons
import com.woowla.compose.icon.collections.heroicons.heroicons.Outline
import com.woowla.compose.icon.collections.heroicons.heroicons.Solid
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.Folder
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.PlusCircle
import com.woowla.compose.icon.collections.heroicons.heroicons.outline.User
import com.woowla.compose.icon.collections.heroicons.heroicons.solid.Folder
import com.woowla.compose.icon.collections.heroicons.heroicons.solid.PlusCircle
import com.woowla.compose.icon.collections.heroicons.heroicons.solid.User

@Composable
fun NavBar(
    selectedScreen: MainScreen,
    onScreenSelected: (MainScreen) -> Unit
){
    NavigationBar {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            NavBarIcon(
                screen = MainScreen.Questions,
                selectedScreen = selectedScreen,
                imageVectorActive = Heroicons.Solid.Folder,
                imageVectorOutline = Heroicons.Outline.Folder,
                contentDescription = "folder",
                onClick = onScreenSelected
            )

            NavBarIcon(
                screen = MainScreen.Create,
                selectedScreen = selectedScreen,
                imageVectorActive = Heroicons.Solid.PlusCircle,
                imageVectorOutline = Heroicons.Outline.PlusCircle,
                contentDescription = "create",
                onClick = onScreenSelected
            )

            NavBarIcon(
                screen = MainScreen.Profile,
                selectedScreen = selectedScreen,
                imageVectorActive = Heroicons.Solid.User,
                imageVectorOutline = Heroicons.Outline.User,
                contentDescription = "profile",
                onClick = onScreenSelected
            )
        }
    }
}

@Composable
fun NavBarIcon(
    screen: MainScreen,
    selectedScreen: MainScreen,
    imageVectorActive: androidx.compose.ui.graphics.vector.ImageVector,
    imageVectorOutline: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    onClick: (MainScreen) -> Unit
) {
    val active = selectedScreen == screen

    Box(
        modifier = Modifier
            .size(56.dp)
            .clickable(
                onClick = { onClick(screen) },
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = if (active) imageVectorActive else imageVectorOutline,
            contentDescription = contentDescription,
            tint = animateColor(active),
            modifier = Modifier.size(32.dp)
        )
    }
}


@Composable
private fun animateColor(active: Boolean): Color {
    val iconColor by animateColorAsState(
        targetValue = if (active) Color.Blue else Color.Gray,
        animationSpec = tween(durationMillis = 300)
    )

    return iconColor
}