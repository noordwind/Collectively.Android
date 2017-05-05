package pl.adriankremski.collectively.presentation.dagger

import android.app.Application
import dagger.Component
import pl.adriankremski.collectively.presentation.addremark.AddRemarkActivity
import pl.adriankremski.collectively.presentation.authentication.login.LoginActivity
import pl.adriankremski.collectively.presentation.authentication.retrievepassword.ResetPasswordActivity
import pl.adriankremski.collectively.presentation.authentication.signup.SignUpActivity
import pl.adriankremski.collectively.presentation.main.MainActivity
import pl.adriankremski.collectively.presentation.profile.ProfileActivity
import pl.adriankremski.collectively.presentation.remarkpreview.RemarkActivity
import pl.adriankremski.collectively.presentation.remarkpreview.comments.RemarkCommentsActivity
import pl.adriankremski.collectively.presentation.settings.SettingsActivity
import pl.adriankremski.collectively.presentation.statistics.StatisticsActivity
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
}
