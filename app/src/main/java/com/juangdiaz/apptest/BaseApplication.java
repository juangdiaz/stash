package com.juangdiaz.apptest;

import android.app.Application;

/**
 *  @author juandiaz <juan@juangdiaz.com> Android Developer
 */
public class BaseApplication extends Application {

    private AppComponent component;


    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerAppComponent.builder().appModule(new AppModule()).build();

    }

    public AppComponent getComponent() {
        return  component;
    }
}
