package com.juangdiaz.apptest.view;

import com.juangdiaz.apptest.model.User;

import java.util.List;

/**
 * @author juandiaz <juan@juangdiaz.com> Android Developer
 */
public interface UserView {

    void displayUsers(List<User> userList);
    void showLoadingLayout();
    void hideLoadingLayout();
}
