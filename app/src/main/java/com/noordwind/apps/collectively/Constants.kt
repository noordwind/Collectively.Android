package com.noordwind.apps.collectively

import android.os.Environment

interface Constants {

    interface RetryTime {
        companion object {
            val LOAD_REMARKS_RETRY_MS = 10000L
        }
    }

    interface PreferencesKey {
        companion object {
            val SESSION_TOKEN = "session_token"
            val SESSION_KEY = "session"
            val REMARK_CATEGORIES_CACHE_TIME = "remark_categories_cache_time"
            val PROFILE_CACHE_TIME = "profile_cache_time"
            val GROUPS_CACHE_TIME = "groups_cache_time"
            val REMARK_CATEGORIES = "remark_categories"
            val USER_GROUPS = "user_groups"
            val PROFILE = "profile"
            val MAP_FILTERS = "map_filters"
            val REMARK_CATEGORY_FILTERS = "remark_category_filters"
            val REMARK_STATUS_FILTERS = "remark_status_filters"
            val REMARK_STATUS = "remark_status"
            val SHOW_ONLY_MINE = "show_only_mine_remark0"
            val REMARK_GROUP = "remark_group"
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
            val PICK_LOCATION = 1238
        }
    }

    interface OnceKey {
        companion object {
            val WALKTHROUGH = "walkthrough"
            val SHOW_SWIPE_LEFT_TOOLTIP_ON_MAIN_SCREEN = "SHOW_SWIPE_LEFT_TOOLTIP_ON_MAIN_SCREEN"
            val SHOW_PICK_LOCATION_HINT = "SHOW_PICK_LOCATION_HINT"
            val SHOW_TAP_TO_ZOOM_ICON = "show_tap_to_zoom_icon"
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

    interface RemarkStates {
        companion object {
            val NEW = "new"
            val RESOLVED = "resolved"
            val RENEWED = "renewed"
            val PROCESSING = "processing"
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
            const val STATES = "states"
            const val COMMENTS = "comments"
            const val REMARK_ID = "remark_id"
            const val USER = "user"
            const val LOCATION = "location"
            const val ADDRESS = "address"
            const val REMARK_PHOTO_URI = "remark_photo_uri"
        }
    }

    interface AccountStates {
        companion object {
            const val ACCOUNT_INCOMPLETE = "incomplete"
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
            const val CATEGORY_SELECT = "camera"
            const val LOCATION_SERVICE_ENABLED = "location_service_enabled"
            const val INTERNET_CONNECTION_ENABLED = "internet_connection_enabled"
            const val RESOLVE_REMARK_EVENT = "resolve_remark_event"
            const val REOPEN_REMARK_EVENT = "reopen_remark_event"
            const val REMARK_STATE_CHANGED_EVENT = "remark_changed_disposable"
        }
    }
}
