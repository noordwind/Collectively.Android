package com.noordwind.apps.collectively.domain.interactor.authentication

import io.reactivex.Observable
import com.noordwind.apps.collectively.data.model.Optional
import com.noordwind.apps.collectively.data.repository.FacebookTokenRepository
import com.noordwind.apps.collectively.domain.interactor.UseCase
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread

class GetFacebookTokenUseCase(val facebookTokenRepository: FacebookTokenRepository,
                              useCaseThread: UseCaseThread,
                              postExecutionThread: PostExecutionThread) : UseCase<Optional<String>, Void>(useCaseThread, postExecutionThread) {
    override fun buildUseCaseObservable(params: Void?): Observable<Optional<String>> = facebookTokenRepository.facebookToken
}

