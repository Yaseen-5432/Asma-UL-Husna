package com.example.asma_ul_husna.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.asma_ul_husna.R
import com.example.asma_ul_husna.audio.AppAudioPlayer
import com.example.asma_ul_husna.domain.repository.NamesRepository
import com.example.asma_ul_husna.ui.detail.NameDetailScreen
import com.example.asma_ul_husna.ui.detail.NameDetailViewModel
import com.example.asma_ul_husna.ui.detail.NameDetailViewModelFactory
import com.example.asma_ul_husna.ui.favorites.FavoritesScreen
import com.example.asma_ul_husna.ui.favorites.FavoritesViewModel
import com.example.asma_ul_husna.ui.favorites.FavoritesViewModelFactory
import com.example.asma_ul_husna.ui.home.HomeScreen
import com.example.asma_ul_husna.ui.home.HomeViewModel
import com.example.asma_ul_husna.ui.home.HomeViewModelFactory
import com.example.asma_ul_husna.ui.settings.SettingsScreen
import com.example.asma_ul_husna.ui.settings.SettingsViewModel
import com.example.asma_ul_husna.ui.settings.SettingsViewModelFactory
import com.example.asma_ul_husna.ui.theme.BrightGold
import com.example.asma_ul_husna.ui.theme.DeepIndigo
import com.example.asma_ul_husna.ui.theme.DeepIndigoDark
import com.example.asma_ul_husna.ui.theme.PrimaryPurple
import com.example.asma_ul_husna.ui.theme.TextOnDark
import com.example.asma_ul_husna.ui.theme.TextOnDarkSecondary

sealed class Screen(
    val route: String,
    val titleRes: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    object Home : Screen(
        route = "home",
        titleRes = R.string.nav_home,
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    )

    object Favorites : Screen(
        route = "favorites",
        titleRes = R.string.nav_favorites,
        selectedIcon = Icons.Filled.Favorite,
        unselectedIcon = Icons.Outlined.FavoriteBorder
    )

    object Settings : Screen(
        route = "settings",
        titleRes = R.string.nav_settings,
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings
    )

    object Detail : Screen(
        route = "detail/{nameId}",
        titleRes = R.string.app_name,
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    ) {
        fun createRoute(nameId: Int): String = "detail/$nameId"
    }
}

val bottomNavScreens = listOf(
    Screen.Home,
    Screen.Favorites,
    Screen.Settings
)

@Composable
fun AppNavigation(
    repository: NamesRepository,
    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current
    val audioPlayer = remember { AppAudioPlayer(context) }

    DisposableEffect(Unit) {
        onDispose {
            audioPlayer.release()
        }
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val shouldShowBottomBar = bottomNavScreens.any { it.route == currentRoute }

    Scaffold(
        containerColor = DeepIndigo,
        bottomBar = {
            if (shouldShowBottomBar) {
                NavigationBar(
                    containerColor = DeepIndigoDark,
                    tonalElevation = 0.dp
                ) {
                    bottomNavScreens.forEach { screen ->
                        val isSelected = currentRoute == screen.route
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = if (isSelected) screen.selectedIcon else screen.unselectedIcon,
                                    contentDescription = stringResource(screen.titleRes),
                                    modifier = Modifier.size(22.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = stringResource(screen.titleRes),
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = BrightGold,
                                selectedTextColor = BrightGold,
                                indicatorColor = PrimaryPurple,
                                unselectedIconColor = TextOnDarkSecondary.copy(alpha = 0.7f),
                                unselectedTextColor = TextOnDarkSecondary.copy(alpha = 0.7f)
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Home Screen
            composable(Screen.Home.route) {
                val homeViewModel: HomeViewModel = viewModel(
                    factory = HomeViewModelFactory(repository, audioPlayer)
                )
                HomeScreen(
                    viewModel = homeViewModel,
                    onNameClick = { nameId ->
                        navController.navigate(Screen.Detail.createRoute(nameId))
                    }
                )
            }

            // Favorites Screen
            composable(Screen.Favorites.route) {
                val favoritesViewModel: FavoritesViewModel = viewModel(
                    factory = FavoritesViewModelFactory(repository)
                )
                FavoritesScreen(
                    viewModel = favoritesViewModel,
                    onNameClick = { nameId ->
                        navController.navigate(Screen.Detail.createRoute(nameId))
                    }
                )
            }

            // Settings Screen
            composable(Screen.Settings.route) {
                val settingsViewModel: SettingsViewModel = viewModel(
                    factory = SettingsViewModelFactory(repository)
                )
                SettingsScreen(
                    viewModel = settingsViewModel
                )
            }

            // Detail Screen
            composable(
                route = Screen.Detail.route,
                arguments = listOf(
                    navArgument("nameId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val nameId = backStackEntry.arguments?.getInt("nameId") ?: 1
                val detailViewModel: NameDetailViewModel = viewModel(
                    factory = NameDetailViewModelFactory(
                        nameId = nameId,
                        repository = repository,
                        audioPlayer = audioPlayer
                    )
                )
                NameDetailScreen(
                    viewModel = detailViewModel,
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
