package com.noordwind.apps.collectively.data.cache

import android.content.SharedPreferences
import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.data.model.RemarkCategory
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class RemarkCategoriesCache(val sharedPreferences: SharedPreferences, val gson: Gson) : Cache<List<RemarkCategory>> {

    private val CACHE_EXPIRATION_TIME = TimeUnit.DAYS.toMillis(7)
    private val typeToken = object : TypeToken<List<RemarkCategory>>() {}.type

    override fun isExpired(): Boolean {
        var cacheTime = sharedPreferences.getLong(Constants.PreferencesKey.REMARK_CATEGORIES_CACHE_TIME, 0)
        var currentTime = System.currentTimeMillis()

        return TextUtils.isEmpty(remarksInJson()) || currentTime - CACHE_EXPIRATION_TIME >= cacheTime
    }

    private fun remarksInJson() : String = sharedPreferences.getString(Constants.PreferencesKey.REMARK_CATEGORIES, "")

    override fun putData(remarkCategories: List<RemarkCategory>) {
        val remarksInJson = gson.toJson(remarkCategories, typeToken)
        sharedPreferences.edit().putString(Constants.PreferencesKey.REMARK_CATEGORIES, remarksInJson).commit()
        sharedPreferences.edit().putLong(Constants.PreferencesKey.REMARK_CATEGORIES_CACHE_TIME, System.currentTimeMillis()).commit()
    }

    override fun getData(): Observable<List<RemarkCategory>> {
        return Observable.just(getDataSync())
    }

    override fun getDataSync(): List<RemarkCategory> = gson.fromJson(remarksInJson(), typeToken)

    override fun clear() {
        sharedPreferences.edit().clear().commit()
    }
}

