package com.noordwind.apps.collectively.presentation.dagger

import android.content.Context
import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.data.datasource.*
import com.noordwind.apps.collectively.data.net.Api
import dagger.Module
import dagger.Provides

@Module
class DataSourceModule : Constants {

    @Provides
    fun authenticationDataSource(api: Api): AuthDataSource = AuthDataSourceImpl(api)

    @Provides
    fun userGroupsSource(api: Api): UserGroupsDataSource = UserGroupsDataSourceImpl(api)

    @Provides
    fun remarksDataSource(api: Api): RemarksDataSource = RemarksDataSourceImpl(api)

    @Provides
    fun fileDataSource(context: Context): FileDataSource = FileDataSourceImpl(context)

    @Provides
    fun operationDataSource(api: Api): OperationDataSource = OperationDataSourceImpl(api)

    @Provides
    fun statisticsDataSource(api: Api): StatisticsDataSource = StatisticsDataSourceImpl(api)

    @Provides
    fun profileDataSource(api: Api): ProfileDataSource = ProfileDataSourceImpl(api)

    @Provides
    fun mapFiltersDataSource(context: Context): MapFiltersDataSource = MapFiltersDataSourceImpl(context)

    @Provides
    fun remarkFiltersDataSource(context: Context): RemarkFiltersDataSource = RemarkFiltersDataSourceImpl(context)

    @Provides
    fun usersDataSource(api: Api): UserDataSource = UserDataSourceImpl(api)

    @Provides
    fun settingsDataSource(api: Api): SettingsDataSource = SettingsDataSourceImpl(api)

    @Provides
    fun translationsDataSource(context: Context): FiltersTranslationsDataSource = FiltersTranslationsDataSourceImpl(context)
}
