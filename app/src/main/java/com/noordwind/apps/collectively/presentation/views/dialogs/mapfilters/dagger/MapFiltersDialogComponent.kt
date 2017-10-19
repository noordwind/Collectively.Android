package com.noordwind.apps.collectively.presentation.settings.dagger

import com.noordwind.apps.collectively.presentation.dagger.ActivityScope
import com.noordwind.apps.collectively.presentation.views.dialogs.mapfilters.MapFiltersDialog
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = arrayOf(MainNavigationMenuModule::class))
interface MapFiltersDialogComponent {
    fun inject(dialog: MapFiltersDialog)
}
