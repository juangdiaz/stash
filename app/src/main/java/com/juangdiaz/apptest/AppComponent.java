package com.juangdiaz.apptest;

import com.juangdiaz.apptest.repository.DatabaseRealm;
import com.juangdiaz.apptest.view.activity.DeepLinkActivity;
import com.juangdiaz.apptest.view.activity.PlacesActivity;
import com.juangdiaz.apptest.view.activity.SentHistoryActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author juandiaz <juan@juangdiaz.com> Android Developer
 */
@Singleton
@Component(modules = {AppModule.class, RepositoryModule.class})
public interface AppComponent {

    void inject(BaseApplication application);
    void inject(DeepLinkActivity activity);
    void inject(PlacesActivity activity);
    void inject(SentHistoryActivity activity);
    void inject(DatabaseRealm databaseRealm);
}
