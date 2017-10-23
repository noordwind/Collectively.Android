package com.noordwind.apps.collectively.domain.repository

interface SessionRepository {
    val isLoggedIn: Boolean
    fun setSessionKey(sessionKey: String)
    var sessionToken: String?
}
