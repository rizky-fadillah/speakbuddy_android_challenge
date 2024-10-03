package jp.speakbuddy.edisonandroidexercise.domain

import javax.inject.Inject
import jp.speakbuddy.edisonandroidexercise.data.repository.CatsRepository
import jp.speakbuddy.edisonandroidexercise.model.PresentableFact
import jp.speakbuddy.edisonandroidexercise.model.toPresentableFact
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchCatFacts @Inject constructor(private val catsRepository: CatsRepository) {

    operator fun invoke(searchQuery: String): Flow<List<PresentableFact>> =
        catsRepository.searchCatFacts(searchQuery)
            .map { it.map { fact -> fact.toPresentableFact() } }
}