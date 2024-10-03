package jp.speakbuddy.edisonandroidexercise.feature.facthistory

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object FactHistoryRoute

fun NavController.navigateToFactHistory() = navigate(route = FactHistoryRoute)

fun NavGraphBuilder.factHistoryScreen(onNavigationUpClick: () -> Unit) {
    composable<FactHistoryRoute> {
        FactHistoryScreen(onNavigationUpClick = onNavigationUpClick)
    }
}
