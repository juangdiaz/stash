package com.juangdiaz.apptest;

import android.app.Application;

import com.github.tamir7.contacts.Contacts;
import com.juangdiaz.apptest.repository.DatabaseRealm;

import javax.inject.Inject;

/**
 *  @author juandiaz <juan@juangdiaz.com> Android Developer
 */
public class BaseApplication extends Application {

    @Inject
    DatabaseRealm databaseRealm;

    @Override
    public void onCreate() {
        super.onCreate();

        //Init Contacts Lib
        Contacts.initialize(this);

        initDagger();
        initRealm();

    }




    protected void initDagger() {
        Injector.initializeApplicationComponent(this);
        Injector.getApplicationComponent().inject(this);
    }

    protected void initRealm() {
        databaseRealm.setup();
    }


}
