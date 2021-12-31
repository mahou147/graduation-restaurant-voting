package com.mahou.bootjava.restaurantvoting.web.json;

import com.mahou.bootjava.restaurantvoting.UserTestData;
import com.mahou.bootjava.restaurantvoting.model.User;
import com.mahou.bootjava.restaurantvoting.util.JsonUtil;
import com.mahou.bootjava.restaurantvoting.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.mahou.bootjava.restaurantvoting.UserTestData.*;
import static com.mahou.bootjava.restaurantvoting.util.JsonUtil.*;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonUtilTest extends AbstractControllerTest {

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
        String json = JsonUtil.writeValue(UserTestData.user);
        System.out.println(json);
        assertThat(json, not(containsString("password")));
        String jsonWithPass = UserTestData.jsonWithPassword(UserTestData.user, "newPass");
        System.out.println(jsonWithPass);
        User user = JsonUtil.readValue(jsonWithPass, User.class);
        assertEquals(user.getPassword(), "newPass");
    }
}
