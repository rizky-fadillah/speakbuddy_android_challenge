package jp.speakbuddy.edisonandroidexercise.feature.facthistory

import jp.speakbuddy.edisonandroidexercise.domain.SearchCatFacts
import jp.speakbuddy.edisonandroidexercise.model.Fact
import jp.speakbuddy.edisonandroidexercise.model.toPresentableFact
import jp.speakbuddy.edisonandroidexercise.testing.repository.TestCatsRepository
import jp.speakbuddy.edisonandroidexercise.testing.util.CoroutineTest
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
    fun testInitialization_ShowsLoadingState() = runTest {
        // Verify that the initial state is Loading
        assertEquals(FactHistoryUiState.Loading, viewModel.factHistoryUiState.value)
    }

    @Test
    fun testCollectingUiState_ShowsLoading() = runTest {
        // Collect the uiState in a background scope
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.factHistoryUiState.collect() }

        // Verify that the initial state is Loading during collection
        assertEquals(FactHistoryUiState.Loading, viewModel.factHistoryUiState.value)
    }

    @Test
    fun testSearchCatFacts_Success_ShowsSearchResultsMatchingRepository() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.factHistoryUiState.collect() }

        // Send a random cat fact to the repository
        catsRepository.sendRandomCatFact(Fact(fact = "This is a cat fact.", length = 19))

        // Verify the UI state is Success and matches the repository data
        val successItem = viewModel.factHistoryUiState.value
        assertIs<FactHistoryUiState.Success>(successItem)

        val catFactsFromRepository = catsRepository.getCatFacts().first()
        assertEquals(catFactsFromRepository.map { it.toPresentableFact() }, successItem.facts)
    }

    @Test
    fun testSearchQueryChanged_ResultsInLoadingStateAndMatchingSearchResults() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.factHistoryUiState.collect() }

        // Trigger search query change
        viewModel.onSearchQueryChanged("Query")

        // Verify the UI enters Loading state
        assertEquals(FactHistoryUiState.Loading, viewModel.factHistoryUiState.value)

        // Send a random cat fact to the repository
        catsRepository.sendRandomCatFact(Fact("This is a cat fact.", length = 19))

        // Verify the UI state is Success and matches the repository data
        val successItem = viewModel.factHistoryUiState.value
        assertIs<FactHistoryUiState.Success>(successItem)

        val catFactsFromRepository = catsRepository.getCatFacts().first()
        assertEquals(catFactsFromRepository.map { it.toPresentableFact() }, successItem.facts)
    }

    @Test
    fun testSearchCatFacts_Success_EmptyData_ShowsEmptyResults() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.factHistoryUiState.collect() }

        // Simulating an empty data scenario in the local db by emitting an empty list to the backing flow
        catsRepository.setFlowEmpty()

        // Verify the UI state is Success
        val successItem = viewModel.factHistoryUiState.value
        assertIs<FactHistoryUiState.Success>(successItem)

        // Verify that the result list is empty
        assertEquals(emptyList(), successItem.facts)
    }

}
