package com.juangdiaz.apptest.presenter;

import com.juangdiaz.apptest.repository.UserRepository;

/**
 * @author juandiaz <juan@juangdiaz.com> Android Developer
 */
public class UserPresenterImpl  implements UserPresenter{
    //TODO: FILL ME UP PLEASE

    private UserRepository userRepository;

    public UserPresenterImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void loadUserDetails() {

    }

}
