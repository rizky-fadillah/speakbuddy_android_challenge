package jp.speakbuddy.edisonandroidexercise.feature.randomfact

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jp.speakbuddy.edisonandroidexercise.R
import jp.speakbuddy.edisonandroidexercise.core.designsystem.theme.EdisonAndroidExerciseTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FactScreen(
    viewModel: FactViewModel = hiltViewModel()
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    title = {
                        Text(text = stringResource(R.string.fact_screen_toolbar_title))
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(space = 16.dp)
            ) {
                val randomCatFactUiState: RandomCatFactUiState by viewModel.uiState.collectAsStateWithLifecycle()

                val error by viewModel.error.collectAsStateWithLifecycle()

                RandomFactContent(
                    randomCatFactUiState = randomCatFactUiState,
                    error = error,
                    onClick = { viewModel.refresh() }
                )
            }
        }
    }
}

@Composable
private fun ColumnScope.RandomFactContent(
    randomCatFactUiState: RandomCatFactUiState, error: Exception?, onClick: () -> Unit
) {
    Box {
        if (error != null) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                text = error.message.orEmpty(),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        } else {
            when (randomCatFactUiState) {
                is RandomCatFactUiState.Success -> FactContent(randomCatFactUiState = randomCatFactUiState)

                RandomCatFactUiState.Loading -> Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(40.dp)
                    )
                }

                is RandomCatFactUiState.Error -> Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    text = randomCatFactUiState.message.orEmpty(),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    Button(modifier = Modifier
        .padding(16.dp)
        .fillMaxWidth()
        .wrapContentHeight(align = Alignment.Bottom)
        .weight(1F),
        onClick = {
            onClick()
        }) {
        Text(text = stringResource(R.string.fact_screen_update_fact_cta_label))
    }
}

@Composable
private fun FactContent(randomCatFactUiState: RandomCatFactUiState.Success) {
    Column(modifier = Modifier.padding(16.dp)) {
        if (randomCatFactUiState.shouldShowMultipleCats) {
            Text(
                text = stringResource(R.string.fact_screen_multiple_cats_label),
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle()) {
                    append(randomCatFactUiState.fact)
                }

                if (randomCatFactUiState.shouldShowMultipleCats) {
                    val startingIndex = randomCatFactUiState.fact.indexOf(
                        "cats", ignoreCase = true
                    )

                    addStyle(
                        style = SpanStyle(
                            color = Color.Red,
                            fontWeight = FontWeight.SemiBold
                        ), start = startingIndex, end = startingIndex + "cats".length
                    )
                }
            }, style = MaterialTheme.typography.bodyLarge
        )

        randomCatFactUiState.length?.let { length ->
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
private fun FactContentNoMultipleCatsNoLength() {
    EdisonAndroidExerciseTheme {
        Surface {
            FactContent(
                RandomCatFactUiState.Success(
                    "The Maine Coon is 4 to 5 times larger than Singapura, smallest breed of breed of cat",
                    length = null,
                    false
                )
            )
        }
    }
}

@Preview
@Composable
private fun FactContentMultipleCatsWithLength() {
    EdisonAndroidExerciseTheme {
        Surface {
            FactContent(
                randomCatFactUiState = RandomCatFactUiState.Success(
                    "The cat's front paw has 5 toes, but the back paws have 4. Some cats are born with as many as 7 front toes and extra back toes (polydactl).",
                    length = "138",
                    true
                )
            )
        }
    }
}

@Preview
@Composable
private fun RandomFactContentNoCatsNoLength() {
    EdisonAndroidExerciseTheme {
        Surface {
            Column {
                RandomFactContent(
                    randomCatFactUiState = RandomCatFactUiState.Success(
                        "The Maine Coon is 4 to 5 times larger than Singapura, smallest breed of breed of cat",
                        length = null,
                        false
                    ),
                    error = null,
                    onClick = {}
                )
            }
        }
    }
}

@Preview
@Composable
private fun RandomFactContentMultipleCatsWithLength() {
    EdisonAndroidExerciseTheme {
        Surface {
            Column {
                RandomFactContent(
                    randomCatFactUiState = RandomCatFactUiState.Success(
                        "The cat's front paw has 5 toes, but the back paws have 4. Some cats are born with as many as 7 front toes and extra back toes (polydactl).",
                        length = "138",
                        true
                    ),
                    error = null,
                    onClick = {}
                )
            }
        }
    }
}

@Preview
@Composable
private fun RandomFactContentMultipleCatsWithNoLength() {
    EdisonAndroidExerciseTheme {
        Surface {
            Column {
                RandomFactContent(
                    randomCatFactUiState = RandomCatFactUiState.Success(
                        "Cats have about 130,000 hairs per square inch (20,155 hairs per square centimeter).",
                        length = null,
                        true
                    ),
                    error = null,
                    onClick = {}
                )
            }
        }
    }
}

@Preview
@Composable
private fun RandomFactContentLoading() {
    EdisonAndroidExerciseTheme {
        Surface {
            Column {
                RandomFactContent(
                    randomCatFactUiState = RandomCatFactUiState.Loading,
                    error = null,
                    onClick = {}
                )
            }
        }
    }
}

@Preview
@Composable
private fun RandomFactContentError() {
    EdisonAndroidExerciseTheme {
        Surface {
            Column {
                RandomFactContent(
                    randomCatFactUiState = RandomCatFactUiState.Loading,
                    error = Exception("Unable to resolve host catfact.ninja: No address associated with hostname"),
                    onClick = {}
                )
            }
        }
    }
}

@Preview
@Composable
private fun FactScreenPreview() {
    EdisonAndroidExerciseTheme {
        FactScreen()
    }
}