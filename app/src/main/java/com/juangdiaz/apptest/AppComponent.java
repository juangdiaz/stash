package com.juangdiaz.apptest;

import javax.inject.Singleton;

import dagger.Component;
/**
 * @author juandiaz <juan@juangdiaz.com> Android Developer
 */


@Singleton
@Component(modules = { AppModule.class })
public interface AppComponent {
   // void inject(someActivity/Fragment target); // fix this
}