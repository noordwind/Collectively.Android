package pl.adriankremski.coolector.repository

import android.content.SharedPreferences
import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Observable
import pl.adriankremski.coolector.Constants
import pl.adriankremski.coolector.model.Remark
import pl.adriankremski.coolector.model.RemarkCategory
import pl.adriankremski.coolector.model.RemarkTag
import pl.adriankremski.coolector.network.Api
import java.util.concurrent.TimeUnit

class RemarkRepositoryImpl(val mSharedPreferences: SharedPreferences, val mGson: Gson, val mApi: Api) : RemarksRepository {

    private val CACHE_EXPIRATION_TIME = TimeUnit.DAYS.toMillis(7)
    private val mTypeToken = object : TypeToken<List<RemarkCategory>>() {}.type

    override fun loadRemarkCategories(): Observable<List<RemarkCategory>> {
        var remarksJson = mSharedPreferences.getString(Constants.PreferencesKey.REMARK_CATEGORIES, "")
        var cacheTime = mSharedPreferences.getLong(Constants.PreferencesKey.REMARK_CATEGORIES_CACHE_TIME, 0)
        var currentTime = System.currentTimeMillis()

        if (TextUtils.isEmpty(remarksJson) || currentTime - CACHE_EXPIRATION_TIME >= cacheTime) {
            // cache is expired
            return mApi.remarkCategories().doOnNext { saveRemarksToCache(it) }
        } else {
            return Observable.just(mGson.fromJson(remarksJson, mTypeToken))
        }
    }

    fun saveRemarksToCache(remarkCategories: List<RemarkCategory>) {
        val remarksInJson = mGson.toJson(remarkCategories, mTypeToken)
        mSharedPreferences.edit().putString(Constants.PreferencesKey.REMARK_CATEGORIES, remarksInJson)
        mSharedPreferences.edit().putLong(Constants.PreferencesKey.REMARK_CATEGORIES_CACHE_TIME, System.currentTimeMillis())
    }

    override fun loadRemarks(): Observable<List<Remark>> {
        return mApi.remarks(true)
    }

    override fun loadRemarkTags(): Observable<List<RemarkTag>> {
        return mApi.remarkTags()
    }
}


