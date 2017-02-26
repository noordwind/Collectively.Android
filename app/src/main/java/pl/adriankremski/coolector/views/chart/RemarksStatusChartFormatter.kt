package pl.adriankremski.coolector.views.chart

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.utils.ViewPortHandler

class RemarksChartValueFormatter : IValueFormatter, IAxisValueFormatter {

    override fun getFormattedValue(value: Float, entry: Entry, dataSetIndex: Int, viewPortHandler: ViewPortHandler): String {
        return value.toLong().toString()
    }

    override fun getFormattedValue(value: Float, axis: AxisBase): String {
        return value.toLong().toString()
    }
}