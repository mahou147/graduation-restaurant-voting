package com.mahou.bootjava.restaurantvoting.service;

import com.mahou.bootjava.restaurantvoting.error.NotFoundException;
import com.mahou.bootjava.restaurantvoting.model.Menu;
import com.mahou.bootjava.restaurantvoting.repository.MenuRepository;
import com.mahou.bootjava.restaurantvoting.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import static com.mahou.bootjava.restaurantvoting.util.ValidationUtil.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    private final RestaurantRepository restaurantRepository;

    public Menu getWithDishes(int id) {
        log.info("get {}", id);
        return menuRepository.getWithDishes(id).orElseThrow(() -> new NotFoundException("Menu not found, id: " + id));
    }

    @Cacheable("menus")
    public Page<Menu> getAllWithDishesByRestaurantId(int pageIndex, int pageSize, int id) {
        log.info("getAll");
        return menuRepository.findAllByRestaurantId(PageRequest.of(pageIndex, pageSize), id);
    }

    @Transactional
    @CacheEvict(value = "menus", allEntries = true)
    public void deleteById(int id) {
        log.info("delete {}", id);
        menuRepository.deleteExisted(id);
    }

    @Transactional
    @CacheEvict(value = "menus", allEntries = true)
    public Menu create(@RequestBody Menu menu, @RequestParam Integer restId) {
        log.info("create for restaurnat {}", restId);
        checkNew(menu);
        Assert.notNull(menu, "Menu must not be null");
        setRestaurant(menu, restId);
        return menuRepository.save(menu);
    }

    @Transactional
    @CacheEvict(value = "menus", allEntries = true)
    public void update(@RequestBody Menu menu, @PathVariable int id, @RequestParam Integer restId) {
        log.info("update {}", id);
        assureIdConsistent(menu, id);
        Assert.notNull(menu, "Menu must not be null");
        setRestaurant(menu, restId);
        checkNotFoundWithId(menuRepository.save(menu), id);
    }

    private void setRestaurant(Menu menu, Integer restId) {
        menu.setRestaurant(checkNotFoundWithId(restaurantRepository.getById(restId), restId));
    }
}
