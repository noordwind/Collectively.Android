package pl.adriankremski.coolector.dagger

import android.app.Application
import dagger.Component
import pl.adriankremski.coolector.addremark.AddRemarkActivity
import pl.adriankremski.coolector.authentication.login.LoginActivity
import pl.adriankremski.coolector.authentication.retrievepassword.ResetPasswordActivity
import pl.adriankremski.coolector.authentication.signup.SignUpActivity
import pl.adriankremski.coolector.main.MainActivity
import pl.adriankremski.coolector.repository.AuthenticationRepositoryImpl
import pl.adriankremski.coolector.repository.SessionRepositoryImpl
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

    //Repositories
    fun inject(repository: AuthenticationRepositoryImpl)
    fun inject(repository: SessionRepositoryImpl)
}
