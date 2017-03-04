package pl.adriankremski.collectively.dagger

import android.app.Application
import dagger.Component
import pl.adriankremski.collectively.addremark.AddRemarkActivity
import pl.adriankremski.collectively.authentication.login.LoginActivity
import pl.adriankremski.collectively.authentication.retrievepassword.ResetPasswordActivity
import pl.adriankremski.collectively.authentication.signup.SignUpActivity
import pl.adriankremski.collectively.main.MainActivity
import pl.adriankremski.collectively.profile.ProfileActivity
import pl.adriankremski.collectively.remarkpreview.RemarkActivity
import pl.adriankremski.collectively.repository.*
import pl.adriankremski.collectively.statistics.StatisticsActivity
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

    //Repositories
    fun inject(repository: AuthenticationRepositoryImpl)
    fun inject(repository: SessionRepositoryImpl)
    fun inject(repository: OperationRepositoryImpl)
    fun inject(repository: StatisticsRepositoryImpl)
    fun inject(repository: ProfileRepositoryImpl)
}
