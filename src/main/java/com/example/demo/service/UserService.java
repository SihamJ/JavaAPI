package com.example.demo.service;

import com.example.demo.api.model.User;

import java.util.Optional;
import java.util.*;


public class UserService {

    private List<User> userList;

    public UserService(){
        userList = new ArrayList<User>();

        User user1 = new User(1, "Isa", 30, "isa@gmail.com");
        User user2 = new User(2, "Yahya", 14, "yahya@gmail.com");

        userList.addAll(Arrays.asList(user1, user2));
    }
    public Optional<User> getUser(Integer id){
        Optional optional = Optional.empty();
        for(User user: userList){
            if(id == user.getId()){
                optional = Optional.of(user);
                return optional;
            }
        }
        return optional;
    }
}
