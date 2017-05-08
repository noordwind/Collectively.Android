package pl.adriankremski.collectively.domain.interactor.remark

import io.reactivex.Observable
import pl.adriankremski.collectively.domain.thread.PostExecutionThread
import pl.adriankremski.collectively.domain.thread.UseCaseThread
import pl.adriankremski.collectively.domain.interactor.UseCase
import pl.adriankremski.collectively.data.model.RemarkCategory
import pl.adriankremski.collectively.data.repository.RemarksRepository

class LoadRemarkCategoriesUseCase(val remarksRepository: RemarksRepository,
                                  useCaseThread: UseCaseThread,
                                  postExecutionThread: PostExecutionThread) : UseCase<List<RemarkCategory>, Void>(useCaseThread, postExecutionThread) {

    override fun buildUseCaseObservable(params: Void?): Observable<List<RemarkCategory>> = remarksRepository.loadRemarkCategories()
}