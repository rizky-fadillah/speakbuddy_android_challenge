package jp.speakbuddy.edisonandroidexercise.ui

import jp.speakbuddy.edisonandroidexercise.core.testing.repository.TestCatsRepository
import jp.speakbuddy.edisonandroidexercise.core.testing.util.CoroutineTest
import jp.speakbuddy.edisonandroidexercise.domain.SearchCatFacts
import jp.speakbuddy.edisonandroidexercise.feature.facthistory.FactHistoryUiState
import jp.speakbuddy.edisonandroidexercise.feature.facthistory.FactHistoryViewModel
import jp.speakbuddy.edisonandroidexercise.model.Fact
import jp.speakbuddy.edisonandroidexercise.model.toPresentableFact
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@OptIn(ExperimentalCoroutinesApi::class)
class FactHistoryViewModelTest : CoroutineTest {

    override lateinit var testScope: TestScope

    private val catsRepository = TestCatsRepository()

    private val searchCatFacts = SearchCatFacts(
        catsRepository = catsRepository
    )

    private lateinit var viewModel: FactHistoryViewModel

    @BeforeEach
    fun setUp() {
        viewModel = FactHistoryViewModel(searchCatFacts = searchCatFacts)
    }

    @Test
    fun uiState_whenInitialized_thenShowLoading() = runTest {
        assertEquals(FactHistoryUiState.Loading, viewModel.factHistoryUiState.value)
    }

    @Test
    fun uiState_whenCollectStarts_thenShowLoading() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.factHistoryUiState.collect() }

        assertEquals(FactHistoryUiState.Loading, viewModel.factHistoryUiState.value)
    }

    @Test
    fun uiState_whenSuccess_matchesCatFactsFromRepository() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.factHistoryUiState.collect() }

        catsRepository.sendRandomCatFact(Fact("This is a cat fact.", length = 19))

        val successItem = viewModel.factHistoryUiState.value
        assertIs<FactHistoryUiState.Success>(successItem)

        val catFactsFromRepository = catsRepository.getCatFacts().first()
        assertEquals(catFactsFromRepository.map { it.toPresentableFact() }, successItem.facts)
    }

    @Test
    fun uiState_searchQuery_matchesCatFactsFromRepository() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.factHistoryUiState.collect() }

        assertEquals(FactHistoryUiState.Loading, viewModel.factHistoryUiState.value)

        viewModel.onSearchQueryChanged("Query")

        assertEquals(FactHistoryUiState.Loading, viewModel.factHistoryUiState.value)

        catsRepository.sendRandomCatFact(Fact("This is a cat fact.", length = 19))

        val successItem = viewModel.factHistoryUiState.value
        assertIs<FactHistoryUiState.Success>(successItem)

        val catFactsFromRepository = catsRepository.getCatFacts().first()
        assertEquals(catFactsFromRepository.map { it.toPresentableFact() }, successItem.facts)
    }
}
