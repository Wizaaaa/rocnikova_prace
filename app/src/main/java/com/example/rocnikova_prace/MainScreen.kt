package com.example.rocnikova_prace

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rocnikova_prace.ui.CreateScreen
import com.example.rocnikova_prace.ui.ProfileScreen
import com.example.rocnikova_prace.ui.QuestionsScreen
import com.example.rocnikova_prace.ui.components.NavBar

enum class MainScreen() {
    Create,
    Questions,
    Profile
}

@Composable
fun MainScreen(
    navController: NavHostController = rememberNavController()
) {
    Scaffold(
        bottomBar = { NavBar(navController) }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = MainScreen.Create.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = MainScreen.Create.name) {
                CreateScreen()
            }

            composable(route = MainScreen.Questions.name) {
                QuestionsScreen()
            }

            composable(route = MainScreen.Profile.name) {
                ProfileScreen()
            }
        }
    }
}