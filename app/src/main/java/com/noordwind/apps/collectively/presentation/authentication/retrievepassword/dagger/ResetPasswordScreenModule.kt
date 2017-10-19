package com.noordwind.apps.collectively.presentation.settings.dagger

import com.noordwind.apps.collectively.data.repository.AuthenticationRepository
import com.noordwind.apps.collectively.data.repository.util.ConnectivityRepository
import com.noordwind.apps.collectively.domain.interactor.authentication.RetrievePasswordUseCase
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import com.noordwind.apps.collectively.presentation.authentication.retrievepassword.mvp.ResetPasswordMvp
import com.noordwind.apps.collectively.presentation.authentication.retrievepassword.mvp.ResetPasswordPresenter
import dagger.Module
import dagger.Provides

@Module
class ResetPasswordScreenModule(val view: ResetPasswordMvp.View) {

    @Provides
    internal fun presenter(authenticationRepository: AuthenticationRepository,
                           connectivityRepository: ConnectivityRepository,
                           ioThread: UseCaseThread, uiThread: PostExecutionThread): ResetPasswordMvp.Presenter {

        return ResetPasswordPresenter(view,
                RetrievePasswordUseCase(authenticationRepository, ioThread, uiThread),
                connectivityRepository)
    }
}
