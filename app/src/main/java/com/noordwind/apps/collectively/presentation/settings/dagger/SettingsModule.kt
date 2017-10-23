package com.noordwind.apps.collectively.presentation.settings.dagger

import com.noordwind.apps.collectively.data.cache.ProfileCache
import com.noordwind.apps.collectively.data.datasource.Session
import com.noordwind.apps.collectively.domain.interactor.authentication.DeleteAccountUseCase
import com.noordwind.apps.collectively.presentation.settings.mvp.SettingsMvp
import com.noordwind.apps.collectively.presentation.settings.mvp.SettingsPresenter
import dagger.Module
import dagger.Provides

@Module
class SettingsModule(val view: SettingsMvp.View) {

    @Provides
    internal fun presenter(session: Session, profileCache: ProfileCache,
                           deleteAccountUseCase: DeleteAccountUseCase): SettingsMvp.Presenter {
        return SettingsPresenter(view, session, profileCache, deleteAccountUseCase)
    }
}
