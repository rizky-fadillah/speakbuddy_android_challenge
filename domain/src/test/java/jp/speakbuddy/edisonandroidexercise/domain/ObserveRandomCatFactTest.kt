package jp.speakbuddy.edisonandroidexercise.domain

import jp.speakbuddy.edisonandroidexercise.model.Fact
import jp.speakbuddy.edisonandroidexercise.model.PresentableFact
import jp.speakbuddy.edisonandroidexercise.testing.data.factGreaterThan100WithMultipleCats
import jp.speakbuddy.edisonandroidexercise.testing.data.factGreaterThan100WithoutMultipleCats
import jp.speakbuddy.edisonandroidexercise.testing.data.factSmallerThan100WithMultipleCats
import jp.speakbuddy.edisonandroidexercise.testing.data.factSmallerThan100WithoutMultipleCats
import jp.speakbuddy.edisonandroidexercise.testing.repository.TestCatsRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * This class tests the mapping logic of the `Fact.toPresentableFact()` function
 * in various scenarios using the `ObserveRandomCatFact` use case.
 *
 * Scenarios include:
 * - Facts with length > 100 and containing multiple cats
 * - Facts with length > 100 and without multiple cats
 * - Facts with length < 100 and containing multiple cats
 * - Facts with length < 100 and without multiple cats
 */
class ObserveRandomCatFactTest {

    // A fake implementation of the CatsRepository to simulate sending and observing cat facts.
    private val catsRepository = TestCatsRepository()

    // The use case under test.
    private lateinit var observeRandomCatFact: ObserveRandomCatFact

    @BeforeEach
    fun setUp() {
        observeRandomCatFact = ObserveRandomCatFact(
            catsRepository = catsRepository,
        )
    }

    @Test
    fun greaterThan100WithMultipleCats_ReturnsExpectedPresentableFact() =
        runTestWithFact(factGreaterThan100WithMultipleCats)

    @Test
    fun greaterThan100WithoutMultipleCats_ReturnsExpectedPresentableFact() =
        runTestWithFact(factGreaterThan100WithoutMultipleCats)

    @Test
    fun smallerThan100WithMultipleCats_ReturnsExpectedPresentableFact() =
        runTestWithFact(factSmallerThan100WithMultipleCats)

    @Test
    fun smallerThan100WithoutMultipleCats_ReturnsExpectedPresentableFact() =
        runTestWithFact(factSmallerThan100WithoutMultipleCats)

    /**
     * Helper function to test various scenarios of cat facts mapping to `PresentableFact`.
     * It sends a fact to the repository and verifies that the observed `PresentableFact`
     * matches the expected output.
     *
     * @param expectedPresentableFact The expected mapped result of the fact.
     */
    private fun runTestWithFact(expectedPresentableFact: PresentableFact) = runTest {
        // Arrange: Send the fact to the repository using its length
        val fact = Fact(expectedPresentableFact.fact, expectedPresentableFact.fact.length)
        catsRepository.sendRandomCatFact(fact)

        // Act: Observe and map the fact from the repository
        val actualPresentableFact = observeRandomCatFact.invoke().first()

        // Assert: Verify the fact matches the expected presentable fact
        assertEquals(expectedPresentableFact, actualPresentableFact)
    }
}
