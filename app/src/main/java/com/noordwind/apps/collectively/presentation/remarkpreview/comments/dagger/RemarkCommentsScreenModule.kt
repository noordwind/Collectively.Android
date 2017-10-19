package com.noordwind.apps.collectively.presentation.settings.dagger

import com.noordwind.apps.collectively.data.repository.RemarksRepository
import com.noordwind.apps.collectively.domain.interactor.remark.comments.LoadRemarkCommentsUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.comments.SubmitRemarkCommentUseCase
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import com.noordwind.apps.collectively.presentation.remarkpreview.comments.mvp.RemarkCommentsMvp
import com.noordwind.apps.collectively.presentation.remarkpreview.comments.mvp.RemarkCommentsPresenter
import dagger.Module
import dagger.Provides

@Module
class RemarkCommentsScreenModule(val view: RemarkCommentsMvp.View) {

    @Provides
    internal fun presenter(remarksRepository: RemarksRepository,
                           ioThread: UseCaseThread,
                           uiThread: PostExecutionThread): RemarkCommentsMvp.Presenter {

        return RemarkCommentsPresenter(view, LoadRemarkCommentsUseCase(remarksRepository, ioThread, uiThread),
                SubmitRemarkCommentUseCase(remarksRepository, ioThread, uiThread))
    }
}
