package com.noordwind.apps.collectively.presentation.settings.dagger

import com.noordwind.apps.collectively.data.repository.SettingsRepository
import com.noordwind.apps.collectively.data.repository.util.NotificationOptionNameRepository
import com.noordwind.apps.collectively.domain.interactor.LoadSettingsUseCase
import com.noordwind.apps.collectively.domain.interactor.SaveSettingsUseCase
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import com.noordwind.apps.collectively.presentation.profile.notifications.mvp.NotificationsSettingsMvp
import com.noordwind.apps.collectively.presentation.profile.notifications.mvp.NotificationsSettingsPresenter
import dagger.Module
import dagger.Provides

@Module
class NotificationsSettingsScreenModule(val view: NotificationsSettingsMvp.View) {

    @Provides
    internal fun presenter(settingsRepository: SettingsRepository,
                           notificationOptionNameRepository: NotificationOptionNameRepository,
                           ioThread: UseCaseThread, uiThread: PostExecutionThread): NotificationsSettingsMvp.Presenter {
        return NotificationsSettingsPresenter(view,
                LoadSettingsUseCase(settingsRepository, ioThread, uiThread),
                SaveSettingsUseCase(settingsRepository, ioThread, uiThread),
                notificationOptionNameRepository)
    }
}
