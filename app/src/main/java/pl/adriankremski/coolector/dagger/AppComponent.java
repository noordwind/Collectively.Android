package pl.adriankremski.coolector.dagger;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Component;
import pl.adriankremski.coolector.authentication.login.LoginActivity;
import pl.adriankremski.coolector.authentication.retrievepassword.RetrievePasswordActivity;
import pl.adriankremski.coolector.authentication.signup.SignUpActivity;
import pl.adriankremski.coolector.main.MainActivity;
import pl.adriankremski.coolector.repository.AuthenticationRepositoryImpl;
import pl.adriankremski.coolector.repository.SessionRepositoryImpl;

@Singleton
@Component(
        modules = {
                AppModule.class
        }
)
public interface AppComponent {
    Application application();

    //Activites
    void inject(LoginActivity activity);
    void inject(RetrievePasswordActivity activity);
    void inject(SignUpActivity activity);
    void inject(MainActivity mainActivity);

    //Repositories
    void inject(AuthenticationRepositoryImpl repository);
    void inject(SessionRepositoryImpl repository);
}

