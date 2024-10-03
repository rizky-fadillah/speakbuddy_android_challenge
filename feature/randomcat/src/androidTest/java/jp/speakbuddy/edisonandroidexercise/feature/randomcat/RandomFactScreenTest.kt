package jp.speakbuddy.edisonandroidexercise.feature.randomcat

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import de.mannodermaus.junit5.compose.ComposeContext
import de.mannodermaus.junit5.compose.createAndroidComposeExtension
import jp.speakbuddy.edisonandroidexercise.core.ui.FactContent
import jp.speakbuddy.edisonandroidexercise.model.PresentableFact
import jp.speakbuddy.edisonandroidexercise.testing.data.factGreaterThan100WithMultipleCats
import jp.speakbuddy.edisonandroidexercise.testing.data.factGreaterThan100WithoutMultipleCats
import jp.speakbuddy.edisonandroidexercise.testing.data.factSmallerThan100WithMultipleCats
import jp.speakbuddy.edisonandroidexercise.testing.data.factSmallerThan100WithoutMultipleCats
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

@OptIn(ExperimentalTestApi::class)
class RandomFactScreenTest {

    @JvmField
    @RegisterExtension
    val extension = createAndroidComposeExtension<ComponentActivity>()

    @Test
    fun testFactScreen_ToolbarTitleAndActionButtonAreDisplayed() {
        extension.use {
            setContent {
                RandomCatFactScreen(
                    randomCatFactUiState = RandomCatFactUiState.Loading,
                    error = null
                ) {}
            }

            onNodeWithText(extension.activity.getString(R.string.fact_screen_toolbar_title))
                .assertIsDisplayed()
            onNodeWithContentDescription("Action button")
                .assertIsDisplayed()
                .assertHasClickAction()

            onNodeWithText(extension.activity.getString(R.string.fact_screen_update_fact_cta_label))
                .assertIsDisplayed()
                .assertHasClickAction()
        }
    }

    @Test
    fun testRandomFactContent_LoadingState_ShowsLoadingIndicator() {
        extension.use {
            setContent {
                RandomFactContent(
                    randomCatFactUiState = RandomCatFactUiState.Loading,
                    error = null
                )
            }

            onNodeWithContentDescription("Loading").assertIsDisplayed()
        }
    }

    @Test
    fun testRandomFactContent_LoadingStateWithError_ShowsErrorMessage() {
        extension.use {
            val errorMessage = "This is error message"

            setContent {
                RandomFactContent(
                    randomCatFactUiState = RandomCatFactUiState.Loading,
                    error = Exception(errorMessage)
                )
            }

            // Verify that the error message is displayed, even in the Loading state
            onNodeWithText(errorMessage).assertIsDisplayed()
        }
    }

    @Test
    fun testRandomFactContent_ErrorStateWithoutException_ShowsErrorMessage() {
        extension.use {
            val errorMessage = "This is error message"

            setContent {
                RandomFactContent(
                    randomCatFactUiState = RandomCatFactUiState.Error(errorMessage),
                    error = null
                )
            }

            // Verify that the error message is displayed when the UI state is Error but no exception
            onNodeWithText(errorMessage).assertIsDisplayed()
        }
    }

    @Test
    fun testFactContent_GreaterThan100LengthWithMultipleCats_ShowsCorrectContent() {
        extension.use {
            val presentableFact = factGreaterThan100WithMultipleCats

            setContent {
                FactContent(
                    presentableFact = presentableFact
                )
            }

            verifyFactContent(
                presentableFact = presentableFact,
                shouldShowMultipleCats = true,
                shouldShowLength = true
            )
        }
    }

    @Test
    fun testFactContent_GreaterThan100LengthWithoutMultipleCats_ShowsCorrectContent() {
        extension.use {
            val presentableFact = factGreaterThan100WithoutMultipleCats

            setContent {
                FactContent(
                    presentableFact = presentableFact
                )
            }

            verifyFactContent(
                presentableFact = presentableFact,
                shouldShowMultipleCats = false,
                shouldShowLength = true
            )
        }
    }

    @Test
    fun testFactContent_SmallerThan100LengthWithMultipleCats_ShowsCorrectContent() {
        extension.use {
            val presentableFact = factSmallerThan100WithMultipleCats

            setContent {
                FactContent(
                    presentableFact = presentableFact
                )
            }

            verifyFactContent(
                presentableFact = presentableFact,
                shouldShowMultipleCats = true,
                shouldShowLength = false
            )
        }
    }

    @Test
    fun testFactContent_SmallerThan100LengthWithoutMultipleCats_ShowsCorrectContent() {
        extension.use {
            val presentableFact = factSmallerThan100WithoutMultipleCats

            setContent {
                FactContent(
                    presentableFact = presentableFact
                )
            }

            verifyFactContent(
                presentableFact = presentableFact,
                shouldShowMultipleCats = false,
                shouldShowLength = false
            )
        }
    }

    private fun ComposeContext.verifyFactContent(
        presentableFact: PresentableFact,
        shouldShowMultipleCats: Boolean,
        shouldShowLength: Boolean
    ) {
        onNodeWithText(presentableFact.fact).assertIsDisplayed()

        if (shouldShowMultipleCats) {
            onNodeWithText(extension.activity.getString(R.string.fact_screen_multiple_cats_label))
                .assertIsDisplayed()
        } else {
            onNodeWithText(extension.activity.getString(R.string.fact_screen_multiple_cats_label))
                .assertIsNotDisplayed()
        }

        if (shouldShowLength) {
            onNodeWithText(
                extension.activity.getString(
                    R.string.fact_screen_length_label,
                    presentableFact.length
                )
            ).assertIsDisplayed()
        } else {
            onNodeWithText("Length").assertIsNotDisplayed()
        }
    }
}