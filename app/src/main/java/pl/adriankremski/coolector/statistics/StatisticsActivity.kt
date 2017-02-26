package pl.adriankremski.coolector.statistics

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.widget.TextView
import android.widget.Toast
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend.*
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.utils.ViewPortHandler
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_statistics.*
import pl.adriankremski.coolector.BaseActivity
import pl.adriankremski.coolector.R
import pl.adriankremski.coolector.TheApp
import pl.adriankremski.coolector.model.StatisticEntry
import pl.adriankremski.coolector.model.Statistics
import pl.adriankremski.coolector.repository.StatisticsRepository
import pl.adriankremski.coolector.utils.MaterialColorsTemplate
import pl.adriankremski.coolector.utils.uppercaseFirstLetter
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

    lateinit var mPresenter: StatisticsMvp.Presenter

    internal var mTitleLabel: TextView? = null

    private var mCompositeDisposable: CompositeDisposable? = null

    private var colorTemplate = MaterialColorsTemplate()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TheApp[this].appComponent?.inject(this)
        setContentView(R.layout.activity_statistics);
        var span = SpannableString(getString(R.string.statistics_screen_title))
        span.setSpan(RelativeSizeSpan(1.2f), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        span.setSpan(StyleSpan(Typeface.BOLD), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        mTitleLabel = findViewById(R.id.title) as TextView
        mTitleLabel?.text = span;
        mCompositeDisposable = CompositeDisposable();

        mPresenter = StatisticsPresenter(this, mStatisticsRepository)
        mPresenter.loadStatistics()
    }

    override fun showLoading() {
    }

    override fun showLoadStatisticsError(message: String?) {
        Toast.makeText(this, "Loading error", Toast.LENGTH_SHORT).show()
    }

    override fun showStatistics(statistics: Statistics) {
        showRemarksByStatusChart(statistics.categoryStatistics)
        showRemarksByCategoryChart(statistics.categoryStatistics)
        showRemarksByTagChart(statistics.tagStatistics)
    }

    fun showRemarksByStatusChart(categoryStatistics: List<StatisticEntry>) {
        mRemarksByStatusChart.setUsePercentValues(false)
        mRemarksByStatusChart.isDrawHoleEnabled = true
        mRemarksByStatusChart.setHoleColor(Color.WHITE)
        mRemarksByStatusChart.setTransparentCircleColor(Color.WHITE)
        mRemarksByStatusChart.setTransparentCircleAlpha(0)
        mRemarksByStatusChart.transparentCircleRadius = 20f
        mRemarksByStatusChart.legend.isEnabled = false
        mRemarksByStatusChart.setEntryLabelColor(Color.WHITE)
        mRemarksByStatusChart.setEntryLabelTextSize(12f)
        var description = Description()
        description.isEnabled = false
        description.text = ""
        mRemarksByStatusChart.description = description
        mRemarksByStatusChart.isRotationEnabled = true
        mRemarksByStatusChart.holeRadius = 30f
        mRemarksByStatusChart.animateY(1400, Easing.EasingOption.EaseInOutQuad)

        var resolvedStatisticsCount : Long = 0
        var reportedStatisticsCount : Long = 0

        for (catStat in categoryStatistics) {
            resolvedStatisticsCount += catStat.resolvedCount
            reportedStatisticsCount += catStat.reportedCount
        }

        setRemarksByStatusChartData(resolvedStatisticsCount, reportedStatisticsCount)

        mRemarksByStatusChart.highlightValues(null)
        mRemarksByStatusChart.invalidate()
        var statusLabelText = String.format("There are <b>%d</b> resolved remarks out of <b>%d</b>", resolvedStatisticsCount, reportedStatisticsCount)
        mRemarksByStatusLabel.text = Html.fromHtml(statusLabelText)
    }

    private fun setRemarksByStatusChartData(resolvedStatisticsCount: Long, reportedStatisticsCount: Long) {
        val entries = ArrayList<PieEntry>()
        entries.add(PieEntry(resolvedStatisticsCount.toFloat(), getString(R.string.statistics_resolved_label)))
        entries.add(PieEntry((reportedStatisticsCount - resolvedStatisticsCount).toFloat(), getString(R.string.statistics_unresolved_label)))

        val dataSet = PieDataSet(entries, "")

        dataSet.sliceSpace = 3f
        dataSet.selectionShift = 5f

        val colors = ArrayList<Int>()
        colors.add(ContextCompat.getColor(this, R.color.resolved_remark_color))
        colors.add(ContextCompat.getColor(this, R.color.unresolved_remark_color))
        dataSet.colors = colors

        val data = PieData(dataSet)
        data.setValueFormatter(RemarksStatusChartFormatter())
        data.setValueTextSize(12f)
        data.setValueTextColor(Color.WHITE)
        mRemarksByStatusChart.data = data
    }

    fun showRemarksByCategoryChart(categoryStatistics: List<StatisticEntry>) {
        mRemarksByCategoryChart.setDrawBarShadow(false);
        mRemarksByCategoryChart.setDrawValueAboveBar(true);
        mRemarksByCategoryChart.description.isEnabled = false;
        mRemarksByCategoryChart.setMaxVisibleValueCount(60);
        mRemarksByCategoryChart.setPinchZoom(false);
        mRemarksByCategoryChart.setDrawGridBackground(false);

        var xAxis = mRemarksByCategoryChart.xAxis;
        xAxis.position = XAxisPosition.BOTTOM;
        xAxis.setDrawGridLines(false);

        var leftAxis = mRemarksByCategoryChart.axisLeft;
        leftAxis.setDrawAxisLine(false)
        leftAxis.setDrawGridLines(false)
        leftAxis.setDrawLabels(false)

        var rightAxis = mRemarksByCategoryChart.axisRight;
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawLabels(false)
        rightAxis.setDrawAxisLine(false)

        mRemarksByCategoryChart.xAxis.setDrawGridLines(false)
        mRemarksByCategoryChart.xAxis.setDrawGridLines(false)
        mRemarksByCategoryChart.xAxis.setDrawLabels(false)
        mRemarksByCategoryChart.animateY(1400, Easing.EasingOption.EaseInOutQuad)

        var l = mRemarksByCategoryChart.legend;
        l.verticalAlignment = LegendVerticalAlignment.BOTTOM;
        l.horizontalAlignment = LegendHorizontalAlignment.LEFT;
        l.orientation = LegendOrientation.HORIZONTAL;
        l.setDrawInside(false);
        l.form = LegendForm.SQUARE;
        l.formSize = 9f;
        l.textSize = 11f;
        l.xEntrySpace = 4f;

        var dataSets = mutableListOf<IBarDataSet>()

        for ((valuePosition, catStatistics) in categoryStatistics.withIndex()) {
            var values = mutableListOf<BarEntry>()
            values.add(BarEntry(valuePosition.toFloat(), catStatistics.reportedCount.toFloat()))
            var set = BarDataSet(values, catStatistics.name.uppercaseFirstLetter())
            set.color = getCategoryColor(catStatistics.name)
            dataSets.add(set);
        }

        var data = BarData(dataSets);
        data.setValueTextSize(10f);
        data.barWidth = 0.9f;
        data.setValueFormatter(RemarksStatusChartFormatter())

        mRemarksByCategoryChart.data = data;
    }

    fun  getCategoryColor(name: String): Int {
        return when(name.toLowerCase()) {
            "litter" -> Color.parseColor("#E65100")
            "damages" -> Color.parseColor("#E53935")
            "accidents" -> Color.parseColor("#757575")
            else -> Color.parseColor("#0288D1")
        }
    }

    fun showRemarksByTagChart(tagStatistics: List<StatisticEntry>) {
        mRemarksByTagChart.setDrawBarShadow(false);
        mRemarksByTagChart.setDrawValueAboveBar(true);
        mRemarksByTagChart.description.isEnabled = false;
        mRemarksByTagChart.setMaxVisibleValueCount(60);
        mRemarksByTagChart.setPinchZoom(false);
        mRemarksByTagChart.setDrawGridBackground(false);

        var xAxis = mRemarksByTagChart.xAxis;
        xAxis.setDrawAxisLine(false)
        xAxis.setDrawGridLines(false)
        xAxis.setDrawLabels(false)

        var leftAxis = mRemarksByTagChart.axisLeft;
        leftAxis.setDrawAxisLine(false)
        leftAxis.setDrawGridLines(false)
        leftAxis.setDrawLabels(false)

        var rightAxis = mRemarksByTagChart.axisRight;
        rightAxis.setDrawGridLines(false)
        rightAxis.setDrawLabels(false)
        rightAxis.setDrawAxisLine(false)


        var legend = mRemarksByTagChart.legend;
        legend.verticalAlignment = LegendVerticalAlignment.BOTTOM;
        legend.horizontalAlignment = LegendHorizontalAlignment.LEFT;
        legend.orientation = LegendOrientation.HORIZONTAL;
        legend.setDrawInside(false);
        legend.form = LegendForm.SQUARE;
        legend.formSize = 8f;
        legend.textSize = 8f;
        legend.xEntrySpace = 4f;

        var dataSets = mutableListOf<IBarDataSet>()

        for ((valuePosition, tagStatistic) in tagStatistics.withIndex()) {
            var values = mutableListOf<BarEntry>()
            values.add(BarEntry(valuePosition.toFloat(), tagStatistic.reportedCount.toFloat()))
            var set = BarDataSet(values, tagStatistic.name.uppercaseFirstLetter())
            set.color = colorTemplate.nextColor()
            dataSets.add(set);
        }

        var data = BarData(dataSets);
        data.setValueTextSize(10f);
        data.barWidth = 0.9f;
        data.setValueFormatter(RemarksStatusChartFormatter())

        mRemarksByTagChart.data = data;
        mRemarksByTagChart.animateY(1400, Easing.EasingOption.EaseInOutQuad)
    }

    override fun showLoadStatisticsNetworkError() {
        Toast.makeText(this, "Network error", Toast.LENGTH_SHORT).show()
    }

    class RemarksStatusChartFormatter : IValueFormatter, IAxisValueFormatter {

        override fun getFormattedValue(value: Float, entry: Entry, dataSetIndex: Int, viewPortHandler: ViewPortHandler): String {
            return value.toLong().toString()
        }

        override fun getFormattedValue(value: Float, axis: AxisBase): String {
            return value.toLong().toString()
        }
    }
}
