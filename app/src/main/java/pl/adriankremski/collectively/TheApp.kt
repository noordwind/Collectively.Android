package pl.adriankremski.collectively

import android.content.Context
import android.support.multidex.MultiDexApplication
import com.facebook.FacebookSdk
import jonathanfinerty.once.Once
import pl.adriankremski.collectively.presentation.dagger.AppComponent
import pl.adriankremski.collectively.presentation.dagger.AppModule
import pl.adriankremski.collectively.presentation.dagger.DaggerAppComponent

class TheApp : MultiDexApplication() {

    companion object {
        operator fun get(context: Context): TheApp {
            return context.applicationContext as TheApp
        }
    }

    var appComponent: AppComponent? = null

    override fun onCreate() {
        super.onCreate()
        initAppComponent()
        FacebookSdk.sdkInitialize(applicationContext)
        Once.initialise(this);
    }

    private fun initAppComponent() {
        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }
}
