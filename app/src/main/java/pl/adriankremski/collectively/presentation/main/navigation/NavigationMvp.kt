package pl.adriankremski.collectively.presentation.main.navigation

import pl.adriankremski.collectively.presentation.mvp.BasePresenter

interface NavigationMvp {
    interface View {
        fun showProfile(name: String, avatarUrl: String?)
    }

    interface Presenter : BasePresenter{
        fun loadProfile()
    }
}

