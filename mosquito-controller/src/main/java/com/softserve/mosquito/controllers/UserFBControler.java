package com.softserve.mosquito.controllers;


import com.softserve.mosquito.dtos.UserDto;
import com.softserve.mosquito.services.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/fb")
public class UserFBControler {
    @Autowired
    private final Facebook facebook;
    @Autowired
    private ConnectionRepository connectionRepository;
    private UserDto userFromFb;
    private UserService userService;

    public UserFBControler(Facebook facebook, ConnectionRepository connectionRepository) {
        this.facebook = facebook;
        this.connectionRepository = connectionRepository;
    }
    @GetMapping(path = "/create")
    @ResponseStatus(HttpStatus.OK)
    public UserDto createUserFb() {
        if (connectionRepository.findPrimaryConnection(Facebook.class) == null) {
            return null;
        }

        userFromFb = UserDto.newBuilder().id(Long.getLong(facebook.userOperations().getUserProfile().getId()))
                .email(facebook.userOperations().getUserProfile().getEmail())
                .firstName(facebook.userOperations().getUserProfile().getFirstName())
                .lastName(facebook.userOperations().getUserProfile().getLastName()).build();

        return userFromFb;
    }
    @GetMapping(path = "/login")
    @ResponseStatus(HttpStatus.OK)
    public UserDto userFromFb() {
        if (connectionRepository.findPrimaryConnection(Facebook.class) == null) {
            return null;
        }

        userFromFb = UserDto.newBuilder().id(Long.getLong(facebook.userOperations().getUserProfile().getId()))
                .email(facebook.userOperations().getUserProfile().getEmail()).build();

        return userFromFb;
    }
}