package pl.adriankremski.collectively

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
            val COOLECTOR = "collectively"
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

    interface Headers {
        companion object {
            val X_OPERATION = "X-Operation"
        }
    }

    interface Operation {
        companion object {
            val STATE_COMPLETED = "completed"
            val STATE_REJECTED = "rejected"
        }
    }

    interface ApiHeader {
        companion object {
            const val ACCEPT_HEADER = "Accept: application/json"
            const val CONTENT_TYPE_HEADER = "Content-type: application/json"
        }
    }
}