package com.noordwind.apps.collectively.domain.interactor.remark

import io.reactivex.Observable
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import com.noordwind.apps.collectively.domain.interactor.UseCase
import com.noordwind.apps.collectively.data.model.RemarkTag
import com.noordwind.apps.collectively.data.repository.RemarksRepository

class LoadRemarkTagsUseCase(val remarksRepository: RemarksRepository,
                            useCaseThread: UseCaseThread,
                            postExecutionThread: PostExecutionThread) : UseCase<List<RemarkTag>, Void>(useCaseThread, postExecutionThread) {

    override fun buildUseCaseObservable(params: Void?): Observable<List<RemarkTag>> = remarksRepository.loadRemarkTags()
}