package com.github.mahou147.voting;

import com.github.mahou147.voting.model.MenuItem;

import java.math.BigDecimal;

import static com.github.mahou147.voting.MenuTestData.*;

public class DishTestData {
    public static final MatcherFactory.Matcher<MenuItem> MENU_ITEM_MATCHER = MatcherFactory
            .usingEqualsComparator(MenuItem.class);
    public static final int MENU_ITEM_1_ID = 1;
    public static final int MENU_ITEM_2_ID = 2;
    public static final int MENU_ITEM_3_ID = 3;
    public static final int MENU_ITEM_4_ID = 4;
    public static final int MENU_ITEM_5_ID = 5;
    public static final int MENU_ITEM_6_ID = 6;
    public static final int MENU_ITEM_7_ID = 7;
    public static final int MENU_ITEM_8_ID = 8;
    public static final int MENU_ITEM_9_ID = 9;
    public static final int MENU_ITEM_10_ID = 10;
    public static final int MENU_ITEM_11_ID = 11;
    public static final int MENU_ITEM_12_ID = 12;
    public static final int MENU_ITEM_13_ID = 13;
    public static final int MENU_ITEM_14_ID = 14;
    public static final int MENU_ITEM_15_ID = 15;

    public static final MenuItem MENU_ITEM_1 = new MenuItem(MENU_ITEM_1_ID, "Capricciosa", new BigDecimal(890));
    public static final MenuItem MENU_ITEM_2 = new MenuItem(MENU_ITEM_2_ID, "Martini Fiero", new BigDecimal(490));
    public static final MenuItem MENU_ITEM_3 = new MenuItem(MENU_ITEM_3_ID, "Espresso", new BigDecimal(90));
    public static final MenuItem MENU_ITEM_4 = new MenuItem(MENU_ITEM_4_ID, "Chinotto", new BigDecimal(90));

    public static final MenuItem MENU_ITEM_5 = new MenuItem(MENU_ITEM_5_ID, "Cinque formaggi", new BigDecimal(790));
    public static final MenuItem MENU_ITEM_6 = new MenuItem(MENU_ITEM_6_ID, "Americano", new BigDecimal(190));
    public static final MenuItem MENU_ITEM_7 = new MenuItem(MENU_ITEM_7_ID, "Negroni", new BigDecimal(590));
    public static final MenuItem MENU_ITEM_8 = new MenuItem(MENU_ITEM_8_ID, "San Pellegrino", new BigDecimal(90));

    public static final MenuItem MENU_ITEM_9 = new MenuItem(MENU_ITEM_9_ID, "Miso siru", new BigDecimal(890));
    public static final MenuItem MENU_ITEM_10 = new MenuItem(MENU_ITEM_10_ID, "Gyoza", new BigDecimal(490));
    public static final MenuItem MENU_ITEM_11 = new MenuItem(MENU_ITEM_11_ID, "Milk Oolong", new BigDecimal(90));
    public static final MenuItem MENU_ITEM_12 = new MenuItem(MENU_ITEM_12_ID, "Tayaki", new BigDecimal(90));

    public static final MenuItem MENU_ITEM_13 = new MenuItem(MENU_ITEM_13_ID, "Takoyaki", new BigDecimal(790));
    public static final MenuItem MENU_ITEM_14 = new MenuItem(MENU_ITEM_14_ID, "Tom yam", new BigDecimal(190));
    public static final MenuItem MENU_ITEM_15 = new MenuItem(MENU_ITEM_15_ID, "Sencha", new BigDecimal(590));

    static {
        MENU_ITEM_1.setMenu(eataly_current_menu);
        MENU_ITEM_2.setMenu(eataly_current_menu);
        MENU_ITEM_3.setMenu(eataly_current_menu);
        MENU_ITEM_4.setMenu(eataly_current_menu);
        MENU_ITEM_5.setMenu(eataly_menu);
        MENU_ITEM_6.setMenu(eataly_menu);
        MENU_ITEM_7.setMenu(eataly_menu);
        MENU_ITEM_8.setMenu(eataly_menu);
        MENU_ITEM_9.setMenu(ramen_current_menu);
        MENU_ITEM_10.setMenu(ramen_current_menu);
        MENU_ITEM_11.setMenu(ramen_current_menu);
        MENU_ITEM_12.setMenu(ramen_current_menu);
        MENU_ITEM_13.setMenu(ramen_menu);
        MENU_ITEM_14.setMenu(ramen_menu);
        MENU_ITEM_15.setMenu(ramen_menu);
    }
}
