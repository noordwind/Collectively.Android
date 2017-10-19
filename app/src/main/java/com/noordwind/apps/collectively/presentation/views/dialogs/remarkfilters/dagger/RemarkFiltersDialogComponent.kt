package com.noordwind.apps.collectively.presentation.settings.dagger

import com.noordwind.apps.collectively.presentation.dagger.ActivityScope
import com.noordwind.apps.collectively.presentation.views.dialogs.mapfilters.MapFiltersDialog
import com.noordwind.apps.collectively.presentation.views.dialogs.mapfilters.RemarkFiltersDialog
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = arrayOf(RemarkFiltersDialogModule::class))
interface RemarkFiltersDialogComponent {
    fun inject(dialog: RemarkFiltersDialog)
}
