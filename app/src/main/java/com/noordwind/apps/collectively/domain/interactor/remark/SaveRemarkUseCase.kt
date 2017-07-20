package com.noordwind.apps.collectively.domain.interactor.remark

import io.reactivex.Observable
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import com.noordwind.apps.collectively.domain.interactor.UseCase
import com.noordwind.apps.collectively.data.model.NewRemark
import com.noordwind.apps.collectively.data.model.RemarkNotFromList
import com.noordwind.apps.collectively.data.repository.RemarksRepository

class SaveRemarkUseCase(val remarksRepository: RemarksRepository,
                        useCaseThread: UseCaseThread,
                        postExecutionThread: PostExecutionThread) : UseCase<RemarkNotFromList, NewRemark>(useCaseThread, postExecutionThread) {

    override fun buildUseCaseObservable(newRemark: NewRemark?): Observable<RemarkNotFromList> = remarksRepository.saveRemark(newRemark!!)
}