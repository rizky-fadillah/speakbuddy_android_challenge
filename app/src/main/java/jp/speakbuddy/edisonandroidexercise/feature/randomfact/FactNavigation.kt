package jp.speakbuddy.edisonandroidexercise.feature.randomfact

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object FactRoute

fun NavGraphBuilder.factScreen(onFactHistoryClick: () -> Unit) {
    composable<FactRoute> {
        RandomCatFactScreen(onFactHistoryClick = onFactHistoryClick)
    }
}
