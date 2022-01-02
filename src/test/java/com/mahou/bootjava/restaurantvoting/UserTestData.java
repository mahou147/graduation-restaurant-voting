package com.mahou.bootjava.restaurantvoting;

import com.mahou.bootjava.restaurantvoting.model.Role;
import com.mahou.bootjava.restaurantvoting.model.User;
import com.mahou.bootjava.restaurantvoting.util.JsonUtil;

import java.util.List;

public class UserTestData {
    public static final MatcherFactory.Matcher<User> USER_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(User.class, "registered", "password");
    public static final int USER_ID = 1;
    public static final int ADMIN_ID = 2;
    public static final int NOT_FOUND = 10;
    public static final String USER_MAIL = "user@gmail.com";
    public static final String ADMIN_MAIL = "tori.plaksunova@gmail.com";
    public static final User user = new User(USER_ID, USER_MAIL, "User_First", "User_Last",
            "password", Role.USER);
    public static final User admin = new User(ADMIN_ID, ADMIN_MAIL, "Viktoria", "Plaksunova",
            "admin", Role.ADMIN, Role.USER);
    public static final List<User> users = List.of(admin, user);

    public static String jsonWithPassword(User user, String passw) {
        return JsonUtil.writeAdditionProps(user, "password", passw);
    }
}
