package com.noordwind.apps.collectively.presentation.dagger

import android.content.Context
import android.location.LocationManager
import android.net.ConnectivityManager
import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.data.cache.ProfileCache
import com.noordwind.apps.collectively.data.cache.RemarkCategoriesCache
import com.noordwind.apps.collectively.data.cache.UserGroupsCache
import com.noordwind.apps.collectively.data.datasource.*
import com.noordwind.apps.collectively.data.repository.*
import com.noordwind.apps.collectively.data.repository.util.*
import com.noordwind.apps.collectively.domain.repository.*
import dagger.Module
import dagger.Provides
import pl.charmas.android.reactivelocation.ReactiveLocationProvider

@Module
class RepositoryModule : Constants {

    @Provides
    fun userGroupsRepository(dataCache: UserGroupsCache, dataSource: UserGroupsDataSource): UserGroupsRepository = UserGroupsRepositoryImpl(dataSource, dataCache)

    @Provides
    fun authenticationRepository(authDataSource: AuthDataSource,
                                 profileRepository: ProfileRepository,
                                 mapFiltersRepository: MapFiltersRepository,
                                 userGroupsRepository: UserGroupsRepository,
                                 operationRepository: OperationRepository,
                                 sessionRepository: SessionRepository): AuthenticationRepository {
        return AuthenticationRepositoryImpl(authDataSource, mapFiltersRepository, profileRepository, userGroupsRepository, operationRepository, sessionRepository)
    }

    @Provides
    fun sessionRepository(session: Session): SessionRepository = SessionRepositoryImpl(session)

    @Provides
    fun remarkCategoriesRepository(
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
    fun reactiveLocationProvider(context: Context): ReactiveLocationProvider = ReactiveLocationProvider(context)

    @Provides
    fun locationRepository(context: Context, reactiveLocationProvider: ReactiveLocationProvider): LocationRepository
            = LocationRepositoryImpl(context.getSystemService(Context.LOCATION_SERVICE) as LocationManager,
            reactiveLocationProvider)

    @Provides
    fun operationRepository(operationDataSource: OperationDataSource): OperationRepository = OperationRepositoryImpl(operationDataSource)

    @Provides
    fun statisticsRepository(statisticsDataSource: StatisticsDataSource): StatisticsRepository = StatisticsRepositoryImpl(statisticsDataSource)

    @Provides
    fun profileRepository(profileDataSource: ProfileDataSource, profileCache: ProfileCache): ProfileRepository = ProfileRepositoryImpl(profileDataSource, profileCache)

    @Provides
    fun mapFiltersRepository(context: Context, filtersDataSource: MapFiltersDataSource): MapFiltersRepository = MapFiltersRepositoryImpl(filtersDataSource, context)

    @Provides
    fun remarkFiltersRepository(context: Context, filtersDataSource: RemarkFiltersDataSource): RemarkFiltersRepository = RemarkFiltersRepositoryImpl(filtersDataSource, context)

    @Provides
    fun usersRepository(userDataSource: UserDataSource): UsersRepository = UsersRepositoryImpl(userDataSource)

    @Provides
    fun settingsRepository(operationRepository: OperationRepository, settingsDataSource: SettingsDataSource): SettingsRepository = SettingsRepositoryImpl(settingsDataSource, operationRepository)

    @Provides
    fun facebookRepository(): FacebookTokenRepository = FacebookTokenRepositoryImpl()

    @Provides
    fun connectivityRepository(context: Context): ConnectivityRepository = ConnectivityRepositoryImpl(context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)

    @Provides
    fun notificationOptionNameRepository(context: Context): NotificationOptionNameRepository = NotificationOptionNameRepositoryImpl(context)

    @Provides
    fun barNotificationRepository(context: Context): BarNotificationRepository = BarNotificationRepositoryImpl(context)
}
