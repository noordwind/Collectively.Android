package com.noordwind.apps.collectively.data.datasource

import android.content.Context
import com.noordwind.apps.collectively.R
import java.util.*

class FiltersTranslationsDataSourceImpl(val context: Context) : FiltersTranslationsDataSource {

    var toFilterTypeTranslation = HashMap<String,String>()
    var fromFilterTypeTranslation = HashMap<String,String>()

    init {
        // CATEGORIES TRANSLATIONS
        fromFilterTypeTranslation.put(context.getString(R.string.remark_category_defect).toLowerCase(),
                context.getString(R.string.remark_category_defect_name).toLowerCase())

        fromFilterTypeTranslation.put(context.getString(R.string.remark_category_issue).toLowerCase(),
                context.getString(R.string.remark_category_issue_name).toLowerCase())

        fromFilterTypeTranslation.put(context.getString(R.string.remark_category_praise).toLowerCase(),
                context.getString(R.string.remark_category_praise_name).toLowerCase())

        fromFilterTypeTranslation.put(context.getString(R.string.remark_category_suggestion).toLowerCase(),
                context.getString(R.string.remark_category_suggestion_name).toLowerCase())

        toFilterTypeTranslation.put(context.getString(R.string.remark_category_defect_name).toLowerCase(),
                context.getString(R.string.remark_category_defect).toLowerCase())

        toFilterTypeTranslation.put(context.getString(R.string.remark_category_issue_name).toLowerCase(),
                context.getString(R.string.remark_category_issue).toLowerCase())

        toFilterTypeTranslation.put(context.getString(R.string.remark_category_praise_name).toLowerCase(),
                context.getString(R.string.remark_category_praise).toLowerCase())

        toFilterTypeTranslation.put(context.getString(R.string.remark_category_suggestion_name).toLowerCase(),
                context.getString(R.string.remark_category_suggestion).toLowerCase())

        // STATES TRANSLATIONS
        fromFilterTypeTranslation.put(context.getString(R.string.remark_state_new).toLowerCase(),
                context.getString(R.string.remark_state_new_name).toLowerCase())

        fromFilterTypeTranslation.put(context.getString(R.string.remark_state_processing).toLowerCase(),
                context.getString(R.string.remark_state_processing_name).toLowerCase())

        fromFilterTypeTranslation.put(context.getString(R.string.remark_state_removed).toLowerCase(),
                context.getString(R.string.remark_state_removed_name).toLowerCase())

        fromFilterTypeTranslation.put(context.getString(R.string.remark_state_resolved).toLowerCase(),
                context.getString(R.string.remark_state_resolved_name).toLowerCase())


        toFilterTypeTranslation.put(context.getString(R.string.remark_state_new_name).toLowerCase(),
                context.getString(R.string.remark_state_new).toLowerCase())

        toFilterTypeTranslation.put(context.getString(R.string.remark_state_resolved_name).toLowerCase(),
                context.getString(R.string.remark_state_resolved).toLowerCase())

        toFilterTypeTranslation.put(context.getString(R.string.remark_state_processing_name).toLowerCase(),
                context.getString(R.string.remark_state_processing).toLowerCase())

        toFilterTypeTranslation.put(context.getString(R.string.remark_state_removed_name).toLowerCase(),
                context.getString(R.string.remark_state_removed).toLowerCase())
    }

    override fun translateFromType(type: String): String {
        if (fromFilterTypeTranslation.containsKey(type)) {
            return fromFilterTypeTranslation[type]!!
        }
        return type
    }

    override fun translateToType(translation: String): String {
        if (toFilterTypeTranslation.containsKey(translation)) {
            return toFilterTypeTranslation[translation]!!
        }
        return translation
    }
}
