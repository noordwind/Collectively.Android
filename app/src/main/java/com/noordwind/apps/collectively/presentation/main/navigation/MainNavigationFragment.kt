package com.noordwind.apps.collectively.presentation.main.navigation

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.noordwind.apps.collectively.R
import com.noordwind.apps.collectively.TheApp
import com.noordwind.apps.collectively.data.repository.ProfileRepository
import com.noordwind.apps.collectively.domain.interactor.profile.LoadProfileUseCase
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import com.noordwind.apps.collectively.presentation.profile.ProfileActivity
import com.noordwind.apps.collectively.presentation.settings.SettingsActivity
import com.noordwind.apps.collectively.presentation.statistics.StatisticsActivity
import com.noordwind.apps.collectively.presentation.users.UsersActivity
import com.noordwind.apps.collectively.presentation.util.FacebookUtils
import kotlinx.android.synthetic.main.fragment_main_navigation.*
import javax.inject.Inject


class MainNavigationFragment : Fragment(), NavigationMvp.View {
    @Inject
    lateinit var profileRepository: ProfileRepository

    @Inject
    lateinit var ioThread: UseCaseThread

    @Inject
    lateinit var uiThread: PostExecutionThread

    private lateinit var presenter: NavigationMvp.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TheApp[context].appComponent?.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = inflater?.inflate(R.layout.fragment_main_navigation, container, false)
        return layout
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mProfileOptionView.setOnClickListener { openProfile() }
        mUsersOptionView.setOnClickListener { openUsers() }
        mStatisticsOptionView.setOnClickListener { openStatistics() }
        mSettingsOptionView.setOnClickListener { openSettings() }
        mFanpageOptionView.setOnClickListener { openFanPage() }

        presenter = NavigationPresenter(this, LoadProfileUseCase(profileRepository, ioThread, uiThread))
    }

    override fun onStart() {
        super.onStart()
        presenter.loadProfile()
    }

    fun openProfile() {
        ProfileActivity.start(context)
    }

    fun openUsers() {
        UsersActivity.start(context)
    }

    fun openStatistics() {
        StatisticsActivity.start(context)
    }

    fun openSettings() {
        SettingsActivity.start(context)
    }

    fun openFanPage() {
        activity.startActivity(FacebookUtils.newFacebookIntent(activity.packageManager, "https://www.facebook.com/becollectively/"))
    }

    override fun showProfile(name: String, avatarUrl: String?) {
        if (avatarUrl != null) {
            Glide.with(this).load(avatarUrl).into(profileImage)
        }
        userNameLabel.text = name
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.destroy()
    }
}
