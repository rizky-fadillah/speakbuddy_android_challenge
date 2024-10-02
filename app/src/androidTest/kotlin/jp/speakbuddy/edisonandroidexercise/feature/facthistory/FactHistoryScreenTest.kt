package jp.speakbuddy.edisonandroidexercise.feature.facthistory

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import de.mannodermaus.junit5.compose.createAndroidComposeExtension
import jp.speakbuddy.edisonandroidexercise.R
import jp.speakbuddy.edisonandroidexercise.domain.model.PresentableFact
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

@OptIn(ExperimentalTestApi::class)
class FactHistoryScreenTest {

    @JvmField
    @RegisterExtension
    @ExperimentalTestApi
    val extension = createAndroidComposeExtension<ComponentActivity>()

    @Test
    fun searchTextField_isFocused() {
        extension.use {
            setContent {
                FactHistoryScreen(
                    FactHistoryUiState.Loading,
                    ""
                )
            }

            onNodeWithTag("searchTextField")
                .assertIsFocused()
                .assertTextEquals("")
        }
    }

    @Test
    fun searchResult_success_state() {
        val searchQuery = "Search query"

        val fact1 = "This is a cat fact no. 1"
        val fact2 =
            "Cats have about 130,000 hairs per square inch (20,155 hairs per square centimeter)."
        val fact3 =
            "The cat's front paw has 5 toes, but the back paws have 4. Some cats are born with as many as 7 front toes and extra back toes (polydactl)."

        extension.use {
            setContent {
                FactHistoryScreen(
                    uiState = FactHistoryUiState.Success(
                        facts = listOf(
                            PresentableFact(
                                fact1,
                                null,
                                false
                            ),
                            PresentableFact(
                                fact = fact2,
                                length = null,
                                shouldShowMultipleCats = true
                            ),
                            PresentableFact(
                                fact = fact3,
                                length = "138",
                                shouldShowMultipleCats = true
                            )
                        )
                    ),
                    searchQuery = searchQuery
                ) {

                }
            }

            onNodeWithTag("searchTextField")
                .assertIsDisplayed()
                .assertTextEquals(searchQuery)
            onNodeWithContentDescription("Search").assertIsDisplayed()
            onNodeWithContentDescription(extension.activity.getString(R.string.fact_history_screen_clear_search_text_content_desc)).assertIsDisplayed()

            onNodeWithText(fact1).assertIsDisplayed()
            onNodeWithText(fact2).assertIsDisplayed()
            onNodeWithText(fact3).assertIsDisplayed()
        }
    }

    @Test
    fun emptySearchResult_emptySearchResultMessageAndSadCatFaceImageAreDisplayed() {
        extension.use {
            setContent {
                FactHistoryScreen(
                    uiState = FactHistoryUiState.Success(emptyList()),
                    searchQuery = ""
                ) { }
            }

            onNodeWithText(extension.activity.getString(R.string.fact_history_screen_empty_search_result_message)).assertIsDisplayed()
            onNodeWithContentDescription(extension.activity.getString(R.string.fact_history_screen_empty_search_result_image_content_desc)).assertIsDisplayed()
        }
    }
}