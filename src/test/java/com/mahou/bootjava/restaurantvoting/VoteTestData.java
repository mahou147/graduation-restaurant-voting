package com.mahou.bootjava.restaurantvoting;

import com.mahou.bootjava.restaurantvoting.model.Vote;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.mahou.bootjava.restaurantvoting.RestaurantTestData.eataly;
import static com.mahou.bootjava.restaurantvoting.UserTestData.user;

public class VoteTestData {
    public static final MatcherFactory.Matcher<Vote> VOTE_MATCHER = MatcherFactory.usingEqualsComparator(Vote.class);
    public static final int USER_VOTE_ID = 1;
    public static final Vote user_vote = new Vote(USER_VOTE_ID, LocalDate.now(), LocalTime.of(10, 0));

    static {
        user_vote.setUser(user);
        user_vote.setRestaurant(eataly);
    }
}
