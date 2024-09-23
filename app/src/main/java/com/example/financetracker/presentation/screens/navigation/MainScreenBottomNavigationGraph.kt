package com.example.financetracker.presentation.screens.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.financetracker.presentation.screens.AnalysisScreen
import com.example.financetracker.presentation.screens.home.BottomNavScreen
import com.example.financetracker.presentation.screens.home.HomeScreen
import com.example.financetracker.presentation.screens.profile.ProfileScreen
import com.example.financetracker.presentation.viewmodels.AppViewModel

@Composable
fun MainScreenBottomNavigationGraph(
    bottomNavController: NavHostController,
    mainNavController: NavController,
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = bottomNavController,
        startDestination = BottomNavScreen.Home.route,
        modifier = modifier
    ) {
        composable(BottomNavScreen.Home.route) { HomeScreen(navController = mainNavController) }
        composable(BottomNavScreen.Search.route) { AnalysisScreen() }
        composable(BottomNavScreen.Profile.route) { ProfileScreen() }
    }
}