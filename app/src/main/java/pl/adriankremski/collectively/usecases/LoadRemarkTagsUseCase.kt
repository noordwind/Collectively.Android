package pl.adriankremski.collectively.usecases

import io.reactivex.Observable
import pl.adriankremski.collectively.model.RemarkTag
import pl.adriankremski.collectively.repository.RemarksRepository

class LoadRemarkTagsUseCase(val remarksRepository: RemarksRepository) {
    fun loadRemarkTags() : Observable<List<RemarkTag>> = remarksRepository.loadRemarkTags()
}