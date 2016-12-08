package com.juangdiaz.apptest.repository;

import android.content.Context;

import com.juangdiaz.apptest.Injector;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * @author juandiaz <juandiaz@us.univision.com> Android Developer
 *         Copyright (C) 2016, Univision Communications Inc.
 */
public class DatabaseRealm {
    @Inject
    Context mContext;

    RealmConfiguration realmConfiguration;

    public DatabaseRealm() {
        Injector.getApplicationComponent().inject(this);
    }

    public void setup() {
        if (realmConfiguration == null) {

            Realm.init(mContext);
            realmConfiguration = new RealmConfiguration.Builder()
                    .name("shareplaces.realm")
                    .build();

            //use this to generate a new realm db
            //Realm.deleteRealm(realmConfiguration);

            //Make this realm the default
            Realm.setDefaultConfiguration(realmConfiguration);
        } else {
            throw new IllegalStateException("database already configured");
        }
    }

    public Realm getRealmInstance() {
        return Realm.getDefaultInstance();
    }
    public void close() {
        getRealmInstance().close();
    }
}
