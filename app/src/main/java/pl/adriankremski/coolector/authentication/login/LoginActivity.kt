package pl.adriankremski.coolector.authentication.login

import android.graphics.Typeface
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.text.Spannable
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.View
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.view_login_progress.*
import pl.adriankremski.coolector.R
import pl.adriankremski.coolector.TheApp
import pl.adriankremski.coolector.authentication.retrievepassword.ResetPasswordActivity
import pl.adriankremski.coolector.authentication.signup.SignUpActivity
import pl.adriankremski.coolector.main.MainActivity
import pl.adriankremski.coolector.repository.AuthenticationRepository
import pl.adriankremski.coolector.repository.SessionRepository
import pl.adriankremski.coolector.utils.showLoginErrorDialog
import pl.adriankremski.coolector.utils.textInString
import javax.inject.Inject

class LoginActivity : AppCompatActivity(), LoginMvp.View {
    @Inject
    lateinit var mAuthenticationRepository: AuthenticationRepository

    @Inject
    lateinit var mSessionRepository: SessionRepository

    private lateinit var mLoginPresenter: LoginPresenter

    private lateinit var mCompositeDisposable: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TheApp[this].appComponent?.inject(this)
        setContentView(R.layout.activity_login);

        var span = SpannableString(getString(R.string.app_name))
        span.setSpan(RelativeSizeSpan(1.2f), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        span.setSpan(StyleSpan(Typeface.BOLD), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        mTitleLabel.text = span;

        loginButton.setOnClickListener { login() }
        mSignupButton.setOnClickListener { signUp() }
        mRetrievePasswordButton.setOnClickListener { retrievePassword() }

        mLoginPresenter = LoginPresenter(this, mAuthenticationRepository, mSessionRepository);
        mLoginPresenter.onCreate();
        mCompositeDisposable = CompositeDisposable();

    }

    fun login() = mLoginPresenter.loginWithEmail(mEmailInput.textInString(), mPasswordInput.textInString());

    fun signUp() = SignUpActivity.launch(this)

    fun retrievePassword() = ResetPasswordActivity.start(this)

    override fun showMainScreen() = MainActivity.login(this)

    override fun closeScreen() = finish()

    override fun registerDisposable(disposable: Disposable)  { mCompositeDisposable.add(disposable) }

    override fun showLoading() { mProgressView.visibility = View.VISIBLE }

    override fun hideLoading() { mProgressView.visibility = View.GONE }

    override fun showNetworkError() = Snackbar.make(findViewById(android.R.id.content), getString(R.string.error_no_network), Snackbar.LENGTH_LONG).show();

    override fun showLoginSuccess() = showMainScreen();

    override fun showInvalidUserError() {
        showLoginErrorDialog(getString(R.string.error_invalid_user_credentials))
    }

    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable.clear();
    }
}

