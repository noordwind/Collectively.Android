package pl.adriankremski.coolector

import android.content.Context
import android.support.multidex.MultiDexApplication
import pl.adriankremski.coolector.dagger.AppComponent
import pl.adriankremski.coolector.dagger.AppModule
import pl.adriankremski.coolector.dagger.DaggerAppComponent

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
    }

    private fun initAppComponent() {
        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }
}
