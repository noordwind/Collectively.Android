package com.noordwind.apps.collectively.presentation.views

import android.content.Context
import android.support.graphics.drawable.VectorDrawableCompat
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.noordwind.apps.collectively.R
import com.noordwind.apps.collectively.TheApp
import com.noordwind.apps.collectively.data.model.RemarkComment
import com.noordwind.apps.collectively.data.repository.UsersRepository
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import com.noordwind.apps.collectively.presentation.rxjava.AppDisposableObserver
import com.noordwind.apps.collectively.presentation.statistics.LoadUserPictureUrlUseCase
import kotlinx.android.synthetic.main.view_remark_comment.view.*
import javax.inject.Inject


class RemarkCommentView(context: Context, comment: RemarkComment) : LinearLayout(context) {

    @Inject
    lateinit var usersRepository: UsersRepository

    @Inject
    lateinit var ioThread: UseCaseThread

    @Inject
    lateinit var uiThread: PostExecutionThread

    private var useCase: LoadUserPictureUrlUseCase? = null;

    init {
        View.inflate(getContext(), R.layout.view_remark_comment, this)
        TheApp[getContext()].appComponent!!.inject(this)

        authorLabel.text = comment.user?.name
        commentLabel.text = comment.text

        userImage.setImageDrawable(VectorDrawableCompat.create(context.resources, R.drawable.ic_person_grey_48dp, null))
        useCase = LoadUserPictureUrlUseCase(usersRepository, ioThread, uiThread)
        useCase!!.execute(UserPictureUrlObserver(context, userImage), comment.user?.name)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        useCase?.dispose()
    }

    class UserPictureUrlObserver(val context: Context, val userImage: ImageView) : AppDisposableObserver<String>() {

        override fun onStart() {
            super.onStart()
        }

        override fun onNext(url: String) {
            super.onNext(url)
            Glide.with(context).load(url).placeholder(VectorDrawableCompat.create(context.resources, R.drawable.ic_person_grey_48dp, null)).into(userImage)
        }

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onServerError(message: String?) {
            super.onServerError(message)
        }

        override fun onNetworkError() {
            super.onNetworkError()
        }
    }
}

