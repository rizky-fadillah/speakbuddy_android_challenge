package jp.speakbuddy.edisonandroidexercise.feature

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jp.speakbuddy.edisonandroidexercise.R
import jp.speakbuddy.edisonandroidexercise.core.designsystem.theme.EdisonAndroidExerciseTheme
import jp.speakbuddy.edisonandroidexercise.core.testing.data.factGreaterThan100WithMultipleCats
import jp.speakbuddy.edisonandroidexercise.core.testing.data.factGreaterThan100WithoutMultipleCats
import jp.speakbuddy.edisonandroidexercise.core.testing.data.factSmallerThan100WithMultipleCats
import jp.speakbuddy.edisonandroidexercise.core.testing.data.factSmallerThan100WithoutMultipleCats
import jp.speakbuddy.edisonandroidexercise.domain.model.PresentableFact

@Composable
fun FactContent(presentableFact: PresentableFact) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        if (presentableFact.shouldShowMultipleCats) {
            Text(
                text = stringResource(R.string.fact_screen_multiple_cats_label),
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle()) {
                    append(presentableFact.fact)
                }

                if (presentableFact.shouldShowMultipleCats) {
                    val startingIndex = presentableFact.fact.indexOf(
                        "cats", ignoreCase = true
                    )

                    addStyle(
                        style = SpanStyle(
                            color = Color.Red,
                            fontWeight = FontWeight.SemiBold
                        ),
                        start = startingIndex,
                        end = startingIndex + "cats".length
                    )
                }
            },
            style = MaterialTheme.typography.bodyLarge
        )

        presentableFact.length?.let { length ->
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(
                    R.string.fact_screen_length_label, length
                ),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Preview
@Composable
private fun FactContentGreaterThan100WithMultipleCatsPreview() {
    EdisonAndroidExerciseTheme {
        Surface {
            FactContent(
                presentableFact = factGreaterThan100WithMultipleCats
            )
        }
    }
}

@Preview
@Composable
private fun FactContentSmallerThan100WithMultipleCatsPreview() {
    EdisonAndroidExerciseTheme {
        Surface {
            FactContent(
                presentableFact = factSmallerThan100WithMultipleCats
            )
        }
    }
}

@Preview
@Composable
private fun FactContentGreaterThan100WithoutMultipleCatsPreview() {
    EdisonAndroidExerciseTheme {
        Surface {
            FactContent(
                presentableFact = factGreaterThan100WithoutMultipleCats
            )
        }
    }
}

@Preview
@Composable
private fun FactContentSmallerThan100WithoutMultipleCatsPreview() {
    EdisonAndroidExerciseTheme {
        Surface {
            FactContent(
                presentableFact = factSmallerThan100WithoutMultipleCats
            )
        }
    }
}
