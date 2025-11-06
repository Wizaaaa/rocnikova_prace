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
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
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
fun NavBar(navController: NavHostController){
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry.value?.destination?.route
    NavigationBar(
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            NavBarIcon(
                navController = navController,
                destination = MainScreen.Questions.name,
                currentDestination = currentDestination,
                imageVectorActive = Heroicons.Solid.Folder,
                imageVectorOutline = Heroicons.Outline.Folder,
                contentDescription = "folder"
            )

            NavBarIcon(
                navController = navController,
                destination = MainScreen.Create.name,
                currentDestination = currentDestination,
                imageVectorActive = Heroicons.Solid.PlusCircle,
                imageVectorOutline = Heroicons.Outline.PlusCircle,
                contentDescription = "create"
            )

            NavBarIcon(
                navController = navController,
                destination = MainScreen.Profile.name,
                currentDestination = currentDestination,
                imageVectorActive = Heroicons.Solid.User,
                imageVectorOutline = Heroicons.Outline.User,
                contentDescription = "profile"
            )
        }
    }
}

@Composable
fun NavBarIcon(
    navController: NavHostController,
    destination: String,
    currentDestination: String?,
    imageVectorActive: androidx.compose.ui.graphics.vector.ImageVector,
    imageVectorOutline: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String
) {
    val active = currentDestination == destination

    Box(
        modifier = Modifier
            .size(56.dp)
            .clickable(
                onClick = { navController.navigate(destination) },
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

//@Preview
//@Composable
//fun NavBarPreview(){
//    NavBar()
//}