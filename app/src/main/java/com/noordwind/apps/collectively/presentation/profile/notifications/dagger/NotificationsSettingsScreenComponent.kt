package com.noordwind.apps.collectively.presentation.settings.dagger

import com.noordwind.apps.collectively.presentation.dagger.ActivityScope
import com.noordwind.apps.collectively.presentation.profile.notifications.NotificationsSettingsActivity
import com.noordwind.apps.collectively.presentation.profile.remarks.user.UserRemarksActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = arrayOf(NotificationsSettingsScreenModule::class))
interface NotificationsSettingsScreenComponent {
    fun inject(activity: NotificationsSettingsActivity)
}
