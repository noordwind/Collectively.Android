package com.noordwind.apps.collectively.presentation.dagger

import android.app.Activity
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.util.Log
import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.noordwind.apps.collectively.BuildConfig
import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.data.cache.ProfileCache
import com.noordwind.apps.collectively.data.cache.RemarkCategoriesCache
import com.noordwind.apps.collectively.data.cache.UserGroupsCache
import com.noordwind.apps.collectively.data.datasource.*
import com.noordwind.apps.collectively.data.net.Api
import com.noordwind.apps.collectively.data.repository.*
import com.noordwind.apps.collectively.data.repository.util.*
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import com.noordwind.apps.collectively.presentation.IOThread
import com.noordwind.apps.collectively.presentation.UIThread
import dagger.Module
import dagger.Provides
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import pl.charmas.android.reactivelocation.ReactiveLocationProvider
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.InetAddress
import java.net.Socket
import java.net.UnknownHostException
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocket
import javax.net.ssl.SSLSocketFactory

@Module
class AppModule(private val application: Application) : Constants {

    @Provides
    @Singleton
    internal fun provideOkHttpClient(sessionRepository: SessionRepository): OkHttpClient {
        val builder = enableTls12OnPreLollipop(OkHttpClient.Builder())
        builder.writeTimeout(30, TimeUnit.SECONDS)
        builder.readTimeout(30, TimeUnit.SECONDS)
        builder.connectTimeout(30, TimeUnit.SECONDS)

        var sessionInterceptor = object : Interceptor {
            override fun intercept(chain: Interceptor.Chain?): Response {
                if (sessionRepository.isLoggedIn) {
                    var request = chain?.request()?.newBuilder()?.addHeader("Authorization", "Bearer " + sessionRepository.sessionToken)?.build()
                    return chain!!.proceed(request)
                } else {
                    return chain!!.proceed(chain.request())
                }
            }
        }

        builder.addInterceptor(sessionInterceptor)

        val logging = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message -> Log.i("OkHttp", message) })
        logging.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(logging)

        return builder.build()
    }

    fun enableTls12OnPreLollipop(client: OkHttpClient.Builder): OkHttpClient.Builder {
        if (Build.VERSION.SDK_INT in 16..21) {
            try {
                val sc = SSLContext.getInstance("TLSv1.2")
                sc.init(null, null, null)
                client.sslSocketFactory(Tls12SocketFactory(sc.socketFactory))

                val cs = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                        .tlsVersions(TlsVersion.TLS_1_2)
                        .build()

                val specs = LinkedList<ConnectionSpec>()
                specs.add(cs)
                specs.add(ConnectionSpec.COMPATIBLE_TLS)
                specs.add(ConnectionSpec.CLEARTEXT)

                client.connectionSpecs(specs)
            } catch (exc: Exception) {
                Log.e("OkHttpTLSCompat", "Error while setting TLS 1.2", exc)
            }
        }

        return client
    }


    @Provides
    @Singleton
    fun provideApplication(): Application = application

    @Provides
    @Singleton
    protected fun provideApi(client: OkHttpClient): Api {
        val builder = Retrofit.Builder()
                .client(client)
                .baseUrl(BuildConfig.SERVER_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        return builder.create(Api::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthenticationDataSource(api: Api): AuthDataSource {
        return AuthDataSourceImpl(api)
    }

    @Provides
    @Singleton
    fun providerUserGroupsCache(): UserGroupsCache {
        return UserGroupsCache(application.getSharedPreferences("shared_preferences_user_groups", Activity.MODE_PRIVATE), Gson())
    }

    @Provides
    @Singleton
    fun providerUserGroupsSource(api: Api): UserGroupsDataSource {
        return UserGroupsDataSourceImpl(api)
    }

    @Provides
    @Singleton
    fun providerUserGroupsRepository(dataCache: UserGroupsCache, dataSource: UserGroupsDataSource): UserGroupsRepository {
        return UserGroupsRepositoryImpl(dataSource, dataCache)
    }

    @Provides
    @Singleton
    fun provideAuthenticationRepository(authDataSource: AuthDataSource,
                                        profileRepository: ProfileRepository,
                                        userGroupsRepository: UserGroupsRepository,
                                        operationRepository: OperationRepository,
                                        sessionRepository: SessionRepository): AuthenticationRepository {
        return AuthenticationRepositoryImpl(authDataSource, profileRepository, userGroupsRepository, operationRepository, sessionRepository)
    }

    @Provides
    @Singleton
    fun provideSession(): Session = Session(application.baseContext)

    @Provides
    @Singleton
    fun provideSessionRepository(session: Session): SessionRepository = SessionRepositoryImpl(session)

    @Provides
    @Singleton
    fun provideRemarkCategoriesCache(): RemarkCategoriesCache = RemarkCategoriesCache(application.getSharedPreferences("shared_preferences_remark_categories", Activity.MODE_PRIVATE), Gson())

    @Provides
    @Singleton
    fun provideRemarksDataSource(api: Api): RemarksDataSource = RemarksDataSourceImpl(api)

    @Provides
    @Singleton
    fun provideFileDataSource(): FileDataSource = FileDataSourceImpl(application.applicationContext)

    @Provides
    @Singleton
    fun provideRemarkCategoriesRepository(remarkCategoriesCache: RemarkCategoriesCache, remarksDataSource: RemarksDataSource,
                                          fileDataSource: FileDataSource, profileRepository: ProfileRepository,
                                          mapFiltersRepository: MapFiltersRepository, translationsDataSource: FiltersTranslationsDataSource, userGroupsRepository: UserGroupsRepository, operationRepository: OperationRepository): RemarksRepository
            = RemarkRepositoryImpl(context = application.baseContext, remarkCategoriesCache = remarkCategoriesCache, remarksDataSource = remarksDataSource,
            fileDataSource = fileDataSource, profileRepository = profileRepository, mapFiltersRepository = mapFiltersRepository,
            userGroupsRepository = userGroupsRepository, operationRepository = operationRepository, translationsDataSource = translationsDataSource)

    @Provides
    @Singleton
    fun provideReactiveLocationProvider(): ReactiveLocationProvider = ReactiveLocationProvider(application.applicationContext)

    @Provides
    @Singleton
    fun provideLocationRepository(reactiveLocationProvider: ReactiveLocationProvider): LocationRepository = LocationRepositoryImpl(reactiveLocationProvider)

    @Provides
    @Singleton
    fun provideOperationDataSource(api: Api): OperationDataSource = OperationDataSourceImpl(api)

    @Provides
    @Singleton
    fun provideOperationRepository(operationDataSource: OperationDataSource): OperationRepository = OperationRepositoryImpl(operationDataSource)

    @Provides
    @Singleton
    fun provideStatisticsDataSource(api: Api): StatisticsDataSource = StatisticsDataSourceImpl(api)

    @Provides
    @Singleton
    fun provideStatisticsRepository(statisticsDataSource: StatisticsDataSource): StatisticsRepository = StatisticsRepositoryImpl(statisticsDataSource)

    @Provides
    @Singleton
    fun provideProfileDataSource(api: Api): ProfileDataSource = ProfileDataSourceImpl(api)

    @Provides
    @Singleton
    fun provideProfileCache(): ProfileCache = ProfileCache(application.getSharedPreferences("shared_preferences_profile", Activity.MODE_PRIVATE), Gson())

    @Provides
    @Singleton
    fun provideProfileRepository(profileDataSource: ProfileDataSource, profileCache: ProfileCache): ProfileRepository = ProfileRepositoryImpl(profileDataSource, profileCache)

    @Provides
    @Singleton
    fun provideMapFiltersDataSource(): MapFiltersDataSource = MapFiltersDataSourceImpl(application.applicationContext)

    @Provides
    @Singleton
    fun provideMapFiltersRepository(filtersDataSource: MapFiltersDataSource): MapFiltersRepository = MapFiltersRepositoryImpl(filtersDataSource, application.applicationContext)

    @Provides
    @Singleton
    fun provideRemarkFiltersDataSource(): RemarkFiltersDataSource = RemarkFiltersDataSourceImpl(application.applicationContext)

    @Provides
    @Singleton
    fun provideRemarkFiltersRepository(filtersDataSource: RemarkFiltersDataSource): RemarkFiltersRepository = RemarkFiltersRepositoryImpl(filtersDataSource, application.applicationContext)

    @Provides
    @Singleton
    fun provideUsersDataSource(api: Api): UserDataSource = UserDataSourceImpl(api)

    @Provides
    @Singleton
    fun provideUsersRepository(userDataSource: UserDataSource): UsersRepository = UsersRepositoryImpl(userDataSource)

    @Provides
    @Singleton
    fun provideSettingsDataSource(api: Api): SettingsDataSource = SettingsDataSourceImpl(api)

    @Provides
    @Singleton
    fun provideSettingsRepository(operationRepository: OperationRepository, settingsDataSource: SettingsDataSource): SettingsRepository = SettingsRepositoryImpl(settingsDataSource, operationRepository)

    @Provides
    @Singleton
    fun provideFacebookRepository(): FacebookTokenRepository = FacebookTokenRepositoryImpl()

    @Provides
    @Singleton
    fun provideTranslationsDataSource(): FiltersTranslationsDataSource = FiltersTranslationsDataSourceImpl(application.applicationContext)

    @Provides
    @Singleton
    fun provideConnectivityRepository(): ConnectivityRepository {
        var connectivityManager = application.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return ConnectivityRepositoryImpl(connectivityManager)
    }

    @Provides
    @Singleton
    fun providerNotificationOptionNameRepository(): NotificationOptionNameRepository {
        return NotificationOptionNameRepositoryImpl(application.applicationContext)
    }

    @Provides
    @Singleton
    fun useCaseThread(): UseCaseThread = IOThread()

    @Provides
    @Singleton
    fun postExecutionThread(): PostExecutionThread = UIThread()

    class Tls12SocketFactory(internal val delegate: SSLSocketFactory) : SSLSocketFactory() {
        private val TLS_V12_ONLY = arrayOf("TLSv1.2")

        override fun getDefaultCipherSuites(): Array<String> {
            return delegate.defaultCipherSuites
        }

        override fun getSupportedCipherSuites(): Array<String> {
            return delegate.supportedCipherSuites
        }

        @Throws(IOException::class)
        override fun createSocket(s: Socket, host: String, port: Int, autoClose: Boolean): Socket {
            return patch(delegate.createSocket(s, host, port, autoClose))
        }

        @Throws(IOException::class, UnknownHostException::class)
        override fun createSocket(host: String, port: Int): Socket {
            return patch(delegate.createSocket(host, port))
        }

        @Throws(IOException::class, UnknownHostException::class)
        override fun createSocket(host: String, port: Int, localHost: InetAddress, localPort: Int): Socket {
            return patch(delegate.createSocket(host, port, localHost, localPort))
        }

        @Throws(IOException::class)
        override fun createSocket(host: InetAddress, port: Int): Socket {
            return patch(delegate.createSocket(host, port))
        }

        @Throws(IOException::class)
        override fun createSocket(address: InetAddress, port: Int, localAddress: InetAddress, localPort: Int): Socket {
            return patch(delegate.createSocket(address, port, localAddress, localPort))
        }

        private fun patch(s: Socket): Socket {
            if (s is SSLSocket) {
                s.enabledProtocols = TLS_V12_ONLY
            }
            return s
        }
    }

}
