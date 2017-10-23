package com.noordwind.apps.collectively.data.repository

import com.noordwind.apps.collectively.data.datasource.Session
import com.noordwind.apps.collectively.domain.repository.SessionRepository

class SessionRepositoryImpl(val session: Session) : SessionRepository {

    override val isLoggedIn: Boolean
        get() = session.isLoggedIn

    override fun setSessionKey(sessionKey: String) {
        session!!.setSessionKey(sessionKey)
    }

    override var sessionToken: String?
        get() = session.sessionToken
        set(token) {
            session.setSessionToken(token)
        }
}
