package com.github.mahou147.voting.web.vote;

import com.github.mahou147.voting.AuthUser;
import com.github.mahou147.voting.mappers.VoteMapper;
import com.github.mahou147.voting.model.Vote;
import com.github.mahou147.voting.service.VoteService;
import com.github.mahou147.voting.to.VoteTo;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;

import static com.github.mahou147.voting.util.ValidationUtil.checkHour;

@RestController
@RequestMapping(value = VoteController.URL, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
public class VoteController {
    static final String URL = "/api/profile/vote";

    private final VoteService voteService;

    @Operation(summary = "get authentication user`s Vote")
    @GetMapping
    public VoteTo findByUserId(@AuthenticationPrincipal AuthUser authUser) {
        return VoteMapper.INSTANCE.toVoteTo(voteService.findByUserId(authUser.getId()));
    }

    @Operation(summary = "delete authentication user`s Vote")
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthUser authUser) {
        voteService.deleteByUserId(authUser.getId());
    }

    @Operation(summary = "create authentication user`s Vote")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Vote> createWithLocation(@AuthenticationPrincipal AuthUser authUser,
                                                   @RequestParam Integer restId) {
        log.info("create {} user`s vote", authUser.getId());
        Vote v = new Vote(LocalDate.now(), LocalTime.now(), authUser.getUser());
        Vote created = voteService.save(v, restId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Operation(summary = "update authentication user`s Vote")
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@AuthenticationPrincipal AuthUser authUser,
                       @RequestParam Integer restId) {
        Integer userId = authUser.getId();
        log.info("update {} user`s vote", userId);
        checkHour();
        Vote v = voteService.findByUserId(userId);
        voteService.save(v, restId);
    }
}
