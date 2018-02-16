package pl.adriankremski.collectively

import android.os.Environment

interface Constants {
    interface PreferencesKey {
        companion object {
            val SESSION_TOKEN = "session_token"
            val SESSION_KEY = "session"
            val REMARK_CATEGORIES_CACHE_TIME = "remark_categories_cache_time"
            val PROFILE_CACHE_TIME = "profile_cache_time"
            val REMARK_CATEGORIES = "remark_categories"
            val PROFILE = "profile"
            val FILTERS = "filters"
            val REMARK_STATUS = "remark_status"
            val SHOW_ONLY_MINE = "show_only_mine_remark0"
        }
    }

    interface ExternalDirs {
        companion object {
            val APP_DIR_NAME = "Fortitudo"
            val PHOTOS_DIR_NAME = "Images"
            val TEMP_DIR_NAME = "Temp"

            val APP_DIR_PATH = String.format("%s/%s", Environment.getExternalStorageDirectory(), APP_DIR_NAME)
            val APP_PHOTOS_PATH = String.format("%s/%s", APP_DIR_PATH, PHOTOS_DIR_NAME)
            val APP_TEMP_PHOTOS_PATH = String.format("%s/%s", APP_PHOTOS_PATH, TEMP_DIR_NAME)
        }
    }

    interface RequestCodes {
        companion object {
            val PICK_PICTURE_FROM_GALLERY = 1236
            val TAKE_PICTURE = 1237
        }
    }

    interface OnceKey {
        companion object {
            val WALKTHROUGH = "walkthrough"
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
            val DEFECT = "defect"
            val ISSUE = "issue"
            val SUGGESTION = "suggestion"
            val PRAISE = "praise"
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

    interface BundleKey {
        companion object {
            const val CATEGORY = "category"
            const val ID = "id"
            const val USER_ID = "user_id"
            const val STATISTICS = "statistics"
            const val MODE = "mode"
        }
    }

    interface UseCaseKeys {
        companion object {
            const val REMARK_ID = "remark_id"
            const val COMMENT_ID = "comment_id"
            const val VOTE = "vote"
        }
    }

    interface RxBusEvent {
        companion object {
            const val GALLERY_EVENT = "gallery"
            const val CAMERA_EVENT = "camera"
        }
    }
}
