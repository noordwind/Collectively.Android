package com.noordwind.apps.collectively.data.datasource

interface FiltersTranslationsDataSource {
    fun translateFromType(type: String): String
    fun translateToType(translation: String): String
}
