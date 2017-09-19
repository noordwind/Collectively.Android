package com.noordwind.apps.collectively.presentation.views

import android.content.Context
import android.content.res.ColorStateList
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.LinearLayout
import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.R
import com.noordwind.apps.collectively.TheApp
import com.noordwind.apps.collectively.data.datasource.FiltersTranslationsDataSource
import com.noordwind.apps.collectively.presentation.extension.iconOfCategory
import com.noordwind.apps.collectively.presentation.extension.textInString
import com.noordwind.apps.collectively.presentation.extension.uppercaseFirstLetter
import com.noordwind.apps.collectively.presentation.rxjava.RxBus
import kotlinx.android.synthetic.main.remark_map_filter.view.*
import javax.inject.Inject


class FilterView(context: Context, val filter: String, isChecked: Boolean, showIcon: Boolean = true, val type: String? = null) : LinearLayout(context) {

    @Inject
    lateinit var translationsDataSource : FiltersTranslationsDataSource

    init {
        TheApp[context].appComponent!!.inject(this)
        View.inflate(getContext(), R.layout.remark_map_filter, this)
        filterIconLabel.setImageDrawable(ContextCompat.getDrawable(getContext(), filter.iconOfCategory()!!))
        if (!showIcon) {
            imageSection.visibility = View.GONE
        }

        var filterName = translationsDataSource.translateFromType(filter.toLowerCase())
        filterLabel.text = filterName.uppercaseFirstLetter()

        setOnClickListener {
            mapFilterCheckbox.isChecked = !mapFilterCheckbox.isChecked
            postFilterSelectionEvent(mapFilterCheckbox.isChecked)
        }
        mapFilterCheckbox.isChecked = isChecked
        mapFilterCheckbox.setOnCheckedChangeListener { compoundButton, isSelected -> postFilterSelectionEvent(isSelected)}

        when (filter.toLowerCase())  {
            Constants.RemarkStates.NEW.toLowerCase() -> {
                setCheckboxTintColor(R.color.remark_state_new_color)
            }
            Constants.RemarkStates.PROCESSING.toLowerCase() -> {
                setCheckboxTintColor(R.color.remark_state_processing_color)
            }
            Constants.RemarkStates.RENEWED.toLowerCase() -> {
                setCheckboxTintColor(R.color.remark_state_renewed_color)
            }
            Constants.RemarkStates.RESOLVED.toLowerCase() -> {
                setCheckboxTintColor(R.color.remark_state_resolved_color)
            }
        }
    }

    fun setCheckboxTintColor(color: Int) {
        mapFilterCheckbox.supportButtonTintList = ColorStateList.valueOf(ContextCompat.getColor(context, color))
    }

    fun postFilterSelectionEvent(isSelected: Boolean) {
        var filterType = translationsDataSource.translateToType(filterLabel.textInString().toLowerCase())
        RxBus.instance.postEvent(FilterSelectionChangedEvent(mapFilterCheckbox.isChecked, filterType, type))
    }

    class FilterSelectionChangedEvent(val selected: Boolean, val filter: String, val type: String? = null)
}

