package com.noordwind.apps.collectively.domain.interactor.remark

import io.reactivex.Observable
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import com.noordwind.apps.collectively.domain.interactor.UseCase
import com.noordwind.apps.collectively.data.model.RemarkCategory
import com.noordwind.apps.collectively.data.repository.RemarksRepository

class LoadRemarkCategoriesUseCase(val remarksRepository: RemarksRepository,
                                  useCaseThread: UseCaseThread,
                                  postExecutionThread: PostExecutionThread) : UseCase<List<RemarkCategory>, Void>(useCaseThread, postExecutionThread) {

    override fun buildUseCaseObservable(params: Void?): Observable<List<RemarkCategory>> = remarksRepository.loadRemarkCategories()
}