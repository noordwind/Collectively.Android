package pl.adriankremski.collectively.model

class AuthResponse(
        val token: String,
        val sessionId: String,
        val sessionKey: String
)
