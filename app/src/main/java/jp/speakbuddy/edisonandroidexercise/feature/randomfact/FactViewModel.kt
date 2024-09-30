package jp.speakbuddy.edisonandroidexercise.feature.randomfact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import jp.speakbuddy.edisonandroidexercise.core.common.Result
import jp.speakbuddy.edisonandroidexercise.core.common.asResult
import jp.speakbuddy.edisonandroidexercise.data.network.retrofit.FactServiceProvider
import jp.speakbuddy.edisonandroidexercise.domain.ObserveRandomCatFact
import jp.speakbuddy.edisonandroidexercise.domain.RefreshRandomCatFact
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@HiltViewModel
class FactViewModel @Inject constructor(
    private val refreshRandomCatFact: RefreshRandomCatFact,
    observeRandomCatFact: ObserveRandomCatFact
) : ViewModel() {

    private val _triggerRefresh = MutableStateFlow(false)

    private val _error: MutableStateFlow<Exception?> = MutableStateFlow(null)
    val error: StateFlow<Exception?> = _error

    val uiState: StateFlow<RandomCatFactUiState> = uiState(observeRandomCatFact, _triggerRefresh)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = RandomCatFactUiState.Loading,
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

    fun updateFact(completion: () -> Unit): String = runBlocking {
        try {
            FactServiceProvider.provide().getFact().fact
        } catch (e: Throwable) {
            "something went wrong. error = ${e.message}"
        }.also { completion() }
    }
}

private fun uiState(
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
                        RandomCatFactUiState.Loading
                    } else {
                        factToTriggerRefresh.data.first?.let { catFact ->
                            RandomCatFactUiState.Success(
                                fact = catFact.fact,
                                length = if (catFact.length > 100) catFact.length.toString() else null,
                                shouldShowMultipleCats = catFact.fact.lowercase().contains("cats")
                            )
                        } ?: RandomCatFactUiState.Error("No data")
                    }
                }

                is Result.Loading -> RandomCatFactUiState.Loading
                is Result.Error -> RandomCatFactUiState.Error(factToTriggerRefresh.exception.message)
            }
        }
}

sealed interface RandomCatFactUiState {
    data class Success(val fact: String, val length: String?, val shouldShowMultipleCats: Boolean) :
        RandomCatFactUiState

    data class Error(val message: String?) : RandomCatFactUiState
    data object Loading : RandomCatFactUiState
}
