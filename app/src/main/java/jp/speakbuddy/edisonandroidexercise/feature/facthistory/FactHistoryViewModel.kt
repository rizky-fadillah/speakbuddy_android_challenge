package jp.speakbuddy.edisonandroidexercise.feature.facthistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import jp.speakbuddy.edisonandroidexercise.core.common.Result
import jp.speakbuddy.edisonandroidexercise.core.common.asResult
import jp.speakbuddy.edisonandroidexercise.domain.SearchCatFacts
import jp.speakbuddy.edisonandroidexercise.domain.model.PresentableFact
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class FactHistoryViewModel @Inject constructor(
    searchCatFacts: SearchCatFacts
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    val factHistoryUiState: StateFlow<FactHistoryUiState> =
        factHistoryUiState(_searchQuery.flatMapLatest { searchCatFacts(it) })
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = FactHistoryUiState.Loading,
            )

    fun onSearchQueryChanged(searchQuery: String) {
        this@FactHistoryViewModel._searchQuery.value = searchQuery
    }
}

private fun factHistoryUiState(
    catFactsStream: Flow<List<PresentableFact>>,
): Flow<FactHistoryUiState> {
    return catFactsStream
        .asResult()
        .map { catFactsResult ->
            when (catFactsResult) {
                is Result.Success -> {
                    catFactsResult.data.let { facts ->
                        FactHistoryUiState.Success(facts = facts)
                    }
                }

                is Result.Loading -> FactHistoryUiState.Loading
                is Result.Error -> FactHistoryUiState.Error(catFactsResult.exception.message)
            }
        }
}

sealed interface FactHistoryUiState {
    data class Success(val facts: List<PresentableFact>) :
        FactHistoryUiState

    data class Error(val message: String?) : FactHistoryUiState
    data object Loading : FactHistoryUiState
}
