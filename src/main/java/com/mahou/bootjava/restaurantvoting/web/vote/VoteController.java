package com.mahou.bootjava.restaurantvoting.web.vote;

import com.mahou.bootjava.restaurantvoting.AuthUser;
import com.mahou.bootjava.restaurantvoting.model.Vote;
import com.mahou.bootjava.restaurantvoting.service.VoteService;
import com.mahou.bootjava.restaurantvoting.to.VoteTo;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(value = VoteController.URL, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
public class VoteController {
    static final String URL = "/api/profile/votes";

    private final VoteService voteService;

    @Operation(summary = "get authentication user`s Vote")
    @GetMapping
    public VoteTo findByUserId(@AuthenticationPrincipal AuthUser authUser) {
        return voteService.findByAuthUser(authUser);
    }

    @Operation(summary = "delete authentication user`s Vote")
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthUser authUser) {
        voteService.deleteByAuthUser(authUser);
    }

    @Operation(summary = "create authentication user`s Vote")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Vote> createWithLocation(@Valid @RequestBody Vote vote, @AuthenticationPrincipal AuthUser authUser) {
        Vote created = voteService.create(vote, authUser);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Operation(summary = "update authentication user`s Vote")
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Vote vote, @AuthenticationPrincipal AuthUser authUser) {
        voteService.update(vote, authUser);
    }
}
