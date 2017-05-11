package pl.adriankremski.collectively.presentation.statistics

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_statistics.*
import kotlinx.android.synthetic.main.view_error.*
import kotlinx.android.synthetic.main.view_progress.*
import kotlinx.android.synthetic.main.view_toolbar_with_title.*
import pl.adriankremski.collectively.R
import pl.adriankremski.collectively.TheApp
import pl.adriankremski.collectively.data.model.Statistics
import pl.adriankremski.collectively.data.repository.StatisticsRepository
import pl.adriankremski.collectively.domain.thread.PostExecutionThread
import pl.adriankremski.collectively.domain.thread.UseCaseThread
import pl.adriankremski.collectively.presentation.BaseActivity
import pl.adriankremski.collectively.presentation.adapter.StatisticsTabsAdapter
import pl.adriankremski.collectively.presentation.util.RequestErrorDecorator
import pl.adriankremski.collectively.presentation.util.Switcher
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

    @Inject
    lateinit var ioThread: UseCaseThread

    @Inject
    lateinit var uiThread: PostExecutionThread

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

        presenter = StatisticsPresenter(this, LoadStatisticsUseCase(statisticsRepository, ioThread, uiThread))
        presenter.loadStatistics()

        errorDecorator = RequestErrorDecorator(switcherErrorImage, switcherErrorTitle, switcherErrorFooter)
        switcherErrorButton.setOnClickListener { presenter.loadStatistics() }
    }

    private fun setupSwitcher() {
        val contentViews = LinkedList<View>()
        contentViews.add(tabsLayout)
        contentViews.add(contentPager)
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

        var adapter = StatisticsTabsAdapter(baseContext, supportFragmentManager, statistics)
        contentPager.adapter = adapter
        tabsLayout.setupWithViewPager(contentPager)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true;
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }
}
