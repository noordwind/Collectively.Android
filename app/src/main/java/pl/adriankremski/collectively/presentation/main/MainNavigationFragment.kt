package pl.adriankremski.collectively.presentation.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_main_navigation.*
import pl.adriankremski.collectively.R
import pl.adriankremski.collectively.presentation.profile.ProfileActivity
import pl.adriankremski.collectively.presentation.settings.SettingsActivity
import pl.adriankremski.collectively.presentation.statistics.StatisticsActivity
import pl.adriankremski.collectively.presentation.util.FacebookUtils

class MainNavigationFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = inflater?.inflate(R.layout.fragment_main_navigation, container, false)
        return layout
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mProfileOptionView.setOnClickListener { openProfile() }
        mStatisticsOptionView.setOnClickListener { openStatistics() }
        mSettingsOptionView.setOnClickListener { openSettings() }
        mFanpageOptionView.setOnClickListener { openFanPage() }
    }

    fun openProfile() {
        ProfileActivity.start(context)
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
}
