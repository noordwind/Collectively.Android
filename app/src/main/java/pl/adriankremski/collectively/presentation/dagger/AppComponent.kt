package pl.adriankremski.collectively.presentation.dagger

import android.app.Application
import dagger.Component
import pl.adriankremski.collectively.presentation.addremark.AddRemarkActivity
import pl.adriankremski.collectively.presentation.authentication.login.LoginActivity
import pl.adriankremski.collectively.presentation.authentication.retrievepassword.ResetPasswordActivity
import pl.adriankremski.collectively.presentation.authentication.signup.SignUpActivity
import pl.adriankremski.collectively.presentation.changepassword.ChangePasswordActivity
import pl.adriankremski.collectively.presentation.main.MainActivity
import pl.adriankremski.collectively.presentation.main.navigation.MainNavigationFragment
import pl.adriankremski.collectively.presentation.profile.ProfileActivity
import pl.adriankremski.collectively.presentation.profile.notifications.NotificationsSettingsActivity
import pl.adriankremski.collectively.presentation.profile.remarks.user.UserRemarksActivity
import pl.adriankremski.collectively.presentation.remarkpreview.RemarkActivity
import pl.adriankremski.collectively.presentation.remarkpreview.activity.RemarkStatesActivity
import pl.adriankremski.collectively.presentation.remarkpreview.comments.RemarkCommentsActivity
import pl.adriankremski.collectively.presentation.settings.SettingsActivity
import pl.adriankremski.collectively.presentation.statistics.StatisticsActivity
import pl.adriankremski.collectively.presentation.views.RemarkCommentView
import pl.adriankremski.collectively.presentation.views.RemarkStateView
import pl.adriankremski.collectively.presentation.views.dialogs.mapfilters.MapFiltersDialog
import pl.adriankremski.collectively.presentation.views.remark.comment.RemarkCommentRowHolder
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

    //Views
    fun inject(remarkCommentRowHolder: RemarkCommentRowHolder)
    fun inject(mapFiltersDialog: MapFiltersDialog)
    fun inject(mainNavigationFragment: MainNavigationFragment)
    fun inject(view: RemarkStateView)
    fun inject(view: RemarkCommentView)
}
