package pl.adriankremski.collectively.presentation.statistics.remarks

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pl.adriankremski.collectively.Constants
import pl.adriankremski.collectively.R
import pl.adriankremski.collectively.data.model.Statistics
import pl.adriankremski.collectively.presentation.adapter.UsersStatisticsAdapter
import pl.adriankremski.collectively.presentation.adapter.delegates.UsersStatisticsAdapterDelegate

class TopReportersStatisticsFragment : Fragment() {

    companion object {
        fun newInstance(statistics: Statistics): TopReportersStatisticsFragment {
            var fragment = TopReportersStatisticsFragment()
            var arguments = Bundle()
            arguments.putSerializable(Constants.BundleKey.STATISTICS, statistics)
            fragment.arguments = arguments
            return fragment
        }
    }

    private lateinit var userStatisticsAdapter: UsersStatisticsAdapter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = inflater?.inflate(R.layout.fragment_users_statistics, container, false)

        var statistics = arguments.getSerializable(Constants.BundleKey.STATISTICS) as Statistics
        var recycler = layout?.findViewById(R.id.recycler) as RecyclerView

        var entries = statistics.usersStatistics.sortedByDescending { it.reportedCount() }.map { UsersStatisticsAdapterDelegate.UserStatistic(it.name, it.reportedCount().toInt()) }

        userStatisticsAdapter = UsersStatisticsAdapter().setData(entries).initDelegates()
        recycler.adapter = userStatisticsAdapter
        recycler.layoutManager = LinearLayoutManager(context)
        userStatisticsAdapter.notifyDataSetChanged()

        return layout
    }
}

