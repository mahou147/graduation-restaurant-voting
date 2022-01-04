package com.mahou.bootjava.restaurantvoting;

import com.mahou.bootjava.restaurantvoting.model.Restaurant;

public class RestaurantTestData {
    public static MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER =
            MatcherFactory.usingEqualsComparator(Restaurant.class);

    public static final int EATALY_ID = 1;
    public static final int RAMEN_ID = 2;
    public static final int NOT_FOUND = 1000;
    public static final Restaurant eataly = new Restaurant(EATALY_ID, "Eataly",
            "Kiyevskaya, 2, Moscow, 121059", "84956680679");
    public static final Restaurant ramen = new Restaurant(RAMEN_ID, "Ra`men",
            "Tsvetnoy Blvd 21, b. 7, Moscow, 127051", "89855508152");
}
