package com.smartspender.navigation

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.smartspender.ui.add.AddTransactionActivity
import com.smartspender.ui.dashboard.DashboardScreen
import com.smartspender.ui.dashboard.DashboardViewModel
import com.smartspender.ui.home.HomeScreen
import com.smartspender.ui.home.HomeViewModel
import com.smartspender.ui.profile.ProfileScreen
import com.smartspender.ui.profile.ProfileViewModel
import com.smartspender.ui.transactions.TransactionsScreen
import com.smartspender.ui.transactions.TransactionsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
// PUBLIC_INTERFACE
fun SmartSpenderApp(appContext: Context) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            NavigationBar {
                bottomItems.forEach { item ->
                    NavigationBarItem(
                        selected = currentRoute == item.route,
                        onClick = {
                            if (item.route == BottomNavItem.Add.route) {
                                appContext.startActivity(Intent(appContext, AddTransactionActivity::class.java))
                            } else {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Home.route) { HomeScreen(vm = HomeViewModel(appContext.applicationContext as android.app.Application)) }
            composable(BottomNavItem.Dashboard.route) { DashboardScreen(vm = DashboardViewModel(appContext.applicationContext as android.app.Application)) }
            composable(BottomNavItem.Transactions.route) { TransactionsScreen(vm = TransactionsViewModel(appContext.applicationContext as android.app.Application)) }
            composable(BottomNavItem.Profile.route) { ProfileScreen(vm = ProfileViewModel(appContext.applicationContext as android.app.Application)) }
        }
    }
}
