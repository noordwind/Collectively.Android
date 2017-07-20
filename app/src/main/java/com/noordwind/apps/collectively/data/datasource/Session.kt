package com.noordwind.apps.collectively.data.datasource

import android.content.Context
import net.grandcentrix.tray.TrayPreferences
import com.noordwind.apps.collectively.Constants

class Session(context: Context) : TrayPreferences(context, "SESSION", 1), Constants {

    val isLoggedIn: Boolean
        get() = getString(Constants.PreferencesKey.SESSION_TOKEN, null) != null

    val sessionToken: String?
        get() = getString(Constants.PreferencesKey.SESSION_TOKEN, null)

    fun setSessionToken(token: String?): Session {
        put(Constants.PreferencesKey.SESSION_TOKEN, token)
        return this
    }

    val sessionKey: String?
        get() = getString(Constants.PreferencesKey.SESSION_KEY, null)

    fun setSessionKey(key: String): Session {
        put(Constants.PreferencesKey.SESSION_KEY, key)
        return this
    }

    override fun onCreate(i: Int) {

    }

    override fun onUpgrade(i: Int, i1: Int) {

    }
}
