package com.noordwind.apps.collectively.presentation.authentication.setnickname

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.facebook.AccessToken
import com.noordwind.apps.collectively.R
import com.noordwind.apps.collectively.TheApp
import com.noordwind.apps.collectively.data.cache.ProfileCache
import com.noordwind.apps.collectively.data.datasource.Session
import com.noordwind.apps.collectively.data.repository.AuthenticationRepository
import com.noordwind.apps.collectively.data.repository.util.ConnectivityRepository
import com.noordwind.apps.collectively.domain.interactor.authentication.SetNickNameUseCase
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import com.noordwind.apps.collectively.presentation.authentication.login.LoginActivity
import com.noordwind.apps.collectively.presentation.extension.setGone
import com.noordwind.apps.collectively.presentation.extension.setVisible
import com.noordwind.apps.collectively.presentation.extension.showSetNicknameErrorDialog
import com.noordwind.apps.collectively.presentation.main.MainActivity
import kotlinx.android.synthetic.main.activity_set_nickname.*
import kotlinx.android.synthetic.main.view_login_progress.*
import javax.inject.Inject

class SetNickNameActivity : AppCompatActivity(), SetNickNameMvp.View {
    companion object {
        fun start(context: Context) {
            val intent = Intent(context, SetNickNameActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
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

    @Inject
    lateinit var session: Session

    @Inject
    lateinit var profileCache: ProfileCache

    lateinit var presenter : SetNickNamePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TheApp[this].appComponent?.inject(this)
        setContentView(R.layout.activity_set_nickname)
        titleLabel.text = getString(R.string.set_nickname_screen_title)

        mLogoutButton.setOnClickListener {
            session.clear()
            profileCache.clear()
            AccessToken.setCurrentAccessToken(null)

            val intent = Intent(baseContext, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        presenter = SetNickNamePresenter(this,
                SetNickNameUseCase(authenticationRepository, ioThread, uiThread),
                connectivityRepository)

        mSetNickName.setOnClickListener { presenter.setNickName(nickNameInput.text.toString().trim()) }
    }

    override fun showLoading() {
        progressView.setVisible()
    }

    override fun hideLoading() {
        progressView.setGone()
    }

    override fun showNetworkError() = Snackbar.make(findViewById(android.R.id.content), getString(R.string.error_no_network), Snackbar.LENGTH_LONG).show()


    override fun showSetNickNameSuccess() {
        MainActivity.login(this)
    }

    override fun showSetNickNameError(message: String?) {
        showSetNicknameErrorDialog(message!!)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }
}