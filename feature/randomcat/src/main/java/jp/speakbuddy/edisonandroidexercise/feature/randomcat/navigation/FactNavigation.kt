package jp.speakbuddy.edisonandroidexercise.feature.randomcat.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import jp.speakbuddy.edisonandroidexercise.feature.randomcat.RandomCatFactScreen
import kotlinx.serialization.Serializable

@Serializable
data object FactRoute

fun NavGraphBuilder.factScreen(onFactHistoryClick: () -> Unit) {
    composable<FactRoute> {
        RandomCatFactScreen(onFactHistoryClick = onFactHistoryClick)
    }
}
