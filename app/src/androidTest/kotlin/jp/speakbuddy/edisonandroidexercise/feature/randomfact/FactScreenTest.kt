package jp.speakbuddy.edisonandroidexercise.feature.randomfact

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import de.mannodermaus.junit5.compose.createAndroidComposeExtension
import jp.speakbuddy.edisonandroidexercise.R
import jp.speakbuddy.edisonandroidexercise.domain.model.PresentableFact
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

@OptIn(ExperimentalTestApi::class)
class FactScreenTest {

    @JvmField
    @RegisterExtension
    val extension = createAndroidComposeExtension<ComponentActivity>()

    @Test
    fun factScreen_verifyToolbarTitleAndActionButtonAreDisplayed() {
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
    fun randomFactContent_whenLoadingState_verifyLoadingIndicatorIsDisplayed() {
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
    fun randomFactContent_whenErrorExists_verifyErrorMessageIsDisplayed() {
        extension.use {
            val errorMessage = "This is error message"

            setContent {
                RandomFactContent(
                    randomCatFactUiState = RandomCatFactUiState.Loading,
                    error = Exception(errorMessage)
                )
            }

            onNodeWithText(errorMessage).assertIsDisplayed()
        }
    }

    @Test
    fun randomFactContent_whenErrorState_verifyErrorMessageIsDisplayed() {
        extension.use {
            val errorMessage = "This is error message"

            setContent {
                RandomFactContent(
                    randomCatFactUiState = RandomCatFactUiState.Error(errorMessage),
                    error = null
                )
            }

            onNodeWithText(errorMessage).assertIsDisplayed()
        }
    }

    @Test
    fun factContent_verifyFactDisplayedIsSmallerThan100LengthAndHasNoMultipleCats() {
        extension.use {
            val fact = "This is a fact."

            setContent {
                FactContent(
                    presentableFact = PresentableFact(
                        fact = fact,
                        length = null,
                        shouldShowMultipleCats = false
                    )
                )
            }

            onNodeWithText(fact).assertIsDisplayed()
            onNodeWithText(extension.activity.getString(R.string.fact_screen_multiple_cats_label)).assertIsNotDisplayed()
            onNodeWithText("Length").assertIsNotDisplayed()
        }
    }

    @Test
    fun factContent_verifyFactDisplayedIsGreaterThan100LengthAndHasMultipleCats() {
        extension.use {
            val fact =
                "The cat's front paw has 5 toes, but the back paws have 4. Some cats are born with as many as 7 front toes and extra back toes (polydactl)."
            val length = "138"

            setContent {
                FactContent(
                    presentableFact = PresentableFact(
                        fact = fact,
                        length = length,
                        shouldShowMultipleCats = true
                    )
                )
            }

            onNodeWithText(fact).assertIsDisplayed()
            onNodeWithText(extension.activity.getString(R.string.fact_screen_multiple_cats_label)).assertIsDisplayed()
            onNodeWithText(
                extension.activity.getString(
                    R.string.fact_screen_length_label,
                    length
                )
            ).assertIsDisplayed()
        }
    }
}