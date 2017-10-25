package com.noordwind.apps.collectively.presentation.dagger

import android.app.Activity
import android.content.Context
import com.google.gson.Gson
import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.data.cache.ProfileCache
import com.noordwind.apps.collectively.data.cache.RemarkCategoriesCache
import com.noordwind.apps.collectively.data.cache.UserGroupsCache
import com.noordwind.apps.collectively.data.datasource.Session
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataCacheModule : Constants {

    @Provides
    @Singleton
    fun userGroupsCache(context: Context): UserGroupsCache = UserGroupsCache(context.getSharedPreferences("shared_preferences_user_groups", Activity.MODE_PRIVATE), Gson())

    @Provides
    @Singleton
    fun remarkCategoriesCache(context: Context): RemarkCategoriesCache = RemarkCategoriesCache(context.getSharedPreferences("shared_preferences_remark_categories", Activity.MODE_PRIVATE), Gson())

    @Provides
    @Singleton
    fun session(context: Context): Session = Session(context)

    @Provides
    @Singleton
    fun profileCache(context: Context): ProfileCache = ProfileCache(context.getSharedPreferences("shared_preferences_profile", Activity.MODE_PRIVATE), Gson())

}
