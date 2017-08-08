package com.noordwind.apps.collectively.domain.model

import com.noordwind.apps.collectively.data.model.UserGroup


data class MapFilters(val allFilters: List<String>,
                      val selectedFilters: List<String>,
                      val remarkStatus: String,
                      val allGroups: List<UserGroup>,
                      val selectedGroup: String,
                      val showOnlyMine: Boolean)
