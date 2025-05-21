package com.example.vicerichallenge

sealed class NavRoutes(val route: String) {
    object UserList : NavRoutes("user_list")
    object UserDetails : NavRoutes("user_details/{userId}") {
        fun createRoute(userId: Int) = "user_details/$userId"
    }
}