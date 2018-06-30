package com.softserve.mosquito.configs.Social;//package com.softserve.mosquito.configs;

import com.softserve.mosquito.entities.User;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.web.context.request.NativeWebRequest;

public final class SimpleSignInAdapter implements SignInAdapter {


    public String signIn(String userId, Connection<?> connection, NativeWebRequest request) {
        SecurityContext.setCurrentUser(new User(Long.getLong(userId)));
        return null;
    }

}