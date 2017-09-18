package com.noordwind.apps.collectively.data.cache

import android.content.SharedPreferences
import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.data.model.RemarkCategory
import io.reactivex.Observable

class RemarkCategoriesCache(val sharedPreferences: SharedPreferences, val gson: Gson) : Cache<List<RemarkCategory>> {

    private val typeToken = object : TypeToken<List<RemarkCategory>>() {}.type

    override fun isExpired(): Boolean {
        return TextUtils.isEmpty(remarksInJson())
    }

    private fun remarksInJson() : String = sharedPreferences.getString(Constants.PreferencesKey.REMARK_CATEGORIES, "")

    override fun putData(remarkCategories: List<RemarkCategory>) {
        val remarksInJson = gson.toJson(remarkCategories, typeToken)
        sharedPreferences.edit().putString(Constants.PreferencesKey.REMARK_CATEGORIES, remarksInJson).commit()
    }

    override fun getData(): Observable<List<RemarkCategory>> {
        return Observable.just(getDataSync())
    }

    override fun getDataSync(): List<RemarkCategory> = gson.fromJson(remarksInJson(), typeToken)

    override fun clear() {
        sharedPreferences.edit().clear().commit()
    }
}

