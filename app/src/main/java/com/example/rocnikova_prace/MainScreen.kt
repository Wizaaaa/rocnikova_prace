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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.rocnikova_prace.ui.components.NavBar
import com.example.rocnikova_prace.ui.components.TopAppBar
import com.example.rocnikova_prace.ui.screens.ProfileScreen
import com.example.rocnikova_prace.ui.screens.createInformation.CreateInformation
import com.example.rocnikova_prace.ui.screens.createInformation.CreateInformationViewModel
import com.example.rocnikova_prace.ui.screens.createInformation.CreateInformationViewModelFactory
import com.example.rocnikova_prace.ui.screens.createScreen.CreateScreen
import com.example.rocnikova_prace.ui.screens.questionsScreen.GroupsViewModel
import com.example.rocnikova_prace.ui.screens.questionsScreen.GroupsViewModelFactory
import com.example.rocnikova_prace.ui.screens.questionsScreen.QuestionsScreen
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

fun getScreenFromRoute(route: String?): MainScreen? {
    if (route == null) return null
    val routeName = route.substringBefore("/")
    return try {
        MainScreen.valueOf(routeName)
    } catch (_: IllegalArgumentException) {
        null
    }
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current
    val app = context.applicationContext as App
    val repository = app.repository

    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute?.startsWith(MainScreen.CreateInformation.name) != true) {
                NavBar(
                    navController,
                    viewModel = viewModel()
                )
            }
        }
    ) { innerPadding ->
        AnimatedNavHost(
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding()),
            navController = navController,
            startDestination = MainScreen.Create.name,
            enterTransition = {
                val fromScreen = getScreenFromRoute(initialState.destination.route)
                val toScreen = getScreenFromRoute(targetState.destination.route)

                val prevIndex = if (fromScreen != null) bottomScreens.indexOf(fromScreen) else -1
                val newIndex = if (toScreen != null) bottomScreens.indexOf(toScreen) else -1

                if (newIndex > prevIndex) {
                    slideInHorizontally(tween(300)) { it } + fadeIn()
                } else {
                    slideInHorizontally(tween(300)) { -it } + fadeIn()
                }
            },

            exitTransition = {
                val fromScreen = getScreenFromRoute(initialState.destination.route)
                val toScreen = getScreenFromRoute(targetState.destination.route)

                val prevIndex = if (fromScreen != null) bottomScreens.indexOf(fromScreen) else -1
                val newIndex = if (toScreen != null) bottomScreens.indexOf(toScreen) else -1

                if (newIndex > prevIndex) {
                    slideOutHorizontally(tween(300)) { -it } + fadeOut()
                } else {
                    slideOutHorizontally(tween(300)) { it } + fadeOut()
                }
            },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            composable(MainScreen.Questions.name) {
                val questionsScreenViewModel: GroupsViewModel = viewModel(
                    factory = GroupsViewModelFactory(repository)
                )

                QuestionsScreen(
                    viewModel = questionsScreenViewModel,
                    onGroupClick = { groupId ->
                        navController.navigate("${MainScreen.CreateInformation.name}/$groupId")
                    }
                )
            }

            composable(MainScreen.Create.name) { CreateScreen(navController = navController) }

            composable(MainScreen.Profile.name) { ProfileScreen() }


            composable(
                route = "${MainScreen.CreateInformation.name}/{groupId}",
                arguments = listOf(navArgument("groupId") { type = NavType.StringType })
            ) { backStackEntry ->
                val groupId = backStackEntry.arguments?.getString("groupId")
                    ?: return@composable

                val createInfoViewModel: CreateInformationViewModel = viewModel(
                    factory = CreateInformationViewModelFactory(repository, groupId)
                )

                Scaffold(
                    topBar = { TopAppBar(navController) }
                ) { padding ->
                    CreateInformation(viewModel = createInfoViewModel, modifier = Modifier.padding(padding))
                }
            }
        }
    }
}
