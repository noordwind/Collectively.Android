package com.noordwind.apps.collectively.presentation.settings.dagger

import com.noordwind.apps.collectively.data.repository.AuthenticationRepository
import com.noordwind.apps.collectively.domain.interactor.authentication.ChangePasswordUseCase
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import com.noordwind.apps.collectively.presentation.changepassword.mvp.ChangePasswordMvp
import com.noordwind.apps.collectively.presentation.changepassword.mvp.ChangePasswordPresenter
import dagger.Module
import dagger.Provides

@Module
class ChangePasswordScreenModule(val view: ChangePasswordMvp.View) {

    @Provides
    internal fun presenter(authenticationRepository: AuthenticationRepository, ioThread: UseCaseThread, uiThread: PostExecutionThread): ChangePasswordMvp.Presenter {
        return ChangePasswordPresenter(view, ChangePasswordUseCase(authenticationRepository, ioThread, uiThread))
    }
}
