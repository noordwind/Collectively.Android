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
import android.widget.EditText
import android.widget.TextView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import pl.adriankremski.coolector.R
import pl.adriankremski.coolector.TheApp
import pl.adriankremski.coolector.authentication.retrievepassword.ResetPasswordActivity
import pl.adriankremski.coolector.authentication.signup.SignUpActivity
import pl.adriankremski.coolector.main.MainActivity
import pl.adriankremski.coolector.repository.AuthenticationRepository
import pl.adriankremski.coolector.repository.SessionRepository
import javax.inject.Inject


class LoginActivity : AppCompatActivity(), LoginMvp.View {

    internal var mTitleLabel: TextView? = null
    internal var mEmailLabel: EditText? = null
    internal var mPasswordLabel: EditText? = null
    internal var mProgressView: View? = null

    @Inject
    lateinit var mAuthenticationRepository: AuthenticationRepository

    @Inject
    lateinit var mSessionRepository: SessionRepository

    private var mLoginPresenter: LoginPresenter? = null

    private var mCompositeDisposable: CompositeDisposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        TheApp[this].appComponent?.inject(this)
        setContentView(R.layout.activity_login);

        var span = SpannableString(getString(R.string.app_name))

        span.setSpan(RelativeSizeSpan(1.2f), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        span.setSpan(StyleSpan(Typeface.BOLD), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        mEmailLabel = findViewById(R.id.email) as EditText
        mPasswordLabel = findViewById(R.id.password) as EditText
        mProgressView = findViewById(R.id.progress)
        mTitleLabel = findViewById(R.id.title) as TextView

        mTitleLabel?.text = span;

        mLoginPresenter = LoginPresenter(this, mAuthenticationRepository, mSessionRepository);
        mLoginPresenter?.onCreate();
        mCompositeDisposable = CompositeDisposable();

        findViewById(R.id.login).setOnClickListener { login() }
        findViewById(R.id.signup).setOnClickListener { signUp() }
        findViewById(R.id.retrive_password).setOnClickListener { retrievePassword() }
    }

    fun login() = mLoginPresenter?.loginWithEmail(mEmailLabel?.text.toString(), mPasswordLabel?.text.toString());

    fun signUp() = SignUpActivity.launch(this)

    fun retrievePassword() = ResetPasswordActivity.start(this)

    override fun showMainScreen() = MainActivity.login(this)

    override fun closeScreen() = finish()

    override fun registerDisposable(disposable: Disposable)  { mCompositeDisposable?.add(disposable) }

    override fun showLoading() { mProgressView?.visibility = View.VISIBLE }

    override fun hideLoading() { mProgressView?.visibility = View.GONE }

    override fun showNetworkError() = Snackbar.make(findViewById(android.R.id.content), getString(R.string.error_no_network), Snackbar.LENGTH_LONG).show();

    override fun showLoginSuccess() = showMainScreen();

    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable?.clear();
    }
}

