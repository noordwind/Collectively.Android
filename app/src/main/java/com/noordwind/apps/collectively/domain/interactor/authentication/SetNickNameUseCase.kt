package com.noordwind.apps.collectively.domain.interactor.authentication

import com.noordwind.apps.collectively.data.repository.AuthenticationRepository
import com.noordwind.apps.collectively.domain.interactor.UseCase
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import io.reactivex.Observable

class SetNickNameUseCase(val authenticationRepository: AuthenticationRepository,
                         useCaseThread: UseCaseThread,
                         postExecutionThread: PostExecutionThread) : UseCase<Boolean, String>(useCaseThread, postExecutionThread) {

    override fun buildUseCaseObservable(name: String?): Observable<Boolean> = authenticationRepository.setNickName(name!!)
}

