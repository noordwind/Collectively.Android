package com.noordwind.apps.collectively.data.repository

interface SessionRepository {
    val isLoggedIn: Boolean
    fun setSessionKey(sessionKey: String)
    var sessionToken: String?
}
