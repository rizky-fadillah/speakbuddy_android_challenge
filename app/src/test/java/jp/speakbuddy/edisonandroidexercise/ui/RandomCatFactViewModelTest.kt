package jp.speakbuddy.edisonandroidexercise.ui

import io.mockk.coEvery
import io.mockk.mockk
import jp.speakbuddy.edisonandroidexercise.core.testing.repository.TestCatsRepository
import jp.speakbuddy.edisonandroidexercise.core.testing.util.CoroutineTest
import jp.speakbuddy.edisonandroidexercise.domain.ObserveRandomCatFact
import jp.speakbuddy.edisonandroidexercise.domain.RefreshRandomCatFact
import jp.speakbuddy.edisonandroidexercise.domain.model.PresentableFact
import jp.speakbuddy.edisonandroidexercise.domain.model.toPresentableFact
import jp.speakbuddy.edisonandroidexercise.feature.randomfact.RandomCatFactUiState
import jp.speakbuddy.edisonandroidexercise.feature.randomfact.RandomCatFactViewModel
import jp.speakbuddy.edisonandroidexercise.model.Fact
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
class RandomCatFactViewModelTest : CoroutineTest {

    override lateinit var testScope: TestScope

    private val catsRepository = TestCatsRepository()

    private val refreshRandomCatFact = RefreshRandomCatFact(
        catsRepository = catsRepository
    )
    private val observeRandomCatFact = ObserveRandomCatFact(
        catsRepository = catsRepository
    )

    private lateinit var viewModel: RandomCatFactViewModel

    @BeforeEach
    fun setUp() {
        viewModel = RandomCatFactViewModel(
            refreshRandomCatFact = refreshRandomCatFact,
            observeRandomCatFact = observeRandomCatFact
        )
    }

    @Test
    fun uiStateRandomCatFact_whenInitialized_thenShowLoading() = runTest {
        assertEquals(RandomCatFactUiState.Loading, viewModel.uiState.value)
    }

    @Test
    fun uiStateRandomCatFact_whenCollectStarts_thenShowLoading() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }

        assertEquals(RandomCatFactUiState.Loading, viewModel.uiState.value)
    }

    @Test
    fun uiStateRandomCatFact_whenRandomCatFactObserved_matchesLatestCatFactFromRepository() =
        runTest {
            backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }

            catsRepository.sendRandomCatFact(Fact("This is a cat fact.", length = 19))

            val successItem = viewModel.uiState.value
            assertIs<RandomCatFactUiState.Success>(successItem)

            val latestCatFactFromRepository = catsRepository.getLatestCatFact().first()
            assertEquals(
                latestCatFactFromRepository?.toPresentableFact(),
                successItem.presentableFact
            )
        }

    @Test
    fun uiStateRandomCatFact_whenCollectStartsAndRandomCatFactObserved_thenShowLoadingAndSuccess() =
        runTest {
            backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }

            assertEquals(RandomCatFactUiState.Loading, viewModel.uiState.value)

            catsRepository.sendRandomCatFact(Fact("This is a cat fact.", length = 19))

            assertEquals(
                RandomCatFactUiState.Success(PresentableFact("This is a cat fact.", null, false)),
                viewModel.uiState.value
            )
        }

    @Test
    fun uiStateRandomCatFact_whenRefreshStartsAndFails_thenShowLoadingAndError() =
        runTest {
            /**
             *  `RefreshRandomCatFact` is mocked here intentionally to demonstrate the usage of
             *  mocking approach as per the challenge requirement
             */
            val mockRefreshRandomCatFact = mockk<RefreshRandomCatFact>()

            /**
             *   Force `refreshRandomCatFact()` to throw an Exception here to emulate the failure
             *   scenario during the invocation of `catsRepository.loadRandomCatFact()`
             */
            val thrownException = Exception("Error message")
            coEvery { mockRefreshRandomCatFact.invoke() } throws thrownException

            val viewModel = RandomCatFactViewModel(mockRefreshRandomCatFact, observeRandomCatFact)

            backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }

            assertEquals(RandomCatFactUiState.Loading, viewModel.uiState.value)

            assertEquals(
                thrownException,
                viewModel.error.value
            )
        }

    @Test
    fun uiStateRandomCatFact_whenObserveRandomCatFactFail_thenShowError() =
        runTest {
            backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }

            assertEquals(RandomCatFactUiState.Loading, viewModel.uiState.value)

            // Emit empty list to the backing flow to emulate the empty data scenario in local db.
            catsRepository.setFlowEmpty()

            assertEquals(
                RandomCatFactUiState.Error("No data"),
                viewModel.uiState.value
            )
        }
}
