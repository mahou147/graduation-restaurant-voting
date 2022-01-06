package com.mahou.bootjava.restaurantvoting.web.vote;

import com.mahou.bootjava.restaurantvoting.AuthUser;
import com.mahou.bootjava.restaurantvoting.error.NotFoundException;
import com.mahou.bootjava.restaurantvoting.model.Vote;
import com.mahou.bootjava.restaurantvoting.repository.VoteRepository;
import com.mahou.bootjava.restaurantvoting.service.VoteService;
import com.mahou.bootjava.restaurantvoting.util.JsonUtil;
import com.mahou.bootjava.restaurantvoting.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.mahou.bootjava.restaurantvoting.RestaurantTestData.eataly;
import static com.mahou.bootjava.restaurantvoting.RestaurantTestData.ramen;
import static com.mahou.bootjava.restaurantvoting.UserTestData.*;
import static com.mahou.bootjava.restaurantvoting.VoteTestData.*;
import static com.mahou.bootjava.restaurantvoting.util.JsonUtil.*;
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
    @WithUserDetails(value = USER_MAIL)
    void findByUserId() throws Exception {
        perform(MockMvcRequestBuilders.get(URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(VOTE_MATCHER.contentJson(user_vote));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(URL))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> service.findByAuthUser(new AuthUser(user)));
    }

    @Test
    @WithUserDetails(value = USER3_MAIL)
    void createWithLocation() throws Exception {
        Vote newVote = new Vote(null, LocalDate.now(), LocalTime.of(10, 0));
        newVote.setRestaurant(eataly);
        ResultActions action = perform(MockMvcRequestBuilders.post(URL)
                .param("restId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(newVote)))
                .andDo(print())
                .andExpect(status().isCreated());

        Vote created = VOTE_MATCHER.readFromJson(action);
        int newId = created.id();
        newVote.setId(newId);
        VOTE_MATCHER.assertMatch(created, newVote);
        VOTE_MATCHER.assertMatch(repository.findById(newId).orElseThrow(), newVote);
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void update() throws Exception {
        Vote updated = user_vote;
        updated.setRestaurant(ramen);
        perform(MockMvcRequestBuilders.put(URL).contentType(MediaType.APPLICATION_JSON)
                .param("restId", "2")
                .content(writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());

        VOTE_MATCHER.assertMatch(repository.getById(USER_VOTE_ID), updated);
    }
}