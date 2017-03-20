package pl.adriankremski.collectively.presentation.authentication.signup

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.text.Spannable
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.view_login_progress.*
import pl.adriankremski.collectively.R
import pl.adriankremski.collectively.TheApp
import pl.adriankremski.collectively.domain.thread.PostExecutionThread
import pl.adriankremski.collectively.domain.thread.UseCaseThread
import pl.adriankremski.collectively.domain.interactor.SignUpUseCase
import pl.adriankremski.collectively.data.repository.AuthenticationRepository
import pl.adriankremski.collectively.data.repository.util.ConnectivityRepository
import pl.adriankremski.collectively.data.repository.SessionRepository
import pl.adriankremski.collectively.presentation.extension.setGone
import pl.adriankremski.collectively.presentation.extension.setVisible
import pl.adriankremski.collectively.presentation.extension.showLoginErrorDialog
import pl.adriankremski.collectively.presentation.main.MainActivity
import javax.inject.Inject

class SignUpActivity : AppCompatActivity(), SignUpMvp.View {

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, SignUpActivity::class.java)
            context.startActivity(intent)
        }
    }

    @Inject
    lateinit var authenticationRepository: AuthenticationRepository

    @Inject
    lateinit var sessionRepository: SessionRepository

    @Inject
    lateinit var connectivityRepository: ConnectivityRepository

    @Inject
    lateinit var ioThread: UseCaseThread

    @Inject
    lateinit var uiThread: PostExecutionThread

    lateinit var presenter: SignUpMvp.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        TheApp[this].appComponent?.inject(this);
        setContentView(R.layout.activity_signup);

        var span = SpannableString(getString(R.string.signup_screen_title));
        span.setSpan(RelativeSizeSpan(1.2f), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(StyleSpan(Typeface.BOLD), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        titleLabel.text = span

        presenter = SignUpPresenter(this,
                SignUpUseCase(authenticationRepository, sessionRepository, ioThread, uiThread),
                connectivityRepository)

        registerButton.setOnClickListener { signUp() }
    }

    fun signUp() {
        presenter.signUp(usernameInput.text.toString(), emailInput.text.toString(), passwordInput.text.toString());
    }

    override fun showLoading() {
        progressView.setVisible()
    }

    override fun showRegisterSuccess() = MainActivity.login(baseContext);

    override fun hideLoading() {
        progressView.setGone()
    }

    override fun showNetworkError() {
        Snackbar.make(findViewById(android.R.id.content), getString(R.string.error_no_network), Snackbar.LENGTH_LONG).show();
    }

    override fun showRegisterServerError(message: String?) = showLoginErrorDialog(message!!)

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }
}
