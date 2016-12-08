package com.juangdiaz.apptest;

import android.os.Build;

import java.util.Objects;

/**
 * @author juandiaz <juan@juangdiaz.com> Android Developer
 */
public class Injector {

    private static AppComponent applicationComponent;

    private Injector() {}

    public static void initializeApplicationComponent(BaseApplication baseApplication) {
        applicationComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(baseApplication))
                .repositoryModule(new RepositoryModule())
                .build();
    }

    public static AppComponent getApplicationComponent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(applicationComponent, "applicationComponent is null");
        }
        return applicationComponent;
    }

}
