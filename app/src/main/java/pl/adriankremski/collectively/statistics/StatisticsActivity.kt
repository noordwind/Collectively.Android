package pl.adriankremski.collectively.statistics

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.View
import kotlinx.android.synthetic.main.activity_statistics.*
import kotlinx.android.synthetic.main.view_error.*
import kotlinx.android.synthetic.main.view_progress.*
import kotlinx.android.synthetic.main.view_toolbar_with_title.*
import pl.adriankremski.collectively.BaseActivity
import pl.adriankremski.collectively.R
import pl.adriankremski.collectively.TheApp
import pl.adriankremski.collectively.model.StatisticEntry
import pl.adriankremski.collectively.model.Statistics
import pl.adriankremski.collectively.repository.StatisticsRepository
import pl.adriankremski.collectively.utils.RequestErrorDecorator
import pl.adriankremski.collectively.utils.Switcher
import java.util.*
import javax.inject.Inject

class StatisticsActivity : BaseActivity(), StatisticsMvp.View {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, StatisticsActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    @Inject
    lateinit var statisticsRepository: StatisticsRepository

    private lateinit var presenter: StatisticsMvp.Presenter

    private lateinit var switcher: Switcher

    private lateinit var errorDecorator: RequestErrorDecorator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TheApp[this].appComponent?.inject(this)
        setContentView(R.layout.activity_statistics);
        var span = SpannableString(getString(R.string.statistics_screen_title))
        span.setSpan(RelativeSizeSpan(1.2f), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        span.setSpan(StyleSpan(Typeface.BOLD), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        toolbarTitleLabel.text = span;

        setupSwitcher()

        presenter = StatisticsPresenter(this, LoadStatisticsUseCase(statisticsRepository))
        presenter.loadStatistics()

        errorDecorator = RequestErrorDecorator(switcherErrorImage, switcherErrorTitle, switcherErrorFooter)
        switcherErrorButton.setOnClickListener { presenter.loadStatistics() }
    }

    private fun setupSwitcher() {
        val contentViews = LinkedList<View>()
        contentViews.add(content)
        switcher = Switcher.Builder()
                .withContentViews(contentViews)
                .withErrorViews(listOf<View>(switcherError))
                .withProgressViews(listOf<View>(switcherProgress))
                .build(this)
    }

    override fun showLoading() {
        switcher.showProgressViewsImmediately()
    }

    override fun showLoadStatisticsError(message: String?) {
        errorDecorator.onServerError(message)
        switcher.showErrorViewsImmediately()
    }

    override fun showLoadStatisticsNetworkError() {
        errorDecorator.onNetworkError(getString(R.string.error_statistics_no_network))
        switcher.showErrorViewsImmediately()
    }

    override fun showStatistics(statistics: Statistics) {
        switcher.showContentViewsImmediately()
        remarksByStatusChart.setStatistics(statistics.categoryStatistics)
        showRemarksByStatusLabel(statistics.categoryStatistics)
        remarksByCategoryChart.setStatistics(statistics.categoryStatistics)
        remarksByTagChart.setStatistics(statistics.tagStatistics)
    }

    fun showRemarksByStatusLabel(categoryStatistics: List<StatisticEntry>) {
        var resolvedStatisticsCount : Long = 0
        var reportedStatisticsCount : Long = 0

        for (catStat in categoryStatistics) {
            resolvedStatisticsCount += catStat.resolvedCount
            reportedStatisticsCount += catStat.reportedCount
        }

        var statusLabelText = String.format("There are <b>%d</b> resolved remarks out of <b>%d</b>", resolvedStatisticsCount, reportedStatisticsCount)
        remarksByStatusLabel.text = Html.fromHtml(statusLabelText)
    }

}
