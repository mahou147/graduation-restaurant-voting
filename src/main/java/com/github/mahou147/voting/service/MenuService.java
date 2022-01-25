package com.github.mahou147.voting.service;

import com.github.mahou147.voting.model.Menu;
import com.github.mahou147.voting.repository.MenuRepository;
import com.github.mahou147.voting.repository.RestaurantRepository;
import com.github.mahou147.voting.util.ErrorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    private final RestaurantRepository restaurantRepository;

    public Menu getWithDishes(int id) {
        log.info("get {}", id);
        return menuRepository.getWithDishes(id).orElseThrow(ErrorUtil.notFound(Menu.class, id));
    }

    public Page<Menu> getAllWithDishesByRestaurantId(int pageIndex, int pageSize, int id) {
        log.info("getAll");
        return menuRepository.findAllByRestaurantId(PageRequest.of(pageIndex, pageSize), id);
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = "menus", allEntries = true),
                    @CacheEvict(value = "restaurants", allEntries = true),
            }
    )
    public void deleteById(int id) {
        log.info("delete {}", id);
        menuRepository.deleteExisted(id);
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = "menus", allEntries = true),
                    @CacheEvict(value = "restaurants", allEntries = true),
            }
    )
    public Menu create(Menu menu, Integer restId) {
        log.info("create for restaurant {}", restId);
        Assert.notNull(menu, "Menu must not be null");
        setRestaurant(menu, restId);
        return menuRepository.save(menu);
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = "menus", allEntries = true),
                    @CacheEvict(value = "restaurants", allEntries = true),
            }
    )
    public void update(Menu menu, int id, Integer restId) {
        log.info("update {}", id);
        Assert.notNull(menu, "Menu must not be null");
        setRestaurant(menu, restId);
        menuRepository.save(menu);
    }

    private void setRestaurant(Menu menu, Integer restId) {
        menu.setRestaurant(restaurantRepository.getById(restId));
    }
}
