package com.noordwind.apps.collectively.presentation.statistics

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.noordwind.apps.collectively.R
import com.noordwind.apps.collectively.TheApp
import com.noordwind.apps.collectively.data.model.Statistics
import com.noordwind.apps.collectively.presentation.adapter.StatisticsTabsAdapter
import com.noordwind.apps.collectively.presentation.settings.dagger.StatisticsModule
import com.noordwind.apps.collectively.presentation.statistics.mvp.StatisticsMvp
import com.noordwind.apps.collectively.presentation.util.RequestErrorDecorator
import com.noordwind.apps.collectively.presentation.util.Switcher
import kotlinx.android.synthetic.main.activity_statistics.*
import kotlinx.android.synthetic.main.view_error.*
import kotlinx.android.synthetic.main.view_progress.*
import kotlinx.android.synthetic.main.view_toolbar_with_title.*
import java.util.*
import javax.inject.Inject

class StatisticsActivity : com.noordwind.apps.collectively.presentation.BaseActivity(), StatisticsMvp.View {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, StatisticsActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    @Inject
    lateinit var presenter: StatisticsMvp.Presenter

    private lateinit var switcher: Switcher

    private lateinit var errorDecorator: RequestErrorDecorator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TheApp[this].appComponent!!.plusStatisticsComponent(StatisticsModule(this)).inject(this)
        setContentView(R.layout.activity_statistics);

        toolbarTitleLabel.text = getString(R.string.statistics_screen_title)

        setupSwitcher()

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
