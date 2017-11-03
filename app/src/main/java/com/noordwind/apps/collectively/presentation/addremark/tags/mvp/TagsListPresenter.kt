package com.noordwind.apps.collectively.presentation.addremark.mvp

import com.noordwind.apps.collectively.data.model.RemarkTag
import com.noordwind.apps.collectively.domain.interactor.remark.LoadRemarkTagsUseCase
import com.noordwind.apps.collectively.presentation.extension.firstLetter
import com.noordwind.apps.collectively.presentation.mvp.BasePresenter
import com.noordwind.apps.collectively.presentation.rxjava.AppDisposableObserver
import java.util.*

class TagsListPresenter(val view: TagsListMvp.View,
                        val loadRemarkTagsUseCase: LoadRemarkTagsUseCase) : BasePresenter, TagsListMvp.Presenter {

    override fun loadTags() {
        var observer = object : AppDisposableObserver<List<RemarkTag>>() {

            override fun onStart() {
                super.onStart()
            }

            override fun onNext(remarkTags: List<RemarkTag>) {
                super.onNext(remarkTags)
                var tagNames = remarkTags.map { it.name }
                var groupedTagNames = HashMap<String, LinkedList<String>>()

                for (name in tagNames) {
                    var firstLetter = name.firstLetter()

                    firstLetter?.let {
                        var tagsUnderLetter = groupedTagNames[firstLetter]

                        if (tagsUnderLetter == null) {
                            tagsUnderLetter = LinkedList<String>()
                        }

                        tagsUnderLetter.add(name)

                        groupedTagNames.put(firstLetter, tagsUnderLetter)
                    }
                }

                for (entry in groupedTagNames) {
                    entry.value.sort()
                }

                view.showTags(groupedTagNames)
            }

            override fun onError(e: Throwable) {
                super.onError(e)
            }

            override fun onNetworkError() {
                super.onNetworkError()
            }
        }

        loadRemarkTagsUseCase.execute(observer, null)
    }


    override fun destroy() {
        loadRemarkTagsUseCase.dispose()
    }
}
