package com.github.mahou147.voting;

import com.github.mahou147.voting.model.Menu;
import com.github.mahou147.voting.util.DateTimeUtil;

import java.time.LocalDate;
import java.util.List;

import static com.github.mahou147.voting.RestaurantTestData.eataly;
import static com.github.mahou147.voting.RestaurantTestData.ramen;

public class MenuTestData {
    public static final LocalDate TOMORROW = DateTimeUtil.setCurrentDate().plusDays(1);
    public static final MatcherFactory.Matcher<Menu> MENU_MATCHER = MatcherFactory.usingEqualsComparator(Menu.class);
    public static final int EATALY_CURRENT_MENU_ID = 1;
    public static final int EATALY_MENU_ID = 2;
    public static final int RAMEN_CURRENT_MENU_ID = 3;
    public static final int RAMEN_MENU_ID = 4;
    public static final int NOT_FOUND = 1000;
    public static final Menu eataly_current_menu = new Menu(EATALY_CURRENT_MENU_ID, LocalDate.now(),
            List.of(DishTestData.MENU_ITEM_1, DishTestData.MENU_ITEM_2, DishTestData.MENU_ITEM_3, DishTestData.MENU_ITEM_4));
    public static final Menu eataly_menu = new Menu(EATALY_MENU_ID, LocalDate.now(),
            List.of(DishTestData.MENU_ITEM_5, DishTestData.MENU_ITEM_6, DishTestData.MENU_ITEM_7, DishTestData.MENU_ITEM_8));
    public static final Menu ramen_current_menu = new Menu(RAMEN_CURRENT_MENU_ID, LocalDate.now(),
            List.of(DishTestData.MENU_ITEM_9, DishTestData.MENU_ITEM_10, DishTestData.MENU_ITEM_11, DishTestData.MENU_ITEM_12));
    public static final Menu ramen_menu = new Menu(RAMEN_MENU_ID, LocalDate.now(),
            List.of(DishTestData.MENU_ITEM_13, DishTestData.MENU_ITEM_14, DishTestData.MENU_ITEM_15));

    static {
        eataly_current_menu.setRestaurant(eataly);
        eataly_menu.setRestaurant(eataly);
        ramen_current_menu.setRestaurant(ramen);
        ramen_menu.setRestaurant(ramen);
    }
}
