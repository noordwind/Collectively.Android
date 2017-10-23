package com.noordwind.apps.collectively.presentation.settings.dagger

import com.noordwind.apps.collectively.domain.interactor.remark.LoadRemarkPhotoUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.LoadRemarkViewDataUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.votes.DeleteRemarkVoteUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.votes.SubmitRemarkVoteUseCase
import com.noordwind.apps.collectively.presentation.remarkpreview.mvp.RemarkPresenter
import com.noordwind.apps.collectively.presentation.remarkpreview.mvp.RemarkPreviewMvp
import dagger.Module
import dagger.Provides

@Module
class RemarkScreenModule(val view: RemarkPreviewMvp.View) {

    @Provides
    internal fun presenter(loadRemarkPhotoUseCase: LoadRemarkPhotoUseCase,
                           loadRemarkViewDataUseCase: LoadRemarkViewDataUseCase,
                           submitRemarkVoteUseCase: SubmitRemarkVoteUseCase,
                           deleteRemarkVoteUseCase: DeleteRemarkVoteUseCase): RemarkPreviewMvp.Presenter {

        return RemarkPresenter(view, loadRemarkPhotoUseCase, loadRemarkViewDataUseCase, submitRemarkVoteUseCase,
                deleteRemarkVoteUseCase)
    }
}
