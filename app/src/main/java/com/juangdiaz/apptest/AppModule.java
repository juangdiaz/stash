package com.juangdiaz.apptest;

import com.juangdiaz.apptest.presenter.UserPresenter;
import com.juangdiaz.apptest.presenter.UserPresenterImpl;
import com.juangdiaz.apptest.repository.InSharePrefsUserRepositoryImpl;
import com.juangdiaz.apptest.repository.UserRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author juandiaz <juan@juangdiaz.com> Android Developer
 */
@Module
public class AppModule {
    @Provides @Singleton
    public UserRepository provideUserRepository() {
        return new InSharePrefsUserRepositoryImpl();
    }

    @Provides
    public UserPresenter provideUserPresenter(UserRepository userRepository) {
        return new UserPresenterImpl(userRepository);
    }
}
