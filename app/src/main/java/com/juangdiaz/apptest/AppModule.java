package com.juangdiaz.apptest;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author juandiaz <juan@juangdiaz.com> Android Developer
 */
@Module
public class AppModule{


        private final BaseApplication application;

        public AppModule(BaseApplication application) {
            this.application = application;
        }

        @Provides
        @Singleton
        public BaseApplication application() {
            return application;
        }

        @Provides
        @Singleton
        public Context applicationContext() {
            return application.getApplicationContext();
        }

    /*    @Provides
        @Singleton
        public PreferenceService providePreferenceService() {
            return new PreferenceServiceImpl(application);
        }*/
}
