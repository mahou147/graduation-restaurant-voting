package com.github.mahou147.voting;

import com.github.mahou147.voting.model.Vote;
import com.github.mahou147.voting.util.DateTimeUtil;
import com.github.mahou147.voting.util.ValidationUtil;

import java.time.*;

import static com.github.mahou147.voting.RestaurantTestData.eataly;
import static com.github.mahou147.voting.UserTestData.user;

public class VoteTestData {
    public static final MatcherFactory.Matcher<Vote> VOTE_MATCHER = MatcherFactory.usingEqualsComparator(Vote.class);
    public static final int USER_VOTE_ID = 1;
    public static final Vote user_vote = new Vote(USER_VOTE_ID, LocalDate.now(), LocalTime.of(10, 0), user, eataly);

    public static Clock voteRestrictionClock(boolean before) {
        LocalTime time = ValidationUtil.VOTING_RESTRICTION.minusMinutes(before ? 1 : 0);
        Instant instant = LocalDateTime.of(DateTimeUtil.setCurrentDate(), time).toInstant(OffsetDateTime.now().getOffset());
        return Clock.fixed(instant, ZoneId.systemDefault());
    }
}
