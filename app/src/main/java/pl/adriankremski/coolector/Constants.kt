package pl.adriankremski.coolector

interface Constants {
    interface PreferencesKey {
        companion object {
            val SESSION_TOKEN = "session_token"
            val SESSION_KEY = "session"
            val REMARK_CATEGORIES_CACHE_TIME = "remark_categories_cache_time"
            val REMARK_CATEGORIES= "remark_categories"
        }
    }

    interface AuthProvider {
        companion object {
            val FACEBOOK = "facebook"
            val COOLECTOR = "coolector"
        }
    }

    interface RemarkCategories {
        companion object {
            val LITTTER = "litter"
            val DAMAGE = "damage"
            val ACCIDENT = "accident"
        }
    }

    interface RecyclerItemType {
        companion object {
            val REMARK = 1
        }
    }
}
