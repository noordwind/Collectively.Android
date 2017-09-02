package com.noordwind.apps.collectively.presentation.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.noordwind.apps.collectively.R
import com.noordwind.apps.collectively.TheApp
import com.noordwind.apps.collectively.data.cache.ProfileCache
import com.noordwind.apps.collectively.data.datasource.Session
import com.noordwind.apps.collectively.data.repository.AuthenticationRepository
import com.noordwind.apps.collectively.domain.interactor.authentication.DeleteAccountUseCase
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import com.noordwind.apps.collectively.presentation.authentication.login.LoginActivity
import com.noordwind.apps.collectively.presentation.changepassword.ChangePasswordActivity
import com.noordwind.apps.collectively.presentation.extension.showOperationFailedDialog
import com.noordwind.apps.collectively.presentation.views.toast.ToastManager
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.view_toolbar_with_title.*
import javax.inject.Inject

class SettingsActivity : com.noordwind.apps.collectively.presentation.BaseActivity(), SettingsMvp.View {
    companion object {
        fun start(context: Context) {
            val intent = Intent(context, SettingsActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    @Inject
    lateinit var session: Session

    @Inject
    lateinit var profileCache: ProfileCache

    @Inject
    lateinit var authenticationRepository: AuthenticationRepository

    @Inject
    lateinit var ioThread: UseCaseThread

    @Inject
    lateinit var uiThread: PostExecutionThread

    private lateinit var presenter: SettingsMvp.Presenter

    private lateinit var toast: ToastManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TheApp[this].appComponent?.inject(this)
        setContentView(R.layout.activity_settings);

        toolbarTitleLabel?.text = getString(R.string.settings_screen_title)

        changePasswordButton.setOnClickListener { ChangePasswordActivity.start(baseContext) }

        presenter = SettingsPresenter(this, session, profileCache, DeleteAccountUseCase(authenticationRepository, ioThread, uiThread))

        logoutButton.setOnClickListener { presenter.logout() }

        deleteAccountButton.setOnClickListener { presenter.removeAccount() }
        deleteAccountButton.visibility = View.GONE

        presenter.onCreate()
    }

    override fun hideChangePasswordButton() {
        changePasswordButton.visibility = View.GONE
    }

    override fun showLogoutSuccess() {
        val intent = Intent(baseContext, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun showRemoveAccountProgress() {
        toast = ToastManager(this, getString(R.string.removing_account), Toast.LENGTH_LONG).progress().show()
        toolbarOptionProgress.visibility = View.VISIBLE
    }

    override fun hideRemoveAccountProgress() {
        toolbarOptionProgress.visibility = View.GONE
        toast.hide()
    }

    override fun showRemoveAccountSuccess() {
        ToastManager(this, getString(R.string.account_removed_message), Toast.LENGTH_SHORT).success().show()
    }

    override fun showRemoveAccountError(message: String?) {
        showOperationFailedDialog(message!!)
    }

    override fun showRemoveAccountNetworkError() {
        ToastManager(this, getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).networkError().show()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true;
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
