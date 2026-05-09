package com.arushlab.android.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.arushlab.android.ui.screens.HomeScreen
import com.arushlab.android.ui.screens.SplashScreen
import com.arushlab.android.ui.screens.TrackingScreen
import com.arushlab.android.ui.screens.AuthScreen

@Composable
fun ArushLabNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(
                onNavigateToHome = {
                    navController.navigate("home") {
                        popUpTo("splash") { inclusive = true }
                    }
                },
                onNavigateToAuth = {
                    navController.navigate("auth") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }
        composable("auth") {
            AuthScreen(
                onAuthSuccess = {
                    navController.navigate("home") {
                        popUpTo("auth") { inclusive = true }
                    }
                }
            )
        }
        composable("home") {
            HomeScreen(onNavigateToTracking = { trackingId ->
                if (trackingId.isNullOrEmpty()) {
                    navController.navigate("tracking")
                } else {
                    navController.navigate("tracking/$trackingId")
                }
            })
        }
        composable(
            route = "tracking/{trackingId}",
            arguments = listOf(navArgument("trackingId") { type = NavType.StringType; nullable = true })
        ) { backStackEntry ->
            val trackingId = backStackEntry.arguments?.getString("trackingId")
            TrackingScreen(
                trackingId = trackingId,
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(route = "tracking") {
            TrackingScreen(
                trackingId = null,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
