package com.mahou.bootjava.restaurantvoting;

import com.mahou.bootjava.restaurantvoting.model.Dish;

import java.math.BigDecimal;

import static com.mahou.bootjava.restaurantvoting.MenuTestData.*;

public class DishTestData {
    public static final MatcherFactory.Matcher<Dish> DISH_MATCHER = MatcherFactory
            .usingEqualsComparator(Dish.class);
    public static final int DISH1_ID = 1;
    public static final int DISH2_ID = 2;
    public static final int DISH3_ID = 3;
    public static final int DISH4_ID = 4;
    public static final int DISH5_ID = 5;
    public static final int DISH6_ID = 6;
    public static final int DISH7_ID = 7;
    public static final int DISH8_ID = 8;
    public static final int DISH9_ID = 9;
    public static final int DISH10_ID = 10;
    public static final int DISH11_ID = 11;
    public static final int DISH12_ID = 12;
    public static final int DISH13_ID = 13;
    public static final int DISH14_ID = 14;
    public static final int DISH15_ID = 15;

    public static final Dish dish1 = new Dish(DISH1_ID, "Capricciosa", new BigDecimal(890));
    public static final Dish dish2 = new Dish(DISH2_ID, "Martini Fiero", new BigDecimal(490));
    public static final Dish dish3 = new Dish(DISH3_ID, "Espresso", new BigDecimal(90));
    public static final Dish dish4 = new Dish(DISH4_ID, "Chinotto", new BigDecimal(90));

    public static final Dish dish5 = new Dish(DISH5_ID, "Cinque formaggi", new BigDecimal(790));
    public static final Dish dish6 = new Dish(DISH6_ID, "Americano", new BigDecimal(190));
    public static final Dish dish7 = new Dish(DISH7_ID, "Negroni", new BigDecimal(590));
    public static final Dish dish8 = new Dish(DISH8_ID, "San Pellegrino", new BigDecimal(90));

    public static final Dish dish9 = new Dish(DISH9_ID, "Miso siru", new BigDecimal(890));
    public static final Dish dish10 = new Dish(DISH10_ID, "Gyoza", new BigDecimal(490));
    public static final Dish dish11 = new Dish(DISH11_ID, "Milk Oolong", new BigDecimal(90));
    public static final Dish dish12 = new Dish(DISH12_ID, "Tayaki", new BigDecimal(90));

    public static final Dish dish13 = new Dish(DISH13_ID, "Takoyaki", new BigDecimal(790));
    public static final Dish dish14 = new Dish(DISH14_ID, "Tom yam", new BigDecimal(190));
    public static final Dish dish15 = new Dish(DISH15_ID, "Sencha", new BigDecimal(590));

    static {
        dish1.setMenu(eataly_current_menu);
        dish2.setMenu(eataly_current_menu);
        dish3.setMenu(eataly_current_menu);
        dish4.setMenu(eataly_current_menu);
        dish5.setMenu(eataly_menu);
        dish6.setMenu(eataly_menu);
        dish7.setMenu(eataly_menu);
        dish8.setMenu(eataly_menu);
        dish9.setMenu(ramen_current_menu);
        dish10.setMenu(ramen_current_menu);
        dish11.setMenu(ramen_current_menu);
        dish12.setMenu(ramen_current_menu);
        dish13.setMenu(ramen_menu);
        dish14.setMenu(ramen_menu);
        dish15.setMenu(ramen_menu);
    }
}
