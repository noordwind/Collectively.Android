package pl.adriankremski.collectively.data.repository

import pl.adriankremski.collectively.data.datasource.Session

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
