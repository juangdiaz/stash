package com.juangdiaz.apptest;

import android.app.Application;

import com.github.tamir7.contacts.Contacts;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 *  @author juandiaz <juan@juangdiaz.com> Android Developer
 */
public class BaseApplication extends Application {

    private AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        //Realm
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name("shareplaces.realm")
                .build();

        //use this to generate a new realm db
        //Realm.deleteRealm(realmConfiguration);

        //Make this realm the default
        Realm.setDefaultConfiguration(realmConfiguration);

        component = DaggerAppComponent.builder().appModule(new AppModule()).build();

        Contacts.initialize(this);

    }

    public AppComponent getComponent() {

        return  component;
    }
}
