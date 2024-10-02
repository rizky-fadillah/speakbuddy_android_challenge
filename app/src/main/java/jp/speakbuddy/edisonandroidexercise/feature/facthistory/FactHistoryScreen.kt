package jp.speakbuddy.edisonandroidexercise.feature.facthistory

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jp.speakbuddy.edisonandroidexercise.R
import jp.speakbuddy.edisonandroidexercise.core.designsystem.theme.EdisonAndroidExerciseTheme
import jp.speakbuddy.edisonandroidexercise.domain.model.PresentableFact
import jp.speakbuddy.edisonandroidexercise.feature.randomfact.FactContent

@Composable
fun FactHistoryScreen(
    viewModel: FactHistoryViewModel = hiltViewModel(), onNavigationUpClick: () -> Unit
) {
    val uiState: FactHistoryUiState by viewModel.factHistoryUiState.collectAsStateWithLifecycle()
    val searchQuery: String by viewModel.searchQuery.collectAsStateWithLifecycle()

    FactHistoryScreen(uiState, searchQuery, viewModel::onSearchQueryChanged, onNavigationUpClick)
}

@Composable
fun FactHistoryScreen(
    uiState: FactHistoryUiState,
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit = {},
    onNavigationUpClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            SearchToolbar(searchQuery, onSearchQueryChanged, onNavigationUpClick)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding),
        ) {
            when (uiState) {
                is FactHistoryUiState.Success -> {
                    if (uiState.facts.isNotEmpty()) {
                        SearchResultBody(uiState.facts)
                    } else {
                        EmptySearchResultBody()
                    }
                }

                is FactHistoryUiState.Error -> {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = uiState.message.orEmpty()
                    )
                }

                FactHistoryUiState.Loading -> {}
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchToolbar(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit = {},
    onNavigationUpClick: () -> Unit = {}
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = { onNavigationUpClick() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Localized description"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Box {
                val focusRequester = remember { FocusRequester() }

                TextField(
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                    ),
                    textStyle = TextStyle.Default.copy(
                        fontSize = TextUnit(
                            16F,
                            TextUnitType.Sp
                        )
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .focusRequester(focusRequester)
                        .onKeyEvent {
                            if (it.key == Key.Enter) {
//                                onSearchExplicitlyTriggered()
                                true
                            } else {
                                false
                            }
                        }
                        .testTag("searchTextField"),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Search,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.onSurface,
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(
                                onClick = {
                                    onSearchQueryChanged("")
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Close,
                                    contentDescription = stringResource(R.string.fact_history_screen_clear_search_text_content_desc),
                                    tint = MaterialTheme.colorScheme.onSurface,
                                )
                            }
                        }
                    },
                    shape = RoundedCornerShape(32.dp),
                    value = searchQuery,
                    onValueChange = {
                        onSearchQueryChanged(it)
                    },
                    maxLines = 1,
                    singleLine = true,
                )

                LaunchedEffect(Unit) {
                    focusRequester.requestFocus()
                }
            }
        })
}

@Composable
private fun BoxScope.SearchResultBody(facts: List<PresentableFact>) {
    LazyColumn(
        modifier = Modifier.align(Alignment.TopCenter),
        verticalArrangement = Arrangement.SpaceAround
    ) {
        facts.forEach {
            item {
                FactContent(presentableFact = it)
                HorizontalDivider()
            }
        }
    }
}

@Composable
private fun BoxScope.EmptySearchResultBody() {
    Column(
        modifier = Modifier.align(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.fact_history_screen_empty_search_result_message),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            painter = painterResource(R.drawable.sad_cat_face),
            contentDescription = stringResource(R.string.fact_history_screen_empty_search_result_image_content_desc)
        )
    }
}

@Preview
@Composable
private fun SearchToolbarPreview() {
    EdisonAndroidExerciseTheme {
        Surface {
            SearchToolbar("Cats") { }
        }
    }
}

@Preview
@Composable
private fun FactHistoryScreenSearchResultBodyPreview() {
    FactHistoryScreen(
        FactHistoryUiState.Success(
            listOf(
                PresentableFact(
                    "This is a cat fact no. 1",
                    null,
                    false
                ),
                PresentableFact(
                    fact = "Cats have about 130,000 hairs per square inch (20,155 hairs per square centimeter).",
                    length = null,
                    shouldShowMultipleCats = true
                ),
                PresentableFact(
                    fact = "The cat's front paw has 5 toes, but the back paws have 4. Some cats are born with as many as 7 front toes and extra back toes (polydactl).",
                    length = "138",
                    shouldShowMultipleCats = true
                )
            )
        ), "Cats"
    )
}

@Preview
@Composable
private fun FactHistoryScreenEmptySearchResultBodyPreview() {
    EdisonAndroidExerciseTheme {
        Surface {
            FactHistoryScreen(
                FactHistoryUiState.Success(emptyList()), "Cats"
            )
        }
    }
}

@Preview
@Composable
private fun SearchResultPreview() {
    EdisonAndroidExerciseTheme {
        Surface {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                SearchResultBody(
                    listOf(
                        PresentableFact(
                            "This is a cat fact no. 1",
                            null,
                            false
                        ),
                        PresentableFact(
                            fact = "Cats have about 130,000 hairs per square inch (20,155 hairs per square centimeter).",
                            length = null,
                            shouldShowMultipleCats = true
                        ),
                        PresentableFact(
                            fact = "The cat's front paw has 5 toes, but the back paws have 4. Some cats are born with as many as 7 front toes and extra back toes (polydactl).",
                            length = "138",
                            shouldShowMultipleCats = true
                        )
                    )
                )
            }
        }
    }
}

@Preview
@Composable
private fun EmptySearchResultPreview() {
    EdisonAndroidExerciseTheme {
        Surface {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                EmptySearchResultBody()
            }
        }
    }
}