package com.github.mahou147.voting.web.restaurant;

import com.github.mahou147.voting.UserTestData;
import com.github.mahou147.voting.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.github.mahou147.voting.RestaurantTestData.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RestaurantControllerTest extends AbstractControllerTest {
    private static final String URL = RestaurantController.URL + '/';

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void getWithMenuOfTheDay() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + EATALY_ID + "/menu-of-the-day"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(eataly));
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + NOT_FOUND + "/menu-of-the-day"))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + EATALY_ID + "/menu-of-the-day"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void getAllWithMenuOfTheDay() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + "/menu-of-the-day"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(eataly, ramen));
    }
}