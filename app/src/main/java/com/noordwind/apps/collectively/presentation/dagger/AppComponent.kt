package com.noordwind.apps.collectively.presentation.dagger

import android.app.Application
import com.noordwind.apps.collectively.presentation.addremark.AddRemarkActivity
import com.noordwind.apps.collectively.presentation.authentication.login.LoginActivity
import com.noordwind.apps.collectively.presentation.authentication.retrievepassword.ResetPasswordActivity
import com.noordwind.apps.collectively.presentation.authentication.signup.SignUpActivity
import com.noordwind.apps.collectively.presentation.changepassword.ChangePasswordActivity
import com.noordwind.apps.collectively.presentation.main.MainActivity
import com.noordwind.apps.collectively.presentation.main.navigation.MainNavigationFragment
import com.noordwind.apps.collectively.presentation.profile.ProfileActivity
import com.noordwind.apps.collectively.presentation.profile.notifications.NotificationsSettingsActivity
import com.noordwind.apps.collectively.presentation.profile.remarks.user.UserRemarksActivity
import com.noordwind.apps.collectively.presentation.remarkpreview.RemarkActivity
import com.noordwind.apps.collectively.presentation.remarkpreview.activity.RemarkStatesActivity
import com.noordwind.apps.collectively.presentation.remarkpreview.comments.RemarkCommentsActivity
import com.noordwind.apps.collectively.presentation.settings.SettingsActivity
import com.noordwind.apps.collectively.presentation.statistics.StatisticsActivity
import com.noordwind.apps.collectively.presentation.users.UsersActivity
import com.noordwind.apps.collectively.presentation.views.RemarkCommentView
import com.noordwind.apps.collectively.presentation.views.dialogs.mapfilters.MapFiltersDialog
import com.noordwind.apps.collectively.presentation.views.dialogs.mapfilters.RemarkFiltersDialog
import com.noordwind.apps.collectively.presentation.views.remark.RemarkStateView
import com.noordwind.apps.collectively.presentation.views.remark.comment.RemarkCommentRowHolder
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
    fun application(): Application

    //Activites
    fun inject(activity: LoginActivity)
    fun inject(activity: ResetPasswordActivity)
    fun inject(activity: SignUpActivity)
    fun inject(mainActivity: MainActivity)
    fun inject(activity: AddRemarkActivity)
    fun inject(activity: StatisticsActivity)
    fun inject(activity: ProfileActivity)
    fun inject(activity: RemarkActivity)
    fun inject(activity: SettingsActivity)
    fun inject(activity: RemarkCommentsActivity)
    fun inject(activity: RemarkStatesActivity)
    fun inject(activity: UserRemarksActivity)
    fun inject(activity: NotificationsSettingsActivity)
    fun inject(activity: ChangePasswordActivity)
    fun inject(activity: UsersActivity)

    //Views
    fun inject(remarkCommentRowHolder: RemarkCommentRowHolder)
    fun inject(mapFiltersDialog: MapFiltersDialog)
    fun inject(remarkFiltersDialog: RemarkFiltersDialog)
    fun inject(mainNavigationFragment: MainNavigationFragment)
    fun inject(view: RemarkStateView)
    fun inject(view: RemarkCommentView)
}
