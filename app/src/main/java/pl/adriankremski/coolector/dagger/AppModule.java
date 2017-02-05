package pl.adriankremski.coolector.dagger;

import android.app.Application;
import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import pl.adriankremski.coolector.BuildConfig;
import pl.adriankremski.coolector.Constants;
import pl.adriankremski.coolector.network.Api;
import pl.adriankremski.coolector.repository.AuthenticationRepository;
import pl.adriankremski.coolector.repository.AuthenticationRepositoryImpl;
import pl.adriankremski.coolector.repository.Session;
import pl.adriankremski.coolector.repository.SessionRepository;
import pl.adriankremski.coolector.repository.SessionRepositoryImpl;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule implements Constants {
    private Application application;

    private static final String TAG = AppModule.class.getName();
    private static final long MAX_CACHE_SIZE = 20 * 1024 * 1024; // 20MB cache


    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Application context) {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        try {
//            Cache responseCache = new Cache(context.getCacheDir(), MAX_CACHE_SIZE);
//            builder.cache(responseCache);
//        } catch (Exception e) {
//            Log.d(TAG, "Unable to set http cache", e);
//        }
        builder.readTimeout(10, TimeUnit.SECONDS);
        builder.connectTimeout(30, TimeUnit.SECONDS);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override public void log(String message) {
                Log.i("OkHttp", message);
            }
        });
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        builder.addInterceptor(logging);

        return builder.build();
    }


    @Provides
    @Singleton
    public Application provideApplication() {
        return application;
    }

    @Provides
    @Singleton
    protected Api provideApi(OkHttpClient client) {
        Retrofit builder = new Retrofit.Builder()
                .client(client)
                .baseUrl(BuildConfig.SERVER_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return builder.create(Api.class);
    }

    @Provides
    @Singleton
    public AuthenticationRepository provideAuthenticationRepository() {
        return new AuthenticationRepositoryImpl(application.getBaseContext());
    }

    @Provides
    @Singleton
    public Session provideSession() {
        return new Session(application.getBaseContext());
    }

    @Provides
    @Singleton
    public SessionRepository provideSessionRepository() {
        return new SessionRepositoryImpl(application.getBaseContext());
    }
}
