package com.noordwind.apps.collectively.presentation.authentication.setnickname

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.noordwind.apps.collectively.R
import com.noordwind.apps.collectively.TheApp
import com.noordwind.apps.collectively.presentation.authentication.login.LoginActivity
import com.noordwind.apps.collectively.presentation.authentication.setnickname.mvp.SetNickNameMvp
import com.noordwind.apps.collectively.presentation.authentication.setnickname.mvp.SetNickNamePresenter
import com.noordwind.apps.collectively.presentation.extension.setGone
import com.noordwind.apps.collectively.presentation.extension.setVisible
import com.noordwind.apps.collectively.presentation.extension.showSetNicknameErrorDialog
import com.noordwind.apps.collectively.presentation.main.MainActivity
import com.noordwind.apps.collectively.presentation.settings.dagger.SetNicknameScreenModule
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
    lateinit var presenter : SetNickNamePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TheApp[this].appComponent!!.plusSetNicknameScreenComponent(SetNicknameScreenModule(this)).inject(this)
        setContentView(R.layout.activity_set_nickname)
        titleLabel.text = getString(R.string.set_nickname_screen_title)

        mLogoutButton.setOnClickListener {
            presenter.clearSession()
            val intent = Intent(baseContext, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

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