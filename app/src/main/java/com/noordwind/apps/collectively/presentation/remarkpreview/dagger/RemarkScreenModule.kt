package com.noordwind.apps.collectively.presentation.settings.dagger

import com.noordwind.apps.collectively.data.repository.ProfileRepository
import com.noordwind.apps.collectively.data.repository.RemarksRepository
import com.noordwind.apps.collectively.domain.interactor.remark.LoadRemarkPhotoUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.LoadRemarkViewDataUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.votes.DeleteRemarkVoteUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.votes.SubmitRemarkVoteUseCase
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import com.noordwind.apps.collectively.presentation.remarkpreview.mvp.RemarkPresenter
import com.noordwind.apps.collectively.presentation.remarkpreview.mvp.RemarkPreviewMvp
import dagger.Module
import dagger.Provides

@Module
class RemarkScreenModule(val view: RemarkPreviewMvp.View) {

    @Provides
    internal fun presenter(remarksRepository: RemarksRepository,
                           profileRepository: ProfileRepository,
                           ioThread: UseCaseThread,
                           uiThread: PostExecutionThread): RemarkPreviewMvp.Presenter {

        return RemarkPresenter(view,
                LoadRemarkPhotoUseCase(remarksRepository, ioThread, uiThread),
                LoadRemarkViewDataUseCase(profileRepository, remarksRepository, ioThread, uiThread),
                SubmitRemarkVoteUseCase(remarksRepository, profileRepository, ioThread, uiThread),
                DeleteRemarkVoteUseCase(remarksRepository, profileRepository, ioThread, uiThread))
    }
}
