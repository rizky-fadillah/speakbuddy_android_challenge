package jp.speakbuddy.edisonandroidexercise.testing.data

import jp.speakbuddy.edisonandroidexercise.model.PresentableFact

val factGreaterThan100WithMultipleCats = PresentableFact(
    fact = """
        Cats' eyes shine in the dark because of the tapetum, a reflective layer in the eye, which 
        acts like a mirror.
    """.trimIndent(),
    length = "109",
    shouldShowMultipleCats = true
)

val factGreaterThan100WithoutMultipleCats = PresentableFact(
    fact = """
        The most traveled cat is Hamlet, who escaped from his carrier while on a flight. 
        He hid for seven weeks behind a panel on the airplane. By the time he was discovered,
        he had traveled nearly 373,000 miles (600,000 km).
    """.trimIndent(),
    length = "217",
    shouldShowMultipleCats = false
)

val factSmallerThan100WithMultipleCats = PresentableFact(
    fact = "A group of cats is called a clowder.",
    length = null,
    shouldShowMultipleCats = true
)

val factSmallerThan100WithoutMultipleCats = PresentableFact(
    fact = "A tomcat (male cat) can begin mating when he is between 7 and 10 months old.",
    length = null,
    shouldShowMultipleCats = false
)