package com.noordwind.apps.collectively.presentation.views.dialogs.mapfilters

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.remark_map_filter.view.*
import com.noordwind.apps.collectively.R
import com.noordwind.apps.collectively.presentation.extension.iconOfCategory
import com.noordwind.apps.collectively.presentation.rxjava.RxBus


class MapFilterView(context: Context, filter: String, isChecked: Boolean) : LinearLayout(context) {

    init {
        View.inflate(getContext(), R.layout.remark_map_filter, this)
        filterIconLabel.setImageDrawable(ContextCompat.getDrawable(getContext(), filter.iconOfCategory()!!))
        filterLabel.text = filter

        mapFilterCheckbox.isChecked = isChecked
        mapFilterCheckbox.setOnCheckedChangeListener { compoundButton, isSelected -> RxBus.instance.postEvent(MapFilterSelectionChangedEvent(isSelected, filter))}
    }

    class MapFilterSelectionChangedEvent(val selected: Boolean, val filter: String)
}

