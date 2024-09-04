package com.example.financetracker.presentation.screens.home

sealed class BottomNavScreen(val route: String, val label: String, val icon: Int) {
    object Home : BottomNavScreen("home", "Home", android.R.drawable.ic_menu_view)
    object Search : BottomNavScreen("search", "Search", android.R.drawable.ic_menu_search)
    object Profile : BottomNavScreen("profile", "Profile", android.R.drawable.ic_menu_myplaces)
}
