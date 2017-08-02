package com.noordwind.apps.collectively.presentation.authentication.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.noordwind.apps.collectively.R
import com.noordwind.apps.collectively.TheApp
import com.noordwind.apps.collectively.data.repository.AuthenticationRepository
import com.noordwind.apps.collectively.data.repository.FacebookTokenRepository
import com.noordwind.apps.collectively.data.repository.ProfileRepository
import com.noordwind.apps.collectively.data.repository.SessionRepository
import com.noordwind.apps.collectively.data.repository.util.ConnectivityRepository
import com.noordwind.apps.collectively.domain.interactor.GetFacebookTokenUseCase
import com.noordwind.apps.collectively.domain.interactor.authentication.FacebookLoginUseCase
import com.noordwind.apps.collectively.domain.interactor.authentication.LoginUseCase
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import com.noordwind.apps.collectively.presentation.authentication.retrievepassword.ResetPasswordActivity
import com.noordwind.apps.collectively.presentation.authentication.setnickname.SetNickNameActivity
import com.noordwind.apps.collectively.presentation.authentication.signup.SignUpActivity
import com.noordwind.apps.collectively.presentation.extension.setGone
import com.noordwind.apps.collectively.presentation.extension.setVisible
import com.noordwind.apps.collectively.presentation.extension.showLoginErrorDialog
import com.noordwind.apps.collectively.presentation.extension.textInString
import com.noordwind.apps.collectively.presentation.main.MainActivity
import com.noordwind.apps.collectively.presentation.walkthrough.WalkthroughActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.view_login_progress.*
import java.lang.ref.WeakReference
import java.util.*
import javax.inject.Inject

class LoginActivity : AppCompatActivity(), LoginMvp.View {

    companion object {
        fun login(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            context.startActivity(intent)
        }
    }

    @Inject
    lateinit var authenticationRepository: AuthenticationRepository

    @Inject
    lateinit var connectivityRepository: ConnectivityRepository

    @Inject
    lateinit var sessionRepository: SessionRepository

    @Inject
    lateinit var facebookRepository: FacebookTokenRepository

    @Inject
    lateinit var profileRepository: ProfileRepository

    @Inject
    lateinit var ioThread: UseCaseThread

    @Inject
    lateinit var uiThread: PostExecutionThread

    private lateinit var loginPresenter: LoginPresenter

    private lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TheApp[this].appComponent?.inject(this)
        setContentView(R.layout.activity_login);
        titleLabel.text = getString(R.string.app_name)

        loginButton.setOnClickListener { login() }
        signupButton.setOnClickListener { signUp() }
        retrievePasswordButton.setOnClickListener { retrievePassword() }

        callbackManager = CallbackManager.Factory.create()

        LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    private val activityWeakReference = WeakReference<LoginActivity>(this@LoginActivity)

                    override fun onError(error: FacebookException?) {}

                    override fun onCancel() {}

                    override fun onSuccess(result: LoginResult?) {
                        val activity = activityWeakReference.get()
                        if (activity != null && result != null) {
                            loginPresenter.facebookLogin(result.accessToken.token)
                        }
                    }
                })

        loginPresenter = LoginPresenter(this,
                LoginUseCase(authenticationRepository, sessionRepository, ioThread, uiThread),
                FacebookLoginUseCase(authenticationRepository, ioThread, uiThread),
                GetFacebookTokenUseCase(facebookRepository, ioThread, uiThread),
                profileRepository,
                connectivityRepository);

        loginPresenter.onCreate();

        facebookLoginButton.setOnClickListener {
            loginPresenter.facebookLoginClicked()
        }
    }

    override fun loginWithFacebookNoToken() {
        facebookLoginHidden.setReadPermissions(Arrays.asList("email"))
        facebookLoginHidden.callOnClick()
    }

    override fun loginWithFacebookToken(token: String) {
        loginPresenter.facebookLogin(token);
    }

    override fun showLoginError() {
        Snackbar.make(findViewById(android.R.id.content), getString(R.string.error_login_general), Snackbar.LENGTH_LONG).show()
    }

    fun login() = loginPresenter.loginWithEmail(emailInput.textInString(), passwordInput.textInString());

    fun signUp() = SignUpActivity.launch(this)

    fun retrievePassword() = ResetPasswordActivity.start(this)

    override fun showWalkthroughScreen() = WalkthroughActivity.start(this)

    override fun showMainScreen() = MainActivity.login(this)

    override fun closeScreen() = finish()

    override fun showLoading() {
        progressView.setVisible()
    }

    override fun hideLoading() {
        progressView.setGone()
    }

    override fun showNetworkError() = Snackbar.make(findViewById(android.R.id.content), getString(R.string.error_no_network), Snackbar.LENGTH_LONG).show();

    override fun showInvalidEmailError() = Snackbar.make(findViewById(android.R.id.content), getString(R.string.error_invalid_email), Snackbar.LENGTH_LONG).show();

    override fun showInvalidPasswordError() = Snackbar.make(findViewById(android.R.id.content), getString(R.string.error_invalid_password), Snackbar.LENGTH_LONG).show();

    override fun showSetNicknameScreen() {
        SetNickNameActivity.start(this)
    }

    override fun showLoginSuccess() = showMainScreen();

    override fun showInvalidUserError() {
        showLoginErrorDialog(getString(R.string.error_invalid_user_credentials))
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        super.onDestroy()
        loginPresenter.destroy()
    }
}

