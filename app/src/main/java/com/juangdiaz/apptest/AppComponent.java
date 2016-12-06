package com.juangdiaz.apptest;

import com.juangdiaz.apptest.view.activity.MainActivity;

import javax.inject.Singleton;

import dagger.Component;
/**
 * @author juandiaz <juan@juangdiaz.com> Android Developer
 */
@Singleton
@Component(modules = { AppModule.class })
public interface AppComponent {

    void inject(MainActivity target);
}