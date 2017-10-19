package com.noordwind.apps.collectively.presentation.authentication.signup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.noordwind.apps.collectively.R
import com.noordwind.apps.collectively.TheApp
import com.noordwind.apps.collectively.presentation.authentication.signup.mvp.SignUpMvp
import com.noordwind.apps.collectively.presentation.extension.setGone
import com.noordwind.apps.collectively.presentation.extension.setVisible
import com.noordwind.apps.collectively.presentation.extension.showLoginErrorDialog
import com.noordwind.apps.collectively.presentation.main.MainActivity
import com.noordwind.apps.collectively.presentation.settings.dagger.SignUpScreenModule
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.view_login_progress.*
import javax.inject.Inject

class SignUpActivity : AppCompatActivity(), SignUpMvp.View {

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, SignUpActivity::class.java)
            context.startActivity(intent)
        }
    }

    @Inject
    lateinit var presenter: SignUpMvp.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TheApp[this].appComponent!!.plusSignUpScreenComponent(SignUpScreenModule(this)).inject(this)
        setContentView(R.layout.activity_signup);

        titleLabel.text = getString(R.string.signup_screen_title)
        registerButton.setOnClickListener { signUp() }
    }

    fun signUp() {
        presenter.signUp(usernameInput.text.toString().trim(),
                emailInput.text.toString().trim(), passwordInput.text.toString().trim());
    }

    override fun showInvalidNameError() = Snackbar.make(findViewById(android.R.id.content), getString(R.string.error_invalid_username), Snackbar.LENGTH_LONG).show();

    override fun showInvalidEmailError() = Snackbar.make(findViewById(android.R.id.content), getString(R.string.error_invalid_email), Snackbar.LENGTH_LONG).show();

    override fun showInvalidPasswordError() = Snackbar.make(findViewById(android.R.id.content), getString(R.string.error_invalid_password), Snackbar.LENGTH_LONG).show();


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

