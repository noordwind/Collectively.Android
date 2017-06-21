package com.noordwind.apps.collectively.presentation.adapter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.noordwind.apps.collectively.presentation.walkthrough.WalkthroughPageOneFragment
import com.noordwind.apps.collectively.presentation.walkthrough.WalkthroughPageThreeFragment
import com.noordwind.apps.collectively.presentation.walkthrough.WalkthroughPageTwoFragment

class WalkthroughAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> { return WalkthroughPageOneFragment.getInstance() }
            1 -> { return WalkthroughPageTwoFragment.getInstance() }
            2 -> { return WalkthroughPageThreeFragment.getInstance() }
        }
        throw IllegalArgumentException("Illegal position")
    }

    override fun getPageTitle(position: Int): CharSequence = ""

    override fun getCount(): Int = 3
}
