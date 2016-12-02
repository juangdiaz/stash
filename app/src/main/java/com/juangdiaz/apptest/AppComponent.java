package com.juangdiaz.apptest;

import com.juangdiaz.apptest.view.fragment.UserFragment;

import javax.inject.Singleton;

import dagger.Component;
/**
 * @author juandiaz <juan@juangdiaz.com> Android Developer
 */


@Singleton
@Component(modules = { AppModule.class })
public interface AppComponent {
    void inject(UserFragment target);
}