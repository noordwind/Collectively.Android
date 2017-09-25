package com.noordwind.apps.collectively.data.model

import android.text.TextUtils
import com.noordwind.apps.collectively.Constants

class Operation(val code: String?, val success: Boolean, val resource: String, val message: String, val state: String) {

    fun isFinished() : Boolean = TextUtils.equals(state, Constants.Operation.STATE_COMPLETED) || TextUtils.equals(state, Constants.Operation.STATE_REJECTED)
    fun isCompleted(): Boolean = TextUtils.equals(state, Constants.Operation.STATE_COMPLETED)

}
