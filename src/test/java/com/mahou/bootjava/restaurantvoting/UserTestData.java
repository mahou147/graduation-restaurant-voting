package com.mahou.bootjava.restaurantvoting;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mahou.bootjava.restaurantvoting.model.Role;
import com.mahou.bootjava.restaurantvoting.model.User;
import com.mahou.bootjava.restaurantvoting.web.json.JsonUtil;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class UserTestData {
    public static final MatcherFactory.Matcher<User> USER_MATCHER = MatcherFactory.usingEqualsComparator(User.class);
    public static final int USER_ID = 1;
    public static final int ADMIN_ID = 2;
    public static final String USER_MAIL = "user@gmail.com";
    public static final String ADMIN_MAIL = "tori.plaksunova@gmail.com";
    public static final User user = new User(USER_ID, USER_MAIL, "User_First", "User_Last",
            "password", List.of(Role.USER));
    public static final User admin = new User(ADMIN_ID, ADMIN_MAIL, "Viktoria", "Plaksunova",
            "admin", List.of(Role.ADMIN, Role.USER));
    public static final List<User> users = List.of(admin, user);

    public static User getNew() {
        return new User(null, "new@gmail.com", "New_First", "New_Last",
                "newpass", List.of(Role.USER));
    }

    public static User getUpdated() {
        return new User(USER_ID, "user_update@gmail.com", "User_First_Update", "User_Last_Update",
                "password_update", List.of(Role.USER));
    }

    public static User asUser(MvcResult mvcResult) throws UnsupportedEncodingException {
        String jsonActual = mvcResult.getResponse().getContentAsString();
        return JsonUtil.readValue(jsonActual, User.class);
    }

    public static String jsonWithPassword(User user, String passw) {
        return JsonUtil.writeAdditionProps(user, "password", passw);
    }
}
