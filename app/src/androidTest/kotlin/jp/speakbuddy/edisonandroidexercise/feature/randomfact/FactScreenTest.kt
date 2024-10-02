package jp.speakbuddy.edisonandroidexercise.feature.randomfact

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import de.mannodermaus.junit5.compose.createAndroidComposeExtension
import jp.speakbuddy.edisonandroidexercise.R
import jp.speakbuddy.edisonandroidexercise.domain.model.PresentableFact
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

class FactScreenTest {

    @JvmField
    @RegisterExtension
    @ExperimentalTestApi
    val extension = createAndroidComposeExtension<ComponentActivity>()

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun fact_loading_state() {
        extension.use {
            setContent {
                Column {
                    RandomFactContent(
                        RandomCatFactUiState.Loading,
                        null
                    ) {

                    }
                }
            }

            onNodeWithContentDescription("Loading").assertExists()
        }
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun fact_withoutMultipleCats() {
        extension.use {
            val fact = "This is a fact."

            setContent {
                Column {
                    RandomFactContent(
                        randomCatFactUiState = RandomCatFactUiState.Success(
                            presentableFact = PresentableFact(
                                fact = fact,
                                length = null,
                                shouldShowMultipleCats = false
                            )
                        ), error = null
                    ) {

                    }
                }
            }

            onNodeWithText(fact).assertExists()
            onNodeWithText(extension.activity.getString(R.string.fact_screen_multiple_cats_label)).assertDoesNotExist()
        }
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun fact_withMultipleCats() {
        extension.use {
            val fact =
                "The cat's front paw has 5 toes, but the back paws have 4. Some cats are born with as many as 7 front toes and extra back toes (polydactl)."
            val length = "138"

            setContent {
                Column {
                    RandomFactContent(
                        randomCatFactUiState = RandomCatFactUiState.Success(
                            presentableFact = PresentableFact(
                                fact = fact,
                                length = length,
                                shouldShowMultipleCats = true
                            )
                        ), error = null
                    ) {

                    }
                }
            }

            onNodeWithText(fact).assertExists()
            onNodeWithText("Length: $length").assertExists()
            onNodeWithText(extension.activity.getString(R.string.fact_screen_multiple_cats_label)).assertExists()
        }
    }
}