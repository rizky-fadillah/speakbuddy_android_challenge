package jp.speakbuddy.edisonandroidexercise.ui

import jp.speakbuddy.edisonandroidexercise.core.testing.repository.TestCatsRepository
import jp.speakbuddy.edisonandroidexercise.core.testing.util.CoroutineTest
import jp.speakbuddy.edisonandroidexercise.domain.ObserveRandomCatFact
import jp.speakbuddy.edisonandroidexercise.domain.RefreshRandomCatFact
import jp.speakbuddy.edisonandroidexercise.feature.randomfact.FactViewModel
import jp.speakbuddy.edisonandroidexercise.feature.randomfact.RandomCatFactUiState
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
class FactViewModelTest : CoroutineTest {

    override lateinit var testScope: TestScope

    private val catsRepository = TestCatsRepository()

    private val refreshRandomCatFact = RefreshRandomCatFact(
        catsRepository = catsRepository
    )
    private val observeRandomCatFact = ObserveRandomCatFact(
        catsRepository = catsRepository
    )

    private lateinit var viewModel: FactViewModel

    @BeforeEach
    fun setUp() {
        viewModel = FactViewModel(
            refreshRandomCatFact = refreshRandomCatFact,
            observeRandomCatFact = observeRandomCatFact
        )
    }

    @Test
    fun uiState_whenSuccess_matchesCatFactFromRepository() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }

        catsRepository.sendRandomCatFact(Fact("This is a cat fact.", length = 19))

        val successItem = viewModel.uiState.value

        assertIs<RandomCatFactUiState.Success>(successItem)

        val catFactFromRepository = catsRepository.getLatestCatFact().first()

        assertEquals(catFactFromRepository?.fact, successItem.fact)
    }
}
