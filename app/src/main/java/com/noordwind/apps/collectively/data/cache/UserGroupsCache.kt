package com.noordwind.apps.collectively.data.cache

import android.content.SharedPreferences
import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.data.model.UserGroup
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class UserGroupsCache(val sharedPreferences: SharedPreferences, val gson: Gson) : Cache<List<UserGroup>> {
    private val CACHE_EXPIRATION_TIME = TimeUnit.DAYS.toMillis(7)
    private val typeToken = object : TypeToken<List<UserGroup>>() {}.type

    override fun isExpired(): Boolean {
        var cacheTime = sharedPreferences.getLong(Constants.PreferencesKey.GROUPS_CACHE_TIME, 0)
        var currentTime = System.currentTimeMillis()

        return TextUtils.isEmpty(groupsInJson()) || currentTime - CACHE_EXPIRATION_TIME >= cacheTime
    }

    private fun groupsInJson() : String = sharedPreferences.getString(Constants.PreferencesKey.USER_GROUPS, "")

    override fun putData(groups: List<UserGroup>) {
        val json = gson.toJson(groups, typeToken)
        sharedPreferences.edit().putString(Constants.PreferencesKey.USER_GROUPS, json).commit()
        sharedPreferences.edit().putLong(Constants.PreferencesKey.GROUPS_CACHE_TIME, System.currentTimeMillis()).commit()
    }

    override fun getData(): Observable<List<UserGroup>> {
        return Observable.just(gson.fromJson(groupsInJson(), typeToken))
    }

    override fun clear() {
        sharedPreferences.edit().clear().commit()
    }
}

