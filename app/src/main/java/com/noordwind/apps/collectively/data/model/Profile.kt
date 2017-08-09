package com.noordwind.apps.collectively.data.model

import com.noordwind.apps.collectively.Constants

class Profile(val name: String, val userId: String, val avatarUrl: String?, val state: String, val provider: String) {
    fun isAccountIncomplete() : Boolean  = state.equals(Constants.AccountStates.ACCOUNT_INCOMPLETE, true)
}

