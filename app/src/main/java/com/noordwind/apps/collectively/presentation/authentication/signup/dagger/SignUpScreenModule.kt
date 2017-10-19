package com.noordwind.apps.collectively.presentation.settings.dagger

import com.noordwind.apps.collectively.data.repository.AuthenticationRepository
import com.noordwind.apps.collectively.data.repository.util.ConnectivityRepository
import com.noordwind.apps.collectively.domain.interactor.authentication.SignUpUseCase
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import com.noordwind.apps.collectively.presentation.authentication.signup.mvp.SignUpMvp
import com.noordwind.apps.collectively.presentation.authentication.signup.mvp.SignUpPresenter
import dagger.Module
import dagger.Provides

@Module
class SignUpScreenModule(val view: SignUpMvp.View) {

    @Provides
    internal fun presenter(authenticationRepository: AuthenticationRepository,
                           connectivityRepository: ConnectivityRepository,
                           ioThread: UseCaseThread, uiThread: PostExecutionThread): SignUpMvp.Presenter {

        return SignUpPresenter(view,
                SignUpUseCase(authenticationRepository, ioThread, uiThread),
                connectivityRepository)

    }
}
