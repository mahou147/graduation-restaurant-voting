package com.github.mahou147.voting.web.menuitem;

import com.github.mahou147.voting.UserTestData;
import com.github.mahou147.voting.model.MenuItem;
import com.github.mahou147.voting.util.JsonUtil;
import com.github.mahou147.voting.web.AbstractControllerTest;
import com.github.mahou147.voting.repository.MenuItemRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;

import static com.github.mahou147.voting.DishTestData.*;
import static com.github.mahou147.voting.MenuTestData.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminMenuItemControllerTest extends AbstractControllerTest {
    public static final String URL = AdminMenuItemController.URL + '/';

    @Autowired
    private MenuItemRepository repository;

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + MENU_ITEM_1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_ITEM_MATCHER.contentJson(MENU_ITEM_1));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getAllByMenuId() throws Exception {
        perform(MockMvcRequestBuilders.get(URL + "by-menu/" + EATALY_CURRENT_MENU_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_ITEM_MATCHER.contentJson(MENU_ITEM_1, MENU_ITEM_2, MENU_ITEM_3, MENU_ITEM_4));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(URL + EATALY_CURRENT_MENU_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        Assertions.assertFalse(repository.findById(UserTestData.USER_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void createWithLocation() throws Exception {
        MenuItem newMenuItem = new MenuItem(null, "Mochaccino", new BigDecimal(190));
        newMenuItem.setMenu(eataly_current_menu);
        ResultActions action = perform(MockMvcRequestBuilders.post(URL)
                .param("menuId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMenuItem)));

        MenuItem created = MENU_ITEM_MATCHER.readFromJson(action);
        int newId = created.id();
        newMenuItem.setId(newId);
        MENU_ITEM_MATCHER.assertMatch(created, newMenuItem);
        MENU_ITEM_MATCHER.assertMatch(repository.findById(newId).orElseThrow(), newMenuItem);
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void update() throws Exception {
        MenuItem updated = MENU_ITEM_1;
        updated.setMenu(eataly_menu);
        updated.setTitle("Super Capricciosa");
        perform(MockMvcRequestBuilders.put(URL + MENU_ITEM_1_ID)
                .param("menuId", "2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        MENU_ITEM_MATCHER.assertMatch(repository.findById(MENU_ITEM_1_ID).orElseThrow(), updated);
    }
}