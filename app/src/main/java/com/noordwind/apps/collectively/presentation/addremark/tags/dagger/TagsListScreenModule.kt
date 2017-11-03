package com.noordwind.apps.collectively.presentation.settings.dagger

import com.noordwind.apps.collectively.domain.interactor.remark.LoadRemarkTagsUseCase
import com.noordwind.apps.collectively.presentation.addremark.mvp.TagsListMvp
import com.noordwind.apps.collectively.presentation.addremark.mvp.TagsListPresenter
import dagger.Module
import dagger.Provides

@Module
class TagsListScreenModule(val view: TagsListMvp.View) {

    @Provides
    internal fun presenter(tagsUseCase: LoadRemarkTagsUseCase): TagsListMvp.Presenter {
        return TagsListPresenter(view, tagsUseCase)
    }
}
