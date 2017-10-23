package com.noordwind.apps.collectively.presentation.settings.dagger

import com.noordwind.apps.collectively.data.cache.ProfileCache
import com.noordwind.apps.collectively.data.datasource.Session
import com.noordwind.apps.collectively.domain.repository.ConnectivityRepository
import com.noordwind.apps.collectively.domain.interactor.authentication.SetNickNameUseCase
import com.noordwind.apps.collectively.presentation.authentication.setnickname.mvp.SetNickNameMvp
import com.noordwind.apps.collectively.presentation.authentication.setnickname.mvp.SetNickNamePresenter
import dagger.Module
import dagger.Provides

@Module
class SetNicknameScreenModule(val view: SetNickNameMvp.View) {

    @Provides
    internal fun presenter(
            setNickNameUseCase: SetNickNameUseCase,
            connectivityRepository: ConnectivityRepository,
            session: Session,
            profileCache: ProfileCache): SetNickNameMvp.Presenter {

        return SetNickNamePresenter(view, session, profileCache, setNickNameUseCase, connectivityRepository)
    }
}
