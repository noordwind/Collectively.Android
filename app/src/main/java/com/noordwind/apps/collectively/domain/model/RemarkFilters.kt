package com.noordwind.apps.collectively.domain.model


data class RemarkFilters(val allCategoryFilters: List<String>,
                      val selectedCategoryFilters: List<String>,
                      val allStatusFilters: List<String>,
                      val selectedStatusFilters: List<String>)
