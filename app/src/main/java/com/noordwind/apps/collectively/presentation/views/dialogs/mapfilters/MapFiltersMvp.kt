package com.noordwind.apps.collectively.presentation.views.dialogs.mapfilters

import com.noordwind.apps.collectively.presentation.mvp.BasePresenter


interface MapFiltersMvp {
    interface View {
        fun showFilters(selectedFilters: List<String>, allFilters: List<String>)
        fun selectRemarkStatusFilter(status: String)
        fun  selectShowOnlyMineRemarksFilter(showOnlyMine: Boolean)
    }

    interface Presenter : BasePresenter{
        fun loadFilters()
        fun toggleFilter(filter: String, selected: Boolean)
        fun selectRemarkStatus(status: String)
        fun toggleShouldShowOnlyMyRemarksFilter(shouldShow: Boolean)
    }
}
