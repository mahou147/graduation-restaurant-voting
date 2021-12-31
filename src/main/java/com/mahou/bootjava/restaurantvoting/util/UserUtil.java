package com.mahou.bootjava.restaurantvoting.util;

import com.mahou.bootjava.restaurantvoting.model.Role;
import com.mahou.bootjava.restaurantvoting.model.User;
import com.mahou.bootjava.restaurantvoting.to.UserTo;
import lombok.experimental.UtilityClass;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@UtilityClass
public class UserUtil {
    public static final PasswordEncoder PASSWORD_ENCODER = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    public static User createNewFromTo(UserTo userTo) {
        return new User(null, userTo.getEmail().toLowerCase(), userTo.getFirstName(), userTo.getLastName(), userTo.getPassword(), Role.USER);
    }

    public static User updateFromTo(User user, UserTo userTo) {
        user.setFirstName(userTo.getFirstName());
        user.setLastName(userTo.getLastName());
        user.setEmail(userTo.getEmail().toLowerCase());
        user.setPassword(userTo.getPassword());
        return user;
    }

    public static User prepareToSave(User user) {
        user.setPassword(PASSWORD_ENCODER.encode(user.getPassword()));
        user.setEmail(user.getEmail().toLowerCase());
        return user;
    }
}
