package com.noordwind.apps.collectively.presentation.main.navigation

import com.noordwind.apps.collectively.presentation.mvp.BasePresenter

interface NavigationMvp {
    interface View {
        fun showProfile(name: String, avatarUrl: String?)
    }

    interface Presenter : BasePresenter{
        fun loadProfile()
    }
}

