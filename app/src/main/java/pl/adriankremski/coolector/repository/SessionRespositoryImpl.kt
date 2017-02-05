package pl.adriankremski.coolector.repository

import android.content.Context
import pl.adriankremski.coolector.TheApp
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
