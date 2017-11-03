package com.noordwind.apps.collectively.presentation.settings.dagger

import com.noordwind.apps.collectively.presentation.addremark.tags.TagsListActivity
import com.noordwind.apps.collectively.presentation.dagger.ActivityScope
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = arrayOf(TagsListScreenModule::class))
interface TagsListScreenComponent {
    fun inject(activity: TagsListActivity)
}
