package com.noordwind.apps.collectively.presentation.profile.notifications.mvp

import io.reactivex.disposables.Disposable
import com.noordwind.apps.collectively.presentation.model.NotificationOption
import com.noordwind.apps.collectively.presentation.mvp.BasePresenter

interface NotificationsSettingsMvp {

    interface View {
        fun addDisposable(disposable: Disposable)
        fun showEmailNotificationsOptions(options: List<NotificationOption>)
        fun showSettingsLoading()
        fun showSettingsLoadingError(message: String)
        fun showSettingsLoadingNetworkError()

        fun showSaveSettingsLoading()
        fun showSaveSettingsLoadingError(message: String)
        fun showSaveSettingsLoadingNetworkError()
        fun showSaveSettingsSuccess()
        fun toggleEmailNotificationsEnabled(enabled: Boolean)
    }

    interface Presenter : BasePresenter {
        fun loadSettings()
        fun saveSettings(emailNotificationsEnabled: Boolean, optionsList: List<NotificationOption>)
    }
}
