package pl.adriankremski.collectively.presentation.authentication.retrievepassword

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
import kotlinx.android.synthetic.main.activity_reset_password.*
import kotlinx.android.synthetic.main.view_login_progress.*
import pl.adriankremski.collectively.R
import pl.adriankremski.collectively.TheApp
import pl.adriankremski.collectively.data.repository.AuthenticationRepository
import pl.adriankremski.collectively.data.repository.util.ConnectivityRepository
import pl.adriankremski.collectively.domain.interactor.authentication.RetrievePasswordUseCase
import pl.adriankremski.collectively.domain.thread.PostExecutionThread
import pl.adriankremski.collectively.domain.thread.UseCaseThread
import pl.adriankremski.collectively.presentation.extension.setGone
import pl.adriankremski.collectively.presentation.extension.setVisible
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

    @Inject
    lateinit var connectivityRepository: ConnectivityRepository

    @Inject
    lateinit var ioThread: UseCaseThread

    @Inject
    lateinit var uiThread: PostExecutionThread

    lateinit var presenter : ResetPasswordPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TheApp[this].appComponent?.inject(this)
        setContentView(R.layout.activity_reset_password)

        val span = SpannableString(getString(R.string.retrieve_password_screen_title))
        span.setSpan(RelativeSizeSpan(1.2f), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        span.setSpan(StyleSpan(Typeface.BOLD), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        titleLabel.text = span

        presenter = ResetPasswordPresenter(this,
                RetrievePasswordUseCase(authenticationRepository, ioThread, uiThread),
                connectivityRepository)

        mResetPasswordButton.setOnClickListener { presenter.resetPassword(emailInput.text.toString()) }
    }

    override fun showInvalidEmailError() = Snackbar.make(findViewById(android.R.id.content), getString(R.string.error_invalid_email), Snackbar.LENGTH_LONG).show();

    override fun showLoading() {
        progressView.setVisible()
    }

    override fun hideLoading() {
        progressView.setGone()
    }

    override fun showNetworkError() = Snackbar.make(findViewById(android.R.id.content), getString(R.string.error_no_network), Snackbar.LENGTH_LONG).show()

    override fun showResetPasswordSuccess() = Snackbar.make(findViewById(android.R.id.content), getString(R.string.password_reset_success), Snackbar.LENGTH_LONG).show()

    override fun showResetPasswordServerError(message: String?) {
        showResetPasswordServerError(message!!)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }
}