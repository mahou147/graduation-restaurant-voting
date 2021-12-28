package com.mahou.bootjava.restaurantvoting.service;

import com.mahou.bootjava.restaurantvoting.AuthUser;
import com.mahou.bootjava.restaurantvoting.error.NotFoundException;
import com.mahou.bootjava.restaurantvoting.model.Vote;
import com.mahou.bootjava.restaurantvoting.repository.RestaurantRepository;
import com.mahou.bootjava.restaurantvoting.repository.UserRepository;
import com.mahou.bootjava.restaurantvoting.repository.VoteRepository;
import com.mahou.bootjava.restaurantvoting.to.VoteTo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import static com.mahou.bootjava.restaurantvoting.util.ValidationUtil.assureIdConsistent;
import static com.mahou.bootjava.restaurantvoting.util.ValidationUtil.checkNew;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VoteService {

    private final VoteRepository voteRepository;

    private final UserRepository userRepository;

    private final RestaurantRepository restaurantRepository;

    public VoteTo findByAuthUser(@AuthenticationPrincipal AuthUser authUser) {
        log.info("get {} user`s vote", authUser.getId());
        return voteRepository.findByAuthUser(authUser.getId()).orElseThrow(() ->
                new NotFoundException("Vote not found, id: " + authUser.getId()));
    }

    @Transactional
    public void deleteByAuthUser(@AuthenticationPrincipal AuthUser authUser) {
        log.info("delete {} user`s vote", authUser.getId());
        voteRepository.deleteByAuthUser(authUser.getId());
    }

    @Transactional
    public Vote create(Vote vote, @AuthenticationPrincipal AuthUser authUser) {
        log.info("create {} user`s vote", authUser.getId());
        checkNew(vote);
        Assert.notNull(vote, "dish must not be null");
        vote.setUser(userRepository.getById(authUser.getId()));
        return voteRepository.save(vote);
    }

    @Transactional
    public void update(Vote vote, @AuthenticationPrincipal AuthUser authUser) {
        log.info("update {} user`s vote", authUser.getId());
        assureIdConsistent(vote.getUser(), authUser.getId());
        Assert.notNull(vote, "vote must not be null");
        restaurantRepository.findById(vote.getRestaurant().id()).orElseThrow(() ->
                new NotFoundException("Vote not found, id: " + vote.getRestaurant().id()));
        voteRepository.save(vote);
    }
}