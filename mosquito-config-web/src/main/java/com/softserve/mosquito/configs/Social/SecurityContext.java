package com.softserve.mosquito.configs.Social;//package com.softserve.mosquito.configs;


import com.softserve.mosquito.entities.User;

public class SecurityContext {

    private static final ThreadLocal<User> currentUser = new ThreadLocal<>();

    public static User getCurrentUser() {
        User user = currentUser.get();
        if (user == null) {
            throw new IllegalStateException("No user is currently signed in");
        }
        return user;
    }

    public static void setCurrentUser(User user) {
        currentUser.set(user);
    }

    public static boolean userSignedIn() {
        return currentUser.get() != null;
    }

    public static void remove() {
        currentUser.remove();
    }

}