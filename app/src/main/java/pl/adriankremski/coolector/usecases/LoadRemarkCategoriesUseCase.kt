package pl.adriankremski.coolector.usecases

import io.reactivex.Observable
import pl.adriankremski.coolector.model.RemarkCategory
import pl.adriankremski.coolector.repository.RemarksRepository

class LoadRemarkCategoriesUseCase(val remarksRepository: RemarksRepository) {
    fun loadRemarkCategories() : Observable<List<RemarkCategory>> = remarksRepository.loadRemarkCategories()
}