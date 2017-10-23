package com.noordwind.apps.collectively.presentation.profile.notifications.mvp

import com.noordwind.apps.collectively.data.model.Settings
import com.noordwind.apps.collectively.data.model.SettingsEntry
import com.noordwind.apps.collectively.domain.repository.NotificationOptionNameRepository
import com.noordwind.apps.collectively.domain.interactor.settings.LoadSettingsUseCase
import com.noordwind.apps.collectively.domain.interactor.settings.SaveSettingsUseCase
import com.noordwind.apps.collectively.presentation.model.NotificationOption
import com.noordwind.apps.collectively.presentation.profile.notifications.mvp.NotificationsSettingsMvp
import com.noordwind.apps.collectively.presentation.rxjava.AppDisposableObserver
import java.util.*


class NotificationsSettingsPresenter(
        val view: NotificationsSettingsMvp.View,
        val loadSettingsUseCase: LoadSettingsUseCase,
        val saveSettingsUseCase: SaveSettingsUseCase,
        val optionNamesRepository: NotificationOptionNameRepository
        ): NotificationsSettingsMvp.Presenter {

    override fun loadSettings() {
        var observer = object : AppDisposableObserver<Settings>() {

            override fun onStart() {
                super.onStart()
                view.showSettingsLoading()
            }

            override fun onNext(settings: Settings) {
                super.onNext(settings)

                var notificationsOptions = LinkedList<NotificationOption>()

                notificationsOptions.add(NotificationOption(
                        name = optionNamesRepository.remarkCreatedOptionName(),
                        description = optionNamesRepository.remarkCreatedOptionDescription(),
                        isChecked = settings.emailSettings.remarkCreated))

                notificationsOptions.add(NotificationOption(
                        name = optionNamesRepository.remarkProcessedOptionName(),
                        description = optionNamesRepository.remarkProcessedOptionDescription(),
                        isChecked = settings.emailSettings.remarkProcessed))

                notificationsOptions.add(NotificationOption(
                        name = optionNamesRepository.remarkResolvedOptionName(),
                        description = optionNamesRepository.remarkResolvedOptionDescription(),
                        isChecked = settings.emailSettings.remarkResolved))

                notificationsOptions.add(NotificationOption(
                        name = optionNamesRepository.remarkCanceledOptionName(),
                        description = optionNamesRepository.remarkCanceledOptionDescription(),
                        isChecked = settings.emailSettings.remarkCanceled))

                notificationsOptions.add(NotificationOption(
                        name = optionNamesRepository.remarkRenewedOptionName(),
                        description = optionNamesRepository.remarkRenewedOptionDescription(),
                        isChecked = settings.emailSettings.remarkRenewed))

                notificationsOptions.add(NotificationOption(
                        name = optionNamesRepository.newPhotoAddedOptionName(),
                        description = optionNamesRepository.newPhotoAddedOptionDescription(),
                        isChecked = settings.emailSettings.photosToRemarkAdded))

                notificationsOptions.add(NotificationOption(
                        name = optionNamesRepository.newCommentAddedOptionName(),
                        description = optionNamesRepository.newCommentAddedOptionDescription(),
                        isChecked = settings.emailSettings.commentAdded))

                view.showEmailNotificationsOptions(notificationsOptions)
                view.toggleEmailNotificationsEnabled(settings.emailSettings.enabled)
            }

            override fun onError(e: Throwable) {
                super.onError(e)
            }

            override fun onServerError(message: String?) {
                super.onServerError(message)
                if (message != null) {
                    view.showSettingsLoadingError(message)
                }
            }

            override fun onNetworkError() {
                super.onNetworkError()
                view.showSettingsLoadingNetworkError()
            }
        }

        loadSettingsUseCase.execute(observer)
    }

    override fun saveSettings(emailNotificationEnabled: Boolean, optionsList: List<NotificationOption>) {
        var emailSettings = SettingsEntry(
                enabled = emailNotificationEnabled,
                remarkCreated = optionsList.find { it.name.equals(optionNamesRepository.remarkCreatedOptionName(), true) }!!.isChecked,
                remarkCanceled = optionsList.find { it.name.equals(optionNamesRepository.remarkCanceledOptionName(), true) }!!.isChecked,
                remarkDeleted = false,
                remarkProcessed = optionsList.find { it.name.equals(optionNamesRepository.remarkProcessedOptionName(), true) }!!.isChecked,
                remarkRenewed = optionsList.find { it.name.equals(optionNamesRepository.remarkRenewedOptionName(), true) }!!.isChecked,
                remarkResolved = optionsList.find { it.name.equals(optionNamesRepository.remarkResolvedOptionName(), true) }!!.isChecked,
                photosToRemarkAdded = optionsList.find { it.name.equals(optionNamesRepository.newPhotoAddedOptionName(), true) }!!.isChecked,
                commentAdded = optionsList.find { it.name.equals(optionNamesRepository.newCommentAddedOptionName(), true) }!!.isChecked
        )

        val notificationSettings = Settings(emailSettings)

        var observer = object : AppDisposableObserver<Boolean>() {

            override fun onStart() {
                super.onStart()
                view.showSaveSettingsLoading()
            }

            override fun onNext(result: Boolean) {
                super.onNext(result)
                view.showSaveSettingsSuccess()
            }

            override fun onError(e: Throwable) {
                super.onError(e)
            }

            override fun onServerError(message: String?) {
                super.onServerError(message)
                if (message != null) {
                    view.showSaveSettingsLoadingError(message)
                }
            }

            override fun onNetworkError() {
                super.onNetworkError()
                view.showSaveSettingsLoadingNetworkError()
            }
        }

        saveSettingsUseCase.execute(observer, notificationSettings)
    }

    override fun destroy() {
        loadSettingsUseCase.dispose()
        saveSettingsUseCase.dispose()
    }
}
