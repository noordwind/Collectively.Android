package pl.adriankremski.collectively.presentation.statistics.remarks

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import pl.adriankremski.collectively.Constants
import pl.adriankremski.collectively.R
import pl.adriankremski.collectively.data.model.Statistics
import pl.adriankremski.collectively.presentation.views.chart.RemarksByCategoryChart
import pl.adriankremski.collectively.presentation.views.chart.RemarksByStatusChart
import pl.adriankremski.collectively.presentation.views.chart.RemarksByTagChart

class RemarksStatisticsFragment : Fragment() {

    companion object {
        fun newInstance(statistics: Statistics): RemarksStatisticsFragment {
            var fragment = RemarksStatisticsFragment()
            var arguments = Bundle()
            arguments.putSerializable(Constants.BundleKey.STATISTICS, statistics)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = inflater?.inflate(R.layout.fragment_remarks_statistics, container, false)

        var statistics = arguments.getSerializable(Constants.BundleKey.STATISTICS) as Statistics

        var remarksByStatusChart = layout?.findViewById(R.id.remarksByStatusChart) as RemarksByStatusChart
        var remarksByCategoryChart = layout?.findViewById(R.id.remarksByCategoryChart) as RemarksByCategoryChart
        var remarksByTagChart = layout?.findViewById(R.id.remarksByTagChart) as RemarksByTagChart
        var remarksByStatusLabel = layout?.findViewById(R.id.remarksByStatusLabel) as TextView

        remarksByStatusChart.setStatistics(statistics.categoryStatistics)

        var resolvedStatisticsCount: Long = 0
        var reportedStatisticsCount: Long = 0

        for (catStat in statistics.categoryStatistics) {
            resolvedStatisticsCount += catStat.resolvedCount()
            reportedStatisticsCount += catStat.reportedCount()
        }

        var statusLabelText = String.format("There are <b>%d</b> resolved remarks out of <b>%d</b>", resolvedStatisticsCount, reportedStatisticsCount)
        remarksByStatusLabel.text = Html.fromHtml(statusLabelText)

        remarksByCategoryChart.setStatistics(statistics.categoryStatistics)
        remarksByTagChart.setStatistics(statistics.tagStatistics)
        return layout
    }
}

