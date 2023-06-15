package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.User;

import java.util.List;

public interface UserDao {

    List<User> findAll();

    User getUserById(int id);


    int findIdByUsername(String username);

    static User findByUsername(String username) {
        return null;
    }

    boolean create(String username, String password);
}
