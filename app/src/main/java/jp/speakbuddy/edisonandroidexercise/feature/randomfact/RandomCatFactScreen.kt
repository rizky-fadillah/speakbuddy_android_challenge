@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class
)

package jp.speakbuddy.edisonandroidexercise.feature.randomfact

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jp.speakbuddy.edisonandroidexercise.R
import jp.speakbuddy.edisonandroidexercise.core.designsystem.theme.EdisonAndroidExerciseTheme
import jp.speakbuddy.edisonandroidexercise.core.testing.data.factGreaterThan100WithMultipleCats
import jp.speakbuddy.edisonandroidexercise.domain.model.PresentableFact
import jp.speakbuddy.edisonandroidexercise.feature.FactContent

@Composable
fun RandomCatFactScreen(
    viewModel: RandomCatFactViewModel = hiltViewModel(), onFactHistoryClick: () -> Unit
) {
    val randomCatFactUiState: RandomCatFactUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()

    RandomCatFactScreen(randomCatFactUiState, error, onFactHistoryClick, viewModel::refresh)
}

@Composable
fun RandomCatFactScreen(
    randomCatFactUiState: RandomCatFactUiState,
    error: Exception?,
    onFactHistoryClick: () -> Unit = {},
    onRefreshClick: () -> Unit = {}
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(text = stringResource(R.string.fact_screen_toolbar_title))
                },
                actions = {
                    IconButton(
                        onClick = { onFactHistoryClick() }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = "Action button"
                        )
                    }
                }
            )
        }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding), verticalArrangement = Arrangement.Center
        ) {
            RandomFactContent(
                randomCatFactUiState = randomCatFactUiState,
                error = error,
                modifier = Modifier.weight(1F)
            )

            Button(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                onClick = {
                    onRefreshClick()
                }
            ) {
                Text(text = stringResource(R.string.fact_screen_update_fact_cta_label))
            }
        }
    }
}

@Composable
fun RandomFactContent(
    randomCatFactUiState: RandomCatFactUiState,
    error: Exception?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
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
                is RandomCatFactUiState.Success -> FactContent(
                    presentableFact = randomCatFactUiState.presentableFact
                )

                RandomCatFactUiState.Loading -> Box(
                    modifier = Modifier
                        .semantics {
                            contentDescription = "Loading"
                        }
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
}

@Preview
@Composable
private fun RandomFactContentSuccessPreview() {
    EdisonAndroidExerciseTheme {
        Surface {
            RandomFactContent(
                randomCatFactUiState = RandomCatFactUiState.Success(
                    presentableFact = PresentableFact(
                        fact = "The Maine Coon is 4 to 5 times larger than Singapura, smallest breed of breed of cat",
                        length = null,
                        shouldShowMultipleCats = false
                    )
                ),
                error = null
            )
        }
    }
}

@Preview
@Composable
private fun RandomFactContentLoadingPreview() {
    EdisonAndroidExerciseTheme {
        Surface {
            RandomFactContent(
                randomCatFactUiState = RandomCatFactUiState.Loading,
                error = null
            )
        }
    }
}

@Preview
@Composable
private fun RandomFactContentErrorStatePreview() {
    EdisonAndroidExerciseTheme {
        Surface {
            RandomFactContent(
                randomCatFactUiState = RandomCatFactUiState.Error("No data"),
                error = null
            )
        }
    }
}

@Preview
@Composable
private fun RandomFactContentErrorPreview() {
    EdisonAndroidExerciseTheme {
        Surface {
            RandomFactContent(
                randomCatFactUiState = RandomCatFactUiState.Loading,
                error = Exception("Unable to resolve host catfact.ninja: No address associated with hostname")
            )
        }
    }
}

@Preview
@Composable
private fun RandomCatFactScreenSuccessPreview() {
    EdisonAndroidExerciseTheme {
        Surface {
            RandomCatFactScreen(
                randomCatFactUiState = RandomCatFactUiState.Success(
                    presentableFact = factGreaterThan100WithMultipleCats
                ),
                error = null
            ) { }
        }
    }
}

@Preview
@Composable
private fun RandomCatFactScreenLoadingPreview() {
    EdisonAndroidExerciseTheme {
        Surface {
            RandomCatFactScreen(
                randomCatFactUiState = RandomCatFactUiState.Loading,
                error = null
            ) { }
        }
    }
}

@Preview
@Composable
private fun RandomFactScreenErrorStatePreview() {
    EdisonAndroidExerciseTheme {
        Surface {
            RandomCatFactScreen(
                randomCatFactUiState = RandomCatFactUiState.Error("No data"),
                error = null
            )
        }
    }
}

@Preview
@Composable
private fun RandomFactScreenErrorPreview() {
    EdisonAndroidExerciseTheme {
        Surface {
            RandomCatFactScreen(
                randomCatFactUiState = RandomCatFactUiState.Loading,
                error = Exception("Unable to resolve host catfact.ninja: No address associated with hostname")
            )
        }
    }
}
