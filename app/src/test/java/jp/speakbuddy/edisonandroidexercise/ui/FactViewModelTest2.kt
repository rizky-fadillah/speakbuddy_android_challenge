//package jp.speakbuddy.edisonandroidexercise.ui
//
//import jp.speakbuddy.edisonandroidexercise.core.testing.repository.TestCatsRepository
//import jp.speakbuddy.edisonandroidexercise.core.testing.util.MainDispatcherRule
//import jp.speakbuddy.edisonandroidexercise.domain.ObserveRandomCatFact
//import jp.speakbuddy.edisonandroidexercise.domain.RefreshRandomCatFact
//import jp.speakbuddy.edisonandroidexercise.model.Fact
//import jp.speakbuddy.edisonandroidexercise.ui.fact.FactViewModel
//import jp.speakbuddy.edisonandroidexercise.ui.fact.RandomCatFactUiState
//import kotlinx.coroutines.flow.collect
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.test.UnconfinedTestDispatcher
//import kotlinx.coroutines.test.runTest
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import kotlin.test.assertEquals
//import kotlin.test.assertIs
//
//class FactViewModelTest2 {
//
//    @get:Rule
//    val dispatcherRule = MainDispatcherRule()
//
//    private val catsRepository = TestCatsRepository()
//
//    private val refreshRandomCatFact = RefreshRandomCatFact(
//        catsRepository = catsRepository
//    )
//    private val observeRandomCatFact = ObserveRandomCatFact(
//        catsRepository = catsRepository
//    )
//
//    private lateinit var viewModel: FactViewModel
//
//    @Before
//    fun setUp() {
//        viewModel = FactViewModel(
//            refreshRandomCatFact = refreshRandomCatFact,
//            observeRandomCatFact = observeRandomCatFact
//        )
//    }
//
//    @Test
//    fun test() = runTest {
//        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }
//
//        val loadingItem = viewModel.uiState.value
//
//        assertIs<RandomCatFactUiState.Loading>(loadingItem)
//
//        catsRepository.sendRandomCatFact(Fact("This is a cat fact.", length = 19))
//
//        val successItem = viewModel.uiState.value
//
//        assertIs<RandomCatFactUiState.Success>(successItem)
//        assertEquals("This is a cat fact.", successItem.fact)
//    }
//}
