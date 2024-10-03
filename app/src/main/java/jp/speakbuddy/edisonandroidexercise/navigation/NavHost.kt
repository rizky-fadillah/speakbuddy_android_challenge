package jp.speakbuddy.edisonandroidexercise.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import jp.speakbuddy.edisonandroidexercise.feature.facthistory.factHistoryScreen
import jp.speakbuddy.edisonandroidexercise.feature.facthistory.navigateToFactHistory
import jp.speakbuddy.edisonandroidexercise.feature.randomcat.navigation.FactRoute
import jp.speakbuddy.edisonandroidexercise.feature.randomcat.navigation.factScreen

@Composable
fun CatNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = FactRoute,
    ) {
        factScreen(onFactHistoryClick = navController::navigateToFactHistory)
        factHistoryScreen(onNavigationUpClick = navController::navigateUp)
    }
}