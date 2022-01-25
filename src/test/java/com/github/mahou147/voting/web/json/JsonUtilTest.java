package com.github.mahou147.voting.web.json;

import com.github.mahou147.voting.util.JsonUtil;
import com.github.mahou147.voting.UserTestData;
import com.github.mahou147.voting.model.User;
import com.github.mahou147.voting.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github.mahou147.voting.UserTestData.*;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonUtilTest extends AbstractControllerTest {

    @Test
    void readWriteValue() {
        String json = JsonUtil.writeValue(user);
        System.out.println(json);
        User actual = JsonUtil.readValue(json, User.class);
        USER_MATCHER.assertMatch(actual, user);
    }

    @Test
    void readWriteValues() {
        String json = JsonUtil.writeValue(users);
        System.out.println(json);
        List<User> actual = JsonUtil.readValues(json, User.class);
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
