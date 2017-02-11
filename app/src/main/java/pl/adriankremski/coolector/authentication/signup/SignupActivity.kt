package pl.adriankremski.coolector.authentication.signup

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
import android.view.View
import android.widget.EditText
import android.widget.TextView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import pl.adriankremski.coolector.R
import pl.adriankremski.coolector.TheApp
import pl.adriankremski.coolector.main.MainActivity
import pl.adriankremski.coolector.repository.AuthenticationRepository
import pl.adriankremski.coolector.repository.SessionRepository
import javax.inject.Inject


class SignUpActivity : AppCompatActivity(), SignUpMvp.View {

    lateinit var mTitleLabel: TextView
    lateinit var mUsernameLabel: EditText
    lateinit var mEmailLabel: EditText
    lateinit var mPasswordLabel: EditText
    lateinit var mProgressView: View

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, SignUpActivity::class.java)
            context.startActivity(intent)
        }
    }

    @Inject
    lateinit var mAuthenticationRepository: AuthenticationRepository

    @Inject
    lateinit var mSessionRepository: SessionRepository

    lateinit var mSignupPresenter: SignUpMvp.Presenter
    lateinit var mCompositeDisposable: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        TheApp[this].appComponent?.inject(this);
        setContentView(R.layout.activity_signup);

        mTitleLabel = findViewById(R.id.title) as TextView
        mUsernameLabel = findViewById(R.id.username) as EditText
        mEmailLabel = findViewById(R.id.email) as EditText
        mPasswordLabel = findViewById(R.id.password) as EditText
        mProgressView = findViewById(R.id.progress);

        var span = SpannableString(getString(R.string.signup_screen_title));
        span.setSpan(RelativeSizeSpan(1.2f), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(StyleSpan(Typeface.BOLD), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTitleLabel.text = span

        mSignupPresenter = SignUpPresenter(this, mAuthenticationRepository, mSessionRepository);
        mCompositeDisposable = CompositeDisposable();

        findViewById(R.id.register).setOnClickListener { signUp() }
    }

    fun signUp() {
        mSignupPresenter.signUp(mUsernameLabel.text.toString(), mEmailLabel.text.toString(), mPasswordLabel.text.toString());
    }

    override fun showLoading() {
        mProgressView.visibility = View.VISIBLE
    }

    override fun registerDisposable(disposable: Disposable) { mCompositeDisposable.add(disposable) }

    override fun showRegisterSuccess() = MainActivity.login(getBaseContext());

    override fun hideLoading() {
        mProgressView.visibility = View.GONE;
    }

    override fun showNetworkError() {
        Snackbar.make(findViewById(android.R.id.content), getString(R.string.error_no_network), Snackbar.LENGTH_LONG).show();
    }

    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable.clear();
    }
}

