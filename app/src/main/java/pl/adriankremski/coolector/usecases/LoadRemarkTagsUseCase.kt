package pl.adriankremski.coolector.usecases

import io.reactivex.Observable
import pl.adriankremski.coolector.model.RemarkTag
import pl.adriankremski.coolector.repository.RemarksRepository

class LoadRemarkTagsUseCase(val remarksRepository: RemarksRepository) {
    fun loadRemarkTags() : Observable<List<RemarkTag>> = remarksRepository.loadRemarkTags()
}