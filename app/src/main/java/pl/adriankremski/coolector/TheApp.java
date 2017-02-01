package pl.adriankremski.coolector;

import android.app.Application;
import android.content.Context;

import pl.adriankremski.coolector.dagger.AppComponent;
import pl.adriankremski.coolector.dagger.AppModule;
import pl.adriankremski.coolector.dagger.DaggerAppComponent;

public class TheApp extends Application{

    private AppComponent appComponent;

    public static TheApp get(Context context) {
        return (TheApp) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initAppComponent();
    }

    private void initAppComponent() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }


    public AppComponent getAppComponent() {
        return appComponent;
    }
}
