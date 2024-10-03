package jp.speakbuddy.edisonandroidexercise.feature.randomcat

import io.mockk.coEvery
import io.mockk.mockk
import jp.speakbuddy.edisonandroidexercise.domain.ObserveRandomCatFact
import jp.speakbuddy.edisonandroidexercise.domain.RefreshRandomCatFact
import jp.speakbuddy.edisonandroidexercise.model.Fact
import jp.speakbuddy.edisonandroidexercise.model.PresentableFact
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
    fun `When Initialized Then ShowLoading`() = runTest {
        assertEquals(RandomCatFactUiState.Loading, viewModel.uiState.value)
    }

    @Test
    fun `When CollectStarts Then ShowLoading`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }

        assertEquals(RandomCatFactUiState.Loading, viewModel.uiState.value)
    }

    @Test
    fun testObserveRandomCatFact_Success_MatchesLatestCatFactFromRepository() =
        runTest {
            // Collect the uiState in a background scope
            backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }

            // Send a random cat fact to the repository
            catsRepository.sendRandomCatFact(Fact("This is a cat fact.", length = 19))

            // Verify that the UI state is Success and matches the latest cat fact from the repository
            val successItem = viewModel.uiState.value
            assertIs<RandomCatFactUiState.Success>(successItem)

            val latestCatFactFromRepository = catsRepository.getLatestCatFact().first()
            assertEquals(
                latestCatFactFromRepository?.toPresentableFact(),
                successItem.presentableFact
            )
        }

    @Test
    fun testObserveRandomCatFact_Success_ShowsCorrectPresentableFact() =
        runTest {
            backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }

            catsRepository.sendRandomCatFact(
                fact = Fact(
                    fact = "This is a cat fact.",
                    length = 19
                )
            )

            /**
             * Verify that the `Fact` is correctly mapped to `PresentableFact`.
             *
             * - `PresentableFact.length` should be `null` because `Fact.length` is
             *   less than 100.
             * - `PresentableFact.shouldShowMultipleCats` should be `false` since
             *   the `Fact.fact` string does not contain the word "cats".
             */
            assertEquals(
                RandomCatFactUiState.Success(
                    PresentableFact(
                        fact = "This is a cat fact.",
                        length = null,
                        shouldShowMultipleCats = false
                    )
                ),
                viewModel.uiState.value
            )
        }

    @Test
    fun testRefreshRandomCatFact_ThrowsError_ShowsLoadingAndError() =
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

            // Creating ViewModel with the mocked `RefreshRandomCatFact`
            val viewModel = RandomCatFactViewModel(mockRefreshRandomCatFact, observeRandomCatFact)

            backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }

            assertEquals(RandomCatFactUiState.Loading, viewModel.uiState.value)

            // Verifying the exception is captured in the `viewModel.error` StateFlow
            assertEquals(thrownException, viewModel.error.value)
        }

    @Test
    fun testObserveRandomCatFact_ThrowsError_ShowsErrorState() =
        runTest {
            backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }

            // Simulating an empty data scenario in the local db by emitting an empty list to the backing flow
            catsRepository.setFlowEmpty()

            // Verify that the `viewModel.uiState` transitions to an Error state with the expected message
            assertEquals(
                RandomCatFactUiState.Error("No data"),
                viewModel.uiState.value
            )
        }
}
