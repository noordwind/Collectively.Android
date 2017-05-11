package pl.adriankremski.collectively.presentation.adapter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import pl.adriankremski.collectively.R
import pl.adriankremski.collectively.data.model.Statistics
import pl.adriankremski.collectively.presentation.statistics.remarks.RemarksStatisticsFragment
import pl.adriankremski.collectively.presentation.statistics.remarks.TopReportersStatisticsFragment
import pl.adriankremski.collectively.presentation.statistics.remarks.TopResolversStatisticsFragment

class StatisticsTabsAdapter(val context: Context, fragmentManager: FragmentManager, val statistics: Statistics) : FragmentStatePagerAdapter(fragmentManager) {

    private var TABS_AMOUNT = 3;

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> { return RemarksStatisticsFragment.newInstance(statistics) }
            1 -> { return TopReportersStatisticsFragment.newInstance(statistics) }
            2 -> { return TopResolversStatisticsFragment.newInstance(statistics) }
        }
        throw IllegalArgumentException("Illegal position")
    }

    override fun getCount(): Int = TABS_AMOUNT

    override fun getPageTitle(position: Int): CharSequence {
        when (position) {
            0 -> { return context.getString(R.string.remarks) }
            1 -> { return context.getString(R.string.top_reportes) }
            2 -> { return context.getString(R.string.top_resolvers) }
        }
        throw IllegalArgumentException("Illegal position")
    }
}
