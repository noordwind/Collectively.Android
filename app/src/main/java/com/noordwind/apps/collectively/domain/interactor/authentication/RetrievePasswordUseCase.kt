package com.noordwind.apps.collectively.domain.interactor.authentication

import io.reactivex.Observable
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import com.noordwind.apps.collectively.data.repository.AuthenticationRepository
import com.noordwind.apps.collectively.domain.interactor.UseCase

class RetrievePasswordUseCase(val authenticationRepository: AuthenticationRepository,
                              useCaseThread: UseCaseThread,
                              postExecutionThread: PostExecutionThread) : UseCase<Boolean, String>(useCaseThread, postExecutionThread) {

    override fun buildUseCaseObservable(email: String?): Observable<Boolean> = authenticationRepository.resetPassword(email!!)
}

