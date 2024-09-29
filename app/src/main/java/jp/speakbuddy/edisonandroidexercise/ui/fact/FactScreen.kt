package jp.speakbuddy.edisonandroidexercise.ui.fact

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jp.speakbuddy.edisonandroidexercise.ui.fact.RandomCatFactUiState.Error
import jp.speakbuddy.edisonandroidexercise.ui.fact.RandomCatFactUiState.Loading
import jp.speakbuddy.edisonandroidexercise.ui.fact.RandomCatFactUiState.Success
import jp.speakbuddy.edisonandroidexercise.ui.theme.EdisonAndroidExerciseTheme

@Composable
fun FactScreen(
    viewModel: FactViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            space = 16.dp, alignment = Alignment.CenterVertically
        )
    ) {
        val topicUiState: RandomCatFactUiState by viewModel.uiState.collectAsStateWithLifecycle()

        val isLoading by viewModel.loading.collectAsStateWithLifecycle()
        val error by viewModel.error.collectAsStateWithLifecycle()

        Text(
            text = "Fact", style = MaterialTheme.typography.titleLarge
        )

        Box(contentAlignment = Alignment.Center) {
            if (isLoading) {
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(40.dp)
                    )
                }
            } else if (error != null) {
                Text(
                    text = error!!.message.orEmpty(),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            } else {
                when (topicUiState) {
                    is Success -> {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                textAlign = TextAlign.Center,
                                text = (topicUiState as Success).fact,
                                style = MaterialTheme.typography.bodyLarge
                            )

                            (topicUiState as Success).length?.let { length ->
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = length, style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    }

                    Loading -> {}

                    is Error -> Text(
                        text = (topicUiState as Error).message.orEmpty(),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Button(onClick = {
            viewModel.refresh()
        }) {
            Text(text = "Update fact")
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
