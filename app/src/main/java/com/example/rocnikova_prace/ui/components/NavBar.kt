package com.example.rocnikova_prace.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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

    NavigationBar {
        NavigationBarItem(
            selected = currentDestination == MainScreen.Questions.name,
            onClick = {
                navController.navigate(MainScreen.Questions.name)

            },
            icon = {
                val active = currentDestination == MainScreen.Questions.name

                val icon = if(active) Heroicons.Solid.Folder else Heroicons.Outline.Folder

                val iconColor by animateColorAsState(
                    targetValue = if (active) Color.Blue else Color.Gray,
                    animationSpec = tween(durationMillis = 300)
                )
                Icon(
                    imageVector = icon,
                    contentDescription = "folder",
                    tint = iconColor,
                    modifier = Modifier.size(32.dp)
                )
            }
        )

        NavigationBarItem(
            selected = currentDestination == MainScreen.Create.name,
            onClick = { navController.navigate(MainScreen.Create.name) },
            icon = {
                val active = currentDestination == MainScreen.Create.name

                val icon = if(active) Heroicons.Solid.PlusCircle else Heroicons.Outline.PlusCircle

                val iconColor by animateColorAsState(
                    targetValue = if (active) Color.Blue else Color.Gray,
                    animationSpec = tween(durationMillis = 300)
                )
                Icon(
                    imageVector = icon,
                    contentDescription = "create",
                    tint = iconColor,
                    modifier = Modifier.size(32.dp)
                )
            }
        )

        NavigationBarItem(
            selected = currentDestination == MainScreen.Profile.name,
            onClick = { navController.navigate(MainScreen.Profile.name) },
            icon = {
                val active = currentDestination == MainScreen.Profile.name

                val icon = if(active) Heroicons.Solid.User else Heroicons.Outline.User

                val iconColor by animateColorAsState(
                    targetValue = if (active) Color.Blue else Color.Gray,
                    animationSpec = tween(durationMillis = 300)
                )

                Icon(
                    imageVector = icon,
                    tint = iconColor,
                    contentDescription = "profile",
                    modifier = Modifier.size(32.dp)
                )
            }
        )
    }
}



//@Preview
//@Composable
//fun NavBarPreview(){
//    NavBar()
//}