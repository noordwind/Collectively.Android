package com.noordwind.apps.collectively.domain.interactor.remark

import com.noordwind.apps.collectively.data.model.RemarkPhoto
import com.noordwind.apps.collectively.data.model.RemarkPreview
import com.noordwind.apps.collectively.data.repository.RemarksRepository
import com.noordwind.apps.collectively.domain.interactor.UseCase
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class LoadRemarkPhotoUseCase(
        val remarksRepository: RemarksRepository,
        useCaseThread: UseCaseThread,
        postExecutionThread: PostExecutionThread) : UseCase<RemarkPhoto, String>(useCaseThread, postExecutionThread) {

    private var RETRY_DELAY_IN_SECONDS = 2L

    override fun buildUseCaseObservable(id: String?): Observable<RemarkPhoto> {
        val remarkObs = remarksRepository.loadRemark(id!!)

        return remarkObs
                .repeatWhen { objectObservable: Observable<Any> -> objectObservable.delay(RETRY_DELAY_IN_SECONDS, TimeUnit.SECONDS) }
                .takeUntil { remark: RemarkPreview -> remark.getFirstBigPhoto() != null }
                .filter { remark: RemarkPreview -> remark.getFirstBigPhoto() != null }
                .flatMap { Observable.just(it.getFirstBigPhoto()!!) }
    }
}

