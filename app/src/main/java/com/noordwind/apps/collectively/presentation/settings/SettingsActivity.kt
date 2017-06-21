package com.noordwind.apps.collectively.presentation.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import com.facebook.AccessToken
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.view_toolbar_with_title.*
import com.noordwind.apps.collectively.R
import com.noordwind.apps.collectively.TheApp
import com.noordwind.apps.collectively.data.cache.ProfileCache
import com.noordwind.apps.collectively.data.datasource.Session
import com.noordwind.apps.collectively.presentation.BaseActivity
import com.noordwind.apps.collectively.presentation.authentication.login.LoginActivity
import com.noordwind.apps.collectively.presentation.changepassword.ChangePasswordActivity
import javax.inject.Inject

class SettingsActivity : com.noordwind.apps.collectively.presentation.BaseActivity() {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TheApp[this].appComponent?.inject(this)
        setContentView(R.layout.activity_settings);

        toolbarTitleLabel?.text = getString(R.string.settings_screen_title)

        changePasswordButton.setOnClickListener { ChangePasswordActivity.start(baseContext) }

        logoutButton.setOnClickListener {
            session.clear()
            profileCache.clear()
            AccessToken.setCurrentAccessToken(null)

            val intent = Intent(baseContext, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
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
