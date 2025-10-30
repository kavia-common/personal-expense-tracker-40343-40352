package com.smartspender.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

// PUBLIC_INTERFACE
sealed class BottomNavItem(val route: String, val label: String, val icon: ImageVector) {
    object Home : BottomNavItem("home", "Home", Icons.Filled.Home)
    object Dashboard : BottomNavItem("dashboard", "Dashboard", Icons.Filled.Assessment)
    object Add : BottomNavItem("add", "Add", Icons.Filled.Add)
    object Transactions : BottomNavItem("transactions", "Transactions", Icons.Filled.List)
    object Profile : BottomNavItem("profile", "Profile", Icons.Filled.Person)
}

val bottomItems = listOf(
    BottomNavItem.Home,
    BottomNavItem.Dashboard,
    BottomNavItem.Add,
    BottomNavItem.Transactions,
    BottomNavItem.Profile
)
