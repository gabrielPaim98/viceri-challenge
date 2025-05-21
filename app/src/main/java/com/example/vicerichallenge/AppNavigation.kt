package com.example.vicerichallenge

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.vicerichallenge.ui.userDetails.UserDetailsScreen
import com.example.vicerichallenge.ui.userList.UserListScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = NavRoutes.UserList.route) {
        composable(NavRoutes.UserList.route) {
            UserListScreen(
                onUserClick = { userId ->
                    navController.navigate(NavRoutes.UserDetails.createRoute(userId))
                }
            )
        }
        composable(
            route = NavRoutes.UserDetails.route,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId")!!
            UserDetailsScreen(userId)
        }
    }
}