package com.noordwind.apps.collectively.presentation.settings.dagger

import com.noordwind.apps.collectively.data.repository.ProfileRepository
import com.noordwind.apps.collectively.data.repository.util.ConnectivityRepository
import com.noordwind.apps.collectively.domain.interactor.GetFacebookTokenUseCase
import com.noordwind.apps.collectively.domain.interactor.authentication.FacebookLoginUseCase
import com.noordwind.apps.collectively.domain.interactor.authentication.LoginUseCase
import com.noordwind.apps.collectively.presentation.authentication.login.mvp.LoginMvp
import com.noordwind.apps.collectively.presentation.authentication.login.mvp.LoginPresenter
import dagger.Module
import dagger.Provides

@Module
class LoginScreenModule(val view: LoginMvp.View) {

    @Provides
    internal fun presenter(
            loginUseCase: LoginUseCase,
            facebookLoginUseCase: FacebookLoginUseCase,
            getFacebookTokenUseCase: GetFacebookTokenUseCase,
            profileRepository: ProfileRepository,
            connectivityRepository: ConnectivityRepository): LoginMvp.Presenter {

        return LoginPresenter(view, loginUseCase, facebookLoginUseCase, getFacebookTokenUseCase,
                profileRepository, connectivityRepository)
    }
}
