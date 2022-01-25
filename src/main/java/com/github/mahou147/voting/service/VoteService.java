package com.github.mahou147.voting.service;

import com.github.mahou147.voting.error.NotFoundException;
import com.github.mahou147.voting.model.Restaurant;
import com.github.mahou147.voting.model.Vote;
import com.github.mahou147.voting.repository.RestaurantRepository;
import com.github.mahou147.voting.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import static com.github.mahou147.voting.util.ErrorUtil.notFound;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VoteService {

    private final VoteRepository voteRepository;

    private final RestaurantRepository restaurantRepository;

    public Vote findByUserId(Integer userId) {
        log.info("get {} user`s vote", userId);
        return voteRepository.findByUserId(userId).orElseThrow(() ->
                new NotFoundException("Vote not found for User: " + userId));
    }

    @Transactional
    public void deleteByUserId(Integer userId) {
        log.info("delete {} user`s vote", userId);
        voteRepository.deleteByUserId(userId);
    }

    @Transactional
    public Vote save(Vote vote, Integer restId) {
        Assert.notNull(vote, "vote must not be null");
        setRestaurant(vote, restId);
        return voteRepository.save(vote);
    }

    private void setRestaurant(Vote vote, Integer restId) {
        vote.setRestaurant(restaurantRepository.findById(restId).orElseThrow(notFound(Restaurant.class, restId)));
    }
}
