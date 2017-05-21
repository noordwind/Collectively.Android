package pl.adriankremski.collectively.domain.interactor.remark.filters

import io.reactivex.Observable
import pl.adriankremski.collectively.data.datasource.FiltersRepository
import pl.adriankremski.collectively.domain.interactor.UseCase
import pl.adriankremski.collectively.domain.thread.PostExecutionThread
import pl.adriankremski.collectively.domain.thread.UseCaseThread

class SelectShowOnlyMyRemarksUseCase(val filtersRepository: FiltersRepository,
                            useCaseThread: UseCaseThread,
                            postExecutionThread: PostExecutionThread) : UseCase<Boolean, Boolean>(useCaseThread, postExecutionThread) {

    override fun buildUseCaseObservable(shouldShow: Boolean?): Observable<Boolean> = filtersRepository.selectShowOnlyMineStatus(shouldShow!!)
}

