package com.noordwind.apps.collectively.presentation.dagger

import android.app.Activity
import android.content.Context
import android.location.LocationManager
import android.net.ConnectivityManager
import com.google.gson.Gson
import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.data.cache.ProfileCache
import com.noordwind.apps.collectively.data.cache.RemarkCategoriesCache
import com.noordwind.apps.collectively.data.cache.UserGroupsCache
import com.noordwind.apps.collectively.data.datasource.*
import com.noordwind.apps.collectively.data.net.Api
import com.noordwind.apps.collectively.data.repository.*
import com.noordwind.apps.collectively.data.repository.util.*
import com.noordwind.apps.collectively.domain.repository.*
import dagger.Module
import dagger.Provides
import pl.charmas.android.reactivelocation.ReactiveLocationProvider
import javax.inject.Singleton

@Module
class RepositoryModule : Constants {

    @Provides
    fun provideAuthenticationDataSource(api: Api): AuthDataSource {
        return AuthDataSourceImpl(api)
    }

    @Provides
    @Singleton
    fun providerUserGroupsCache(context: Context): UserGroupsCache {
        return UserGroupsCache(context.getSharedPreferences("shared_preferences_user_groups", Activity.MODE_PRIVATE), Gson())
    }

    @Provides
    fun providerUserGroupsSource(api: Api): UserGroupsDataSource {
        return UserGroupsDataSourceImpl(api)
    }

    @Provides
    fun providerUserGroupsRepository(dataCache: UserGroupsCache, dataSource: UserGroupsDataSource): UserGroupsRepository {
        return UserGroupsRepositoryImpl(dataSource, dataCache)
    }

    @Provides
    fun provideAuthenticationRepository(authDataSource: AuthDataSource,
                                        profileRepository: ProfileRepository,
                                        mapFiltersRepository: MapFiltersRepository,
                                        userGroupsRepository: UserGroupsRepository,
                                        operationRepository: OperationRepository,
                                        sessionRepository: SessionRepository): AuthenticationRepository {
        return AuthenticationRepositoryImpl(authDataSource, mapFiltersRepository, profileRepository, userGroupsRepository, operationRepository, sessionRepository)
    }

    @Provides
    @Singleton
    fun provideSession(context: Context): Session = Session(context)

    @Provides
    fun provideSessionRepository(session: Session): SessionRepository = SessionRepositoryImpl(session)

    @Provides
    @Singleton
    fun provideRemarkCategoriesCache(context: Context): RemarkCategoriesCache = RemarkCategoriesCache(context.getSharedPreferences("shared_preferences_remark_categories", Activity.MODE_PRIVATE), Gson())

    @Provides
    fun provideRemarksDataSource(api: Api): RemarksDataSource = RemarksDataSourceImpl(api)

    @Provides
    fun provideFileDataSource(context: Context): FileDataSource = FileDataSourceImpl(context)

    @Provides
    fun provideRemarkCategoriesRepository(
            context: Context,
            remarkCategoriesCache: RemarkCategoriesCache,
            remarksDataSource: RemarksDataSource,
            fileDataSource: FileDataSource,
            profileRepository: ProfileRepository,
            locationRepository: LocationRepository,
            mapFiltersRepository: MapFiltersRepository,
            translationsDataSource: FiltersTranslationsDataSource,
            userGroupsRepository: UserGroupsRepository,
            operationRepository: OperationRepository): RemarksRepository

            = RemarkRepositoryImpl(context = context, remarkCategoriesCache = remarkCategoriesCache, remarksDataSource = remarksDataSource,
            fileDataSource = fileDataSource, profileRepository = profileRepository, mapFiltersRepository = mapFiltersRepository,
            userGroupsRepository = userGroupsRepository, operationRepository = operationRepository, translationsDataSource = translationsDataSource,
            locationRepository = locationRepository)

    @Provides
    fun provideReactiveLocationProvider(context: Context): ReactiveLocationProvider = ReactiveLocationProvider(context)

    @Provides
    fun provideLocationRepository(context: Context, reactiveLocationProvider: ReactiveLocationProvider): LocationRepository
            = LocationRepositoryImpl(context.getSystemService(Context.LOCATION_SERVICE) as LocationManager,
            reactiveLocationProvider)

    @Provides
    fun provideOperationDataSource(api: Api): OperationDataSource = OperationDataSourceImpl(api)

    @Provides
    fun provideOperationRepository(operationDataSource: OperationDataSource): OperationRepository = OperationRepositoryImpl(operationDataSource)

    @Provides
    fun provideStatisticsDataSource(api: Api): StatisticsDataSource = StatisticsDataSourceImpl(api)

    @Provides
    fun provideStatisticsRepository(statisticsDataSource: StatisticsDataSource): StatisticsRepository = StatisticsRepositoryImpl(statisticsDataSource)

    @Provides
    fun provideProfileDataSource(api: Api): ProfileDataSource = ProfileDataSourceImpl(api)

    @Provides
    @Singleton
    fun provideProfileCache(context: Context): ProfileCache = ProfileCache(context.getSharedPreferences("shared_preferences_profile", Activity.MODE_PRIVATE), Gson())

    @Provides
    fun provideProfileRepository(profileDataSource: ProfileDataSource, profileCache: ProfileCache): ProfileRepository = ProfileRepositoryImpl(profileDataSource, profileCache)

    @Provides
    fun provideMapFiltersDataSource(context: Context): MapFiltersDataSource = MapFiltersDataSourceImpl(context)

    @Provides
    fun provideMapFiltersRepository(context: Context, filtersDataSource: MapFiltersDataSource): MapFiltersRepository = MapFiltersRepositoryImpl(filtersDataSource, context)

    @Provides
    fun provideRemarkFiltersDataSource(context: Context): RemarkFiltersDataSource = RemarkFiltersDataSourceImpl(context)

    @Provides
    fun provideRemarkFiltersRepository(context: Context, filtersDataSource: RemarkFiltersDataSource): RemarkFiltersRepository = RemarkFiltersRepositoryImpl(filtersDataSource, context)

    @Provides
    fun provideUsersDataSource(api: Api): UserDataSource = UserDataSourceImpl(api)

    @Provides
    fun provideUsersRepository(userDataSource: UserDataSource): UsersRepository = UsersRepositoryImpl(userDataSource)

    @Provides
    fun provideSettingsDataSource(api: Api): SettingsDataSource = SettingsDataSourceImpl(api)

    @Provides
    fun provideSettingsRepository(operationRepository: OperationRepository, settingsDataSource: SettingsDataSource): SettingsRepository = SettingsRepositoryImpl(settingsDataSource, operationRepository)

    @Provides
    fun provideFacebookRepository(): FacebookTokenRepository = FacebookTokenRepositoryImpl()

    @Provides
    fun provideTranslationsDataSource(context: Context): FiltersTranslationsDataSource = FiltersTranslationsDataSourceImpl(context)

    @Provides
    fun provideConnectivityRepository(context: Context): ConnectivityRepository {
        var connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return ConnectivityRepositoryImpl(connectivityManager)
    }

    @Provides
    fun providerNotificationOptionNameRepository(context: Context): NotificationOptionNameRepository {
        return NotificationOptionNameRepositoryImpl(context)
    }

    @Provides
    fun providerBarNotificationRepository(context: Context): BarNotificationRepository {
        return BarNotificationRepositoryImpl(context)
    }
}
