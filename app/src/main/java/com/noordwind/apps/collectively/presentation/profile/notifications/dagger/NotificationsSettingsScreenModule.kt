package com.noordwind.apps.collectively.presentation.settings.dagger

import com.noordwind.apps.collectively.domain.repository.NotificationOptionNameRepository
import com.noordwind.apps.collectively.domain.interactor.LoadSettingsUseCase
import com.noordwind.apps.collectively.domain.interactor.SaveSettingsUseCase
import com.noordwind.apps.collectively.presentation.profile.notifications.mvp.NotificationsSettingsMvp
import com.noordwind.apps.collectively.presentation.profile.notifications.mvp.NotificationsSettingsPresenter
import dagger.Module
import dagger.Provides

@Module
class NotificationsSettingsScreenModule(val view: NotificationsSettingsMvp.View) {

    @Provides
    internal fun presenter(loadSettingsUseCase: LoadSettingsUseCase, saveSettingsUseCase: SaveSettingsUseCase,
                           notificationOptionNameRepository: NotificationOptionNameRepository): NotificationsSettingsMvp.Presenter {
        return NotificationsSettingsPresenter(view, loadSettingsUseCase, saveSettingsUseCase, notificationOptionNameRepository)
    }
}
