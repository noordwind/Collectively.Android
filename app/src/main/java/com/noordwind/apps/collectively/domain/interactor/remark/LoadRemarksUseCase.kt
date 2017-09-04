package com.noordwind.apps.collectively.domain.interactor.remark

import com.google.android.gms.maps.model.LatLng
import com.noordwind.apps.collectively.data.model.Remark
import com.noordwind.apps.collectively.data.repository.RemarksRepository
import com.noordwind.apps.collectively.domain.interactor.UseCase
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import io.reactivex.Observable

class LoadRemarksUseCase(val remarksRepository: RemarksRepository,
                         useCaseThread: UseCaseThread,
                         postExecutionThread: PostExecutionThread) : UseCase<List<Remark>, Pair<LatLng, Int>>(useCaseThread, postExecutionThread) {

    override fun buildUseCaseObservable(params: Pair<LatLng, Int>?): Observable<List<Remark>> = remarksRepository.loadRemarks(params!!.first, params!!.second)
}

