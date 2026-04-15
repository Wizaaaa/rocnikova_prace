package com.example.rocnikova_prace

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.rocnikova_prace.data.remote.SupabaseClient
import com.example.rocnikova_prace.ui.ViewModelFactory
import com.example.rocnikova_prace.ui.components.NavBar
import com.example.rocnikova_prace.ui.components.TopAppBar
import com.example.rocnikova_prace.ui.screens.authScreen.AuthScreen
import com.example.rocnikova_prace.ui.screens.authScreen.AuthViewModel
import com.example.rocnikova_prace.ui.screens.createInformation.CreateInformation
import com.example.rocnikova_prace.ui.screens.createInformation.CreateInformationViewModel
import com.example.rocnikova_prace.ui.screens.createScreen.CreateScreen
import com.example.rocnikova_prace.ui.screens.datailProfile.DetailProfile
import com.example.rocnikova_prace.ui.screens.datailProfile.DetailProfileViewModel
import com.example.rocnikova_prace.ui.screens.datailProfile.DetailProfileViewModelFactory
import com.example.rocnikova_prace.ui.screens.graphScreen.GraphScreen
import com.example.rocnikova_prace.ui.screens.graphScreen.GraphScreenViewModel
import com.example.rocnikova_prace.ui.screens.practiceScreen.PracticeScreen
import com.example.rocnikova_prace.ui.screens.practiceScreen.PracticeScreenViewModel
import com.example.rocnikova_prace.ui.screens.profileScreen.ProfileScreen
import com.example.rocnikova_prace.ui.screens.profileScreen.ProfileScreenViewModel
import com.example.rocnikova_prace.ui.screens.questionsScreen.GroupsViewModel
import com.example.rocnikova_prace.ui.screens.questionsScreen.QuestionsScreen
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.launch

enum class MainScreen {
    AuthScreen,
    Create,
    Questions,
    Profile,
    CreateInformation,
    PracticeScreen,
    GraphScreen,
    DetailProfile
}

val pagerScreens = listOf(
    MainScreen.Questions,
    MainScreen.Create,
    MainScreen.Profile
)

@Composable
fun MainScreen(
    navController: NavHostController = rememberNavController(),
    supabase: io.github.jan.supabase.SupabaseClient = SupabaseClient.client
) {
    val context = LocalContext.current
    val app = context.applicationContext as App
    val repository = app.repository
    val authRepository = app.authRepository

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { _ -> }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            launcher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    val currentUser = supabase.auth.currentUserOrNull()
    val startDestination = if (currentUser != null) {
        "home_wrapper"
    } else {
        MainScreen.AuthScreen.name
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = { slideInHorizontally(tween(300)) { it } + fadeIn() },
        exitTransition = { slideOutHorizontally(tween(300)) { -it } + fadeOut() },
        popEnterTransition = { slideInHorizontally(tween(300)) { -it } + fadeIn() },
        popExitTransition = { slideOutHorizontally(tween(300)) { it } + fadeOut() }
    ) {
        composable(
            route = MainScreen.AuthScreen.name
        ) {
            val authViewModel: AuthViewModel = viewModel(
                factory = ViewModelFactory(repository, authRepository)
            )

            AuthScreen(
                viewModel = authViewModel,
                navController = navController,
                supabase = supabase
            )
        }


        composable("home_wrapper") {
            val pagerState = rememberPagerState(initialPage = 1, pageCount = { pagerScreens.size })
            val scope = rememberCoroutineScope()

            val profileScreenViewModel: ProfileScreenViewModel = viewModel(
                factory = ViewModelFactory(repository, authRepository)
            )

            BackHandler(enabled = pagerState.currentPage != 1) {
                scope.launch {
                    pagerState.animateScrollToPage(1)
                }
            }

            Scaffold(
                bottomBar = {
                    NavBar(
                        selectedScreen = pagerScreens[pagerState.currentPage],
                        avatarUrl = profileScreenViewModel.userAvatar,
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
                                factory = ViewModelFactory(repository)
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
                            val authViewModel: AuthViewModel = viewModel(
                                factory = ViewModelFactory(repository, authRepository)
                            )

                            CreateScreen(
                                navController = navController,
                                authViewModel = authViewModel
                            )
                        }
                        MainScreen.Profile -> {
                            ProfileScreen(
                                profileScreenViewModel = profileScreenViewModel,
                                navController = navController
                            )
                        }
                        else -> {  }
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
                factory = ViewModelFactory(repository, authRepository, groupId)
            )

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = stringResource(R.string.new_questions_group),
                        saveButton = { createInfoViewModel.saveInformation {
                            navController.popBackStack()
                        } }
                    )
                }
            ) { padding ->
                CreateInformation(
                    viewModel = createInfoViewModel,
                    modifier = Modifier.padding(padding)
                )
            }
        }

        composable(
            route = "${MainScreen.PracticeScreen.name}/{groupId}"
        ) { backStackEntry ->
            val groupId = backStackEntry.arguments?.getString("groupId")
                ?: return@composable

            val practiceViewModel: PracticeScreenViewModel = viewModel(
                factory = ViewModelFactory(repository, groupId = groupId)
            )

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = practiceViewModel.groupName,
                    )
                }
            ) { padding ->
                PracticeScreen(
                    practiceViewModel,
                    navController = navController,
                    modifier = Modifier.padding(padding)
                )
            }
        }

        composable(
            route = "${MainScreen.GraphScreen.name}/{groupId}"
        ) { backStackEntry ->
            val groupId = backStackEntry.arguments?.getString("groupId")
                ?: return@composable

            val graphScreenViewModel: GraphScreenViewModel = viewModel(
                factory = ViewModelFactory(repository, groupId = groupId)
            )

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = graphScreenViewModel.groupName,
                    )
                }
            ) { padding ->
                GraphScreen(
                    viewModel = graphScreenViewModel,
                    modifier = Modifier.padding(padding)
                )
            }
        }

        composable(
            route = "${MainScreen.DetailProfile.name}/{userId}"
        ) { backStackEntry ->
            val factory = DetailProfileViewModelFactory(
                authRepository = authRepository,
                owner = backStackEntry,
                defaultArgs = backStackEntry.arguments,
                questionRepository = repository
            )
            val viewModel: DetailProfileViewModel = viewModel(factory = factory)

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = viewModel.userName,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            ) { padding ->
                DetailProfile(
                    viewModel = viewModel,
                    navController = navController,
                    modifier = Modifier.padding(padding)
                )
            }
        }
    }
}