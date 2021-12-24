package com.mahou.bootjava.restaurantvoting.web.json;

import com.mahou.bootjava.restaurantvoting.model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.mahou.bootjava.restaurantvoting.UserTestData.*;
import static com.mahou.bootjava.restaurantvoting.config.WebSecurityConfig.PASSWORD_ENCODER;
import static com.mahou.bootjava.restaurantvoting.web.json.JsonUtil.*;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

public class JsonUtilTest {
    @Test
    void readWriteValue() {
        String json = writeValue(user);
        System.out.println(json);
        User actual = readValue(json, User.class);
        USER_MATCHER.assertMatch(actual, user);
    }

    @Test
    void readWriteValues() {
        String json = writeValue(users);
        System.out.println(json);
        List<User> actual = readValues(json, User.class);
        USER_MATCHER.assertMatch(actual, users);
    }

    @Test
    void writeOnlyAccess() {
        String json = writeValue(user);
        System.out.println(json);
        assertThat(json, not(containsString("password")));
        String jsonWithPass = jsonWithPassword(user, "newPass");
        System.out.println(jsonWithPass);
        User user = readValue(jsonWithPass, User.class);
        String encodedPassword = user.getPassword();
        Assertions.assertThat(PASSWORD_ENCODER.matches("newPass", encodedPassword)).isTrue();
    }
}
