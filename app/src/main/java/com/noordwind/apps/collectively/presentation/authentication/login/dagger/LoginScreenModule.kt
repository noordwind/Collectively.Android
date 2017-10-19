package com.noordwind.apps.collectively.presentation.settings.dagger

import com.noordwind.apps.collectively.data.repository.AuthenticationRepository
import com.noordwind.apps.collectively.data.repository.FacebookTokenRepository
import com.noordwind.apps.collectively.data.repository.ProfileRepository
import com.noordwind.apps.collectively.data.repository.SessionRepository
import com.noordwind.apps.collectively.data.repository.util.ConnectivityRepository
import com.noordwind.apps.collectively.domain.interactor.GetFacebookTokenUseCase
import com.noordwind.apps.collectively.domain.interactor.authentication.FacebookLoginUseCase
import com.noordwind.apps.collectively.domain.interactor.authentication.LoginUseCase
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import com.noordwind.apps.collectively.presentation.authentication.login.mvp.LoginMvp
import com.noordwind.apps.collectively.presentation.authentication.login.mvp.LoginPresenter
import dagger.Module
import dagger.Provides

@Module
class LoginScreenModule(val view: LoginMvp.View) {

    @Provides
    internal fun presenter(authenticationRepository: AuthenticationRepository,
                           facebookRepository: FacebookTokenRepository,
                           profileRepository: ProfileRepository,
                           sessionRepository: SessionRepository,
                           connectivityRepository: ConnectivityRepository,
                           ioThread: UseCaseThread, uiThread: PostExecutionThread): LoginMvp.Presenter {

        return LoginPresenter(view,
                LoginUseCase(authenticationRepository, sessionRepository, ioThread, uiThread),
                FacebookLoginUseCase(authenticationRepository, ioThread, uiThread),
                GetFacebookTokenUseCase(facebookRepository, ioThread, uiThread),
                profileRepository,
                connectivityRepository)
    }
}
