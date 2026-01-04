package com.example.rocnikova_prace

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
import kotlinx.coroutines.launch

enum class MainScreen {
    Create,
    Questions,
    Profile,
    CreateInformation
}

val pagerScreens = listOf(
    MainScreen.Questions,
    MainScreen.Create,
    MainScreen.Profile
)

@Composable
fun MainScreen(
    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current
    val app = context.applicationContext as App
    val repository = app.repository

    NavHost(
        navController = navController,
        startDestination = "home_wrapper",
        enterTransition = { slideInHorizontally(tween(300)) { it } + fadeIn() },
        exitTransition = { slideOutHorizontally(tween(300)) { -it } + fadeOut() },
        popEnterTransition = { slideInHorizontally(tween(300)) { -it } + fadeIn() },
        popExitTransition = { slideOutHorizontally(tween(300)) { it } + fadeOut() }
    ) {
        composable("home_wrapper") {
            val pagerState = rememberPagerState(initialPage = 1, pageCount = { pagerScreens.size })
            val scope = rememberCoroutineScope()

            BackHandler(enabled = pagerState.currentPage != 1) {
                scope.launch {
                    pagerState.animateScrollToPage(1)
                }
            }

            Scaffold(
                bottomBar = {
                    NavBar(
                        selectedScreen = pagerScreens[pagerState.currentPage],
                        onScreenSelected = { screen ->
                            val index = pagerScreens.indexOf(screen)
                            if (index != -1) {
                                scope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            }
                        }
                    )
                }
            ) { innerPadding ->
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
                ) { pageIndex ->

                    when (pagerScreens[pageIndex]) {
                        MainScreen.Questions -> {
                            val questionsViewModel: GroupsViewModel = viewModel(
                                factory = GroupsViewModelFactory(repository)
                            )
                            QuestionsScreen(
                                viewModel = questionsViewModel,
                                navController = navController,
                                onGroupClick = { groupId ->
                                    navController.navigate("${MainScreen.CreateInformation.name}/$groupId")
                                }
                            )
                        }
                        MainScreen.Create -> {
                            CreateScreen(navController = navController)
                        }
                        MainScreen.Profile -> {
                            ProfileScreen()
                        }
                        else -> { /* Nic */ }
                    }
                }
            }
        }


        composable(
            route = "${MainScreen.CreateInformation.name}/{groupId}",
            arguments = listOf(navArgument("groupId") { type = NavType.StringType }),
            enterTransition = { slideInHorizontally(tween(300)) { it } },
            exitTransition = { slideOutHorizontally(tween(300)) { -it } },
            popEnterTransition = { slideInHorizontally(tween(300)) { -it } },
            popExitTransition = { slideOutHorizontally(tween(300)) { it } }
        ) { backStackEntry ->
            val groupId = backStackEntry.arguments?.getString("groupId")
                ?: return@composable

            val createInfoViewModel: CreateInformationViewModel = viewModel(
                factory = CreateInformationViewModelFactory(repository, groupId)
            )

            Scaffold(
                topBar = {
                    TopAppBar(navController, createInfoViewModel)
                }
            ) { padding ->
                CreateInformation(
                    viewModel = createInfoViewModel,
                    modifier = Modifier.padding(padding)
                )
            }
        }
    }
}