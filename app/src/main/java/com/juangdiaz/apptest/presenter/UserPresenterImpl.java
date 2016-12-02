package com.juangdiaz.apptest.presenter;

import com.juangdiaz.apptest.model.User;
import com.juangdiaz.apptest.repository.UserRepository;
import com.juangdiaz.apptest.view.UserView;

/**
 * @author juandiaz <juan@juangdiaz.com> Android Developer
 */
public class UserPresenterImpl  implements UserPresenter{
    private UserView view;
    private UserRepository userRepository;
    private User user;


    public UserPresenterImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void loadUserDetails() {

        //Load from contacts

    }

    @Override
    public void setView(UserView view) {
        this.view = view;
        loadUserDetails();
    }

}
