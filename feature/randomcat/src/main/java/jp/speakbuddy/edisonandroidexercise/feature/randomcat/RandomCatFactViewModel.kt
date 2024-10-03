package jp.speakbuddy.edisonandroidexercise.feature.randomcat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import jp.speakbuddy.edisonandroidexercise.common.Result
import jp.speakbuddy.edisonandroidexercise.common.asResult
import jp.speakbuddy.edisonandroidexercise.domain.ObserveRandomCatFact
import jp.speakbuddy.edisonandroidexercise.domain.RefreshRandomCatFact
import jp.speakbuddy.edisonandroidexercise.feature.randomcat.RandomCatFactUiState.Error
import jp.speakbuddy.edisonandroidexercise.feature.randomcat.RandomCatFactUiState.Loading
import jp.speakbuddy.edisonandroidexercise.feature.randomcat.RandomCatFactUiState.Success
import jp.speakbuddy.edisonandroidexercise.model.PresentableFact
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class RandomCatFactViewModel @Inject constructor(
    private val refreshRandomCatFact: RefreshRandomCatFact,
    observeRandomCatFact: ObserveRandomCatFact,
) : ViewModel() {

    private val _triggerRefresh = MutableStateFlow(false)

    private val _error: MutableStateFlow<Exception?> = MutableStateFlow(null)
    val error: StateFlow<Exception?> = _error

    val uiState: StateFlow<RandomCatFactUiState> =
        randomCatFactUiState(observeRandomCatFact, _triggerRefresh)
            .onStart { refresh() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = Loading,
            )

    fun refresh() = viewModelScope.launch {
        _triggerRefresh.value = true

        try {
            refreshRandomCatFact()

            _error.value = null
        } catch (e: Exception) {
            _error.value = e
        } finally {
            _triggerRefresh.value = false
        }
    }
}

private fun randomCatFactUiState(
    observeRandomCatFact: ObserveRandomCatFact,
    triggerRefresh: StateFlow<Boolean>
): Flow<RandomCatFactUiState> {
    val randomCatFactStream = observeRandomCatFact()

    return combine(randomCatFactStream, triggerRefresh, ::Pair)
        .asResult()
        .map { factToTriggerRefresh ->
            when (factToTriggerRefresh) {
                is Result.Success -> {
                    if (factToTriggerRefresh.data.second) {
                        Loading
                    } else {
                        factToTriggerRefresh.data.first?.let { catFact ->
                            Success(catFact)
                        } ?: Error("No data")
                    }
                }

                is Result.Loading -> Loading
                is Result.Error -> Error(factToTriggerRefresh.exception.message)
            }
        }
}

sealed interface RandomCatFactUiState {
    data class Success(val presentableFact: PresentableFact) :
        RandomCatFactUiState

    data class Error(val message: String?) : RandomCatFactUiState
    data object Loading : RandomCatFactUiState
}
