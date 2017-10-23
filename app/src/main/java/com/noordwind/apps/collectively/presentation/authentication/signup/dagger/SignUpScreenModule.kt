package com.noordwind.apps.collectively.presentation.settings.dagger

import com.noordwind.apps.collectively.domain.repository.ConnectivityRepository
import com.noordwind.apps.collectively.domain.interactor.authentication.SignUpUseCase
import com.noordwind.apps.collectively.presentation.authentication.signup.mvp.SignUpMvp
import com.noordwind.apps.collectively.presentation.authentication.signup.mvp.SignUpPresenter
import dagger.Module
import dagger.Provides

@Module
class SignUpScreenModule(val view: SignUpMvp.View) {

    @Provides
    internal fun presenter(signUpUseCase: SignUpUseCase, connectivityRepository: ConnectivityRepository): SignUpMvp.Presenter {
        return SignUpPresenter(view, signUpUseCase, connectivityRepository)
    }
}
