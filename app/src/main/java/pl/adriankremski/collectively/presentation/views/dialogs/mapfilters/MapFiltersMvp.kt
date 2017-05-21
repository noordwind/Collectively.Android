package pl.adriankremski.collectively.presentation.views.dialogs.mapfilters

import pl.adriankremski.collectively.presentation.mvp.BasePresenter


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
