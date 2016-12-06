package com.juangdiaz.apptest.presenter;

import android.content.Context;

import com.juangdiaz.apptest.view.UserView;

/**
 * @author juandiaz <juan@juangdiaz.com> Android Developer
 */
public interface UserPresenter {

    void loadUserDetails();
    void setView(Context context, UserView view);
    //TODO: FILL ME UP PLEASE
}
