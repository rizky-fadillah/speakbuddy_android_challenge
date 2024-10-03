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
    fun factHistoryScreen_verifySearchTextFieldIsDisplayedAndFocused() {
        extension.use {
            setContent {
                FactHistoryScreen(
                    FactHistoryUiState.Loading,
                    ""
                )
            }

            // Verify the search TextField is displayed and focused
            onNodeWithTag("searchTextField")
                .assertIsDisplayed()
                .assertIsFocused()
                .assertTextEquals("")
        }
    }

    @Test
    fun factHistoryScreen_verifySearchToolbarIsDisplayed_andSearchQueryMatches() {
        extension.use {
            val searchQuery = "Search query"

            setContent {
                FactHistoryScreen(
                    uiState = FactHistoryUiState.Loading,
                    searchQuery = searchQuery
                ) {

                }
            }

            // Verify the search TextField is displayed and the current value matches the `searchQuery`
            onNodeWithTag("searchTextField")
                .assertIsDisplayed()
                .assertTextEquals(searchQuery)
            // Verify the leading icon ("Search") is displayed
            onNodeWithContentDescription("Search")
                .assertIsDisplayed()
            // Verify the trailing icon ("Clear search text") is displayed and has click action
            onNodeWithContentDescription(extension.activity.getString(R.string.fact_history_screen_clear_search_text_content_desc))
                .assertIsDisplayed()
                .assertHasClickAction()
        }
    }

    @Test
    fun searchResultBody_verifyFactsAreDisplayed() {
        val presentableFact1 = factGreaterThan100WithMultipleCats
        val presentableFact2 = factGreaterThan100WithoutMultipleCats
        val presentableFact3 = factSmallerThan100WithMultipleCats

        extension.use {
            setContent {
                Box {
                    SearchResultBody(
                        facts = listOf(presentableFact1, presentableFact2, presentableFact3)
                    )
                }
            }

            onNodeWithText(presentableFact1.fact).assertIsDisplayed()
            onNodeWithText(presentableFact2.fact).assertIsDisplayed()
            onNodeWithText(presentableFact3.fact).assertIsDisplayed()
        }
    }

    @Test
    fun searchResult_emptySearchResultMessageAndSadCatFaceImageAreDisplayed() {
        extension.use {
            setContent {
                FactHistoryScreen(
                    uiState = FactHistoryUiState.Success(emptyList()),
                    searchQuery = ""
                ) { }
            }

            // Verify the empty search result message is displayed
            onNodeWithText(extension.activity.getString(R.string.fact_history_screen_empty_search_result_message)).assertIsDisplayed()
            // Verify the empty search result image (sad cat face image) is displayed
            onNodeWithContentDescription(extension.activity.getString(R.string.fact_history_screen_empty_search_result_image_content_desc)).assertIsDisplayed()
        }
    }
}