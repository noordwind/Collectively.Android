package pl.adriankremski.collectively.repository

import android.content.SharedPreferences
import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Observable
import pl.adriankremski.collectively.Constants
import pl.adriankremski.collectively.model.*
import pl.adriankremski.collectively.network.Api
import java.util.concurrent.TimeUnit

class RemarkRepositoryImpl(val sharedPreferences: SharedPreferences, val gson: Gson, val api: Api) : RemarksRepository {

    private val CACHE_EXPIRATION_TIME = TimeUnit.DAYS.toMillis(7)
    private val typeToken = object : TypeToken<List<RemarkCategory>>() {}.type

    override fun loadRemarkCategories(): Observable<List<RemarkCategory>> {
        var remarksJson = sharedPreferences.getString(Constants.PreferencesKey.REMARK_CATEGORIES, "")
        var cacheTime = sharedPreferences.getLong(Constants.PreferencesKey.REMARK_CATEGORIES_CACHE_TIME, 0)
        var currentTime = System.currentTimeMillis()

        if (TextUtils.isEmpty(remarksJson) || currentTime - CACHE_EXPIRATION_TIME >= cacheTime) {
            // cache is expired
            return api.remarkCategories().doOnNext { saveRemarksToCache(it) }
        } else {
            return Observable.just(gson.fromJson(remarksJson, typeToken))
        }
    }

    fun saveRemarksToCache(remarkCategories: List<RemarkCategory>) {
        val remarksInJson = gson.toJson(remarkCategories, typeToken)
        sharedPreferences.edit().putString(Constants.PreferencesKey.REMARK_CATEGORIES, remarksInJson)
        sharedPreferences.edit().putLong(Constants.PreferencesKey.REMARK_CATEGORIES_CACHE_TIME, System.currentTimeMillis())
    }

    override fun loadRemarks(): Observable<List<Remark>> {
        return api.remarks(true, "createdat", "descending", 1000)
    }

    override fun loadRemarkTags(): Observable<List<RemarkTag>> {
        return api.remarkTags()
    }

    override fun saveRemark(remark: NewRemark): Observable<RemarkNotFromList> {
        return api.saveRemark(remark).flatMap {
            var operationPath = it.headers().get(Constants.Headers.X_OPERATION)

            api.operation(operationPath)
                    .repeatWhen { it.delay(500, TimeUnit.MILLISECONDS) }
                    .takeUntil<Operation> { it.state.equals(Constants.Operation.STATE_COMPLETED) }
                    .filter { !it.state.equals(Constants.Operation.STATE_COMPLETED) }
                    .flatMap { api.createdRemark(it.resource) }
        }
    }

    override fun loadRemark(id: String): Observable<Remark> {
        return Observable.just(Remark("", null, "", "", false, 10))
    }
}


