package pl.adriankremski.collectively.authentication.retrievepassword

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
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_reset_password.*
import kotlinx.android.synthetic.main.view_login_progress.*
import pl.adriankremski.collectively.R
import pl.adriankremski.collectively.TheApp
import pl.adriankremski.collectively.repository.AuthenticationRepository
import javax.inject.Inject

class ResetPasswordActivity : AppCompatActivity(), ResetPasswordMvp.View {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, ResetPasswordActivity::class.java)
            context.startActivity(intent)
        }
    }

    @Inject
    lateinit var authenticationRepository: AuthenticationRepository

    private lateinit var compositeDisposable: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TheApp[this].appComponent?.inject(this)
        setContentView(R.layout.activity_reset_password)

        val span = SpannableString(getString(R.string.retrieve_password_screen_title))
        span.setSpan(RelativeSizeSpan(1.2f), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        span.setSpan(StyleSpan(Typeface.BOLD), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        titleLabel.text = span

        compositeDisposable = CompositeDisposable();

        var presenter = ResetPasswordPresenter(this, RetrievePasswordUseCase(authenticationRepository))
        mResetPasswordButton.setOnClickListener { presenter.resetPassword(emailInput.text.toString()) }
    }

    override fun registerDisposable(disposable: Disposable) { compositeDisposable.add(disposable) }

    override fun showLoading() { progressView.visibility = View.VISIBLE }

    override fun hideLoading() { progressView.visibility = View.GONE }

    override fun showNetworkError() = Snackbar.make(findViewById(android.R.id.content), getString(R.string.error_no_network), Snackbar.LENGTH_LONG).show()

    override fun showResetPasswordSuccess() = Snackbar.make(findViewById(android.R.id.content), getString(R.string.error_no_network), Snackbar.LENGTH_LONG).show()

    override fun showResetPasswordServerError(message: String?) {
        showResetPasswordServerError(message!!)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear();
    }
}