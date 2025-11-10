package com.example.rocnikova_prace

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.rocnikova_prace.ui.CreateScreen
import com.example.rocnikova_prace.ui.ProfileScreen
import com.example.rocnikova_prace.ui.QuestionsScreen
import com.example.rocnikova_prace.ui.components.NavBar
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController


enum class MainScreen() {
    Create,
    Questions,
    Profile
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainScreen(
    navController: NavHostController = rememberAnimatedNavController()
) {
    Scaffold(
        bottomBar = { NavBar(navController) }
    ) { innerPadding ->


        AnimatedNavHost(
            navController = navController,
            startDestination = MainScreen.Create.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(
                route = MainScreen.Create.name,
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { it }, // prava
                        animationSpec = tween(500)
                    )
                },
                exitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { -it }, // leva
                        animationSpec = tween(500)
                    )
                }
            ) {
                CreateScreen()
            }

            composable(
                route = MainScreen.Questions.name,
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { it }, // zprava
                        animationSpec = tween(500)
                    )
                },
                exitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { -it }, // doleva
                        animationSpec = tween(500)
                    )
                }
            ) {
                QuestionsScreen()
            }

            composable(
                route = MainScreen.Profile.name,
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { it }, // zprava
                        animationSpec = tween(500)
                    )
                },
                exitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { it }, // doprava
                        animationSpec = tween(500)
                    )
                }
            ) {
                ProfileScreen()
            }
        }
    }
}