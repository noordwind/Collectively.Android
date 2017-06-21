package com.noordwind.apps.collectively.presentation.views.chart

import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.noordwind.apps.collectively.R
import com.noordwind.apps.collectively.data.model.StatisticEntry
import java.util.*


class RemarksByStatusChart : PieChart {
    constructor(context: Context?) : super(context) {
        initChart()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initChart()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        initChart()
    }

    fun initChart() {
        setUsePercentValues(false)
        isDrawHoleEnabled = true
        setHoleColor(Color.WHITE)
        setTransparentCircleColor(Color.WHITE)
        setTransparentCircleAlpha(0)
        transparentCircleRadius = 20f
        legend.isEnabled = false
        setEntryLabelColor(Color.WHITE)
        setEntryLabelTextSize(12f)
        var description = Description()
        description.isEnabled = false
        description.text = ""
        this.description = description
        isRotationEnabled = true
        holeRadius = 30f
    }

    fun setStatistics(categoryStatistics: List<StatisticEntry>) {
        var resolvedStatisticsCount : Long = 0
        var reportedStatisticsCount : Long = 0

        for (catStat in categoryStatistics) {
            resolvedStatisticsCount += catStat.resolvedCount()
            reportedStatisticsCount += catStat.reportedCount()
        }

        val entries = ArrayList<PieEntry>()
        entries.add(PieEntry(resolvedStatisticsCount.toFloat(), context.getString(R.string.statistics_resolved_label)))
        entries.add(PieEntry((reportedStatisticsCount - resolvedStatisticsCount).toFloat(), context.getString(R.string.statistics_unresolved_label)))

        val dataSet = PieDataSet(entries, "")

        dataSet.sliceSpace = 3f
        dataSet.selectionShift = 5f

        val colors = ArrayList<Int>()
        colors.add(ContextCompat.getColor(context, R.color.resolved_remark_color))
        colors.add(ContextCompat.getColor(context, R.color.unresolved_remark_color))
        dataSet.colors = colors

        val data = PieData(dataSet)
        data.setValueFormatter(RemarksChartValueFormatter())
        data.setValueTextSize(12f)
        data.setValueTextColor(Color.WHITE)
        this.data = data

        highlightValues(null)
        invalidate()

        animateY(1400, Easing.EasingOption.EaseInOutQuad)
    }
}

