package pl.adriankremski.collectively.presentation.profile.notifications

import io.reactivex.disposables.Disposable
import pl.adriankremski.collectively.presentation.model.NotificationOption
import pl.adriankremski.collectively.presentation.mvp.BasePresenter

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

    interface Presenter : BasePresenter{
        fun loadSettings()
        fun saveSettings(emailNotificationsEnabled: Boolean, optionsList: List<NotificationOption>)
    }
}
