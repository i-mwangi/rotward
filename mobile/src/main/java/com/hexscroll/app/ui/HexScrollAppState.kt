package com.hexscroll.app.ui
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object TopicPicker : Screen("topic_picker?isOnboarding={isOnboarding}") {
        fun createRoute(isOnboarding: Boolean) = "topic_picker?isOnboarding=$isOnboarding"
    }
}
class HexScrollAppState(val navController: NavHostController) {
    fun navigateToTopicPicker(isOnboarding: Boolean) { navController.navigate(Screen.TopicPicker.createRoute(isOnboarding)) }
    fun navigateToHome() { navController.navigate(Screen.Home.route) { popUpTo(0) { inclusive = true } } }
    fun navigateBack() { navController.popBackStack() }
}
@Composable
fun rememberHexScrollAppState(navController: NavHostController = rememberNavController()): HexScrollAppState =
    remember(navController) { HexScrollAppState(navController) }