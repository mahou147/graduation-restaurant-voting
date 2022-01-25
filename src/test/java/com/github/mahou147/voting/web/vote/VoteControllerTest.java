package com.github.mahou147.voting.web.vote;

import com.github.mahou147.voting.UserTestData;
import com.github.mahou147.voting.VoteTestData;
import com.github.mahou147.voting.error.NotFoundException;
import com.github.mahou147.voting.model.Vote;
import com.github.mahou147.voting.repository.VoteRepository;
import com.github.mahou147.voting.service.VoteService;
import com.github.mahou147.voting.util.DateTimeUtil;
import com.github.mahou147.voting.util.JsonUtil;
import com.github.mahou147.voting.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.github.mahou147.voting.RestaurantTestData.eataly;
import static com.github.mahou147.voting.RestaurantTestData.ramen;
import static com.github.mahou147.voting.UserTestData.USER_ID;
import static com.github.mahou147.voting.UserTestData.user3;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VoteControllerTest extends AbstractControllerTest {

    private static final String URL = VoteController.URL + '/';

    @Autowired
    private VoteRepository repository;

    @Autowired
    private VoteService service;

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void findByUserId() throws Exception {
        perform(MockMvcRequestBuilders.get(URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(VoteTestData.VOTE_MATCHER.contentJson(VoteTestData.user_vote));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(URL))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> service.findByUserId(USER_ID));
    }

    @Test
    @WithUserDetails(value = UserTestData.USER3_MAIL)
    void createWithLocation() throws Exception {
        Vote newVote = new Vote(null, LocalDate.now(), LocalTime.of(10, 0), user3, eataly);
        ResultActions action = perform(MockMvcRequestBuilders.post(URL)
                .param("restId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newVote)))
                .andDo(print())
                .andExpect(status().isCreated());

        Vote created = VoteTestData.VOTE_MATCHER.readFromJson(action);
        int newId = created.id();
        newVote.setId(newId);
        VoteTestData.VOTE_MATCHER.assertMatch(created, newVote);
        VoteTestData.VOTE_MATCHER.assertMatch(repository.findById(newId).orElseThrow(), newVote);
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void update() throws Exception {
        DateTimeUtil.setClock(VoteTestData.voteRestrictionClock(true));
        Vote updated = VoteTestData.user_vote;
        updated.setRestaurant(ramen);
        perform(MockMvcRequestBuilders.put(URL).contentType(MediaType.APPLICATION_JSON)
                .param("restId", "2")
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());

        VoteTestData.VOTE_MATCHER.assertMatch(repository.getById(VoteTestData.USER_VOTE_ID), updated);
    }
}