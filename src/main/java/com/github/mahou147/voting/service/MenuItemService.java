package com.github.mahou147.voting.service;

import com.github.mahou147.voting.model.MenuItem;
import com.github.mahou147.voting.repository.MenuItemRepository;
import com.github.mahou147.voting.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;

    private final MenuRepository menuRepository;

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = "menus", allEntries = true),
                    @CacheEvict(value = "restaurants", allEntries = true),
                    @CacheEvict(value = "menuItems", allEntries = true)
            }
    )
    public void delete(int id) {
        log.info("delete {}", id);
        menuItemRepository.deleteExisted(id);
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = "menus", allEntries = true),
                    @CacheEvict(value = "restaurants", allEntries = true),
                    @CacheEvict(value = "menuItems", allEntries = true)
            }
    )
    public MenuItem create(MenuItem menuItem, Integer menuId) {
        log.info("create for menu {}", menuId);
        Assert.notNull(menuItem, "Menu must not be null");
        setMenu(menuItem, menuId);
        return menuItemRepository.save(menuItem);
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = "menus", allEntries = true),
                    @CacheEvict(value = "restaurants", allEntries = true),
                    @CacheEvict(value = "menuItems", allEntries = true)
            }
    )
    public void update(MenuItem menuItem, int id) {
        log.info("update MenuItem {}", id);
        Assert.notNull(menuItem, "Menu must not be null");
        menuItem.setMenu(menuItemRepository.getById(id).getMenu());
        menuItemRepository.save(menuItem);
    }

    private void setMenu(MenuItem menuItem, Integer menuId) {
        menuItem.setMenu(menuRepository.getById(menuId));
    }
}
