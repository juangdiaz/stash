package com.juangdiaz.apptest;

import com.juangdiaz.apptest.repository.DatabaseRealm;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author juandiaz <juan@juangdiaz.com> Android Developer
 */
@Module
public class RepositoryModule {

    @Provides
    @Singleton
    public DatabaseRealm provideDatabaseRealm() {
        return new DatabaseRealm();
    }
}
