package com.noordwind.apps.collectively.data.cache

import android.content.SharedPreferences
import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.data.model.RemarkTag
import io.reactivex.Observable

class RemarkTagsCache(val sharedPreferences: SharedPreferences, val gson: Gson) : Cache<List<RemarkTag>> {

    private val typeToken = object : TypeToken<List<RemarkTag>>() {}.type

    override fun isExpired(): Boolean {
        return TextUtils.isEmpty(tagsInJson())
    }

    private fun tagsInJson() : String = sharedPreferences.getString(Constants.PreferencesKey.REMARK_TAGS, "")

    override fun putData(tags: List<RemarkTag>) {
        val tagsInJson = gson.toJson(tags, typeToken)
        sharedPreferences.edit().putString(Constants.PreferencesKey.REMARK_TAGS, tagsInJson).commit()
    }

    override fun getData(): Observable<List<RemarkTag>> {
        return Observable.just(getDataSync())
    }

    override fun getDataSync(): List<RemarkTag> = gson.fromJson(tagsInJson(), typeToken)

    override fun clear() {
        sharedPreferences.edit().clear().commit()
    }
}

