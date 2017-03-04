package pl.adriankremski.collectively.repository

interface SessionRepository {
    val isLoggedIn: Boolean
    fun setSessionKey(sessionKey: String)
    var sessionToken: String?
}
