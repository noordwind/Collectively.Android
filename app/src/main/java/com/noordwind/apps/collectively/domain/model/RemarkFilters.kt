package com.noordwind.apps.collectively.domain.model

import com.noordwind.apps.collectively.data.model.UserGroup


data class RemarkFilters(val allCategoryFilters: List<String>,
                         val selectedCategoryFilters: List<String>,
                         val allStatusFilters: List<String>,
                         val allGroups: List<UserGroup>,
                         val selectedGroup: String,
                         val selectedStatusFilters: List<String>)
