package pl.adriankremski.coolector.repository

interface SessionRepository {
    val isLoggedIn: Boolean
    fun setSessionKey(sessionKey: String)
    var sessionToken: String?
}
