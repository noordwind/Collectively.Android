package pl.adriankremski.collectively.usecases

import io.reactivex.Observable
import pl.adriankremski.collectively.model.RemarkCategory
import pl.adriankremski.collectively.repository.RemarksRepository

class LoadRemarkCategoriesUseCase(val remarksRepository: RemarksRepository) {
    fun loadRemarkCategories() : Observable<List<RemarkCategory>> = remarksRepository.loadRemarkCategories()
}