package pl.adriankremski.coolector.statistics

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
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.utils.ViewPortHandler
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_statistics.*
import kotlinx.android.synthetic.main.view_error.*
import kotlinx.android.synthetic.main.view_progress.*
import kotlinx.android.synthetic.main.view_toolbar_with_title.*
import pl.adriankremski.coolector.BaseActivity
import pl.adriankremski.coolector.R
import pl.adriankremski.coolector.TheApp
import pl.adriankremski.coolector.model.StatisticEntry
import pl.adriankremski.coolector.model.Statistics
import pl.adriankremski.coolector.repository.StatisticsRepository
import pl.adriankremski.coolector.utils.RequestErrorDecorator
import pl.adriankremski.coolector.utils.Switcher
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
    lateinit var mStatisticsRepository: StatisticsRepository

    private lateinit var mPresenter: StatisticsMvp.Presenter

    private var mCompositeDisposable: CompositeDisposable? = null
    private lateinit var switcher: Switcher

    private lateinit var errorDecorator: RequestErrorDecorator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TheApp[this].appComponent?.inject(this)
        setContentView(R.layout.activity_statistics);
        var span = SpannableString(getString(R.string.statistics_screen_title))
        span.setSpan(RelativeSizeSpan(1.2f), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        span.setSpan(StyleSpan(Typeface.BOLD), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        mToolbarTitleLabel.text = span;

        mCompositeDisposable = CompositeDisposable();
        setupSwitcher()
        mPresenter = StatisticsPresenter(this, mStatisticsRepository)
        mPresenter.loadStatistics()

        errorDecorator = RequestErrorDecorator(mSwitcherErrorImage, mSwitcherErrorTitle, mSwitcherErrorFooter)
        mSwitcherErrorButton.setOnClickListener { mPresenter.loadStatistics() }
    }

    private fun setupSwitcher() {
        val contentViews = LinkedList<View>()
        contentViews.add(mContent)
        switcher = Switcher.Builder()
                .withContentViews(contentViews)
                .withErrorViews(listOf<View>(mSwitcherError))
                .withProgressViews(listOf<View>(mSwitcherProgress))
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
        mRemarksByStatusChart.setStatistics(statistics.categoryStatistics)
        showRemarksByStatusLabel(statistics.categoryStatistics)
        mRemarksByCategoryChart.setStatistics(statistics.categoryStatistics)
        mRemarksByTagChart.setStatistics(statistics.tagStatistics)
    }

    fun showRemarksByStatusLabel(categoryStatistics: List<StatisticEntry>) {
        var resolvedStatisticsCount : Long = 0
        var reportedStatisticsCount : Long = 0

        for (catStat in categoryStatistics) {
            resolvedStatisticsCount += catStat.resolvedCount
            reportedStatisticsCount += catStat.reportedCount
        }

        var statusLabelText = String.format("There are <b>%d</b> resolved remarks out of <b>%d</b>", resolvedStatisticsCount, reportedStatisticsCount)
        mRemarksByStatusLabel.text = Html.fromHtml(statusLabelText)
    }

}
