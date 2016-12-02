package com.juangdiaz.apptest.repository;

import com.juangdiaz.apptest.model.User;

/**
 * @author juandiaz <juan@juangdiaz.com> Android Developer
 */
public interface UserRepository {
    User getUser(int id);
}
