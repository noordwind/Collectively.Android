package com.noordwind.apps.collectively.presentation.views

import android.content.Context
import android.widget.TextView
import com.noordwind.apps.collectively.R
import com.noordwind.apps.collectively.data.model.RemarkCategory
import com.noordwind.apps.collectively.presentation.extension.setBackgroundCompat
import com.noordwind.apps.collectively.presentation.rxjava.RxBus


class RemarkCategoryFlowLayoutView(context: Context, val category: RemarkCategory, useOnCLickListener: Boolean = false) : TextView(context) {

    private var isChosen: Boolean = false

    init {
        if (useOnCLickListener) {
            setOnClickListener {
                select(!isChosen)
                RxBus.instance.postEvent(CategorySelectedEvent(category, isChosen))
            }
        }
    }

    fun select(selected: Boolean) {
        this.isChosen = selected

        if (isChosen) {
            setBackgroundCompat(getCategoryFlowLayoutBackgroundResourceId(category))
        } else {
            setBackgroundCompat(R.drawable.remark_category_unselected_flow_layout_background)
        }
    }

    fun getCategoryFlowLayoutBackgroundResourceId(category: RemarkCategory): Int {
        when (category.name.toLowerCase()) {
            "defect" -> {
                return R.drawable.remark_defect_category_selected_flow_layout_background
            }
            "issue" -> {
                return R.drawable.remark_issue_category_selected_flow_layout_background
            }
            "suggestion" -> {
                return R.drawable.remark_suggestion_category_selected_flow_layout_background
            }
            "praise" -> {
                return R.drawable.remark_praise_category_selected_flow_layout_background
            }
            else -> {
                return R.drawable.remark_tag_selected_background
            }
        }
    }

    class CategorySelectedEvent(val category: RemarkCategory, val isSelected: Boolean)
}

