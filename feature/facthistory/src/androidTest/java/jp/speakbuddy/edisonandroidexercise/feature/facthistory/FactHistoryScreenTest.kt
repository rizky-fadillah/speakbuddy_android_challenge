package jp.speakbuddy.edisonandroidexercise.feature.facthistory

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import de.mannodermaus.junit5.compose.createAndroidComposeExtension
import jp.speakbuddy.edisonandroidexercise.testing.data.factGreaterThan100WithMultipleCats
import jp.speakbuddy.edisonandroidexercise.testing.data.factGreaterThan100WithoutMultipleCats
import jp.speakbuddy.edisonandroidexercise.testing.data.factSmallerThan100WithMultipleCats
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

@OptIn(ExperimentalTestApi::class)
class FactHistoryScreenTest {

    @JvmField
    @RegisterExtension
    val extension = createAndroidComposeExtension<ComponentActivity>()

    @Test
    fun testFactHistoryScreen_SearchTextFieldIsDisplayedAndFocused() {
        extension.use {
            setContent {
                FactHistoryScreen(
                    FactHistoryUiState.Loading,
                    searchQuery = ""
                )
            }

            // Verify the search TextField is displayed and focused
            onNodeWithTag(extension.activity.getString(R.string.fact_history_screen_search_text_field_test_tag))
                .assertIsDisplayed()
                .assertIsFocused()
                .assertTextEquals("")
        }
    }

    @Test
    fun testFactHistoryScreen_SearchToolbarIsDisplayedAndQueryMatches() {
        extension.use {
            val searchQuery = "Search query"

            setContent {
                FactHistoryScreen(
                    uiState = FactHistoryUiState.Loading,
                    searchQuery = searchQuery
                ) {}
            }

            // Verify the search TextField is displayed and its value matches the search query
            onNodeWithTag(extension.activity.getString(R.string.fact_history_screen_search_text_field_test_tag))
                .assertIsDisplayed()
                .assertTextEquals(searchQuery)

            // Verify the leading search icon is displayed
            onNodeWithContentDescription(extension.activity.getString(R.string.fact_history_screen_search_icon_content_desc))
                .assertIsDisplayed()

            // Verify the trailing clear icon is displayed and has click action
            onNodeWithContentDescription(extension.activity.getString(R.string.fact_history_screen_clear_search_text_content_desc))
                .assertIsDisplayed()
                .assertHasClickAction()
        }
    }

    @Test
    fun testSearchResultBody_FactsAreDisplayed() {
        val facts = listOf(
            factGreaterThan100WithMultipleCats,
            factGreaterThan100WithoutMultipleCats,
            factSmallerThan100WithMultipleCats
        )

        extension.use {
            setContent {
                Box {
                    SearchResultBody(facts = facts)
                }
            }

            // Verify all facts are displayed
            facts.forEach { presentableFact ->
                onNodeWithText(presentableFact.fact).assertIsDisplayed()
            }
        }
    }

    @Test
    fun testFactHistoryScreen_EmptyMessageAndSadCatFaceImageAreDisplayed() {
        extension.use {
            setContent {
                FactHistoryScreen(
                    uiState = FactHistoryUiState.Success(emptyList()),
                    searchQuery = ""
                ) {}
            }

            // Verify the empty search result message is displayed
            onNodeWithText(extension.activity.getString(R.string.fact_history_screen_empty_search_result_message))
                .assertIsDisplayed()

            // Verify the empty search result image is displayed
            onNodeWithContentDescription(extension.activity.getString(R.string.fact_history_screen_empty_search_result_image_content_desc))
                .assertIsDisplayed()
        }
    }
}