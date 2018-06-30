package com.softserve.mosquito.configs.Social;//package com.softserve.mosquito.configs;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;

import java.util.concurrent.atomic.AtomicLong;

public class SimpleConnectionSignUp implements ConnectionSignUp {

    private final AtomicLong userIdSequence = new AtomicLong();

    public String execute(Connection<?> connection) {
        return Long.toString(userIdSequence.incrementAndGet());
    }
}
