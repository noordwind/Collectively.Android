package com.noordwind.apps.collectively.domain.interactor.authentication

import io.reactivex.Observable
import com.noordwind.apps.collectively.data.repository.AuthenticationRepository
import com.noordwind.apps.collectively.domain.interactor.UseCase
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread

class ChangePasswordUseCase(val authenticationRepository: AuthenticationRepository,
                              useCaseThread: UseCaseThread,
                              postExecutionThread: PostExecutionThread) : UseCase<Boolean, Pair<String, String>>(useCaseThread, postExecutionThread) {

    override fun buildUseCaseObservable(params: Pair<String, String>?): Observable<Boolean> = authenticationRepository.changePassword(params!!.first!!, params!!.second)
}

