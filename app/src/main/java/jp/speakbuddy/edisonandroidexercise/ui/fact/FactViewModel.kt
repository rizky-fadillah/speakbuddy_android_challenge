package jp.speakbuddy.edisonandroidexercise.ui.fact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import jp.speakbuddy.edisonandroidexercise.data.network.FactServiceProvider
import jp.speakbuddy.edisonandroidexercise.domain.ObserveRandomCatFact
import jp.speakbuddy.edisonandroidexercise.domain.RefreshRandomCatFact
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
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

    val uiState: StateFlow<RandomCatFactUiState> =
        combine(observeRandomCatFact(), _triggerRefresh, ::Pair).map {
            if (it.second) {
                RandomCatFactUiState.Loading
            } else {
                it.first?.let { catFact ->
                    RandomCatFactUiState.Success(
                        fact = catFact.fact,
                        length = if (catFact.length > 100) catFact.length.toString() else null,
                        shouldShowMultipleCats = catFact.fact.lowercase().contains("cats")
                    )
                } ?: RandomCatFactUiState.Error("No data")
            }
        }.onStart {
            emit(RandomCatFactUiState.Loading)
        }.catch {
            emit(RandomCatFactUiState.Error(it.message))
        }.stateIn(
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

sealed interface RandomCatFactUiState {
    data class Success(val fact: String, val length: String?, val shouldShowMultipleCats: Boolean) :
        RandomCatFactUiState

    data class Error(val message: String?) : RandomCatFactUiState
    data object Loading : RandomCatFactUiState
}
