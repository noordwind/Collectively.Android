package com.noordwind.apps.collectively.presentation.adapter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.noordwind.apps.collectively.R
import com.noordwind.apps.collectively.data.model.RemarkComment
import com.noordwind.apps.collectively.data.model.RemarkState
import com.noordwind.apps.collectively.presentation.remarkpreview.RemarkCommentsFragment
import com.noordwind.apps.collectively.presentation.remarkpreview.RemarkHistoryFragment

class RemarkPreviewTabsAdapter(val context: Context,
                               fragmentManager: FragmentManager,
                               val comments: List<RemarkComment>,
                               val states: List<RemarkState>,
                               val userId: String,
                               val remarkId: String) : FragmentStatePagerAdapter(fragmentManager) {

    private var TABS_AMOUNT = 2;

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> { return RemarkHistoryFragment.newInstance(states, userId, remarkId) }
            1 -> { return RemarkCommentsFragment.newInstance(comments, userId, remarkId) }
        }
        throw IllegalArgumentException("Illegal position")
    }

    override fun getCount(): Int = TABS_AMOUNT

    override fun getPageTitle(position: Int): CharSequence {
        when (position) {
            0 -> { return context.getString(R.string.history) }
            1 -> { return context.getString(R.string.comments) }
        }
        throw IllegalArgumentException("Illegal position")
    }
}
