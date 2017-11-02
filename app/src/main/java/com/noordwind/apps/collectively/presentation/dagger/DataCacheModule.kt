package com.noordwind.apps.collectively.presentation.dagger

import android.app.Activity
import android.content.Context
import com.google.gson.Gson
import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.data.cache.ProfileCache
import com.noordwind.apps.collectively.data.cache.RemarkCategoriesCache
import com.noordwind.apps.collectively.data.cache.RemarkTagsCache
import com.noordwind.apps.collectively.data.cache.UserGroupsCache
import com.noordwind.apps.collectively.data.datasource.Session
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataCacheModule : Constants {

    @Provides
    @Singleton
    fun userGroupsCache(context: Context): UserGroupsCache =
            UserGroupsCache(context.getSharedPreferences(Constants.PreferencesType.USER_GROUPS, Activity.MODE_PRIVATE), Gson())

    @Provides
    @Singleton
    fun remarkCategoriesCache(context: Context): RemarkCategoriesCache =
            RemarkCategoriesCache(context.getSharedPreferences(Constants.PreferencesType.REMARK_CATEGORIES, Activity.MODE_PRIVATE), Gson())

    @Provides
    @Singleton
    fun remarkTagsCache(context: Context): RemarkTagsCache =
            RemarkTagsCache(context.getSharedPreferences(Constants.PreferencesType.REMARK_TAGS, Activity.MODE_PRIVATE), Gson())

    @Provides
    @Singleton
    fun session(context: Context): Session = Session(context)

    @Provides
    @Singleton
    fun profileCache(context: Context): ProfileCache =
            ProfileCache(context.getSharedPreferences(Constants.PreferencesType.USER_PROFILE, Activity.MODE_PRIVATE), Gson())

}
