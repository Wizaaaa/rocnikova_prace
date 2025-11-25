package com.example.rocnikova_prace

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.rocnikova_prace.ui.screens.ProfileScreen
import com.example.rocnikova_prace.ui.screens.QuestionsScreen
import com.example.rocnikova_prace.ui.components.NavBar
import com.example.rocnikova_prace.ui.screens.createInformation.CreateInformation
import com.example.rocnikova_prace.ui.screens.createInformation.CreateInformationViewModel
import com.example.rocnikova_prace.ui.screens.createScreen.CreateScreen
import com.example.rocnikova_prace.ui.screens.createScreen.CreateScreenViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable


enum class MainScreen {
    Create,
    Questions,
    Profile,
    CreateInformation
}

val bottomScreens = listOf(
    MainScreen.Questions,
    MainScreen.Create,
    MainScreen.Profile
)


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainScreen(navController: NavHostController = rememberNavController()) {
    val createScreenViewModel: CreateScreenViewModel = viewModel()
    val createInformationViewModel: CreateInformationViewModel = viewModel()

    Scaffold(
        bottomBar = { NavBar(
            navController,
            viewModel = viewModel()
        ) }
    ) { innerPadding ->
        AnimatedNavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = MainScreen.Create.name,
            enterTransition = {
                val prevIndex = bottomScreens.indexOf(MainScreen.valueOf(initialState.destination.route!!))
                val newIndex = bottomScreens.indexOf(MainScreen.valueOf(targetState.destination.route!!))

                if (newIndex > prevIndex) {
                    slideInHorizontally(tween(300)) { it } + fadeIn()
                } else {
                    slideInHorizontally(tween(300)) { -it } + fadeIn()
                }
            },

            exitTransition = {
                val prevIndex = bottomScreens.indexOf(MainScreen.valueOf(initialState.destination.route!!))
                val newIndex = bottomScreens.indexOf(MainScreen.valueOf(targetState.destination.route!!))

                if (newIndex > prevIndex) {
                    slideOutHorizontally(tween(300)) { -it } + fadeOut()
                } else {
                    slideOutHorizontally(tween(300)) { it } + fadeOut()
                }
            },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            composable(MainScreen.Questions.name) { QuestionsScreen() }
            composable(MainScreen.Create.name) { CreateScreen(
                viewModel = createScreenViewModel,
                createInformationViewModel = createInformationViewModel
            ) }

            composable(MainScreen.Profile.name) { ProfileScreen() }


            composable(MainScreen.CreateInformation.name) { CreateInformation(
                viewModel = createInformationViewModel,
                createScreenViewModel = createScreenViewModel
            ) }
        }
    }
}
