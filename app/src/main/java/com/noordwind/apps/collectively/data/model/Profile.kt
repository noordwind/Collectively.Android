package com.noordwind.apps.collectively.data.model

import com.noordwind.apps.collectively.Constants
import java.io.Serializable

class Profile(val name: String, val userId: String, val avatarUrl: String?, val state: String, val provider: String) {
    fun isAccountIncomplete() : Boolean  = state.equals(Constants.AccountStates.ACCOUNT_INCOMPLETE, true)
}

class User(val avatarUrl: String?, val name: String, val userId: String) : Serializable

class UserGroup(val id: String, val organizationId: String, val name: String)

class NotificiationSettingsOption(val name: String, val code: String)

