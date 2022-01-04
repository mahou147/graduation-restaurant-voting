package com.mahou.bootjava.restaurantvoting;

import com.mahou.bootjava.restaurantvoting.model.Menu;

import java.time.LocalDate;
import java.util.List;

import static com.mahou.bootjava.restaurantvoting.DishTestData.*;
import static com.mahou.bootjava.restaurantvoting.RestaurantTestData.eataly;
import static com.mahou.bootjava.restaurantvoting.RestaurantTestData.ramen;

public class MenuTestData {
    public static final MatcherFactory.Matcher<Menu> MENU_MATCHER = MatcherFactory.usingEqualsComparator(Menu.class);
    public static final int EATALY_CURRENT_MENU_ID = 1;
    public static final int EATALY_MENU_ID = 2;
    public static final int RAMEN_CURRENT_MENU_ID = 3;
    public static final int RAMEN_MENU_ID = 4;
    public static final int NOT_FOUND = 1000;
    public static final Menu eataly_current_menu = new Menu(EATALY_CURRENT_MENU_ID, LocalDate.now(),
            List.of(dish1, dish2, dish3, dish4));
    public static final Menu eataly_menu = new Menu(EATALY_MENU_ID, LocalDate.now(),
            List.of(dish5, dish6, dish7, dish8));
    public static final Menu ramen_current_menu = new Menu(RAMEN_CURRENT_MENU_ID, LocalDate.now(),
            List.of(dish9, dish10, dish11, dish12));
    public static final Menu ramen_menu = new Menu(RAMEN_MENU_ID, LocalDate.now(),
            List.of(dish13, dish14, dish15));

    static {
        eataly_current_menu.setRestaurant(eataly);
        eataly_menu.setRestaurant(eataly);
        ramen_current_menu.setRestaurant(ramen);
        ramen_menu.setRestaurant(ramen);
    }
}
