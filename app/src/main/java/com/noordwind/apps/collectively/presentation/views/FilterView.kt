package com.noordwind.apps.collectively.presentation.views

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.LinearLayout
import com.noordwind.apps.collectively.R
import com.noordwind.apps.collectively.presentation.extension.iconOfCategory
import com.noordwind.apps.collectively.presentation.extension.uppercaseFirstLetter
import com.noordwind.apps.collectively.presentation.rxjava.RxBus
import kotlinx.android.synthetic.main.remark_map_filter.view.*


class FilterView(context: Context, filter: String, isChecked: Boolean, showIcon: Boolean = true, type: String? = null) : LinearLayout(context) {

    init {
        View.inflate(getContext(), R.layout.remark_map_filter, this)
        filterIconLabel.setImageDrawable(ContextCompat.getDrawable(getContext(), filter.iconOfCategory()!!))
        if (!showIcon) {
            imageSection.visibility = View.GONE
        }
        filterLabel.text = filter.uppercaseFirstLetter()

        setOnClickListener {
            mapFilterCheckbox.isChecked = !mapFilterCheckbox.isChecked
            RxBus.instance.postEvent(FilterSelectionChangedEvent(mapFilterCheckbox.isChecked, filter, type))
        }
        mapFilterCheckbox.isChecked = isChecked
        mapFilterCheckbox.setOnCheckedChangeListener { compoundButton, isSelected -> RxBus.instance.postEvent(FilterSelectionChangedEvent(isSelected, filter, type))}
    }

    class FilterSelectionChangedEvent(val selected: Boolean, val filter: String, val type: String? = null)
}

