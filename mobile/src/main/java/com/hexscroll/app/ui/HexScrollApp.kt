package com.hexscroll.app.ui
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.hexscroll.app.ui.home.HomeScreen
import com.hexscroll.app.ui.topics.TopicPickerScreen
@Composable
fun HexScrollApp(appState: HexScrollAppState = rememberHexScrollAppState()) {
    NavHost(navController = appState.navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(onNavigateToTopicPicker = { appState.navigateToTopicPicker(it) })
        }
        composable(
            route = Screen.TopicPicker.route,
            arguments = listOf(navArgument("isOnboarding") { type = NavType.BoolType; defaultValue = false }),
        ) { back ->
            val isOnboarding = back.arguments?.getBoolean("isOnboarding") ?: false
            TopicPickerScreen(isOnboarding = isOnboarding, onConfirm = { appState.navigateToHome() }, onBack = { appState.navigateBack() })
        }
    }
}