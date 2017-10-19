package com.noordwind.apps.collectively.presentation.settings.dagger

import com.noordwind.apps.collectively.presentation.dagger.ActivityScope
import com.noordwind.apps.collectively.presentation.remarkpreview.comments.RemarkCommentsActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = arrayOf(RemarkCommentsScreenModule::class))
interface RemarkCommentsScreenComponent {
    fun inject(activity: RemarkCommentsActivity)
}
