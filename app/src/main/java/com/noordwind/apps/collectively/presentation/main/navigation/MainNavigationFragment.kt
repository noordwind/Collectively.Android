package com.noordwind.apps.collectively.presentation.main.navigation

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.noordwind.apps.collectively.R
import com.noordwind.apps.collectively.TheApp
import com.noordwind.apps.collectively.presentation.main.navigation.mvp.NavigationMvp
import com.noordwind.apps.collectively.presentation.profile.ProfileActivity
import com.noordwind.apps.collectively.presentation.settings.SettingsActivity
import com.noordwind.apps.collectively.presentation.settings.dagger.MainNavigationMenuModule
import com.noordwind.apps.collectively.presentation.statistics.StatisticsActivity
import com.noordwind.apps.collectively.presentation.users.UsersActivity
import com.noordwind.apps.collectively.presentation.util.FacebookUtils
import kotlinx.android.synthetic.main.fragment_main_navigation.*
import javax.inject.Inject

class MainNavigationFragment : Fragment(), NavigationMvp.View {

    @Inject
    lateinit var presenter: NavigationMvp.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TheApp[context].appComponent!!.plusMainNavigationMenuComponent(MainNavigationMenuModule(this)).inject(this)
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

    }

    override fun onStart() {
        super.onStart()
        presenter.loadProfile()
    }

    override fun onResume() {
        super.onResume()
        presenter.refreshLocation()
    }

    override fun showAddress(addressPretty: String) {
        addressLabel.text = addressPretty
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
