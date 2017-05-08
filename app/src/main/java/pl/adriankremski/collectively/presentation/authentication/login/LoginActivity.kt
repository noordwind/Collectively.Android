package pl.adriankremski.collectively.presentation.authentication.login

import android.graphics.Typeface
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.text.Spannable
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.view_login_progress.*
import pl.adriankremski.collectively.R
import pl.adriankremski.collectively.TheApp
import pl.adriankremski.collectively.domain.thread.PostExecutionThread
import pl.adriankremski.collectively.domain.thread.UseCaseThread
import pl.adriankremski.collectively.domain.interactor.authentication.LoginUseCase
import pl.adriankremski.collectively.data.repository.AuthenticationRepository
import pl.adriankremski.collectively.data.repository.util.ConnectivityRepository
import pl.adriankremski.collectively.data.repository.SessionRepository
import pl.adriankremski.collectively.presentation.extension.setGone
import pl.adriankremski.collectively.presentation.extension.setVisible
import pl.adriankremski.collectively.presentation.extension.showLoginErrorDialog
import pl.adriankremski.collectively.presentation.extension.textInString
import pl.adriankremski.collectively.presentation.authentication.retrievepassword.ResetPasswordActivity
import pl.adriankremski.collectively.presentation.authentication.signup.SignUpActivity
import pl.adriankremski.collectively.presentation.main.MainActivity
import javax.inject.Inject

class LoginActivity : AppCompatActivity(), LoginMvp.View {
    @Inject
    lateinit var authenticationRepository: AuthenticationRepository

    @Inject
    lateinit var connectivityRepository: ConnectivityRepository

    @Inject
    lateinit var sessionRepository: SessionRepository

    @Inject
    lateinit var ioThread: UseCaseThread

    @Inject
    lateinit var uiThread: PostExecutionThread

    private lateinit var loginPresenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TheApp[this].appComponent?.inject(this)
        setContentView(R.layout.activity_login);

        var span = SpannableString(getString(R.string.app_name))
        span.setSpan(RelativeSizeSpan(1.2f), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        span.setSpan(StyleSpan(Typeface.BOLD), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        titleLabel.text = span;

        loginButton.setOnClickListener { login() }
        signupButton.setOnClickListener { signUp() }
        retrievePasswordButton.setOnClickListener { retrievePassword() }

        loginPresenter = LoginPresenter(this,
                LoginUseCase(authenticationRepository, sessionRepository, ioThread, uiThread),
                connectivityRepository);

        loginPresenter.onCreate();
    }

    fun login() = loginPresenter.loginWithEmail(emailInput.textInString(), passwordInput.textInString());

    fun signUp() = SignUpActivity.launch(this)

    fun retrievePassword() = ResetPasswordActivity.start(this)

    override fun showMainScreen() = MainActivity.login(this)

    override fun closeScreen() = finish()

    override fun showLoading() {
        progressView.setVisible()
    }

    override fun hideLoading() {
        progressView.setGone()
    }

    override fun showNetworkError() = Snackbar.make(findViewById(android.R.id.content), getString(R.string.error_no_network), Snackbar.LENGTH_LONG).show();

    override fun showLoginSuccess() = showMainScreen();

    override fun showInvalidUserError() {
        showLoginErrorDialog(getString(R.string.error_invalid_user_credentials))
    }

    override fun onDestroy() {
        super.onDestroy()
        loginPresenter.destroy()
    }
}

