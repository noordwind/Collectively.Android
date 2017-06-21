package com.noordwind.apps.collectively.presentation.views.remark

import android.content.Context
import android.support.graphics.drawable.VectorDrawableCompat
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.view_remark_state.view.*
import com.noordwind.apps.collectively.R
import com.noordwind.apps.collectively.TheApp
import com.noordwind.apps.collectively.data.model.RemarkState
import com.noordwind.apps.collectively.data.repository.UsersRepository
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import com.noordwind.apps.collectively.presentation.rxjava.AppDisposableObserver
import com.noordwind.apps.collectively.presentation.statistics.LoadUserPictureUrlUseCase
import javax.inject.Inject


class RemarkStateView(context: Context, state: RemarkState) : LinearLayout(context) {

    @Inject
    lateinit var usersRepository: UsersRepository

    @Inject
    lateinit var ioThread: UseCaseThread

    @Inject
    lateinit var uiThread: PostExecutionThread

    init {
        View.inflate(getContext(), R.layout.view_remark_state, this)
        TheApp[getContext()].appComponent!!.inject(this)

        userImage.setImageDrawable(VectorDrawableCompat.create(context.resources, R.drawable.ic_person_grey_48dp, null))
        authorLabel.text = state.user?.name
        descriptionLabel.text = state.description
        LoadUserPictureUrlUseCase(usersRepository, ioThread, uiThread).execute(UserPictureUrlObserver(context, userImage), state.user.name)
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

