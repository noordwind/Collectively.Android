package pl.adriankremski.coolector

interface Constants {
    interface PreferencesKey {
        companion object {
            val SESSION_TOKEN = "session_token"
            val SESSION_KEY = "session"
        }
    }

    interface AuthProvider {
        companion object {
            val FACEBOOK = "facebook"
            val COOLECTOR = "coolector"
        }
    }
}
