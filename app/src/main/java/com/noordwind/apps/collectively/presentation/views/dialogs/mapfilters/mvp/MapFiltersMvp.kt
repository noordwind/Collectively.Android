package com.noordwind.apps.collectively.presentation.views.dialogs.mapfilters.mvp

import com.noordwind.apps.collectively.data.model.UserGroup
import com.noordwind.apps.collectively.presentation.mvp.BasePresenter


interface MapFiltersMvp {
    interface View {
        fun selectShowOnlyMineRemarksFilter(showOnlyMine: Boolean)
        fun showUserGroups(allGroups: List<UserGroup>, selectedGroup: String)
        fun showCategoryFilters(selectedCategoryFilters: List<String>, allCategoryFilters: List<String>)
        fun showStatusFilters(selectedStatusFilters: List<String>, allStatusFilters: List<String>)
    }

    interface Presenter : BasePresenter {
        fun loadFilters()
        fun toggleShouldShowOnlyMyRemarksFilter(shouldShow: Boolean)
        fun selectGroup(group: String)
        fun toggleStatusFilter(filter: String, selected: Boolean)
        fun toggleCategoryFilter(filter: String, selected: Boolean)
    }
}
