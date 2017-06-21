package com.noordwind.apps.collectively.presentation.changepassword

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_change_password.*
import kotlinx.android.synthetic.main.view_toolbar_with_title.*
import com.noordwind.apps.collectively.R
import com.noordwind.apps.collectively.TheApp
import com.noordwind.apps.collectively.data.repository.AuthenticationRepository
import com.noordwind.apps.collectively.domain.interactor.authentication.ChangePasswordUseCase
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import com.noordwind.apps.collectively.presentation.extension.showChangePasswordErrorDialog
import com.noordwind.apps.collectively.presentation.extension.textInString
import com.noordwind.apps.collectively.presentation.views.toast.ToastManager
import javax.inject.Inject

class ChangePasswordActivity : com.noordwind.apps.collectively.presentation.BaseActivity(), ChangePasswordMvp.View {
    companion object {
        fun start(context: Context) {
            val intent = Intent(context, ChangePasswordActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    private var toast: ToastManager? = null

    @Inject
    lateinit var authenticationRepository: AuthenticationRepository

    @Inject
    lateinit var ioThread: UseCaseThread

    @Inject
    lateinit var uiThread: PostExecutionThread

    private lateinit var presenter: ChangePasswordPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TheApp[this].appComponent?.inject(this)
        setContentView(R.layout.activity_change_password);
        toolbarTitleLabel?.text = getString(R.string.change_password_screen_title)

        presenter = ChangePasswordPresenter(this, ChangePasswordUseCase(authenticationRepository, ioThread, uiThread))

        changePasswordButton.setOnClickListener { presenter.changePassword(oldPasswordInput.textInString(), newPasswordInput.textInString()) }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true;
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun showChangePasswordLoading() {
        toast = ToastManager(this, getString(R.string.changing_password), Toast.LENGTH_LONG).progress().show()
        toolbarOptionProgress.visibility = View.VISIBLE
    }

    override fun hideChangePasswordLoading() {
        toast?.hide()
        toolbarOptionProgress.visibility = View.GONE
    }

    override fun showChangePasswordError(message: String?) {
        message.let { showChangePasswordErrorDialog(message!!) }
    }

    override fun showChangePasswordNetworkError() {
        Snackbar.make(findViewById(android.R.id.content), getString(R.string.error_no_network), Snackbar.LENGTH_LONG).show();
    }

    override fun showChangePasswordSuccess() {
        ToastManager(this, getString(R.string.password_changed), Toast.LENGTH_SHORT).success().show()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }
}
