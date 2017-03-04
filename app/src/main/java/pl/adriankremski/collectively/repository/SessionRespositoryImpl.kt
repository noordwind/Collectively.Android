package pl.adriankremski.collectively.repository

import android.content.Context
import pl.adriankremski.collectively.TheApp
import javax.inject.Inject

class SessionRepositoryImpl(context: Context) : SessionRepository {

    @Inject
    lateinit var session: Session

    init {
        TheApp[context].appComponent?.inject(this)
    }

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
